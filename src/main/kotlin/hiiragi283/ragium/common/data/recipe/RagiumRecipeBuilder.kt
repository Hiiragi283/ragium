package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.common.data.recipe.builder.HTDoubleMultiOutputRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTSingleMultiOutputRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import net.minecraft.data.recipes.RecipeOutput

data object RagiumRecipeBuilder {
    @JvmStatic
    inline fun cutting(output: RecipeOutput, builderAction: HTSingleMultiOutputRecipeBuilder.() -> Unit) {
        HTSingleMultiOutputRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe).apply(builderAction).save(output)
    }

    @JvmStatic
    inline fun planting(output: RecipeOutput, builderAction: HTDoubleMultiOutputRecipeBuilder.() -> Unit) {
        HTDoubleMultiOutputRecipeBuilder(RagiumConst.PLANTING, ::HTPlantingRecipe).apply(builderAction).save(output)
    }
}
