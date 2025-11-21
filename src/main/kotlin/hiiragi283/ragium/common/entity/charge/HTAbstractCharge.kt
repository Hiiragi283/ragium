package hiiragi283.ragium.common.entity.charge

import hiiragi283.ragium.common.variant.HTChargeVariant
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult

abstract class HTAbstractCharge : ThrowableItemProjectile {
    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level) : super(entityType, level)

    constructor(variant: HTChargeVariant, level: Level, shooter: LivingEntity) : super(
        variant.getEntityType().get(),
        shooter,
        level,
    )

    constructor(variant: HTChargeVariant, level: Level, x: Double, y: Double, z: Double) : super(
        variant.getEntityType().get(),
        x,
        y,
        z,
        level,
    )

    fun getPower(): Float = item.getOrDefault(RagiumDataComponents.BLAST_POWER, 1f)

    final override fun onHit(result: HitResult) {
        super.onHit(result)
        val level: ServerLevel = this.level() as? ServerLevel ?: return
        onHit(level, result)
        discard()
    }

    protected abstract fun onHit(level: ServerLevel, result: HitResult)
}
