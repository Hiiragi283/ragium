package hiiragi283.ragium.common.entity.charge

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.common.HTChargeType
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.neoforged.neoforge.event.EventHooks
import net.neoforged.neoforge.event.entity.EntityTeleportEvent

class HTTeleportCharge : HTAbstractCharge {
    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level) : super(entityType, level)

    constructor(level: Level, shooter: LivingEntity) : super(HTChargeType.TELEPORT, level, shooter)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        HTChargeType.TELEPORT,
        level,
        x,
        y,
        z,
    )

    override fun onHit(level: ServerLevel, result: Either<EntityHitResult, BlockHitResult>) {
        val targets: List<LivingEntity> = getAffectedEntities()
        for (entity: LivingEntity in targets) {
            // 所有者は対象外
            if (entity == this.owner) continue
            // イベントを発火する
            val event: EntityTeleportEvent.EnderEntity = EventHooks.onEnderTeleport(entity, x, y, z)
            if (event.isCanceled) continue
            entity.teleportTo(event.targetX, event.targetY, event.targetZ)
            entity.playSound(SoundEvents.ENDERMAN_TELEPORT)
        }
    }

    override fun getDefaultItem(): Item = HTChargeType.TELEPORT.asItem()
}
