package hiiragi283.ragium.common.entity.charge

import hiiragi283.ragium.api.world.getRangedAABB
import hiiragi283.ragium.common.variant.HTChargeVariant
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult

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

    override fun onHit(result: HitResult) {
        super.onHit(result)
        val level: ServerLevel = level() as? ServerLevel ?: return
        val targets: List<LivingEntity> = level.getEntitiesOfClass(LivingEntity::class.java, position().getRangedAABB(getPower()))

        for (i: Int in targets.indices) {
            val current: LivingEntity = targets[i]
            val next: LivingEntity = targets.getOrNull(i + 1) ?: continue
            if (current is Mob && next !is Player) {
                current.target = next
            }
        }
        discard()
    }

    override fun getDefaultItem(): Item = HTChargeVariant.CONFUSING.asItem()
}
