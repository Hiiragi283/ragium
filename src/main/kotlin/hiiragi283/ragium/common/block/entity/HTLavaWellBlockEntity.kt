package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.entity.HTFluidWellBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack

class HTLavaWellBlockEntity(pos: BlockPos, state: BlockState) : HTFluidWellBlockEntity(RagiumBlockEntityTypes.LAVA_WELL, pos, state) {
    override fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack {
        // ネザー系バイオームにない場合は生産しない
        if (!level.getBiome(pos).`is`(BiomeTags.IS_NETHER)) return FluidStack.EMPTY
        // 周囲に4ブロック以上の溶岩がある -> 10 mB
        val lavaSources: Int = Direction.Plane.HORIZONTAL
            .count { direction: Direction -> level.getFluidState(pos.relative(direction)).`is`(FluidTags.LAVA) }
        return if (lavaSources >= 4) FluidStack(Fluids.LAVA, 10) else FluidStack.EMPTY
    }
}
