package hiiragi283.ragium.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.ItemActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

class HTRopeBlock(settings: Settings) :
    Block(
        settings
            .burnable()
            .noCollision()
            .pistonBehavior(PistonBehavior.DESTROY),
    ) {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
    }

    override fun onUseWithItem(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult,
    ): ItemActionResult = if (stack.isOf(this.asItem())) {
        val placablePos: BlockPos =
            getPlacablePos(world, pos.down()) ?: return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        if (!world.isClient) {
            world.setBlockState(placablePos, defaultState)
            stack.decrementUnlessCreative(1, player)
            world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS)
        }
        ItemActionResult.success(world.isClient)
    } else {
        super.onUseWithItem(stack, state, world, pos, player, hand, hit)
    }

    private fun getPlacablePos(world: World, pos: BlockPos): BlockPos? {
        val state: BlockState = world.getBlockState(pos)
        return when {
            state.isOf(this) -> getPlacablePos(world, pos.down())
            state.isAir -> pos
            else -> null
        }
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos,
    ): BlockState {
        if (!state.canPlaceAt(world, pos)) {
            world.scheduleBlockTick(pos, this, 1)
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun scheduledTick(
        state: BlockState,
        world: ServerWorld,
        pos: BlockPos,
        random: Random,
    ) {
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true)
        }
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val posUp: BlockPos = pos.up()
        val stateUp: BlockState = world.getBlockState(posUp)
        return stateUp.isSideSolidFullSquare(world, posUp, Direction.DOWN) || stateUp.isOf(this)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = SHAPE
}
