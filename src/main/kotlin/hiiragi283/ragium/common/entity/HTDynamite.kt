package hiiragi283.ragium.common.entity

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class HTDynamite : ThrowableItemProjectile {
    companion object {
        @JvmStatic
        fun <T : HTDynamite> withItem(
            level: Level,
            shooter: LivingEntity,
            stack: ItemStack,
            factory: (Level, LivingEntity) -> T,
        ): T {
            val dynamite: T = factory(level, shooter)
            dynamite.item = stack
            return dynamite
        }
    }

    constructor(type: EntityType<out HTDynamite>, level: Level) : super(type, level)

    constructor(type: EntityType<out HTDynamite>, level: Level, shooter: LivingEntity) : super(
        type,
        shooter,
        level,
    )
}
