package hiiragi283.ragium.common.entity

import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class HTPoisonDynamite : HTRangedEntityDynamite<LivingEntity> {
    constructor(type: EntityType<out HTPoisonDynamite>, level: Level) : super(type, level)

    constructor(level: Level, shooter: LivingEntity) : super(
        RagiumEntityTypes.POISON_DYNAMITE.get(),
        level,
        shooter,
    )

    override fun getDefaultItem(): Item = RagiumItems.POISON_DYNAMITE.get()

    override val entityClass: Class<LivingEntity> = LivingEntity::class.java

    override fun forEachEntity(entity: LivingEntity) {
        entity.addEffect(
            MobEffectInstance(MobEffects.POISON, 20 * 30),
            this.owner,
        )
    }
}
