package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.math.toFraction
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTChancedRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTChancedRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val ingredient: HTItemIngredient,
    val result: HTItemResult,
) : HTProcessingRecipeBuilder<HTChancedRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun crushing(ingredient: HTItemIngredient, result: HTItemResult): HTChancedRecipeBuilder =
            HTChancedRecipeBuilder(RagiumConst.CRUSHING, ::HTCrushingRecipe, ingredient, result)

        @JvmStatic
        fun cutting(ingredient: HTItemIngredient, result: HTItemResult): HTChancedRecipeBuilder =
            HTChancedRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe, ingredient, result)
    }

    private var extraResults: MutableList<HTChancedItemResult> = mutableListOf()

    fun addResult(result: HTItemResult, chance: Float): HTChancedRecipeBuilder = addResult(result, chance.toFraction())

    fun addResult(result: HTItemResult, chance: Fraction = Fraction.ONE): HTChancedRecipeBuilder =
        addResult(HTChancedItemResult(result, chance))

    fun addResult(result: HTChancedItemResult): HTChancedRecipeBuilder = apply {
        extraResults += result
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTChancedRecipe = factory.create(
        ingredient,
        result,
        extraResults,
        time,
        exp,
    )

    fun interface Factory<RECIPE : HTChancedRecipe> {
        fun create(
            ingredient: HTItemIngredient,
            result: HTItemResult,
            extraResults: List<HTChancedItemResult>,
            time: Int,
            exp: Fraction,
        ): RECIPE
    }
}
