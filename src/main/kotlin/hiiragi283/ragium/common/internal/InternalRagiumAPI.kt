package hiiragi283.ragium.common.internal

import com.google.common.collect.Multimap
import com.google.common.collect.Table
import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddonCollector
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.getServerSavedData
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.block.machine.HTMachineBlock
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import hiiragi283.ragium.common.storage.energy.HTLimitedEnergyStorage
import hiiragi283.ragium.common.storage.fluid.HTFluidTankImpl
import hiiragi283.ragium.common.storage.item.HTItemSlotImpl
import hiiragi283.ragium.common.storage.item.HTSculkItemStorage
import hiiragi283.ragium.common.util.HTWrappedMultiMap
import hiiragi283.ragium.common.util.HTWrappedTable
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.MapColor
import net.neoforged.fml.LogicalSide
import net.neoforged.fml.util.thread.EffectiveSide
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.templates.EmptyFluidHandler
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

    //    Addon    //

    private lateinit var addonCache: List<RagiumAddon>

    override fun getAddons(): List<RagiumAddon> {
        if (!::addonCache.isInitialized) {
            LOGGER.info("Collecting addons for Ragium...")
            addonCache = HTAddonCollector
                .collectInstances<RagiumAddon>()
                .sortedBy(RagiumAddon::priority)
                .onEach { addon: RagiumAddon ->
                    LOGGER.info("Loaded addon from ${addon::class.qualifiedName}!")
                }
        }
        return addonCache
    }

    //    Material    //

    override fun getMaterialRegistry(): HTMaterialRegistry = HTMaterialRegistryImpl

    //    Server    //

    override fun getCurrentServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    override fun getCurrentSide(): LogicalSide = EffectiveSide.get()

    override fun getEnergyNetwork(level: ServerLevel): IEnergyStorage = level.getServerSavedData(HTEnergyNetwork.DATA_FACTORY)

    override fun getSculkItemStorage(level: ServerLevel, color: DyeColor): IItemHandlerModifiable =
        level.getServerSavedData(HTSculkItemStorage.DATA_FACTORY).getHandler(color)

    override fun getSculkFluidTank(level: ServerLevel, color: DyeColor): IFluidHandler = EmptyFluidHandler.INSTANCE

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

    override fun wrapItemSlot(storageIO: HTStorageIO, slotIn: HTItemSlot): IItemHandlerModifiable = object : HTItemSlotHandler {
        override fun getItemIoFromSlot(slot: Int): HTStorageIO = storageIO

        override fun getItemSlot(slot: Int): HTItemSlot? = slotIn

        override fun getSlots(): Int = 1
    }

    override fun wrapFluidTank(storageIO: HTStorageIO, tankIn: HTFluidTank): IFluidHandler = object : HTFluidSlotHandler {
        override fun getFluidIoFromSlot(tank: Int): HTStorageIO = storageIO

        override fun getFluidTank(tank: Int): HTFluidTank? = tankIn

        override fun getTanks(): Int = 1
    }

    override fun wrapEnergyStorage(storageIO: HTStorageIO, storage: IEnergyStorage): IEnergyStorage =
        HTLimitedEnergyStorage(storageIO, storage)

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
                for ((_, holder: DeferredBlock<*>) in blockMap) {
                    helper.register(holder.id, BlockItem(holder.get(), itemProperty()))
                }
            }
        }
    }

    override fun buildItemSlot(
        nbtKey: String,
        capacity: Int,
        validator: (HTItemVariant) -> Boolean,
        callback: Runnable,
    ): HTItemSlot = HTItemSlotImpl(
        nbtKey,
        capacity,
        validator,
        callback,
    )

    override fun buildFluidTank(
        nbtKey: String,
        capacity: Int,
        validator: (HTFluidVariant) -> Boolean,
        callback: Runnable,
    ): HTFluidTank = HTFluidTankImpl(
        nbtKey,
        capacity,
        validator,
        callback,
    )
}
