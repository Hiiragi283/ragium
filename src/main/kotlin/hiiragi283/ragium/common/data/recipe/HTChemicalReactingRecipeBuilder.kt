package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.util.toOption
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTChemicalReactingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HTChemicalReactingRecipeBuilder : HTProgressRecipeBuilder(RagiumConst.CHEMICAL_REACTING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTChemicalReactingRecipeBuilder.() -> Unit) {
            HTChemicalReactingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val ingredients: MutableList<HTFluidIngredient> = mutableListOf()
    var catalyst: Ingredient? = null
    val fluidResults: MutableList<HTFluidResult> = mutableListOf()
    var itemResult: HTItemResult? = null

    override fun getPrimalId(): ResourceLocation = fluidResults[0].getId()

    override fun createRecipe(): HTChemicalReactingRecipe = HTChemicalReactingRecipe(
        ingredients[0],
        Ior.fromNullable(ingredients.getOrNull(1), catalyst) ?: error("Either second fluid ingredient or catalyst required"),
        fluidResults,
        itemResult.toOption(),
        progressData,
    )
}
