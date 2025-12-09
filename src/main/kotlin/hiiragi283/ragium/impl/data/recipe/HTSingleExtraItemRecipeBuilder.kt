package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.extra.HTSingleExtraItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTCuttingRecipe
import net.minecraft.resources.ResourceLocation
import java.util.Optional

class HTSingleExtraItemRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val ingredient: HTItemIngredient,
    private val result: HTItemResult,
    private val extra: Optional<HTItemResult>,
) : HTRecipeBuilder<HTSingleExtraItemRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun crushing(ingredient: HTItemIngredient, result: HTItemResult, extra: HTItemResult? = null): HTSingleExtraItemRecipeBuilder =
            HTSingleExtraItemRecipeBuilder(RagiumConst.CRUSHING, ::HTCrushingRecipe, ingredient, result, extra.wrapOptional())

        @JvmStatic
        fun cutting(ingredient: HTItemIngredient, result: HTItemResult, extra: HTItemResult? = null): HTSingleExtraItemRecipeBuilder =
            HTSingleExtraItemRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe, ingredient, result, extra.wrapOptional())
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): HTSingleExtraItemRecipe = factory.create(ingredient, result, extra)

    fun interface Factory<RECIPE : HTSingleExtraItemRecipe> {
        fun create(ingredient: HTItemIngredient, result: HTItemResult, extra: Optional<HTItemResult>): RECIPE
    }
}
