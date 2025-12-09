package hiiragi283.ragium.common.block.fluid

import hiiragi283.ragium.api.item.alchemy.HTMobEffectInstance
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FlowingFluid

/**
 * @see blusunrize.immersiveengineering.common.fluids.IEFluidBlock
 */
class HTEffectLiquidBlock(private val instance: HTMobEffectInstance, fluid: FlowingFluid, properties: Properties) :
    HTLiquidBlock(fluid, properties) {
    override fun livingInside(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        entity: LivingEntity,
    ) {
        entity.addEffect(instance.toMutable())
    }
}
