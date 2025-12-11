package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTFluidWithCatalystRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.resources.ResourceLocation
import java.util.Optional

class HTFluidWithCatalystRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val ingredient: HTFluidIngredient,
    private val catalyst: Optional<HTItemIngredient>,
    private val result: HTItemResult,
) : HTRecipeBuilder<HTFluidWithCatalystRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun solidifying(
            ingredient: HTFluidIngredient,
            catalyst: HTItemIngredient?,
            result: HTItemResult,
        ): HTFluidWithCatalystRecipeBuilder =
            HTFluidWithCatalystRecipeBuilder(RagiumConst.SOLIDIFYING, ::HTSolidifyingRecipe, ingredient, catalyst.wrapOptional(), result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): HTFluidWithCatalystRecipe = factory.create(ingredient, catalyst, result)

    fun interface Factory<RECIPE : HTFluidWithCatalystRecipe> {
        fun create(ingredient: HTFluidIngredient, catalyst: Optional<HTItemIngredient>, result: HTItemResult): RECIPE
    }
}
