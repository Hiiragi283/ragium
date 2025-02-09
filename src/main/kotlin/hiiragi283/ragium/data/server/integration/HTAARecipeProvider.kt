package hiiragi283.ragium.data.server.integration

import de.ellpeck.actuallyadditions.api.ActuallyTags
import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems
import hiiragi283.ragium.api.data.recipe.*
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput

object HTAARecipeProvider : RagiumRecipeProvider.ModChild("actuallyadditions") {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Canola Seeds
        HTGrowthChamberRecipeBuilder()
            .itemInput(ActuallyTags.Items.SEEDS_CANOLA)
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(ActuallyItems.CANOLA, 2)
            .save(output)
        // Coffee Seeds
        HTGrowthChamberRecipeBuilder()
            .itemInput(ActuallyTags.Items.SEEDS_COFFEE)
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(ActuallyItems.COFFEE_BEANS, 2)
            .save(output)

        // Canola Oil
        HTExtractorRecipeBuilder()
            .itemInput(ActuallyTags.Items.CROPS_CANOLA)
            .fluidOutput(InitFluids.CANOLA_OIL, 80)
            .save(output)
        // Refined Canola Oil
        HTRefineryRecipeBuilder()
            .fluidInput(InitFluids.CANOLA_OIL.get())
            .fluidOutput(InitFluids.REFINED_CANOLA_OIL)
            .save(output)
        // Crystallized Canola Oil
        HTInfuserRecipeBuilder()
            .itemInput(ActuallyItems.CRYSTALLIZED_CANOLA_SEED)
            .fluidInput(InitFluids.REFINED_CANOLA_OIL.get())
            .fluidOutput(InitFluids.CRYSTALLIZED_OIL)
            .save(output)
        // Empowered Canola Oil
        HTInfuserRecipeBuilder()
            .itemInput(ActuallyItems.EMPOWERED_CANOLA_SEED)
            .fluidInput(InitFluids.CRYSTALLIZED_OIL.get())
            .fluidOutput(InitFluids.EMPOWERED_OIL)
            .save(output)

        // Basic Coil
        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(ActuallyItems.RESTONIA_CRYSTAL, 4)
            .itemInput(ActuallyTags.Items.GEMS_BLACK_QUARTZ)
            .itemOutput(ActuallyItems.BASIC_COIL, 2)
            .saveSuffixed(output, "_aa")
        // Advanced Coil
        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(ActuallyItems.BASIC_COIL)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.GOLD)
            .itemOutput(ActuallyItems.ADVANCED_COIL, 2)
            .saveSuffixed(output, "_aa")
    }
}
