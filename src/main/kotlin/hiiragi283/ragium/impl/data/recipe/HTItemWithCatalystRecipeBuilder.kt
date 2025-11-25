package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.toComplex
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.impl.recipe.HTCompressingRecipe
import hiiragi283.ragium.impl.recipe.HTExtractingRecipe
import hiiragi283.ragium.impl.recipe.HTSimulatingRecipe
import net.minecraft.resources.ResourceLocation
import java.util.*
import java.util.function.Supplier

class HTItemWithCatalystRecipeBuilder<RESULT : Any>(
    prefix: String,
    private val factory: Factory<RESULT, *>,
    private val idProvider: Supplier<ResourceLocation>,
    val required: HTItemIngredient,
    val optional: HTItemIngredient?,
    val result: RESULT,
) : HTRecipeBuilder<HTItemWithCatalystRecipeBuilder<RESULT>>(prefix) {
    companion object {
        @JvmStatic
        fun compressing(
            ingredient: HTItemIngredient,
            itemResult: HTItemResult,
            catalyst: HTItemIngredient? = null,
        ): HTItemWithCatalystRecipeBuilder<HTItemResult> = HTItemWithCatalystRecipeBuilder(
            RagiumConst.COMPRESSING,
            ::HTCompressingRecipe,
            itemResult::id,
            ingredient,
            catalyst,
            itemResult,
        )

        @JvmStatic
        fun extracting(
            ingredient: HTItemIngredient,
            itemResult: HTItemResult?,
            catalyst: HTItemIngredient? = null,
            fluidResult: HTFluidResult? = null,
        ): HTItemWithCatalystRecipeBuilder<HTComplexResult> = HTItemWithCatalystRecipeBuilder(
            RagiumConst.EXTRACTING,
            ::HTExtractingRecipe,
            { (itemResult to fluidResult).toComplex().map(HTItemResult::id, HTFluidResult::id) },
            ingredient,
            catalyst,
            (itemResult to fluidResult).toComplex(),
        )

        @JvmStatic
        fun simulating(
            ingredient: HTItemIngredient?,
            catalyst: HTItemIngredient,
            itemResult: HTItemResult?,
            fluidResult: HTFluidResult? = null,
        ): HTItemWithCatalystRecipeBuilder<HTComplexResult> = HTItemWithCatalystRecipeBuilder(
            RagiumConst.SIMULATING,
            ::HTSimulatingRecipe,
            { (itemResult to fluidResult).toComplex().map(HTItemResult::id, HTFluidResult::id) },
            catalyst,
            ingredient,
            (itemResult to fluidResult).toComplex(),
        )
    }

    override fun getPrimalId(): ResourceLocation = idProvider.get()

    override fun createRecipe(): HTItemWithCatalystRecipe = factory.create(required, optional.wrapOptional(), result)

    fun interface Factory<RESULT : Any, RECIPE : HTItemWithCatalystRecipe> {
        fun create(required: HTItemIngredient, optional: Optional<HTItemIngredient>, result: RESULT): RECIPE
    }
}
