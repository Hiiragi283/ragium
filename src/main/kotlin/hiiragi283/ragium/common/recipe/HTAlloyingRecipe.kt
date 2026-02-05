package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTAlloyingRecipe(
    val ingredients: List<HTItemIngredient>,
    result: HTItemResult,
    extraResults: List<HTChancedItemResult>,
    parameters: SubParameters,
) : HTChancedRecipe<HTShapelessRecipeInput>(result, extraResults, parameters) {
    override fun matches(input: HTShapelessRecipeInput, level: Level): Boolean =
        !HTShapelessRecipeHelper.shapelessMatch(ingredients, input.items).isEmpty()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALLOYING.get()
}
