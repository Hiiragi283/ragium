package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTPrintingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HTPrintingRecipeBuilder : HTProgressRecipeBuilder(RagiumConst.PRINTING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTPrintingRecipeBuilder.() -> Unit) {
            HTPrintingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var ingredient: HTItemIngredient by HTDelegates.onceInitialize()
    var press: Ingredient by HTDelegates.onceInitialize()
    var result: HTItemResult by HTDelegates.onceInitialize()

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTPrintingRecipe = HTPrintingRecipe(
        ingredient,
        press,
        result,
        progressData,
    )
}
