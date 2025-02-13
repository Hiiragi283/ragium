package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.extension.getHTBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult

abstract class HTEntityBlock(properties: Properties) :
    Block(properties),
    EntityBlock {
    companion object {
        @JvmStatic
        fun of(factory: (BlockPos, BlockState) -> HTBlockEntity?, properties: Properties): HTEntityBlock =
            object : HTEntityBlock(properties) {
                override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = factory(pos, state)
            }

        @JvmStatic
        fun horizontal(factory: (BlockPos, BlockState) -> HTBlockEntity?, properties: Properties): Horizontal =
            object : Horizontal(properties) {
                override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = factory(pos, state)
            }
    }

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

    override fun neighborChanged(
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

    override fun getMenuProvider(state: BlockState, level: Level, pos: BlockPos): MenuProvider? =
        level.getHTBlockEntity(pos) as? MenuProvider

    final override fun <T : BlockEntity> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>,
    ): BlockEntityTicker<T>? = BlockEntityTicker<T> { level: Level, pos: BlockPos, state: BlockState, blockEntity: T ->
        (blockEntity as? HTBlockEntity)?.tick(level, pos, state)
    }

    //    Horizontal    //

    abstract class Horizontal(properties: Properties) : HTEntityBlock(properties) {
        init {
            registerDefaultState(
                stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH),
            )
        }

        override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
            builder.add(BlockStateProperties.HORIZONTAL_FACING)
        }

        override fun getStateForPlacement(context: BlockPlaceContext): BlockState? = defaultBlockState()
            .setValue(BlockStateProperties.HORIZONTAL_FACING, context.horizontalDirection.opposite)

        override fun rotate(
            state: BlockState,
            level: LevelAccessor,
            pos: BlockPos,
            direction: Rotation,
        ): BlockState = state.setValue(
            BlockStateProperties.HORIZONTAL_FACING,
            direction.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)),
        )

        override fun mirror(state: BlockState, mirror: Mirror): BlockState = state.setValue(
            BlockStateProperties.HORIZONTAL_FACING,
            mirror.mirror(state.getValue(BlockStateProperties.HORIZONTAL_FACING)),
        )
    }
}
