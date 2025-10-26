package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.entity.animal.MushroomCow
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.fluids.FluidStack

class HTMilkCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidCollectorBlockEntity(RagiumBlocks.MILK_COLLECTOR, pos, state) {
    override fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack {
        val area = AABB(pos.above())
        val multiplier: Int = RagiumConfig.COMMON.milkCollectorMultiplier.asInt
        // Milk from Cow
        val cows: List<Cow> = level.getEntitiesOfClass(Cow::class.java, area)
        if (cows.isNotEmpty()) {
            return HTFluidContent.MILK.toStack(cows.size * multiplier)
        }
        // Mushroom Stew from MushroomCow
        val cows1: List<MushroomCow> = level.getEntitiesOfClass(MushroomCow::class.java, area)
        if (cows1.isNotEmpty()) {
            return RagiumFluidContents.MUSHROOM_STEW.toStack(cows1.size * multiplier)
        }
        return FluidStack.EMPTY
    }

    override fun playSound(level: ServerLevel, pos: BlockPos) {
        level.playSound(null, pos, SoundEvents.COW_MILK, SoundSource.BLOCKS)
    }
}
