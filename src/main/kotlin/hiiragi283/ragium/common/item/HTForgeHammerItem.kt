package hiiragi283.ragium.common.item

import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

object HTForgeHammerItem : Item(Settings().maxDamage(63)) {
    override fun getRecipeRemainder(stack: ItemStack): ItemStack {
        val stack1: ItemStack = stack.copy()
        stack1.apply(DataComponentTypes.DAMAGE, 0) { it - 1 }
        return when {
            stack.getOrDefault(DataComponentTypes.DAMAGE, 0) <= 0 -> ItemStack.EMPTY
            else -> stack1
        }
    }

    override fun hasRecipeRemainder(): Boolean = true
}
