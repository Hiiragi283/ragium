package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.common.network.HTUpdateFluidTankPacket
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.resolver.HTEnergyStorageManager
import hiiragi283.ragium.common.storage.resolver.HTFluidHandlerManager
import hiiragi283.ragium.common.storage.resolver.HTItemHandlerManager
import hiiragi283.ragium.common.util.HTPacketHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Nameable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import java.util.function.Consumer

/**
 * @see [mekanism.common.tile.base.TileEntityMekanism]
 */
abstract class HTBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    ExtendedBlockEntity(
        type,
        pos,
        state,
    ),
    Nameable,
    HTItemHandler,
    HTFluidHandler,
    HTEnergyHandler,
    HTHandlerBlockEntity {
    //    Ticking    //

    companion object {
        /**
         * @see [mekanism.common.tile.base.TileEntityMekanism.tickClient]
         */
        @JvmStatic
        fun tickClient(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTBlockEntity,
        ) {
            blockEntity.onUpdateClient(level, pos, state)
            blockEntity.ticks++
        }

        /**
         * @see [mekanism.common.tile.base.TileEntityMekanism.tickServer]
         */
        @JvmStatic
        fun tickServer(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTBlockEntity,
        ) {
            val serverLevel: ServerLevel = level as? ServerLevel ?: return
            val shouldUpdate: Boolean = blockEntity.onUpdateServer(serverLevel, pos, state)
            blockEntity.ticks++
            if (shouldUpdate) {
                blockEntity.sendUpdatePacket(serverLevel)
            }
        }
    }

    var ticks: Int = 0
        protected set

    protected open fun onUpdateClient(level: Level, pos: BlockPos, state: BlockState) {}

    protected abstract fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

    //    Save & Read    //

    final override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        RagiumPlatform.INSTANCE.createValueOutput(registries, tag).let(::writeValue)
    }

    protected open fun writeValue(output: HTValueOutput) {
        // Capability
        for (type: HTCapabilityCodec<*> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.saveTo(output, this)
            }
        }
        // Custom Name
        output.store("custom_name", BiCodecs.TEXT, customName)
    }

    final override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        RagiumPlatform.INSTANCE.createValueInput(registries, tag).let(::readValue)
    }

    protected open fun readValue(input: HTValueInput) {
        // Capability
        for (type: HTCapabilityCodec<*> in HTCapabilityCodec.TYPES) {
            if (type.canHandle(this)) {
                type.loadFrom(input, this)
            }
        }
        // Custom Name
        customName = input.read("custom_name", BiCodecs.TEXT)
    }

    override fun sendPassivePacket(level: ServerLevel) {
        super.sendPassivePacket(level)
        if (hasFluidHandler()) {
            val tanks: List<HTFluidTank> = getFluidTanks(getFluidSideFor())
            for (i: Int in tanks.indices) {
                HTPacketHelper.sendToClient(level, blockPos, HTUpdateFluidTankPacket.create(this, i))
            }
        }
    }

    //    Nameable    //

    private var customName: Component? = null

    final override fun getName(): Component = customName ?: blockState.block.name

    final override fun getCustomName(): Component? = customName

    //    Capability    //

    override fun onContentsChanged() {
        setChanged()
    }

    protected val fluidHandlerManager: HTFluidHandlerManager?
    protected val energyStorageManager: HTEnergyStorageManager?
    protected val itemHandlerManager: HTItemHandlerManager?

    init {
        fluidHandlerManager = initializeFluidHandler(::setOnlySave)?.let { HTFluidHandlerManager(it, this) }
        energyStorageManager = initializeEnergyStorage(::setOnlySave)?.let { HTEnergyStorageManager(it, this) }
        itemHandlerManager = initializeItemHandler(::setOnlySave)?.let { HTItemHandlerManager(it, this) }
    }

    // Fluid

    /**
     * @see [mekanism.common.tile.base.TileEntityMekanism.getInitialFluidTanks]
     */
    protected open fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder? = null

    /**
     * @see [mekanism.common.tile.base.TileEntityMekanism.canHandleFluid]
     */
    override fun hasFluidHandler(): Boolean = fluidHandlerManager?.canHandle() ?: false

    final override fun getFluidTanks(side: Direction?): List<HTFluidTank> = fluidHandlerManager?.getContainers(side) ?: listOf()

    final override fun getFluidHandler(direction: Direction?): IFluidHandler? =
        fluidHandlerManager?.resolve(HTMultiCapability.FLUID, direction)

    // Energy

    /**
     * @see [mekanism.common.tile.base.TileEntityMekanism.getInitialEnergyContainers]
     */
    protected open fun initializeEnergyStorage(listener: HTContentListener): HTEnergyStorageHolder? = null

    /**
     * @see [mekanism.common.tile.base.TileEntityMekanism.canHandleEnergy]
     */
    override fun hasEnergyStorage(): Boolean = energyStorageManager?.canHandle() ?: false

    final override fun getEnergyHandler(side: Direction?): HTEnergyBattery? = energyStorageManager?.getContainers(side)?.firstOrNull()

    final override fun getEnergyStorage(direction: Direction?): IEnergyStorage? =
        energyStorageManager?.resolve(HTMultiCapability.ENERGY, direction)

    // Item

    /**
     * @see [mekanism.common.tile.base.TileEntityMekanism.getInitialInventory]
     */
    protected open fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? = null

    /**
     * @see [mekanism.common.tile.base.TileEntityMekanism.hasInventory]
     */
    final override fun hasItemHandler(): Boolean = itemHandlerManager?.canHandle() ?: false

    final override fun getItemSlots(side: Direction?): List<HTItemSlot> = itemHandlerManager?.getContainers(side) ?: listOf()

    override fun dropInventory(consumer: Consumer<ItemStack>) {
        super.dropInventory(consumer)
        getItemSlots(getItemSideFor()).map(HTItemSlot::getItemStack).forEach(consumer)
    }

    final override fun getItemHandler(direction: Direction?): IItemHandler? = itemHandlerManager?.resolve(HTMultiCapability.ITEM, direction)
}
