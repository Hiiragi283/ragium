package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.block.entity.HTOwnedBlockEntity
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTTransferIO
import hiiragi283.ragium.api.storage.energy.HTEnergyFilter
import hiiragi283.ragium.api.storage.energy.HTFilteredEnergyStorage
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.storage.HTTransferIOCache
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
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.ItemHandlerHelper
import java.util.UUID

abstract class HTMachineBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state),
    HTOwnedBlockEntity,
    HTTransferIO.Provider,
    HTTransferIO.Receiver {
    constructor(variant: HTVariantKey.WithBE<*>, pos: BlockPos, state: BlockState) : this(variant.blockEntityHolder, pos, state)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        super.writeNbt(writer)
        writer.writeNullable(BiCodecs.UUID, RagiumConst.OWNER, ownerId)
        writer.write(RagiumConst.TRANSFER_IO, transferIOCache)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        super.readNbt(reader)
        reader.read(BiCodecs.UUID, RagiumConst.OWNER).ifSuccess { ownerId = it }
        reader.read(RagiumConst.TRANSFER_IO, transferIOCache)
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
            RagiumMenuTypes.SLOT_CONFIG.openMenu(player, name, this, ::writeExtraContainerData)
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

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        ItemHandlerHelper.calcRedstoneFromInventory(getItemHandler(null))

    //    Ticking    //

    /**
     * このブロックエンティティがtick当たりで消費する電力の値
     */
    protected abstract val energyUsage: Int

    protected var requiredEnergy: Int = 0
    protected var usedEnergy: Int = 0

    protected fun doProgress(network: IEnergyStorage): Boolean {
        if (usedEnergy < requiredEnergy) {
            usedEnergy += network.extractEnergy(energyUsage, false)
        }
        if (usedEnergy < requiredEnergy) return false
        usedEnergy -= requiredEnergy
        return true
    }

    final override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val network: IEnergyStorage = this.network ?: return false
        return onUpdateServer(level, pos, state, network)
    }

    protected abstract fun onUpdateServer(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): Boolean

    val progress: Float
        get() {
            val totalTick: Int = usedEnergy
            val maxTicks: Int = requiredEnergy
            if (maxTicks <= 0) return 0f
            val fixedTotalTicks: Int = totalTick % maxTicks
            return Mth.clamp(fixedTotalTicks / maxTicks.toFloat(), 0f, 1f)
        }

    //    HTHandlerBlockEntity    //

    protected var network: IEnergyStorage? = null
        private set
    private var externalNetwork: IEnergyStorage? = null

    override fun afterLevelInit(level: Level) {
        super.afterLevelInit(level)
        val network: IEnergyStorage = level.getData(RagiumAttachmentTypes.ENERGY_NETWORK) ?: return
        this.network = network
        this.externalNetwork = wrapNetworkToExternal(network)
    }

    final override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = externalNetwork

    protected open fun wrapNetworkToExternal(network: IEnergyStorage): IEnergyStorage =
        HTFilteredEnergyStorage(network, HTEnergyFilter.RECEIVE_ONLY)

    //    HTOwnedBlockEntity    //

    private var ownerId: UUID? = null

    override fun getOwnerUUID(): UUID {
        if (ownerId == null) {
            ownerId = UUID.randomUUID()
        }
        return ownerId!!
    }

    //    HTTransferIOReceiver    //

    private val transferIOCache = HTTransferIOCache()

    override fun apply(direction: Direction): HTTransferIO = transferIOCache[direction]

    override fun accept(direction: Direction, transferIO: HTTransferIO) {
        transferIOCache[direction] = transferIO
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
