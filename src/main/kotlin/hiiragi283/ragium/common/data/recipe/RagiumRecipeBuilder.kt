package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.common.data.recipe.builder.HTItemOrFluidRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToItemRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToMultiItemRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTChemicalWashingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTImplodingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import net.minecraft.data.recipes.RecipeOutput

data object RagiumRecipeBuilder {
    //    Basic    //

    @JvmStatic
    inline fun cutting(output: RecipeOutput, builderAction: HTItemToMultiItemRecipeBuilder.() -> Unit) {
        HTItemToMultiItemRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe)
            .apply { time /= 2 }
            .apply(builderAction)
            .save(output)
    }

    //    Advanced    //

    @JvmStatic
    inline fun imploding(output: RecipeOutput, builderAction: HTItemToItemRecipeBuilder.() -> Unit) {
        HTItemToItemRecipeBuilder(RagiumConst.IMPLODING, ::HTImplodingRecipe)
            .apply { time /= 2 }
            .apply(builderAction)
            .save(output)
    }

    @JvmStatic
    inline fun pyrolyzing(output: RecipeOutput, builderAction: HTItemOrFluidRecipeBuilder.() -> Unit) {
        HTItemOrFluidRecipeBuilder(RagiumConst.PYROLYZING, ::HTPyrolyzingRecipe)
            .apply { time *= 3 }
            .apply(builderAction)
            .save(output)
    }

    @JvmStatic
    inline fun refining(output: RecipeOutput, builderAction: HTItemOrFluidRecipeBuilder.() -> Unit) {
        HTItemOrFluidRecipeBuilder(RagiumConst.REFINING, ::HTRefiningRecipe).apply(builderAction).save(output)
    }

    @JvmStatic
    inline fun washing(output: RecipeOutput, builderAction: HTItemToMultiItemRecipeBuilder.() -> Unit) {
        HTItemToMultiItemRecipeBuilder(RagiumConst.WASHING, ::HTWashingRecipe)
            .apply { time /= 2 }
            .apply(builderAction)
            .save(output)
    }

    //    Elite    //

    @JvmStatic
    inline fun chemicalWashing(output: RecipeOutput, builderAction: HTItemOrFluidRecipeBuilder.() -> Unit) {
        HTItemOrFluidRecipeBuilder(RagiumConst.CHEMICAL_WASHING, ::HTChemicalWashingRecipe).apply(builderAction).save(output)
    }
}
