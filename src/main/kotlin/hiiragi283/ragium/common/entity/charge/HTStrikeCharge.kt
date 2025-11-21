package hiiragi283.ragium.common.entity.charge

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.common.variant.HTChargeVariant
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LightningBolt
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

class HTStrikeCharge : HTAbstractCharge {
    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level) : super(entityType, level)

    constructor(level: Level, shooter: LivingEntity) : super(HTChargeVariant.STRIKE, level, shooter)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        HTChargeVariant.STRIKE,
        level,
        x,
        y,
        z,
    )

    override fun onHit(level: ServerLevel, result: Either<EntityHitResult, BlockHitResult>) {
        // エンティティに当たった場合は対象のみ，ブロックの場合は範囲内のモブ
        val (entities: List<Entity>, damage: Float) = result.map(
            { listOf(it.entity) to getPower() },
            { getAffectedEntities<Mob>() to 0f },
        )
        for (entity: Entity in entities) {
            val bolt: LightningBolt = EntityType.LIGHTNING_BOLT.create(level) ?: continue
            bolt.moveTo(entity.position())
            bolt.damage = damage
            level.addFreshEntity(bolt)
            entity.extinguishFire()
        }
    }

    override fun getDefaultItem(): Item = HTChargeVariant.STRIKE.asItem()
}
