package hiiragi283.ragium.common.item

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTCatalystItem(properties: Properties) : Item(properties.stacksTo(1)) {
    override fun hasCraftingRemainingItem(stack: ItemStack): Boolean = true

    override fun getCraftingRemainingItem(itemStack: ItemStack): ItemStack = itemStack.copy()
}
