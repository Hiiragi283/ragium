package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HTFreezingRecipeBuilder : HTProgressRecipeBuilder(RagiumConst.FREEZING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTFreezingRecipeBuilder.() -> Unit) {
            HTFreezingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var ingredient: HTFluidIngredient by HTDelegates.onceInitialize()
    var catalyst: Ingredient by HTDelegates.onceInitialize()
    var result: HTItemResult by HTDelegates.onceInitialize()

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTFreezingRecipe = HTFreezingRecipe(
        ingredient,
        catalyst,
        result,
        progressData,
    )
}
