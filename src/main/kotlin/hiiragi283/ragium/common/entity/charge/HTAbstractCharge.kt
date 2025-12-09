package hiiragi283.ragium.common.entity.charge

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.world.getRangedAABB
import hiiragi283.ragium.common.HTChargeType
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import org.apache.commons.lang3.math.Fraction

abstract class HTAbstractCharge : ThrowableItemProjectile {
    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level) : super(entityType, level)

    constructor(chargeType: HTChargeType, level: Level, shooter: LivingEntity) : super(
        chargeType.getEntityType().get(),
        shooter,
        level,
    )

    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level, shooter: LivingEntity) : super(
        entityType,
        shooter,
        level,
    )

    constructor(chargeType: HTChargeType, level: Level, x: Double, y: Double, z: Double) : super(
        chargeType.getEntityType().get(),
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

    protected fun getPower(): Fraction = HTChargeType.getPower(item)

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
