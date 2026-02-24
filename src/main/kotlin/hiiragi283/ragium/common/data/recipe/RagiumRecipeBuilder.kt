package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.common.data.recipe.builder.HCItemToChancedRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import net.minecraft.data.recipes.RecipeOutput

data object RagiumRecipeBuilder {
    @HTBuilderMarker
    @JvmStatic
    inline fun cutting(output: RecipeOutput, builderAction: HCItemToChancedRecipeBuilder.() -> Unit) {
        HCItemToChancedRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe).apply(builderAction).save(output)
    }
}
