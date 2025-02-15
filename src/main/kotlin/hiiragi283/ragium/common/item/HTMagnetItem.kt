package hiiragi283.ragium.common.item

import net.minecraft.core.component.DataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB

class HTMagnetItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (!level.isClientSide && player.isShiftKeyDown) {
            val stack: ItemStack = player.getItemInHand(usedHand)
            val hasFoil: Boolean = stack.has(DataComponents.ENCHANTMENT_GLINT_OVERRIDE)
            stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, !hasFoil)
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
        }
        return super.use(level, player, usedHand)
    }

    override fun inventoryTick(
        stack: ItemStack,
        level: Level,
        entity: Entity,
        slotId: Int,
        isSelected: Boolean,
    ) {
        val player: Player = entity as? Player ?: return
        if (!player.isShiftKeyDown) {
            val range = 5
            val itemsInRange: List<ItemEntity> = level.getEntitiesOfClass(
                ItemEntity::class.java,
                AABB(
                    player.x - range,
                    player.y - range,
                    player.z - range,
                    player.x + range,
                    player.y + range,
                    player.z + range,
                ),
            )
            if (itemsInRange.isEmpty()) return
            for (itemEntity: ItemEntity in itemsInRange) {
                // Exclude items on IE Conveyor
                if (itemEntity.persistentData.getBoolean("PreventRemoteMovement")) continue
                if (itemEntity.isAlive && !itemEntity.hasPickUpDelay()) {
                    itemEntity.playerTouch(player)
                }
            }
        }
    }
}
