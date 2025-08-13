package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.extension.toCenterVec3
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class HTExplosiveFluidType(private val power: Float, properties: Properties) : FluidType(properties) {
    companion object {
        @JvmStatic
        fun create(power: Float): (Properties) -> HTExplosiveFluidType = { prop: Properties ->
            HTExplosiveFluidType(power, prop)
        }
    }

    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = level.dimensionType().ultraWarm

    override fun onVaporize(
        player: Player?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        super.onVaporize(player, level, pos, stack)
        level.explode(
            player,
            Explosion.getDefaultDamageSource(level, player),
            null,
            pos.toCenterVec3(),
            power,
            true,
            Level.ExplosionInteraction.BLOCK,
        )
    }
}
