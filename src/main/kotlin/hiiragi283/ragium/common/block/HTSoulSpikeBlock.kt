package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTSoulSpikeBlock(properties: Properties) : Block(properties) {
    override fun stepOn(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        entity: Entity,
    ) {
        // エンティティがスニークしていない場合はプレイヤー由来のダメージを与える
        if (!entity.isSteppingCarefully && entity is LivingEntity && level is ServerLevel) {
            entity.hurt(level.damageSources().playerAttack(RagiumAPI.getInstance().getFakePlayer(level)), 2f)
        }
        super.stepOn(level, pos, state, entity)
    }
}
