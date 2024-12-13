package hiiragi283.ragium.api.machine.multiblock

import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Multiblock structure provider
 */
interface HTMultiblockPatternProvider {
    val multiblockManager: HTMultiblockManager

    /**
     * Called before [HTMultiblockManager.updateValidation]
     */
    fun beforeBuild(world: World?, pos: BlockPos, player: PlayerEntity?) {}

    /**
     * Provides multiblock structure for [builder]
     */
    fun buildMultiblock(builder: HTMultiblockBuilder)

    /**
     * Called after [HTMultiblockManager.updateValidation]
     */
    fun afterBuild(
        world: World?,
        pos: BlockPos,
        player: PlayerEntity?,
        stateMap: Map<BlockPos, Pair<BlockPos, BlockState>>,
    ) {
    }
}
