package hiiragi283.ragium.common.item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class HTForgeHammerItem(settings: Settings) : Item(settings) {
    override fun getRecipeRemainder(stack: ItemStack): ItemStack = when {
        stack.damage < stack.maxDamage -> {
            val stack1: ItemStack = stack.copy()
            stack1.damage++
            stack1
        }

        else -> ItemStack.EMPTY
    }
}
