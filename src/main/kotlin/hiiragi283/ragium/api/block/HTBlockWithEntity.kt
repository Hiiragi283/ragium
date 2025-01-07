package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.block.entity.HTBlockEntityBase
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

/**
 * Ragiumで使用する[BlockEntityProvider]を実装した[Block]クラス
 */
abstract class HTBlockWithEntity(settings: Settings) :
    Block(settings),
    BlockEntityProvider {
    companion object {
        /**
         * 指定された[type]と[settings]からブロックを返します。。
         */
        @JvmStatic
        fun build(type: BlockEntityType<*>, settings: Settings): Block = object : HTBlockWithEntity(settings) {
            init {
                type.addSupportedBlock(this)
            }

            override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? = type.instantiate(pos, state)
        }

        /**
         * 指定された[type]と[settings]から水平方向の回転が可能なブロックを返します。
         */
        @JvmStatic
        fun buildFacing(type: BlockEntityType<*>, settings: Settings): Block = object : Facing(settings) {
            init {
                type.addSupportedBlock(this)
            }

            override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? = type.instantiate(pos, state)
        }

        /**
         * 指定された[type]と[settings]から水平方向の回転が可能なブロックを返します。
         */
        @JvmStatic
        fun buildHorizontal(type: BlockEntityType<*>, settings: Settings): Block = object : Horizontal(settings) {
            init {
                type.addSupportedBlock(this)
            }

            override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? = type.instantiate(pos, state)
        }
    }

    final override fun createScreenHandlerFactory(state: BlockState, world: World, pos: BlockPos): NamedScreenHandlerFactory? =
        (world.getBlockEntity(pos) as? NamedScreenHandlerFactory)?.takeUnless { world.isClient }

    final override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = (world.getBlockEntity(pos) as? HTBlockEntityBase)?.onRightClicked(state, world, pos, player, hit)
        ?: ActionResult.PASS

    final override fun onBlockBreakStart(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
        (world.getBlockEntity(pos) as? HTBlockEntityBase)?.onLeftClicked(state, world, pos, player)
    }

    final override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack,
    ) {
        (world.getBlockEntity(pos) as? HTBlockEntityBase)?.onPlaced(world, pos, state, placer, itemStack)
    }

    final override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean,
    ) {
        if (!state.isOf(newState.block)) {
            (world.getBlockEntity(pos) as? HTBlockEntityBase)?.onStateReplaced(state, world, pos, newState, moved)
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int =
        (world.getBlockEntity(pos) as? HTBlockEntityBase)?.getComparatorOutput(state, world, pos) ?: 0

    final override fun hasComparatorOutput(state: BlockState): Boolean = true

    final override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T> =
        BlockEntityTicker { world1: World, pos: BlockPos, state1: BlockState, blockEntity: T ->
            (blockEntity as? HTBlockEntityBase)?.tick(world1, pos, state1)
        }

    //    Facing    //

    /**
     * 六方向の回転が可能な[HTBlockWithEntity]クラス
     */
    abstract class Facing(settings: Settings) : HTBlockWithEntity(settings) {
        init {
            defaultState = stateManager.defaultState.with(Properties.FACING, Direction.NORTH)
        }

        override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
            defaultState.with(Properties.FACING, ctx.playerLookDirection.opposite)

        override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
            builder.add(Properties.FACING)
        }

        override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
            state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)))

        override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
            state.with(Properties.FACING, mirror.apply(state.get(Properties.FACING)))
    }

    //    Horizontal    //

    /**
     * 水平方向の回転が可能な[HTBlockWithEntity]クラス
     */
    abstract class Horizontal(settings: Settings) : HTBlockWithEntity(settings) {
        init {
            defaultState = stateManager.defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
        }

        override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
            defaultState.with(Properties.HORIZONTAL_FACING, ctx.horizontalPlayerFacing.opposite)

        override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
            builder.add(Properties.HORIZONTAL_FACING)
        }

        override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
            state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)))

        override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
            state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)))
    }
}
