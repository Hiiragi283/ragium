package hiiragi283.ragium.common.entity

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

abstract class HTRangedEntityDynamite<T : Entity> : HTDynamite {
    constructor(type: EntityType<out HTRangedEntityDynamite<*>>, level: Level) : super(
        type,
        level,
    )

    constructor(type: EntityType<out HTRangedEntityDynamite<*>>, level: Level, shooter: LivingEntity) : super(
        type,
        level,
        shooter,
    )

    override fun onHit(result: HitResult) {
        super.onHit(result)
        val level: Level = level()
        if (!level.isClientSide) {
            val hitPos: Vec3 = result.location
            val range: Int = RagiumAPI.getInstance().getEffectRange(item)
            level
                .getEntitiesOfClass(
                    entityClass,
                    AABB(
                        hitPos.x - range,
                        hitPos.y - range,
                        hitPos.z - range,
                        hitPos.x + range,
                        hitPos.y + range,
                        hitPos.z + range,
                    ),
                ).forEach(::forEachEntity)
            discard()
        }
    }

    protected abstract val entityClass: Class<T>

    protected abstract fun forEachEntity(entity: T)
}
