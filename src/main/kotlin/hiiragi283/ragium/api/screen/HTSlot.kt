package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.extension.getStackOrNull
import net.minecraft.inventory.Inventory
import net.minecraft.screen.slot.Slot

open class HTSlot(
    inventory: Inventory,
    index: Int,
    x: Int,
    y: Int,
) : Slot(inventory, index, x, y) {
    override fun isEnabled(): Boolean = inventory.getStackOrNull(index) != null
}
