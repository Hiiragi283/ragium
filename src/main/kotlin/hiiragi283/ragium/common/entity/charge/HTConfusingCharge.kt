package hiiragi283.ragium.common.entity.charge

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.entity.isOf
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.variant.HTChargeVariant
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

class HTConfusingCharge : HTAbstractCharge {
    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level) : super(entityType, level)

    constructor(level: Level, shooter: LivingEntity) : super(HTChargeVariant.CONFUSING, level, shooter)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        HTChargeVariant.CONFUSING,
        level,
        x,
        y,
        z,
    )

    override fun onHit(level: ServerLevel, result: Either<EntityHitResult, BlockHitResult>) {
        val targets: List<Mob> = getAffectedEntities<Mob>()
            .filter { mob: Mob -> mob is Enemy && !mob.isOf(RagiumModTags.EntityTypes.CONFUSION_BLACKLIST) }

        for (i: Int in targets.indices) {
            val current: Mob = targets[i]
            // エンティティにあたるとターゲットを統一，ブロックの場合はシャッフル
            result.map(
                {
                    val livingEntity: LivingEntity = it.entity as? LivingEntity ?: return@map
                    if (livingEntity == current) return@map
                    current.target = livingEntity
                },
                {
                    val next: Mob = targets.getOrNull(i + 1) ?: return@map
                    current.target = next
                },
            )
        }
    }

    override fun getDefaultItem(): Item = HTChargeVariant.CONFUSING.asItem()
}
