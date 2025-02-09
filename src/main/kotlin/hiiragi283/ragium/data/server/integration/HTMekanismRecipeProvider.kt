package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.data.recipe.HTGrinderRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import mekanism.common.registries.MekanismItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput

object HTMekanismRecipeProvider : RagiumRecipeProvider.ModChild("mekanism") {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Yellow Cake -> Yellow Cake Uranium
        HTGrinderRecipeBuilder()
            .itemInput(RagiumItems.YELLOW_CAKE)
            .itemOutput(MekanismItems.YELLOW_CAKE_URANIUM, 8)
            .save(output)
        // Yellow Cake Uranium -> Yellow Cake
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(MekanismItems.YELLOW_CAKE_URANIUM, 8)
            .itemOutput(RagiumItems.YELLOW_CAKE)
            .save(output)
    }
}
