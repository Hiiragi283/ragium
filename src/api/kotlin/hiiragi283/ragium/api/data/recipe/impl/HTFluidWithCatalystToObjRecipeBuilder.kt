package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.HTFluidWithCatalystToObjRecipe
import hiiragi283.ragium.api.recipe.impl.HTSolidifyingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import java.util.*

class HTFluidWithCatalystToObjRecipeBuilder<RESULT : HTRecipeResult<*>, RECIPE : HTFluidWithCatalystToObjRecipe<RESULT>>(
    prefix: String,
    private val factory: Factory<RESULT, RECIPE>,
    private val ingredient: HTFluidIngredient,
    private val catalyst: Optional<HTItemIngredient>,
    private val result: RESULT,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun solidifying(
            itemIngredient: HTItemIngredient?,
            fluidIngredient: HTFluidIngredient,
            result: HTItemResult,
        ): HTFluidWithCatalystToObjRecipeBuilder<HTItemResult, HTSolidifyingRecipe> = HTFluidWithCatalystToObjRecipeBuilder(
            RagiumConst.SOLIDIFYING,
            ::HTSolidifyingRecipe,
            fluidIngredient,
            Optional.ofNullable(itemIngredient),
            result,
        )
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): RECIPE = factory.create(ingredient, catalyst, result)

    fun interface Factory<RESULT : HTRecipeResult<*>, RECIPE : HTFluidWithCatalystToObjRecipe<RESULT>> {
        fun create(ingredient: HTFluidIngredient, catalyst: Optional<HTItemIngredient>, result: RESULT): RECIPE
    }
}
