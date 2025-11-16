package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * @see com.enderio.base.common.item.tool.PoweredToggledItem
 */
abstract class HTActivatableItem(properties: Properties) : Item(properties.stacksTo(1)) {
    protected fun isActive(stack: ItemStack): Boolean = stack.getOrDefault(RagiumDataComponents.IS_ACTIVE, false)

    protected fun activate(stack: ItemStack) {
        val oldActive: Boolean? = stack.get(RagiumDataComponents.IS_ACTIVE)
        if (oldActive != true) {
            stack.set(RagiumDataComponents.IS_ACTIVE, true)
        }
    }

    protected fun inactivate(stack: ItemStack) {
        val oldActive: Boolean? = stack.get(RagiumDataComponents.IS_ACTIVE)
        if (oldActive == true) {
            stack.remove(RagiumDataComponents.IS_ACTIVE)
        }
    }

    final override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        if (player.isCrouching) {
            val stack: ItemStack = player.getItemInHand(usedHand)
            if (isActive(stack)) {
                inactivate(stack)
            } else {
                activate(stack)
            }
        }
        return super.use(level, player, usedHand)
    }

    final override fun inventoryTick(
        stack: ItemStack,
        level: Level,
        entity: Entity,
        slotId: Int,
        isSelected: Boolean,
    ) {
        if (entity is Player) {
            if (isActive(stack)) {
                onTick(stack, level, entity, slotId, isSelected)
            }
        }
    }

    protected abstract fun onTick(
        stack: ItemStack,
        level: Level,
        player: Player,
        slotId: Int,
        isSelected: Boolean,
    )

    final override fun isFoil(stack: ItemStack): Boolean = isActive(stack)
}
