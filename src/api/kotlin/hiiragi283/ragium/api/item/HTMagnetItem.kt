package hiiragi283.ragium.api.item

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB

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
        val range: Int = getRange(stack, level)
        val entitiesInRange: List<T> = level.getEntitiesOfClass(
            entityClass,
            AABB(
                player.x - range,
                player.y - range,
                player.z - range,
                player.x + range,
                player.y + range,
                player.z + range,
            ),
        )
        for (entity: T in entitiesInRange) {
            forEachEntity(entity, player)
        }
    }

    protected abstract val entityClass: Class<T>

    protected abstract fun forEachEntity(entity: T, player: Player)
}
