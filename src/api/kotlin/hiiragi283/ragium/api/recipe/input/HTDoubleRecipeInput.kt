package hiiragi283.ragium.api.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

data class HTDoubleRecipeInput(val first: ItemStack, val second: ItemStack) : RecipeInput {
    override fun getItem(index: Int): ItemStack = when (index) {
        0 -> first
        1 -> second
        else -> error("No item for index $index")
    }

    override fun size(): Int = 2
}
