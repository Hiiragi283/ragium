package hiiragi283.ragium.common.entity.charge

import hiiragi283.ragium.api.world.getRangedAABB
import hiiragi283.ragium.common.variant.HTChargeVariant
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.event.EventHooks
import net.neoforged.neoforge.event.entity.EntityTeleportEvent

class HTTeleportCharge : HTAbstractCharge {
    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level) : super(entityType, level)

    constructor(level: Level, shooter: LivingEntity) : super(HTChargeVariant.TELEPORT, level, shooter)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        HTChargeVariant.TELEPORT,
        level,
        x,
        y,
        z,
    )

    override fun onHit(result: HitResult) {
        super.onHit(result)
        val level: ServerLevel = level() as? ServerLevel ?: return
        val targets: List<LivingEntity> = level.getEntitiesOfClass(LivingEntity::class.java, position().getRangedAABB(getPower()))
        for (entity: LivingEntity in targets) {
            // 所有者は対象外
            if (entity == this.owner) continue
            // イベントを発火する
            val event: EntityTeleportEvent.EnderEntity = EventHooks.onEnderTeleport(entity, x, y, z)
            if (event.isCanceled) continue
            entity.teleportTo(event.targetX, event.targetY, event.targetZ)
            entity.playSound(SoundEvents.ENDERMAN_TELEPORT)
        }
        discard()
    }

    override fun getDefaultItem(): Item = HTChargeVariant.TELEPORT.asItem()
}
