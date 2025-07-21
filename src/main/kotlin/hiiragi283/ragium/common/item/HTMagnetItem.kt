package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.getRangedAABB
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class HTMagnetItem<T : Entity>(properties: Properties) : HTRangedItem(properties) {
    override fun inventoryTick(
        stack: ItemStack,
        level: Level,
        entity: Entity,
        slotId: Int,
        isSelected: Boolean,
    ) {
        val player: Player = entity as? Player ?: return
        if (!isActive(stack)) return
        val range: Double = getRange(stack, level) * 2.0
        val entitiesInRange: List<T> = level.getEntitiesOfClass(
            entityClass,
            player.position().getRangedAABB(range),
        )
        for (entity: T in entitiesInRange) {
            forEachEntity(entity, player)
        }
    }

    protected abstract val entityClass: Class<T>

    protected abstract fun forEachEntity(entity: T, player: Player)
}
