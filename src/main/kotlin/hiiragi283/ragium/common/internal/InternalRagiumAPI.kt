package hiiragi283.ragium.common.internal

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.capability.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.capability.item.HTMachineItemHandler
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.getServerSavedData
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.block.machine.HTMachineBlock
import hiiragi283.ragium.common.capability.energy.HTEnergyNetwork
import hiiragi283.ragium.common.capability.energy.HTLimitedEnergyStorage
import hiiragi283.ragium.common.capability.fluid.HTLimitedFluidHandler
import hiiragi283.ragium.common.capability.fluid.HTMachineFluidTankImpl
import hiiragi283.ragium.common.capability.item.HTLimitedItemHandler
import hiiragi283.ragium.common.capability.item.HTMachineItemHandlerImpl
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.inventory.HTMultiItemContainerMenu
import hiiragi283.ragium.common.inventory.HTSingleItemContainerMenu
import hiiragi283.ragium.common.util.HTWrappedMultiMap
import hiiragi283.ragium.common.util.HTWrappedTable
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.MapColor
import net.neoforged.fml.LogicalSide
import net.neoforged.fml.util.thread.EffectiveSide
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.RegisterEvent
import net.neoforged.neoforge.server.ServerLifecycleHooks
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

class InternalRagiumAPI : RagiumAPI {
    init {
        MOD_BUS.addListener(::onBlockRegister)
    }

    //    Material    //

    override fun getMaterialRegistry(): HTMaterialRegistry = HTMaterialRegistryImpl

    override fun getMoltenMaterialComponent(): DataComponentType<HTMaterialKey> = RagiumComponentTypes.MOLTEN_MATERIAL.get()

    override fun createMoltenMetalStack(key: HTMaterialKey, amount: Int): FluidStack {
        val stack = FluidStack(RagiumFluids.MOLTEN_METAL, 90)
        stack.set(RagiumComponentTypes.MOLTEN_MATERIAL, key)
        return stack
    }

    //    Server    //

    override fun getCurrentServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    override fun getCurrentSide(): LogicalSide = EffectiveSide.get()

    override fun getEnergyNetwork(level: ServerLevel): IEnergyStorage = level.getServerSavedData(HTEnergyNetwork.DATA_FACTORY)

    //    Durability    //

    override fun getForgeHammerDurability(): Int = RagiumConfig.FORGE_HAMMER_DURABILITY.get()

    //    Machine    //

    override fun getDefaultTankCapacity(): Int = RagiumConfig.MACHINE_TANK_CAPACITY.get()

    override fun getMachineSoundVolume(): Float = RagiumConfig.MACHINE_SOUND.get().toFloat()

    //    Misc    //

    override fun getDynamitePower(): Float = RagiumConfig.DYNAMITE_POWER.get().toFloat()

    override fun getGrinderOutputCount(key: HTMaterialKey): Int = RagiumConfig.getGrinderRawCountMap()[key] ?: 1

    //    Platform    //

    override fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V> = HTWrappedMultiMap.Mutable(multimap)

    override fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V> = HTWrappedTable.Mutable(table)

    override fun createItemHandler(size: Int, callback: () -> Unit): HTMachineItemHandler = HTMachineItemHandlerImpl(size, callback)

    override fun createTank(callback: () -> Unit, filter: (FluidStack) -> Boolean, capacity: Int): HTMachineFluidTank =
        HTMachineFluidTankImpl(callback, filter, capacity)

    override fun wrapItemHandler(storageIO: HTStorageIO, handler: IItemHandlerModifiable): IItemHandlerModifiable =
        HTLimitedItemHandler(storageIO, handler)

    override fun wrapFluidHandler(storageIO: HTStorageIO, handler: IFluidHandler): IFluidHandler = HTLimitedFluidHandler(storageIO, handler)

    override fun wrapEnergyStorage(storageIO: HTStorageIO, storage: IEnergyStorage): IEnergyStorage =
        HTLimitedEnergyStorage(storageIO, storage)

    override fun createSingleItemMenu(
        containerId: Int,
        inventory: Inventory,
        pos: BlockPos,
        itemInput: IItemHandler,
        itemCatalyst: IItemHandler,
        itemOutput: IItemHandler,
    ): AbstractContainerMenu = HTSingleItemContainerMenu(
        containerId,
        inventory,
        pos,
        itemInput,
        itemCatalyst,
        itemOutput,
    )

    override fun createMultiItemMenu(
        containerId: Int,
        inventory: Inventory,
        pos: BlockPos,
        itemInput: IItemHandler,
        itemOutput: IItemHandler,
    ): AbstractContainerMenu = HTMultiItemContainerMenu(
        containerId,
        inventory,
        pos,
        itemInput,
        itemOutput,
    )

    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

        @JvmStatic
        private lateinit var blockMap: Map<HTMachineType, DeferredBlock<*>>

        @JvmStatic
        private fun onBlockRegister(event: RegisterEvent) {
            // Block
            event.register(Registries.BLOCK) { helper: RegisterEvent.RegisterHelper<Block> ->
                blockMap = HTMachineType.entries.associateWith { type: HTMachineType ->
                    val block = HTMachineBlock(
                        type,
                        blockProperty()
                            .mapColor(MapColor.STONE)
                            .strength(2f)
                            .sound(SoundType.METAL)
                            .requiresCorrectToolForDrops()
                            .noOcclusion(),
                    )
                    val id: ResourceLocation = RagiumAPI.id(type.serializedName)
                    helper.register(id, block)
                    DeferredBlock.createBlock<Block>(id)
                }
                LOGGER.info("Registered machine blocks!")
            }
            // Item
            event.register(Registries.ITEM) { helper: RegisterEvent.RegisterHelper<Item> ->
                blockMap.forEach { (_, holder: DeferredBlock<*>) ->
                    helper.register(holder.id, BlockItem(holder.get(), itemProperty()))
                }
            }
        }
    }

    override fun getMachineBlock(type: HTMachineType): DeferredBlock<*> =
        blockMap[type] ?: error("Unknown machine type: ${type.serializedName} found!")
}
