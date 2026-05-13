package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.toOptional
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HTRefiningRecipeBuilder : HTProgressRecipeBuilder(RagiumConst.REFINING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTRefiningRecipeBuilder.() -> Unit) {
            HTRefiningRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTFluidIngredient
    var catalyst: Ingredient? = null
    val fluidResults: MutableList<HTFluidResult> = mutableListOf()
    var itemResult: HTItemResult? = null

    override fun getPrimalId(): ResourceLocation = fluidResults[0].getId()

    override fun createRecipe(): HTRefiningRecipe = HTRefiningRecipe(
        ingredient,
        catalyst.toOptional(),
        fluidResults,
        itemResult.toOptional(),
        progressData,
    )
}
