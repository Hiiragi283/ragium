package hiiragi283.ragium.common.block.entity

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface HTKineticProcessor {
    fun getPos(): BlockPos

    fun onActive(world: World, pos: BlockPos)

    fun onInactive(world: World, pos: BlockPos)
}
