package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.common.data.recipe.builder.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.ingredient.HTBluePrintIngredient
import net.minecraft.data.recipes.RecipeOutput

data object RagiumRecipeBuilder {
    @HTBuilderMarker
    @JvmStatic
    inline fun cutting(output: RecipeOutput, builderAction: HTItemToChancedRecipeBuilder.() -> Unit) {
        HTItemToChancedRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe).apply(builderAction).save(output)
    }

    @HTBuilderMarker
    @JvmStatic
    inline fun planting(output: RecipeOutput, builderAction: HTItemToChancedRecipeBuilder.() -> Unit) {
        HTItemToChancedRecipeBuilder(RagiumConst.PLANTING, ::HTPlantingRecipe).apply(builderAction).save(output)
    }
}

fun HTIngredientCreator.blueprint(number: Int): HTItemIngredient = this.create(HTBluePrintIngredient(number).toVanilla(), 0)
