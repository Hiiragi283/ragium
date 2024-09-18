package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTBaseBlockEntity
import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTBlockWithEntity(settings: Settings) :
    Block(settings),
    BlockEntityProvider {
    override fun createScreenHandlerFactory(state: BlockState, world: World, pos: BlockPos): NamedScreenHandlerFactory? =
        world.getBlockEntity(pos) as? NamedScreenHandlerFactory

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = (world.getBlockEntity(pos) as? HTBaseBlockEntity)?.onUse(state, world, pos, player, hit) ?: ActionResult.PASS

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean,
    ) {
        ItemScatterer.onStateReplaced(state, newState, world, pos)
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    //    Builder    //

    class Builder<B : HTBaseBlockEntity> {
        lateinit var type: BlockEntityType<out B>
            private set
        private var ticker: BlockEntityTicker<out B>? = null

        fun type(type: BlockEntityType<out B>): Builder<B> = apply { this.type = type }

        fun ticker(ticker: BlockEntityTicker<out B>): Builder<B> = apply { this.ticker = ticker }

        fun build(settings: Settings = blockSettings()): HTBlockWithEntity = object : HTBlockWithEntity(settings) {
            override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = type.instantiate(pos, state)

            override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? =
                HTBlockEntityTickers.validateTicker(type, type, ticker)
        }

        fun buildHorizontal(settings: Settings = blockSettings()): HTBlockWithEntity = object : HTBlockWithEntity(settings) {
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

            override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int =
                (world.getBlockEntity(pos) as? HTBaseBlockEntity)?.getComparatorOutput(state, world, pos) ?: 0

            override fun hasComparatorOutput(state: BlockState): Boolean = true

            override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = type.instantiate(pos, state)

            override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? =
                HTBlockEntityTickers.validateTicker(type, type, ticker)
        }
    }
}
