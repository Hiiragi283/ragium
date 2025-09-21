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
import hiiragi283.ragium.impl.recipe.HTSawmillRecipe
import net.minecraft.resources.ResourceLocation

class HTItemToObjRecipeBuilder<RESULT : HTRecipeResult<*>, RECIPE : HTSingleInputRecipe>(
    prefix: String,
    private val factory: Factory<RESULT, RECIPE>,
    val ingredient: HTItemIngredient,
    val result: RESULT,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun compressing(ingredient: HTItemIngredient, result: HTItemResult): HTItemToObjRecipeBuilder<HTItemResult, HTCompressingRecipe> =
            HTItemToObjRecipeBuilder(RagiumConst.COMPRESSING, ::HTCompressingRecipe, ingredient, result)

        @JvmStatic
        fun extracting(ingredient: HTItemIngredient, result: HTItemResult): HTItemToObjRecipeBuilder<HTItemResult, HTExtractingRecipe> =
            HTItemToObjRecipeBuilder(RagiumConst.EXTRACTING, ::HTExtractingRecipe, ingredient, result)

        @JvmStatic
        fun pulverizing(ingredient: HTItemIngredient, result: HTItemResult): HTItemToObjRecipeBuilder<HTItemResult, HTPulverizingRecipe> =
            HTItemToObjRecipeBuilder(RagiumConst.CRUSHING, ::HTPulverizingRecipe, ingredient, result)

        @JvmStatic
        fun melting(ingredient: HTItemIngredient, result: HTFluidResult): HTItemToObjRecipeBuilder<HTFluidResult, HTMeltingRecipe> =
            HTItemToObjRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe, ingredient, result)

        @JvmStatic
        fun sawmill(ingredient: HTItemIngredient, result: HTItemResult): HTItemToObjRecipeBuilder<HTItemResult, HTSawmillRecipe> =
            HTItemToObjRecipeBuilder("sawmill", ::HTSawmillRecipe, ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): RECIPE = factory.create(ingredient, result)

    fun interface Factory<RESULT : HTRecipeResult<*>, RECIPE : HTSingleInputRecipe> {
        fun create(ingredient: HTItemIngredient, result: RESULT): RECIPE
    }
}
