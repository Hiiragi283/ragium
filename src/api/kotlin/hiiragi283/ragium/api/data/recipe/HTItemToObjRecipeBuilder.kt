package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.base.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.base.HTMeltingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTItemToObjRecipeBuilder<R1 : HTRecipeResult<*, *>, R2 : HTItemToObjRecipe<R1>>(
    prefix: String,
    private val factory: Factory<R1, R2>,
    val ingredient: HTItemIngredient,
    val result: R1,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun extracting(ingredient: HTItemIngredient, result: HTItemResult): HTItemToObjRecipeBuilder<HTItemResult, HTExtractingRecipe> =
            HTItemToObjRecipeBuilder(RagiumConst.EXTRACTING, ::HTExtractingRecipe, ingredient, result)

        @JvmStatic
        fun melting(ingredient: HTItemIngredient, result: HTFluidResult): HTItemToObjRecipeBuilder<HTFluidResult, HTMeltingRecipe> =
            HTItemToObjRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe, ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): Recipe<*> = factory.create(ingredient, result)

    fun interface Factory<R1 : HTRecipeResult<*, *>, R2 : HTItemToObjRecipe<R1>> {
        fun create(ingredient: HTItemIngredient, result: R1): R2
    }
}
