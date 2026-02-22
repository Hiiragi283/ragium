package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTItemToItemRecipe : HTProcessingRecipe<SingleRecipeInput> {
    fun getRequiredAmount(input: SingleRecipeInput): Int
}
