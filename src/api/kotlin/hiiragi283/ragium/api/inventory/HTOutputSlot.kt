package hiiragi283.ragium.api.inventory

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.SlotItemHandler

class HTOutputSlot(
    itemHandler: IItemHandler,
    index: Int,
    xPosition: Int,
    yPosition: Int,
) : SlotItemHandler(
        itemHandler,
        index,
        xPosition,
        yPosition,
    ) {
    override fun mayPlace(stack: ItemStack): Boolean = false
}
