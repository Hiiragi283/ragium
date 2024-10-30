package hiiragi283.ragium.api.screen

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class HTOutputSlot(
    inventory: Inventory,
    index: Int,
    x: Int,
    y: Int,
) : HTSlot(inventory, index, x, y) {
    override fun canInsert(stack: ItemStack): Boolean = false
}
