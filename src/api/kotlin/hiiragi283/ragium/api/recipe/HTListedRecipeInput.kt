package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

class HTListedRecipeInput(private val items: List<ItemStack>) : RecipeInput {
    operator fun get(index: Int): ItemStack = getItem(index)

    override fun getItem(index: Int): ItemStack = items.getOrNull(index) ?: ItemStack.EMPTY

    override fun size(): Int = items.size
}
