package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTLavaCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidCollectorBlockEntity(RagiumBlocks.LAVA_COLLECTOR, pos, state) {
    override fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack {
        // ネザー系バイオームにない場合は生産しない
        if (!level.getBiome(pos).`is`(BiomeTags.IS_NETHER)) return FluidStack.EMPTY
        // 周囲に4ブロック以上の溶岩がある -> 100 mB
        val lavaSources: Int = Direction.Plane.HORIZONTAL
            .count { direction: Direction -> level.getFluidState(pos.relative(direction)).`is`(FluidTags.LAVA) }
        return if (lavaSources >= 4) HTFluidContent.LAVA.toStack(100) else FluidStack.EMPTY
    }

    override fun playSound(level: ServerLevel, pos: BlockPos) {
        level.playSound(null, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS)
    }
}
