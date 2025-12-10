package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.block.entity.HTOwnedBlockEntity
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTHandlerProvider
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTMenuCallback
import hiiragi283.ragium.common.inventory.container.HTContainerMenu
import hiiragi283.ragium.common.inventory.slot.HTFluidSyncSlot
import hiiragi283.ragium.common.inventory.slot.HTIntSyncSlot
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.energy.battery.HTBasicEnergyBattery
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.resolver.HTEnergyStorageManager
import hiiragi283.ragium.common.storage.resolver.HTFluidHandlerManager
import hiiragi283.ragium.common.storage.resolver.HTItemHandlerManager
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.UUIDUtil
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Nameable
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import java.util.UUID
import java.util.function.Consumer

/**
 * キャパビリティやオーナーを保持する[ExtendedBlockEntity]の拡張クラス
 * @see mekanism.common.tile.base.TileEntityMekanism
 */
abstract class HTBlockEntity(val blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    ExtendedBlockEntity(
        getBlockEntityType(blockHolder),
        pos,
        state,
    ),
    Nameable,
    HTEnergyHandler,
    HTFluidHandler,
    HTHandlerProvider,
    HTItemHandler,
    HTMenuCallback,
    HTOwnedBlockEntity {
    //    Ticking    //

    companion object {
        /**
         * @see mekanism.common.tile.base.TileEntityMekanism.tickClient
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
         * @see mekanism.common.tile.base.TileEntityMekanism.tickServer
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

        @JvmStatic
        fun getBlockEntityType(blockHolder: Holder<Block>): HTDeferredBlockEntityType<*> =
            (blockHolder.value() as HTBlockWithEntity).getBlockEntityType()
    }

    var ticks: Int = 0
        protected set

    protected open fun onUpdateClient(level: Level, pos: BlockPos, state: BlockState) {}

    protected abstract fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

    //    Save & Read    //

    var enchantment: ItemEnchantments
        private set

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
        output.store("custom_name", ComponentSerialization.CODEC, this.customName)
        // Enchantments
        output.store(RagiumConst.ENCHANTMENT, ItemEnchantments.CODEC, enchantment)
        // Owner
        output.store(RagiumConst.OWNER, UUIDUtil.CODEC, ownerId)
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
        this.customName = input.read("custom_name", ComponentSerialization.CODEC)
        // Enchantments
        enchantment = input.read(RagiumConst.ENCHANTMENT, ItemEnchantments.CODEC) ?: ItemEnchantments.EMPTY
        // Owner
        this.ownerId = input.read(RagiumConst.OWNER, UUIDUtil.CODEC)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        this.customName = componentInput.get(DataComponents.CUSTOM_NAME)
        enchantment = componentInput.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(DataComponents.CUSTOM_NAME, this.customName)
        if (!enchantment.isEmpty) {
            components.set(DataComponents.ENCHANTMENTS, enchantment)
        }
    }

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.addContainerTrackers
     */
    open fun addMenuTrackers(menu: HTContainerMenu) {
        // Fluid Tanks
        if (hasFluidHandler()) {
            for (tank: HTFluidTank in this.getFluidTanks(this.getFluidSideFor())) {
                if (tank is HTBasicFluidTank) {
                    menu.track(HTFluidSyncSlot(tank))
                }
            }
        }
        // Energy Battery
        if (hasEnergyStorage()) {
            val battery: HTEnergyBattery? = this.getEnergyBattery(this.getEnergySideFor())
            if (battery is HTBasicEnergyBattery) {
                menu.track(HTIntSyncSlot.create(battery::getAmount, battery::setAmountUnchecked))
            }
        }
    }

    //    Nameable    //

    private var customName: Component? = null

    final override fun getName(): Component = customName ?: blockState.block.name

    final override fun getCustomName(): Component? = customName

    //    HTMenuCallback    //

    override fun openMenu(player: Player) {
        super.openMenu(player)
        this.getServerLevel()?.let(::sendUpdatePacket)
    }

    //    HTOwnedBlockEntity    //

    var ownerId: UUID? = null

    override fun getOwner(): UUID? = ownerId

    //    Capability    //

    protected val fluidHandlerManager: HTFluidHandlerManager?
    protected val energyHandlerManager: HTEnergyStorageManager?
    protected val itemHandlerManager: HTItemHandlerManager?

    init {
        initializeVariables()
        enchantment = ItemEnchantments.EMPTY
        fluidHandlerManager = initializeFluidHandler(::setOnlySave)?.let { HTFluidHandlerManager(it, this) }
        energyHandlerManager = initializeEnergyHandler(::setOnlySave)?.let { HTEnergyStorageManager(it, this) }
        itemHandlerManager = initializeItemHandler(::setOnlySave)?.let { HTItemHandlerManager(it, this) }
    }

    protected open fun initializeVariables() {}

    // Fluid

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialFluidTanks
     */
    protected open fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.canHandleFluid
     */
    override fun hasFluidHandler(): Boolean = fluidHandlerManager?.canHandle() ?: false

    final override fun getFluidTanks(side: Direction?): List<HTFluidTank> = fluidHandlerManager?.getContainers(side) ?: listOf()

    final override fun getFluidHandler(direction: Direction?): IFluidHandler? = fluidHandlerManager?.resolve(direction)

    override fun onRemove(level: Level, pos: BlockPos) {
        super.onRemove(level, pos)
        val expFluidAmount: Int = getFluidTanks(getFluidSideFor())
            .mapNotNull(HTFluidTank::getStack)
            .filter(RagiumFluidContents.EXPERIENCE::isOf)
            .sumOf(ImmutableFluidStack::amount)
        if (expFluidAmount > 0) {
            ExperienceOrb(
                level,
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                HTExperienceHelper.expAmountFromFluid(expFluidAmount),
            ).let(level::addFreshEntity)
        }
    }

    // Energy

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialEnergyContainers
     */
    protected open fun initializeEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.canHandleEnergy
     */
    final override fun hasEnergyStorage(): Boolean = energyHandlerManager?.canHandle() ?: false

    final override fun getEnergyBattery(side: Direction?): HTEnergyBattery? = energyHandlerManager?.getContainers(side)?.firstOrNull()

    final override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = energyHandlerManager?.resolve(direction)

    // Item

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.getInitialInventory
     */
    protected open fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? = null

    /**
     * @see mekanism.common.tile.base.TileEntityMekanism.hasInventory
     */
    final override fun hasItemHandler(): Boolean = itemHandlerManager?.canHandle() ?: false

    final override fun getItemSlots(side: Direction?): List<HTItemSlot> = itemHandlerManager?.getContainers(side) ?: listOf()

    open fun collectDrops(consumer: Consumer<ImmutableItemStack>) {
        getItemSlots(getItemSideFor()).mapNotNull(HTItemSlot::getStack).forEach(consumer)
    }

    final override fun getItemHandler(direction: Direction?): IItemHandler? = itemHandlerManager?.resolve(direction)
}
