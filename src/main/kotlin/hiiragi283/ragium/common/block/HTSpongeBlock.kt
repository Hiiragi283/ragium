package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FluidBlock
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.BiPredicate
import java.util.function.Consumer

class HTSpongeBlock(val replaced: () -> BlockState, val predicate: BiPredicate<World, BlockPos>) : Block(blockSettings()) {
    override fun onBlockAdded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        oldState: BlockState,
        notify: Boolean,
    ) {
        if (!oldState.isOf(state.block)) {
            update(world, pos)
        }
    }

    override fun neighborUpdate(
        state: BlockState,
        world: World,
        pos: BlockPos,
        sourceBlock: Block,
        sourcePos: BlockPos,
        notify: Boolean,
    ) {
        update(world, pos)
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify)
    }

    private fun update(world: World, pos: BlockPos) {
        if (absorbFluid(world, pos)) {
            world.setBlockState(pos, replaced(), NOTIFY_LISTENERS)
            world.playSound(null, pos, SoundEvents.BLOCK_SPONGE_ABSORB, SoundCategory.BLOCKS, 1.0f, 1.0f)
        }
    }

    private fun absorbFluid(world: World, pos: BlockPos): Boolean = BlockPos.iterateRecursively(
        pos,
        6,
        65,
        { currentPos: BlockPos, listeners: Consumer<BlockPos> ->
            Direction.entries
                .map(currentPos::offset)
                .forEach(listeners::accept)
        },
        { currentPos: BlockPos ->
            when (currentPos) {
                pos -> true
                else -> {
                    val state: BlockState = world.getBlockState(currentPos)
                    when {
                        !predicate.test(world, currentPos) -> false
                        else -> {
                            val block: Block = state.block
                            when {
                                // block is FluidDrainable && block.tryDrainFluid(null, world, currentPos, state).isEmpty -> true
                                else -> {
                                    when (block) {
                                        !is FluidBlock -> dropStacks(state, world, currentPos, world.getBlockEntity(currentPos))
                                        else -> Unit
                                    }
                                    world.setBlockState(currentPos, Blocks.AIR.defaultState, NOTIFY_ALL)
                                    true
                                }
                            }
                        }
                    }
                }
            }
        },
    ) > 1
}
