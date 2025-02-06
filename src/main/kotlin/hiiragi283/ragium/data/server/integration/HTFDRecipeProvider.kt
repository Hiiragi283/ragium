package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.data.HTGrowthChamberRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import vectorwing.farmersdelight.common.registry.ModItems
import vectorwing.farmersdelight.common.tag.CommonTags

object HTFDRecipeProvider : RagiumRecipeProvider.ModChild("farmersdelight") {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Growth
        HTGrowthChamberRecipeBuilder()
            .itemInput(ModItems.CABBAGE_SEEDS.get())
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(ModItems.CABBAGE.get(), 2)
            .save(output)

        HTGrowthChamberRecipeBuilder()
            .itemInput(ModItems.TOMATO_SEEDS.get())
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(ModItems.TOMATO.get(), 2)
            .save(output)

        HTGrowthChamberRecipeBuilder()
            .itemInput(ModItems.ONION.get())
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(ModItems.ONION.get(), 2)
            .save(output)

        HTGrowthChamberRecipeBuilder()
            .itemInput(CommonTags.CROPS_RICE)
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(ModItems.WILD_RICE.get(), 2)
            .save(output)
    }
}
