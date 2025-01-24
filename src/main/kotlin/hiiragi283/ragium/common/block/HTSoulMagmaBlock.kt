package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTSoulMagmaBlock(properties: Properties) : Block(properties) {
    override fun stepOn(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        entity: Entity,
    ) {
        if (!entity.isSteppingCarefully && entity is LivingEntity) {
            entity.hurt(level.damageSources().hotFloor(), 2f)
        }
        super.stepOn(level, pos, state, entity)
    }
}
