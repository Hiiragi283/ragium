package hiiragi283.ragium.api.machine.multiblock

import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface HTMultiblockPatternProvider {
    val multiblockManager: HTMultiblockManager

    fun beforeBuild(world: World?, pos: BlockPos, player: PlayerEntity?) {}

    fun buildMultiblock(builder: HTMultiblockBuilder)

    fun afterBuild(
        world: World?,
        pos: BlockPos,
        player: PlayerEntity?,
        stateMap: Map<BlockPos, Pair<BlockPos, BlockState>>,
    ) {
    }
}
