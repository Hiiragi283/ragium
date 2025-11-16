package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.impl.recipe.HTExtractingRecipe
import hiiragi283.ragium.impl.recipe.HTSimulatingRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemWithCatalystRecipe
import net.minecraft.resources.ResourceLocation
import java.util.*

class HTItemWithCatalystRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    val required: HTItemIngredient,
    val optional: HTItemIngredient?,
    val itemResult: HTItemResult?,
    val fluidResult: HTFluidResult?,
) : HTRecipeBuilder<HTItemWithCatalystRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun extracting(
            ingredient: HTItemIngredient,
            itemResult: HTItemResult?,
            catalyst: HTItemIngredient? = null,
            fluidResult: HTFluidResult? = null,
        ): HTItemWithCatalystRecipeBuilder = HTItemWithCatalystRecipeBuilder(
            RagiumConst.EXTRACTING,
            ::HTExtractingRecipe,
            ingredient,
            catalyst,
            itemResult,
            fluidResult,
        )

        @JvmStatic
        fun simulating(
            ingredient: HTItemIngredient?,
            catalyst: HTItemIngredient,
            itemResult: HTItemResult?,
            fluidResult: HTFluidResult? = null,
        ): HTItemWithCatalystRecipeBuilder = HTItemWithCatalystRecipeBuilder(
            RagiumConst.SIMULATING,
            ::HTSimulatingRecipe,
            catalyst,
            ingredient,
            itemResult,
            fluidResult,
        )
    }

    override fun getPrimalId(): ResourceLocation = fluidResult?.id ?: itemResult?.id ?: error("Either item or fluid result required")

    override fun createRecipe(): HTItemWithCatalystRecipe =
        factory.create(required, optional.wrapOptional(), itemResult.wrapOptional(), fluidResult.wrapOptional())

    fun interface Factory<RECIPE : HTItemWithCatalystRecipe> {
        fun create(
            required: HTItemIngredient,
            optional: Optional<HTItemIngredient>,
            itemResult: Optional<HTItemResult>,
            fluidResult: Optional<HTFluidResult>,
        ): RECIPE
    }
}
