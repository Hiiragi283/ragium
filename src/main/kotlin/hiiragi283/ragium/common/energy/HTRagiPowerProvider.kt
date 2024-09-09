package hiiragi283.ragium.common.energy

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

fun interface HTRagiPowerProvider {
    fun getPower(world: World, pos: BlockPos, state: BlockState, direction: Direction?): HTRagiPower
}