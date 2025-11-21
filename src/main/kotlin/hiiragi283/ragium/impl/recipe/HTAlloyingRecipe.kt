package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTMultiInputsToObjRecipe
import hiiragi283.ragium.api.recipe.multi.HTShapelessInputsRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTAlloyingRecipe(override val ingredients: List<HTItemIngredient>, result: HTItemResult) :
    HTBasicSingleOutputRecipe<HTMultiRecipeInput>(result),
    HTShapelessInputsRecipe {
    override fun test(input: HTMultiRecipeInput): Boolean = HTMultiInputsToObjRecipe.hasMatchingSlots(ingredients, input.items)

    override fun isIncomplete(): Boolean =
        ingredients.isEmpty() || ingredients.any(HTItemIngredient::hasNoMatchingStacks) || result.hasNoMatchingStack()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALLOYING.get()
}
