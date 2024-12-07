package hiiragi283.ragium.api.machine.multiblock

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface HTMultiblockPatternProvider {
    val multiblockManager: HTMultiblockManager

    fun beforeBuild(world: World, pos: BlockPos, player: PlayerEntity?) {}

    fun buildMultiblock(builder: HTMultiblockBuilder)
}
