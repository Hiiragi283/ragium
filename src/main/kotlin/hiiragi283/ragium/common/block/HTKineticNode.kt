package hiiragi283.ragium.common.block

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

fun interface HTKineticNode {
    fun findProcessor(world: World, pos: BlockPos, from: Direction): BlockPos?
}
