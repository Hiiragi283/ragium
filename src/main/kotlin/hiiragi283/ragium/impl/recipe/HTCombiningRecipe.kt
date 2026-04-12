package hiiragi283.ragium.impl.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.util.HTShapelessRecipeHelper
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

abstract class HTCombiningRecipe(val ingredients: List<HTItemIngredient>, val result: HTItemResult, final override val time: Int) :
    HTProcessingRecipe.Serializable<HTShapelessRecipeInput> {
    final override fun test(input: HTShapelessRecipeInput): Boolean =
        !HTShapelessRecipeHelper.shapelessMatch(ingredients, input.items).isEmpty()

    final override fun assemble(input: HTShapelessRecipeInput, registries: HolderLookup.Provider): ItemStack =
        result.getStackOrEmpty(registries)
}
