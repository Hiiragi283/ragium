package hiiragi283.ragium.common.fluid

import hiiragi283.lib.fluid.HTFluidType
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack

class HTExplosiveFluidType(private val power: Float, properties: Properties) : HTFluidType(properties) {
    override fun onVaporize(entity: LivingEntity?, level: Level, pos: BlockPos, stack: FluidStack) {
        super.onVaporize(entity, level, pos, stack)
        level.explode(
            null,
            null,
            null,
            pos.center,
            power,
            true,
            Level.ExplosionInteraction.BLOCK,
        )
    }
}
