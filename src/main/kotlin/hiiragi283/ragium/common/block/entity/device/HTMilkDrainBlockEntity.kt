package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.fluids.FluidStack

class HTMilkDrainBlockEntity(pos: BlockPos, state: BlockState) : HTFluidCollectorBlockEntity(HTDeviceVariant.MILK_COLLECTOR, pos, state) {
    override fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack {
        val cows: List<Cow> = level.getEntitiesOfClass(Cow::class.java, AABB(pos.above()))
        return HTFluidContent.MILK.toStack(cows.size * RagiumAPI.getConfig().getMilkDrainMultiplier())
    }

    override fun playSound(level: ServerLevel, pos: BlockPos) {
        level.playSound(null, pos, SoundEvents.COW_MILK, SoundSource.BLOCKS)
    }
}
