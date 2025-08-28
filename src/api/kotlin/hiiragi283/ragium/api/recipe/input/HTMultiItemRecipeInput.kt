package hiiragi283.ragium.api.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

data class HTMultiItemRecipeInput(val items: List<ItemStack>) : RecipeInput {
    constructor(vararg items: ItemStack) : this(items.toList())

    override fun getItem(index: Int): ItemStack = items[index]

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty() || items.all(ItemStack::isEmpty)
}
