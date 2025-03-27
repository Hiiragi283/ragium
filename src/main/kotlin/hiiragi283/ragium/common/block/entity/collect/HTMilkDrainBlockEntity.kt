package hiiragi283.ragium.common.block.entity.collect

import hiiragi283.ragium.api.block.entity.HTFluidCollectorBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.fluids.FluidStack

class HTMilkDrainBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidCollectorBlockEntity(RagiumBlockEntityTypes.MILK_DRAIN, pos, state) {
    override fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack {
        val cows: List<Cow> = level.getEntitiesOfClass(Cow::class.java, AABB(pos.above()))
        return FluidStack(NeoForgeMod.MILK, cows.size * 50)
    }

    override fun playSound(level: ServerLevel, pos: BlockPos) {
        level.playSound(null, pos, SoundEvents.COW_MILK, SoundSource.BLOCKS)
    }
}
