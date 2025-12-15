package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.fluid.HTFluidRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.common.recipe.HTBasicMeltingRecipe
import hiiragi283.ragium.common.recipe.HTBasicRefiningRecipe
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class HTFluidRecipeBuilder<RECIPE : HTFluidRecipe>(
    prefix: String,
    private val recipe: RECIPE,
    private val idProvider: Supplier<ResourceLocation>,
) : HTRecipeBuilder<HTFluidRecipeBuilder<RECIPE>>(prefix) {
    companion object {
        @JvmStatic
        fun melting(ingredient: HTItemIngredient, result: HTFluidResult): HTFluidRecipeBuilder<HTBasicMeltingRecipe> =
            HTFluidRecipeBuilder(RagiumConst.MELTING, HTBasicMeltingRecipe(ingredient, result), result::id)

        @JvmStatic
        fun refining(
            ingredient: HTFluidIngredient,
            fluidResult: HTFluidResult,
            itemResult: HTItemResult? = null,
        ): HTFluidRecipeBuilder<HTBasicRefiningRecipe> = HTFluidRecipeBuilder(
            RagiumConst.REFINING,
            HTBasicRefiningRecipe(ingredient, itemResult.wrapOptional(), fluidResult),
            fluidResult::id,
        )
    }

    override fun getPrimalId(): ResourceLocation = idProvider.get()

    override fun createRecipe(): RECIPE = recipe
}
