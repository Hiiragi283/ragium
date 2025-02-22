package hiiragi283.ragium.api.inventory

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

/**
 * @see [de.ellpeck.actuallyadditions.mod.inventory.slot.SlotImmovable]
 */
class HTImmovableSlot(
    container: Container,
    index: Int,
    xPosition: Int,
    yPosition: Int,
) : Slot(container, index, xPosition, yPosition) {
    override fun mayPlace(stack: ItemStack): Boolean = false

    override fun set(stack: ItemStack) {}

    override fun remove(amount: Int): ItemStack = ItemStack.EMPTY

    override fun mayPickup(playerIn: Player): Boolean = false
}
