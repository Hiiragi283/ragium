package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

data class HTInfusingRecipeInput(val stack: ItemStack, val cost: Float) : RecipeInput {
    override fun getItem(index: Int): ItemStack = stack

    override fun size(): Int = 1
}
