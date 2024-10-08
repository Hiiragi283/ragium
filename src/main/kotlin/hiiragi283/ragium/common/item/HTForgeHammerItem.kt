package hiiragi283.ragium.common.item

import hiiragi283.ragium.common.util.itemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

object HTForgeHammerItem : Item(itemSettings().maxDamage(63)) {
    override fun getRecipeRemainder(stack: ItemStack): ItemStack = when {
        stack.damage < stack.maxDamage -> {
            val stack1: ItemStack = stack.copy()
            stack1.damage++
            stack1
        }

        else -> ItemStack.EMPTY
    }
}
