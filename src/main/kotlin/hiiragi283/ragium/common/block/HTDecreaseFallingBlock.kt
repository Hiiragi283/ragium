package hiiragi283.ragium.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTDecreaseFallingBlock(private val multiplier: Float, settings: Settings) : Block(settings) {
    override fun onLandedUpon(
        world: World,
        state: BlockState,
        pos: BlockPos,
        entity: Entity,
        fallDistance: Float,
    ) {
        entity.handleFallDamage(fallDistance, multiplier, world.damageSources.fall())
    }
}
