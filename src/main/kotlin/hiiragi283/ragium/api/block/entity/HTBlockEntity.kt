package hiiragi283.ragium.api.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.ContainerHelper
import net.minecraft.world.Containers
import net.minecraft.world.InteractionResult
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

abstract class HTBlockEntity(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : BlockEntity(type, pos, state) {
    open fun getContainer(): WorldlyContainer? = null

    override fun saveAdditional(
        tag: CompoundTag,
        registries: HolderLookup.Provider,
    ) {
        super.saveAdditional(tag, registries)
        getContainer()?.let { ContainerHelper.saveAllItems(tag, TODO(), registries) }
    }

    override fun loadAdditional(
        tag: CompoundTag,
        registries: HolderLookup.Provider,
    ) {
        super.loadAdditional(tag, registries)
        getContainer()?.let { ContainerHelper.loadAllItems(tag, TODO(), registries) }
    }

    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag =
        CompoundTag().apply { saveAdditional(this, registries) }

    final override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    override fun handleUpdateTag(
        tag: CompoundTag,
        lookupProvider: HolderLookup.Provider,
    ) {
        super.handleUpdateTag(tag, lookupProvider)
    }

    //    Extension    //

    /**
     * ブロックが右クリックされたときに呼ばれます。
     * @see [hiiragi283.ragium.api.block.HTEntityBlock.useWithoutItem]
     */
    open fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult =
        state.getMenuProvider(level, pos)?.let {
            when (level.isClientSide) {
                true -> InteractionResult.SUCCESS
                else -> {
                    player.openMenu(it)
                    InteractionResult.CONSUME
                }
            }
        } ?: InteractionResult.PASS

    /**
     * ブロックが左クリックされたときに呼ばれます。
     * @see [hiiragi283.ragium.api.block.HTEntityBlock.attack]
     */
    open fun onLeftClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ) {}

    /**
     * ブロックが設置されたときに呼ばれます。
     * @see [hiiragi283.ragium.api.block.HTEntityBlock.setPlacedBy]
     */
    open fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {}

    /**
     * ブロックが置換されたときに呼ばれます。
     * @see [hiiragi283.ragium.api.block.HTEntityBlock.onRemove]
     */
    open fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        Containers.dropContentsOnDestroy(state, newState, level, pos)
    }

    /**
     * ブロックのコンパレータ出力を返します。
     * @see [hiiragi283.ragium.api.block.HTEntityBlock.getAnalogOutputSignal]
     */
    open fun getComparatorOutput(
        state: BlockState,
        level: Level,
        pos: BlockPos,
    ): Int = 0

    open val shouldTick: Boolean = true
    open var ticks: Int = 0
        protected set
    open val tickRate: Int = 200

    /**
     * 毎tick呼び出されます。
     * @see [hiiragi283.ragium.api.block.HTEntityBlock.getTicker]
     */
    fun tick(
        level: Level,
        pos: BlockPos,
        state: BlockState,
    ) {
        if (!shouldTick) return
        tickEach(level, pos, state, ticks)
        if (ticks >= tickRate) {
            tickSecond(level, pos, state)
            ticks = 0
        } else {
            ticks++
        }
    }

    /**
     * 毎[tick]呼び出されます。
     */
    open fun tickEach(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
    }

    /**
     * [ticks]が[tickRate]以上の値となったときに呼び出されます。
     */
    open fun tickSecond(
        level: Level,
        pos: BlockPos,
        state: BlockState,
    ) {}
}
