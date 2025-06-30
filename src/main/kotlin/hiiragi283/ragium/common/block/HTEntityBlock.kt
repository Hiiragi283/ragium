package hiiragi283.ragium.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

/**
 * `Ragium`で使用する[BaseEntityBlock]の拡張クラス
 */
abstract class HTEntityBlock<BE : HTBlockEntity>(val type: HTDeferredBlockEntityType<BE>, properties: Properties) :
    BaseEntityBlock(properties) {
    companion object {
        @JvmStatic
        fun <BE : HTBlockEntity> create(type: HTDeferredBlockEntityType<BE>): (Properties) -> HTEntityBlock<*> = { prop: Properties ->
            object : HTEntityBlock<BE>(type, prop) {
                override fun initDefaultState(): BlockState = stateDefinition.any()
            }
        }
    }

    init {
        registerDefaultState(initDefaultState())
    }

    /**
     * デフォルトの[BlockState]を返します。
     */
    protected abstract fun initDefaultState(): BlockState

    protected fun BlockGetter.getHTBlockEntity(pos: BlockPos): HTBlockEntity? = getBlockEntity(pos) as? HTBlockEntity

    //    Block    //

    override fun codec(): MapCodec<out BaseEntityBlock> = throw UnsupportedOperationException()

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    final override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = level
        .getHTBlockEntity(pos)
        ?.onRightClicked(state, level, pos, player, hitResult)
        ?: super.useWithoutItem(state, level, pos, player, hitResult)

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult = level
        .getHTBlockEntity(pos)
        ?.onRightClickedWithItem(stack, state, level, pos, player, hand, hitResult)
        ?: super.useItemOn(stack, state, level, pos, player, hand, hitResult)

    override fun attack(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ) {
        super.attack(state, level, pos, player)
        level.getHTBlockEntity(pos)?.onLeftClicked(state, level, pos, player)
    }

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
        super.setPlacedBy(level, pos, state, placer, stack)
        level.getHTBlockEntity(pos)?.setPlacedBy(level, pos, state, placer, stack)
    }

    final override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        if (!state.`is`(newState.block)) {
            level.getHTBlockEntity(pos)?.onRemove(state, level, pos, newState, movedByPiston)
        }
        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    final override fun hasAnalogOutputSignal(state: BlockState): Boolean = true

    final override fun getAnalogOutputSignal(state: BlockState, level: Level, pos: BlockPos): Int =
        level.getHTBlockEntity(pos)?.getComparatorOutput(state, level, pos)
            ?: super.getAnalogOutputSignal(state, level, pos)

    final override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean,
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
        level.getHTBlockEntity(pos)?.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
    }

    final override fun getCloneItemStack(
        state: BlockState,
        target: HitResult,
        level: LevelReader,
        pos: BlockPos,
        player: Player,
    ): ItemStack = super.getCloneItemStack(state, target, level, pos, player).apply {
        level.getHTBlockEntity(pos)?.collectComponents()?.let(this::applyComponents)
    }

    final override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = type.create(pos, state)

    final override fun <T : BlockEntity> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>,
    ): BlockEntityTicker<T>? {
        val ticker: BlockEntityTicker<in BE> = type.getTicker(level.isClientSide) ?: return null
        return createTickerHelper(blockEntityType, type.get(), ticker)
    }
}
