package hiiragi283.ragium.common.block.fluid

import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FlowingFluid

/**
 * @see blusunrize.immersiveengineering.common.fluids.IEFluidBlock
 */
class HTEffectLiquidBlock(private val instance: Triple<Holder<MobEffect>, Int, Int>, fluid: FlowingFluid, properties: Properties) :
    HTLiquidBlock(fluid, properties) {
    override fun livingInside(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        entity: LivingEntity,
    ) {
        val (effect: Holder<MobEffect>, duration: Int, amplifier: Int) = instance
        entity.addEffect(MobEffectInstance(effect, duration, amplifier))
    }
}
