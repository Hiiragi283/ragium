package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTItemToItemRecipeBuilder<R : HTItemToItemRecipe>(
    private val prefix: String,
    private val factory: (HTItemIngredient, HTItemResult) -> R,
    val ingredient: HTItemIngredient,
    val result: HTItemResult,
) : HTRecipeBuilder {
    companion object {
        @JvmStatic
        fun extracting(ingredient: HTItemIngredient, result: HTItemResult): HTItemToItemRecipeBuilder<HTExtractingRecipe> =
            HTItemToItemRecipeBuilder(RagiumConst.EXTRACTING, ::HTExtractingRecipe, ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun group(groupName: String?): RecipeBuilder = throw UnsupportedOperationException()

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        recipeOutput.accept(id.withPrefix("$prefix/"), factory(ingredient, result), null)
    }
}
