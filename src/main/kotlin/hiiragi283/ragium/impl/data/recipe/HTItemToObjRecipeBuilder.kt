package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.impl.recipe.HTCompressingRecipe
import hiiragi283.ragium.impl.recipe.HTExtractingRecipe
import hiiragi283.ragium.impl.recipe.HTMeltingRecipe
import hiiragi283.ragium.impl.recipe.HTPulverizingRecipe
import net.minecraft.resources.ResourceLocation

class HTItemToObjRecipeBuilder<RESULT : HTRecipeResult<*>>(
    prefix: String,
    private val factory: Factory<RESULT, *>,
    val ingredient: HTItemIngredient,
    val result: RESULT,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun compressing(ingredient: HTItemIngredient, result: HTItemResult): HTItemToObjRecipeBuilder<HTItemResult> =
            HTItemToObjRecipeBuilder(RagiumConst.COMPRESSING, ::HTCompressingRecipe, ingredient, result)

        @JvmStatic
        fun extracting(ingredient: HTItemIngredient, result: HTItemResult): HTItemToObjRecipeBuilder<HTItemResult> =
            HTItemToObjRecipeBuilder(RagiumConst.EXTRACTING, ::HTExtractingRecipe, ingredient, result)

        @JvmStatic
        fun pulverizing(ingredient: HTItemIngredient, result: HTItemResult): HTItemToObjRecipeBuilder<HTItemResult> =
            HTItemToObjRecipeBuilder(RagiumConst.CRUSHING, ::HTPulverizingRecipe, ingredient, result)

        @JvmStatic
        fun melting(ingredient: HTItemIngredient, result: HTFluidResult): HTItemToObjRecipeBuilder<HTFluidResult> =
            HTItemToObjRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe, ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): HTSingleInputRecipe = factory.create(ingredient, result)

    fun interface Factory<RESULT : HTRecipeResult<*>, RECIPE : HTSingleInputRecipe> {
        fun create(ingredient: HTItemIngredient, result: RESULT): RECIPE
    }
}
