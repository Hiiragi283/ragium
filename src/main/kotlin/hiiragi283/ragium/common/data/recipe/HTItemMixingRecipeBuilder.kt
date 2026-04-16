package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.function.identityLeft
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.holder.HTIorHolder
import hiiragi283.ragium.common.recipe.HTItemMixingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTItemMixingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.ITEM_MIXING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTItemMixingRecipeBuilder.() -> Unit) {
            HTItemMixingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()
    lateinit var fluidIngredient: HTFluidIngredient
    val result: HTIorHolder<HTItemResult, HTFluidResult> = HTIorHolder()

    override fun getPrimalId(): ResourceLocation = result.toIor().map(HTItemResult::getId, HTFluidResult::getId, identityLeft())

    override fun createRecipe(): HTItemMixingRecipe = HTItemMixingRecipe(itemIngredients, fluidIngredient, result.toIor(), time)
}
