package hiiragi283.ragium.common.entity.charge

import hiiragi283.ragium.common.variant.HTChargeVariant
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.level.Level

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
}
