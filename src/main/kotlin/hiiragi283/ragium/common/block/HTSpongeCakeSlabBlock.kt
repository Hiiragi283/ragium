package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.state.BlockState

class HTSpongeCakeSlabBlock(properties: Properties) : SlabBlock(properties) {
    override fun fallOn(
        level: Level,
        state: BlockState,
        pos: BlockPos,
        entity: Entity,
        fallDistance: Float,
    ) {
        entity.causeFallDamage(fallDistance, 0.2f, level.damageSources().fall())
    }
}
