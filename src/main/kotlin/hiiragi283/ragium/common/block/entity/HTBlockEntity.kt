package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.block.entity.HTOwnedBlockEntity
import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTHandlerProvider
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import hiiragi283.ragium.api.storage.experience.HTExperienceHandler
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.storage.experience.IExperienceHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.ragium.api.storage.holder.HTExperienceTankHolder
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.container.HTContainerMenu
import hiiragi283.ragium.common.inventory.slot.HTFluidSyncSlot
import hiiragi283.ragium.common.inventory.slot.HTIntSyncSlot
import hiiragi283.ragium.common.inventory.slot.HTLongSyncSlot
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.energy.battery.HTBasicEnergyBattery
import hiiragi283.ragium.common.storage.experience.tank.HTBasicExperienceTank
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.resolver.HTEnergyStorageManager
import hiiragi283.ragium.common.storage.resolver.HTExperienceHandlerManager
import hiiragi283.ragium.common.storage.resolver.HTFluidHandlerManager
import hiiragi283.ragium.common.storage.resolver.HTItemHandlerManager
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
        (blockHolder.value() as HTBlockWithEntity).getBlockEntityType(),
        pos,
        state,
    ),
    Nameable,
    HTEnergyHandler,
    HTExperienceHandler,
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
                if (tank is HTFluidStackTank) {
                    menu.track(HTFluidSyncSlot(tank))
                }
            }
        }
        // Energy Battery
        if (hasEnergyStorage()) {
            val battery: HTEnergyBattery? = this.getEnergyBattery(this.getEnergySideFor())
            if (battery is HTBasicEnergyBattery) {
                menu.track(HTIntSyncSlot(battery::getAmount, battery::setAmountUnchecked))
            }
        }
        // Experience Tanks
        if (hasExperienceHandler()) {
            for (tank: HTExperienceTank in this.getExpTanks(this.getExperienceSideFor())) {
                if (tank is HTBasicExperienceTank) {
                    menu.track(HTLongSyncSlot(tank::getAmount, tank::setAmountUnchecked))
                }
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
    protected val experienceHandlerManager: HTExperienceHandlerManager?
    protected val itemHandlerManager: HTItemHandlerManager?

    init {
        initializeVariables()
        enchantment = ItemEnchantments.EMPTY
        fluidHandlerManager = initializeFluidHandler(::setOnlySave)?.let { HTFluidHandlerManager(it, this) }
        energyHandlerManager = initializeEnergyHandler(::setOnlySave)?.let { HTEnergyStorageManager(it, this) }
        experienceHandlerManager = initializeExperienceHandler(::setOnlySave)?.let { HTExperienceHandlerManager(it, this) }
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

    // Experience

    protected open fun initializeExperienceHandler(listener: HTContentListener): HTExperienceTankHolder? = null

    final override fun hasExperienceHandler(): Boolean = experienceHandlerManager?.canHandle() ?: false

    final override fun getExpTanks(side: Direction?): List<HTExperienceTank> = experienceHandlerManager?.getContainers(side) ?: listOf()

    final override fun getExperienceHandler(direction: Direction?): IExperienceHandler? = experienceHandlerManager?.resolve(direction)

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

    final override fun dropInventory(consumer: Consumer<ImmutableItemStack>) {
        super.dropInventory(consumer)
        if (doDropItems()) {
            getItemSlots(getItemSideFor()).mapNotNull(HTItemSlot::getStack).forEach(consumer)
        }
    }

    protected open fun doDropItems(): Boolean = hasItemHandler()

    final override fun getItemHandler(direction: Direction?): IItemHandler? = itemHandlerManager?.resolve(direction)
}
