package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTWindGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTKineticGeneratorBlockEntity(RagiumBlockEntityTypes.WIND_GENERATOR, pos, state) {
    override fun canProvidePower(world: World, pos: BlockPos, state: BlockState): Boolean = pos.y >= 128
}
