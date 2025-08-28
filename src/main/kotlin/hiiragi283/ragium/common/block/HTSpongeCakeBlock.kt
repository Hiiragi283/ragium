package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTSpongeCakeBlock(properties: Properties) : Block(properties) {
    override fun fallOn(
        level: Level,
        state: BlockState,
        pos: BlockPos,
        entity: Entity,
        fallDistance: Float,
    ) {
        entity.causeFallDamage(fallDistance, 0f, level.damageSources().fall())
    }
}
