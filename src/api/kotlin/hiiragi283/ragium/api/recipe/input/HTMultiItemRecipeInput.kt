package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 複数の[ItemStack]を受け取る[RecipeInput]の実装
 */
data class HTMultiItemRecipeInput(val items: List<ItemStack>) : RecipeInput {
    companion object {
        @JvmStatic
        fun fromSlots(slots: List<HTItemSlot>): HTMultiItemRecipeInput = HTMultiItemRecipeInput(slots.map(HTItemSlot::getStack))

        @JvmStatic
        fun fromSlots(vararg slots: HTItemSlot): HTMultiItemRecipeInput = fromSlots(slots.toList())
    }

    override fun getItem(index: Int): ItemStack = items[index]

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty() || items.all(ItemStack::isEmpty)
}
