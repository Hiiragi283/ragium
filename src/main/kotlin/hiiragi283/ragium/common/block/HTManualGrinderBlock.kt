package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTManualGrinderBlockEntity
import hiiragi283.ragium.common.init.RagiumBlocks.Properties.LEVEL_7
import hiiragi283.ragium.common.util.modifyBlockState
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object HTManualGrinderBlock : HTBlockWithEntity(Settings.copy(Blocks.SMOOTH_STONE)) {
    init {
        defaultState = stateManager.defaultState.with(LEVEL_7, 0)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val step: Int = state.get(LEVEL_7)
        if (step == 7) {
            (world.getBlockEntity(pos) as? HTManualGrinderBlockEntity)?.process()
        }
        if (!world.isClient) {
            world.modifyBlockState(pos) { stateIn: BlockState ->
                stateIn.with(LEVEL_7, (step + 1) % 8)
            }
        }
        world.playSoundAtBlockCenter(
            pos,
            SoundEvents.BLOCK_GRINDSTONE_USE,
            SoundCategory.BLOCKS,
            1.0f,
            1.0f,
            false,
        )
        return ActionResult.success(world.isClient)
    }

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = state.get(LEVEL_7)

    override fun hasComparatorOutput(state: BlockState): Boolean = true

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(LEVEL_7)
    }

    override fun hasSidedTransparency(state: BlockState): Boolean = true

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTManualGrinderBlockEntity(pos, state)
}
