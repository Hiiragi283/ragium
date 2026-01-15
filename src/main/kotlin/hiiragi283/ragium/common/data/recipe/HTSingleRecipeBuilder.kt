package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTSingleRecipeBuilder<ING : Any, RES : HTRecipeResult<*>>(
    prefix: String,
    private val factory: Factory<ING, RES, *>,
    private val ingredient: ING,
    private val result: RES,
) : HTProcessingRecipeBuilder<HTSingleRecipeBuilder<ING, RES>>(prefix) {
    companion object {
        @JvmStatic
        fun melting(ingredient: HTItemIngredient, result: HTFluidResult): HTSingleRecipeBuilder<*, *> =
            HTSingleRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe, ingredient, result)

        @JvmStatic
        fun pressing(ingredient: HTItemIngredient, catalyst: HTItemIngredient, result: HTItemResult): HTSingleRecipeBuilder<*, *> =
            HTSingleRecipeBuilder(RagiumConst.PRESSING, ::HTPressingRecipe, ingredient to catalyst, result).setTime(100)

        @JvmStatic
        fun solidifying(ingredient: HTFluidIngredient, catalyst: HTItemIngredient, result: HTItemResult): HTSingleRecipeBuilder<*, *> =
            HTSingleRecipeBuilder(RagiumConst.SOLIDIFYING, ::HTSolidifyingRecipe, ingredient to catalyst, result)
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTProcessingRecipe<*> = factory.create(ingredient, result, time, exp)

    fun interface Factory<ING : Any, RES : HTRecipeResult<*>, RECIPE : HTProcessingRecipe<*>> {
        fun create(
            ingredient: ING,
            result: RES,
            time: Int,
            exp: Fraction,
        ): RECIPE
    }
}
