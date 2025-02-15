package hiiragi283.ragium.common.internal

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.getServerSavedData
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.energy.HTEnergyNetwork
import hiiragi283.ragium.common.energy.HTLimitedEnergyStorage
import hiiragi283.ragium.common.fluid.HTLimitedFluidHandler
import hiiragi283.ragium.common.fluid.HTMachineFluidTankImpl
import hiiragi283.ragium.common.inventory.HTMultiItemContainerMenu
import hiiragi283.ragium.common.inventory.HTSingleItemContainerMenu
import hiiragi283.ragium.common.item.HTLimitedItemHandler
import hiiragi283.ragium.common.item.HTMachineItemHandlerImpl
import hiiragi283.ragium.common.recipe.HTSimpleItemResult
import hiiragi283.ragium.common.util.HTWrappedMultiMap
import hiiragi283.ragium.common.util.HTWrappedTable
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.Item
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.registries.DeferredBlock

class InternalRagiumAPI : RagiumAPI {
    override fun getMaterialRegistry(): HTMaterialRegistry = HTMaterialRegistryImpl

    override fun getCurrentServer(): MinecraftServer? = RagiumGameEvents.currentServer

    override fun getEnergyNetwork(level: ServerLevel): IEnergyStorage = level.getServerSavedData(HTEnergyNetwork.DATA_FACTORY)

    //    Durability    //

    override fun getForgeHammerDurability(): Int = RagiumConfig.FORGE_HAMMER_DURABILITY.get()

    override fun getSoapDurability(): Int = RagiumConfig.SOAP_DURABILITY.get()

    //    Machine    //

    override fun getDefaultTankCapacity(): Int = RagiumConfig.MACHINE_TANK_CAPACITY.get()

    override fun getMachineSoundVolume(): Float = RagiumConfig.MACHINE_SOUND.get().toFloat()

    //    Misc    //

    override fun getDynamitePower(): Float = RagiumConfig.DYNAMITE_POWER.get().toFloat()

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

    override fun createItemResult(item: Item, count: Int, components: DataComponentPatch): HTItemResult =
        HTSimpleItemResult(item, count, components)

    override fun createSingleItemMenu(
        containerId: Int,
        playerInv: Inventory,
        pos: BlockPos,
        itemInput: IItemHandler,
        itemCatalyst: IItemHandler,
        itemOutput: IItemHandler,
    ): AbstractContainerMenu = HTSingleItemContainerMenu(
        containerId,
        playerInv,
        pos,
        itemInput,
        itemCatalyst,
        itemOutput,
    )

    override fun createMultiItemMenu(
        containerId: Int,
        playerInv: Inventory,
        pos: BlockPos,
        itemInput: IItemHandler,
        itemOutput: IItemHandler,
    ): AbstractContainerMenu = HTMultiItemContainerMenu(
        containerId,
        playerInv,
        pos,
        itemInput,
        itemOutput,
    )

    /*companion object {
        @JvmStatic
        private val BLOCK_REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)

        @JvmStatic
        private lateinit var blockMap: Map<HTMachineType, DeferredBlock<*>>

        @JvmStatic
        fun initMachineBlocks(eventBus: IEventBus) {
            blockMap = HTMachineType.entries.associateWith { type: HTMachineType ->
                val holder: DeferredBlock<out Block> = BLOCK_REGISTER.registerBlock(
                    type.serializedName,
                    { properties: BlockBehaviour.Properties -> HTMachineBlock(type, properties) },
                    blockProperty()
                        .mapColor(MapColor.STONE)
                        .strength(2f)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops()
                        .noOcclusion(),
                )
                RagiumBlocks.ITEM_REGISTER.registerSimpleBlockItem(holder)
                holder
            }
            BLOCK_REGISTER.register(eventBus)
        }
    }*/

    override fun getMachineBlock(type: HTMachineType): DeferredBlock<*> =
        RagiumModEvents.blockMap[type] ?: error("Unknown machine type: ${type.serializedName} found!")
}
