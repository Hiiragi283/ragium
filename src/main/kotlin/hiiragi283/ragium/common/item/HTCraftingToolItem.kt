package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.restDamage
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTCraftingToolItem(properties: Properties) : Item(properties) {
    override fun hasCraftingRemainingItem(stack: ItemStack): Boolean = true

    override fun getCraftingRemainingItem(itemStack: ItemStack): ItemStack {
        if (itemStack.restDamage > 0) {
            val copied: ItemStack = itemStack.copy()
            copied.damageValue++
            return copied
        }
        return super.getCraftingRemainingItem(itemStack)
    }
}
