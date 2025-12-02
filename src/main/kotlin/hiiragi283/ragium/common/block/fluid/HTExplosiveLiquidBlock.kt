package hiiragi283.ragium.common.block.fluid

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.phys.BlockHitResult

class HTExplosiveLiquidBlock(fluid: FlowingFluid, properties: Properties) : LiquidBlock(fluid, properties) {
    override fun onProjectileHit(
        level: Level,
        state: BlockState,
        hit: BlockHitResult,
        projectile: Projectile,
    ) {
        if (!level.isClientSide) {
            val hitPos: BlockPos = hit.blockPos
            if (projectile.isOnFire && projectile.mayInteract(level, hitPos)) {
                onCaughtFire(state, level, hitPos, null, projectile.owner as? LivingEntity)
                level.removeBlock(hitPos, false)
            }
        }
    }

    override fun onCaughtFire(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        direction: Direction?,
        igniter: LivingEntity?,
    ) {
        super.onCaughtFire(state, level, pos, direction, igniter)
        explode(level, pos)
    }

    override fun onBlockExploded(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        explosion: Explosion,
    ) {
        super.onBlockExploded(state, level, pos, explosion)
        explode(level, pos)
    }

    private fun explode(level: Level, pos: BlockPos) {
        if (!level.isClientSide) {
            level.explode(
                null,
                Explosion.getDefaultDamageSource(level, null),
                null,
                pos.x + 0.5,
                pos.y + 0.5,
                pos.z + 0.5,
                4f,
                false,
                Level.ExplosionInteraction.TNT,
            )
        }
    }
}
