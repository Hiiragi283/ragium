package hiiragi283.ragium.data.server.integration

import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems
import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTAssemblerRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.recipe.HTGrowthChamberRecipe
import hiiragi283.ragium.api.tag.RagiumItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items

object HTAARecipeProvider : HTRecipeProvider.Modded(IntegrationMods.AA) {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Canola
        output.accept(
            id("growth/canola"),
            HTGrowthChamberRecipe(
                ActuallyItems.CANOLA_SEEDS,
                RagiumItemTags.DIRT_SOILS,
                ActuallyItems.CANOLA,
            ),
            null,
        )
        // Coffee
        output.accept(
            id("growth/coffee"),
            HTGrowthChamberRecipe(
                ActuallyItems.COFFEE_BEANS,
                RagiumItemTags.DIRT_SOILS,
                ActuallyItems.COFFEE_BEANS,
            ),
            null,
        )
        // Flax
        output.accept(
            id("growth/flax"),
            HTGrowthChamberRecipe(
                ActuallyItems.FLAX_SEEDS,
                RagiumItemTags.DIRT_SOILS,
                Items.STRING,
            ),
            null,
        )
        // Rice
        output.accept(
            id("growth/rice"),
            HTGrowthChamberRecipe(
                ActuallyItems.RICE_SEEDS,
                RagiumItemTags.DIRT_SOILS,
                ActuallyItems.RICE,
            ),
            null,
        )
        // Canola Oil
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(itemTagKey(commonId("crops/canola")))
            .fluidOutput(InitFluids.CANOLA_OIL, 80)
            .save(output)
        // Refined Canola Oil
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(InitFluids.CANOLA_OIL.get())
            .fluidOutput(InitFluids.REFINED_CANOLA_OIL)
            .save(output)
        // Crystallized Canola Oil
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(ActuallyItems.CRYSTALLIZED_CANOLA_SEED)
            .fluidInput(InitFluids.REFINED_CANOLA_OIL.get())
            .fluidOutput(InitFluids.CRYSTALLIZED_OIL)
            .save(output)
        // Empowered Canola Oil
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(ActuallyItems.EMPOWERED_CANOLA_SEED)
            .fluidInput(InitFluids.CRYSTALLIZED_OIL.get())
            .fluidOutput(InitFluids.EMPOWERED_OIL)
            .save(output)

        // Basic Coil
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(ActuallyItems.RESTONIA_CRYSTAL, 4)
            .itemInput(HTTagPrefix.GEM, IntegrationMaterials.BLACK_QUARTZ)
            .itemOutput(ActuallyItems.BASIC_COIL, 2)
            .saveSuffixed(output, "_aa")
        // Advanced Coil
        HTAssemblerRecipeBuilder(lookup)
            .itemInput(ActuallyItems.BASIC_COIL)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.GOLD)
            .itemOutput(ActuallyItems.ADVANCED_COIL, 2)
            .saveSuffixed(output, "_aa")
    }
}
