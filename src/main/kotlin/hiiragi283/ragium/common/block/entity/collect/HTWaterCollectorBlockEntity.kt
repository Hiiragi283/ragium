package hiiragi283.ragium.common.block.entity.collect

import hiiragi283.ragium.api.block.entity.HTFluidCollectorBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack

class HTWaterCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidCollectorBlockEntity(RagiumBlockEntityTypes.WATER_COLLECTOR, pos, state) {
    override fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack {
        var amount = 0
        // 海洋バイオームまたは河川系バイオームの場合 -> +50 mB
        val biome: Holder<Biome> = level.getBiome(pos)
        if (biome.`is`(BiomeTags.IS_OCEAN) || biome.`is`(BiomeTags.IS_RIVER)) {
            amount += 50
        }
        // 周囲に2ブロック以上の水源がある -> +100 mB
        val waterSources: Int = Direction.Plane.HORIZONTAL
            .count { direction: Direction -> level.getFluidState(pos.relative(direction)).`is`(FluidTags.WATER) }
        if (waterSources >= 2) {
            amount += 100
        }
        return FluidStack(Fluids.WATER, amount)
    }

    override fun playSound(level: ServerLevel, pos: BlockPos) {
        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS)
    }
}
