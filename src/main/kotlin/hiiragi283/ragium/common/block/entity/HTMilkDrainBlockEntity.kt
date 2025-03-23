package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.entity.HTFluidWellBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.fluids.FluidStack

class HTMilkDrainBlockEntity(pos: BlockPos, state: BlockState) : HTFluidWellBlockEntity(RagiumBlockEntityTypes.MILK_DRAIN, pos, state) {
    override fun getGeneratedFluid(level: Level, pos: BlockPos): FluidStack {
        val cows: List<Cow> = level.getEntitiesOfClass(Cow::class.java, AABB(pos.above()))
        return FluidStack(NeoForgeMod.MILK, cows.size * 50)
    }
}
