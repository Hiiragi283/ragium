package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTWaterGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTKineticGeneratorBlockEntity(RagiumBlockEntityTypes.WATER_GENERATOR, pos, state) {
    override fun canProvidePower(world: World, pos: BlockPos, state: BlockState): Boolean =
        Direction.entries.filter { !world.getFluidState(pos.offset(it)).isStill }.size >= 2
}
