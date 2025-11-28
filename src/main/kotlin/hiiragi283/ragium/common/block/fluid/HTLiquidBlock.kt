package hiiragi283.ragium.common.block.fluid

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FlowingFluid

abstract class HTLiquidBlock(fluid: FlowingFluid, properties: Properties) : LiquidBlock(fluid, properties) {
    override fun entityInside(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        entity: Entity,
    ) {
        super.entityInside(state, level, pos, entity)
        if (entity is LivingEntity) {
            livingInside(state, level, pos, entity)
        }
    }

    protected abstract fun livingInside(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        entity: LivingEntity,
    )
}
