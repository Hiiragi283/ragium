package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.impl.recipe.HTExtractingRecipe
import hiiragi283.ragium.impl.recipe.HTSimulatingRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemWithCatalystRecipe
import java.util.*

class HTItemWithCatalystRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    val required: HTItemIngredient,
    val optional: HTItemIngredient?,
) : HTComplexResultRecipeBuilder<HTItemWithCatalystRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun extracting(
            ingredient: HTItemIngredient,
            itemResult: HTItemResult?,
            catalyst: HTItemIngredient? = null,
            fluidResult: HTFluidResult? = null,
        ): HTItemWithCatalystRecipeBuilder {
            val builder = HTItemWithCatalystRecipeBuilder(
                RagiumConst.EXTRACTING,
                ::HTExtractingRecipe,
                ingredient,
                catalyst,
            )
            builder.setResult(itemResult)
            builder.setResult(fluidResult)
            return builder
        }

        @JvmStatic
        fun simulating(
            ingredient: HTItemIngredient?,
            catalyst: HTItemIngredient,
            itemResult: HTItemResult?,
            fluidResult: HTFluidResult? = null,
        ): HTItemWithCatalystRecipeBuilder {
            val builder = HTItemWithCatalystRecipeBuilder(
                RagiumConst.SIMULATING,
                ::HTSimulatingRecipe,
                catalyst,
                ingredient,
            )
            builder.setResult(itemResult)
            builder.setResult(fluidResult)
            return builder
        }
    }

    override fun createRecipe(): HTItemWithCatalystRecipe = factory.create(required, optional.wrapOptional(), toIorResult())

    fun interface Factory<RECIPE : HTItemWithCatalystRecipe> {
        fun create(required: HTItemIngredient, optional: Optional<HTItemIngredient>, results: Ior<HTItemResult, HTFluidResult>): RECIPE
    }
}
