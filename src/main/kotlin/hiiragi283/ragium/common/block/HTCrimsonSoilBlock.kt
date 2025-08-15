package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.FakePlayerFactory

class HTCrimsonSoilBlock(properties: Properties) : Block(properties) {
    override fun stepOn(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        entity: Entity,
    ) {
        // エンティティがスニークしていない場合はプレイヤー由来のダメージを与える
        if (!entity.isSteppingCarefully && entity is LivingEntity && level is ServerLevel) {
            entity.hurt(level.damageSources().playerAttack(FakePlayerFactory.getMinecraft(level)), 2f)
        }
        super.stepOn(level, pos, state, entity)
    }
}
