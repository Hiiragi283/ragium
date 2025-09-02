package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.impl.HTSimulatingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.resources.ResourceLocation
import java.util.*

class HTItemWithCatalystToItemRecipeBuilder<RECIPE : HTItemWithCatalystToItemRecipe>(
    prefix: String,
    private val factory: Factory<RECIPE>,
    val ingredient: Optional<HTItemIngredient>,
    val catalyst: HTItemIngredient,
    val result: HTItemResult,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun simulating(
            ingredient: HTItemIngredient?,
            catalyst: HTItemIngredient,
            result: HTItemResult,
        ): HTItemWithCatalystToItemRecipeBuilder<HTSimulatingRecipe> = HTItemWithCatalystToItemRecipeBuilder(
            RagiumConst.SIMULATING,
            ::HTSimulatingRecipe,
            Optional.ofNullable(ingredient),
            catalyst,
            result,
        )
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): RECIPE = factory.create(ingredient, catalyst, result)

    fun interface Factory<RECIPE : HTItemWithCatalystToItemRecipe> {
        fun create(ingredient: Optional<HTItemIngredient>, catalyst: HTItemIngredient, result: HTItemResult): RECIPE
    }
}
