package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtraProcessingRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction
import java.util.Optional

class HTExtraProcessingRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val ingredient: HTItemIngredient,
    private val result: HTItemResult,
) : HTProcessingRecipeBuilder<HTExtraProcessingRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun crushing(ingredient: HTItemIngredient, result: HTItemResult): HTExtraProcessingRecipeBuilder =
            HTExtraProcessingRecipeBuilder(RagiumConst.CRUSHING, ::HTCrushingRecipe, ingredient, result)
    }

    private var extra: HTItemResult? = null

    fun setExtra(result: HTItemResult): HTExtraProcessingRecipeBuilder = apply {
        this.extra = result
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTExtraProcessingRecipe = factory.create(ingredient, result, Optional.ofNullable(extra), time, exp)

    fun interface Factory<RECIPE : HTExtraProcessingRecipe> {
        fun create(
            ingredient: HTItemIngredient,
            result: HTItemResult,
            extra: Optional<HTItemResult>,
            time: Int,
            exp: Fraction,
        ): RECIPE
    }
}
