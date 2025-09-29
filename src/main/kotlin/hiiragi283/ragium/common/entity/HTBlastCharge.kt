package hiiragi283.ragium.common.entity

import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult

class HTBlastCharge : ThrowableItemProjectile {
    constructor(entityType: EntityType<out HTBlastCharge>, level: Level) : super(entityType, level)

    constructor(level: Level, shooter: LivingEntity) : super(RagiumEntityTypes.BLAST_CHARGE.get(), shooter, level)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        RagiumEntityTypes.BLAST_CHARGE.get(),
        x,
        y,
        z,
        level,
    )

    override fun onHit(result: HitResult) {
        super.onHit(result)
        if (!level().isClientSide) {
            val power: Float = item.getOrDefault(RagiumDataComponents.BLAST_POWER, 1f)
            level().explode(this, x, y, z, power, false, Level.ExplosionInteraction.MOB)
            discard()
        }
    }

    override fun getDefaultItem(): Item = RagiumItems.BLAST_CHARGE.get()
}
