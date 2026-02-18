package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.util.HTShapelessRecipeHelper
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class HTCombineItemRecipe(val ingredients: List<HTItemIngredient>, val result: HTItemResult, parameters: SubParameters) :
    HTProcessingRecipe<HTShapelessRecipeInput>(parameters) {
    override fun matches(input: HTShapelessRecipeInput, level: Level): Boolean =
        !HTShapelessRecipeHelper.shapelessMatch(ingredients, input.items).isEmpty()

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
