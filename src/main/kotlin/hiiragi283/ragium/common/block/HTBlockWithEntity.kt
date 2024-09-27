package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTBlockEntityBase
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
    companion object {
        @JvmField
        val TICKER: BlockEntityTicker<HTBlockEntityBase> =
            BlockEntityTicker { world: World, pos: BlockPos, state: BlockState, blockEntity: HTBlockEntityBase ->
                blockEntity.tick(world, pos, state)
            }

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <E : BlockEntity, A : BlockEntity> validateTicker(
            givenType: BlockEntityType<A>,
            expectedType: BlockEntityType<E>,
            ticker: BlockEntityTicker<*>?,
        ): BlockEntityTicker<A>? = when (expectedType == givenType) {
            true -> ticker
            false -> null
        } as? BlockEntityTicker<A>

        @JvmStatic
        fun build(type: BlockEntityType<*>, settings: Settings): Block = object : HTBlockWithEntity(settings) {
            init {
                type.addSupportedBlock(this)
            }

            override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? = type.instantiate(pos, state)
        }

        @JvmStatic
        fun buildHorizontal(type: BlockEntityType<*>, settings: Settings): Block = object : HTBlockWithEntity(settings) {
            init {
                type.addSupportedBlock(this)
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

            override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? = type.instantiate(pos, state)
        }
    }

    override fun createScreenHandlerFactory(state: BlockState, world: World, pos: BlockPos): NamedScreenHandlerFactory? =
        (world.getBlockEntity(pos) as? NamedScreenHandlerFactory)?.takeUnless { world.isClient }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = (world.getBlockEntity(pos) as? HTBlockEntityBase)?.onUse(state, world, pos, player, hit) ?: ActionResult.PASS

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

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int =
        (world.getBlockEntity(pos) as? HTBlockEntityBase)?.getComparatorOutput(state, world, pos) ?: 0

    override fun hasComparatorOutput(state: BlockState): Boolean = true

    override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? =
        BlockEntityTicker { world1: World, pos: BlockPos, state1: BlockState, blockEntity: T ->
            (blockEntity as? HTBlockEntityBase)?.let { TICKER.tick(world1, pos, state1, it) }
        }
}
