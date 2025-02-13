package hiiragi283.ragium.common.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult

class HTDynamite : ThrowableItemProjectile {
    constructor(type: EntityType<out HTDynamite>, level: Level) : super(type, level)

    constructor(level: Level, shooter: LivingEntity) : super(RagiumEntityTypes.DYNAMITE.get(), shooter, level)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(RagiumEntityTypes.DYNAMITE.get(), x, y, z, level)

    override fun getDefaultItem(): Item = RagiumItems.DYNAMITE.get()

    override fun onHit(result: HitResult) {
        super.onHit(result)
        if (!level().isClientSide) {
            level().explode(
                this,
                x,
                y,
                z,
                RagiumAPI.getInstance().getDynamitePower(),
                false,
                Level.ExplosionInteraction.TNT,
            )
            discard()
        }
    }
}
