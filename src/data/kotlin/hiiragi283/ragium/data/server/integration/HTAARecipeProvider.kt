package hiiragi283.ragium.data.server.integration

import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems
import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTGrowthChamberRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items

object HTAARecipeProvider : HTRecipeProvider.Modded(IntegrationMods.AA) {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Canola
        HTGrowthChamberRecipeBuilder(lookup)
            .itemInput(ActuallyItems.CANOLA_SEEDS)
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(ActuallyItems.CANOLA, 2)
            .save(output)
        // Coffee
        HTGrowthChamberRecipeBuilder(lookup)
            .itemInput(ActuallyItems.COFFEE_BEANS)
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(ActuallyItems.COFFEE_BEANS, 2)
            .save(output)
        // Flax
        HTGrowthChamberRecipeBuilder(lookup)
            .itemInput(ActuallyItems.FLAX_SEEDS)
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(Items.STRING, 4)
            .save(output, id("flax"))
        // Rice
        HTGrowthChamberRecipeBuilder(lookup)
            .itemInput(ActuallyItems.RICE_SEEDS)
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(ActuallyItems.RICE, 2)
            .save(output)

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
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(ActuallyItems.RESTONIA_CRYSTAL, 4)
            .itemInput(HTTagPrefix.GEM, IntegrationMaterials.BLACK_QUARTZ)
            .itemOutput(ActuallyItems.BASIC_COIL, 2)
            .saveSuffixed(output, "_aa")
        // Advanced Coil
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(ActuallyItems.BASIC_COIL)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.GOLD)
            .itemOutput(ActuallyItems.ADVANCED_COIL, 2)
            .saveSuffixed(output, "_aa")
    }
}
