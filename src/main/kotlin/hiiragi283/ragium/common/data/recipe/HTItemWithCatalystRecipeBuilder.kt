package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.common.data.recipe.base.HTComplexResultRecipeBuilder
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import java.util.Optional

class HTItemWithCatalystRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    val required: HTItemIngredient,
    val optional: Optional<HTItemIngredient>,
) : HTComplexResultRecipeBuilder<HTItemWithCatalystRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun compressing(
            ingredient: HTItemIngredient,
            itemResult: HTItemResult?,
            catalyst: HTItemIngredient? = null,
            fluidResult: HTFluidResult? = null,
        ): HTItemWithCatalystRecipeBuilder {
            val builder = HTItemWithCatalystRecipeBuilder(
                RagiumConst.COMPRESSING,
                ::HTCompressingRecipe,
                ingredient,
                catalyst,
            )
            builder.setResult(itemResult)
            builder.setResult(fluidResult)
            return builder
        }

        @JvmStatic
        fun extracting(ingredient: HTItemIngredient, catalyst: HTItemIngredient? = null): HTItemWithCatalystRecipeBuilder =
            HTItemWithCatalystRecipeBuilder(
                RagiumConst.EXTRACTING,
                ::HTExtractingRecipe,
                ingredient,
                catalyst,
            )
    }

    constructor(
        prefix: String,
        factory: Factory<*>,
        required: HTItemIngredient,
        optional: HTItemIngredient?,
    ) : this(prefix, factory, required, optional.wrapOptional())

    override fun createRecipe(): HTItemWithCatalystRecipe = factory.create(required, optional, toIorResult())

    fun interface Factory<RECIPE : HTItemWithCatalystRecipe> {
        fun create(required: HTItemIngredient, optional: Optional<HTItemIngredient>, results: Ior<HTItemResult, HTFluidResult>): RECIPE
    }
}
