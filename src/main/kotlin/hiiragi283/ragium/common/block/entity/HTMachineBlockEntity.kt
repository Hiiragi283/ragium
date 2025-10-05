package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.block.entity.HTOwnedBlockEntity
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTAccessConfiguration
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.storage.HTAccessConfigCache
import hiiragi283.ragium.common.storage.energy.HTEnergyBatteryWrapper
import hiiragi283.ragium.common.storage.holder.HTSimpleEnergyStorageHolder
import hiiragi283.ragium.common.storage.item.HTMachineUpgradeItemHandler
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.items.ItemHandlerHelper
import java.util.*
import java.util.function.Consumer

abstract class HTMachineBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state),
    HTOwnedBlockEntity,
    HTAccessConfiguration.Holder {
    constructor(variant: HTVariantKey.WithBE<*>, pos: BlockPos, state: BlockState) : this(variant.blockEntityHolder, pos, state)

    val upgradeHandler: HTMachineUpgradeItemHandler get() = getData(RagiumAttachmentTypes.MACHINE_UPGRADE)

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.store(RagiumConst.OWNER, BiCodecs.UUID, ownerId)
        accessConfigCache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        ownerId = input.read(RagiumConst.OWNER, BiCodecs.UUID)
        accessConfigCache.deserialize(input)
    }

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        if (stack.`is`(Tags.Items.TOOLS_WRENCH)) {
            RagiumMenuTypes.ACCESS_CONFIG.openMenu(player, name, this, ::writeExtraContainerData)
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return super.onRightClickedWithItem(stack, state, level, pos, player, hand, hitResult)
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = openGui(player, name)

    protected abstract fun openGui(player: Player, title: Component): InteractionResult

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
        super.setPlacedBy(level, pos, state, placer, stack)
        this.ownerId = placer?.uuid
    }

    override fun dropInventory(consumer: Consumer<ItemStack>) {
        super.dropInventory(consumer)
        upgradeHandler.getItemSlots(upgradeHandler.getItemSideFor()).map(HTItemSlot::getStack).forEach(consumer)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        ItemHandlerHelper.calcRedstoneFromInventory(getItemHandler(null))

    //    Ticking    //

    /**
     * このブロックエンティティがtick当たりで消費する電力の値
     */
    protected abstract val energyUsage: Int

    protected var isActive: Boolean = false
    protected var requiredEnergy: Int = 0
    protected var usedEnergy: Int = 0

    protected open fun getModifiedEnergy(base: Int): Int = upgradeHandler.getTier()?.modifyProcessorRate(base) ?: base

    final override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val network: HTEnergyBattery = getter(level) ?: return false
        val result: Boolean = onUpdateServer(level, pos, state, network)
        isActive = result
        return result
    }

    protected abstract fun onUpdateServer(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: HTEnergyBattery,
    ): Boolean

    val progress: Float
        get() {
            val totalTick: Int = usedEnergy
            val maxTicks: Int = requiredEnergy
            if (maxTicks <= 0) return 0f
            val fixedTotalTicks: Int = totalTick % maxTicks
            return Mth.clamp(fixedTotalTicks / maxTicks.toFloat(), 0f, 1f)
        }

    //    Energy Storage    //

    private val getter: (Level?) -> HTEnergyBattery? = RagiumPlatform.INSTANCE::getEnergyNetwork

    override fun initializeEnergyStorage(listener: HTContentListener): HTEnergyStorageHolder? = createStorageHolder(
        HTEnergyBatteryWrapper { getter(level) },
    )

    protected open fun createStorageHolder(battery: HTEnergyBattery): HTEnergyStorageHolder =
        HTSimpleEnergyStorageHolder.input(this, battery)

    //    HTOwnedBlockEntity    //

    private var ownerId: UUID? = null

    override fun getOwnerUUID(): UUID {
        if (ownerId == null) {
            ownerId = UUID.randomUUID()
        }
        return ownerId!!
    }

    //    HTAccessConfiguration    //

    private val accessConfigCache = HTAccessConfigCache()

    override fun getAccessConfiguration(side: Direction): HTAccessConfiguration = accessConfigCache.getAccessConfiguration(side)

    override fun setAccessConfiguration(side: Direction, value: HTAccessConfiguration) {
        accessConfigCache.setAccessConfiguration(side, value)
    }

    //    Menu    //

    final override fun getDisplayName(): Component = super.getDisplayName()

    //    Slot    //

    val containerData: ContainerData = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> usedEnergy
            1 -> requiredEnergy
            else -> -1
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> usedEnergy = value
                1 -> requiredEnergy = value
            }
        }

        override fun getCount(): Int = 2
    }
}
