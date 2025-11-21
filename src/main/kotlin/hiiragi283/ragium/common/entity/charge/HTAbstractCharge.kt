package hiiragi283.ragium.common.entity.charge

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.world.getRangedAABB
import hiiragi283.ragium.common.variant.HTChargeVariant
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

abstract class HTAbstractCharge : ThrowableItemProjectile {
    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level) : super(entityType, level)

    constructor(variant: HTChargeVariant, level: Level, shooter: LivingEntity) : super(
        variant.getEntityType().get(),
        shooter,
        level,
    )

    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level, shooter: LivingEntity) : super(
        entityType,
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

    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level, x: Double, y: Double, z: Double) : super(
        entityType,
        x,
        y,
        z,
        level,
    )

    protected fun getPower(): Float = item.getOrDefault(RagiumDataComponents.CHARGE_POWER, 1f)

    protected inline fun <reified T : Entity> getAffectedEntities(): List<T> = level()
        .getEntitiesOfClass(T::class.java, position().getRangedAABB(getPower()))
        .filterNot { it == owner }

    protected fun getOwnerOrSelf(): Entity = this.owner ?: this

    final override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val level: ServerLevel = this.level() as? ServerLevel ?: return
        onHit(level, Either.left(result))
        discard()
    }

    final override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        val level: ServerLevel = this.level() as? ServerLevel ?: return
        onHit(level, Either.right(result))
        discard()
    }

    protected abstract fun onHit(level: ServerLevel, result: Either<EntityHitResult, BlockHitResult>)
}
