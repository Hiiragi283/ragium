package hiiragi283.ragium.common.entity

import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult

class HTSimpleDynamite : HTDynamite {
    constructor(type: EntityType<out HTSimpleDynamite>, level: Level) : super(type, level)

    constructor(level: Level, shooter: LivingEntity) : super(
        RagiumEntityTypes.DYNAMITE.get(),
        level,
        shooter,
    )

    override fun getDefaultItem(): Item = RagiumItems.DYNAMITE.get()

    override fun onHit(result: HitResult) {
        super.onHit(result)
        val level: Level = level()
        if (!level.isClientSide) {
            level.explode(
                this,
                x,
                y,
                z,
                item.getOrDefault(RagiumComponentTypes.EXPLOSION_POWER, 2f),
                false,
                Level.ExplosionInteraction.TNT,
            )
            discard()
        }
    }
}
