package hiiragi283.ragium.data.server.integration

import de.ellpeck.actuallyadditions.api.ActuallyTags
import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import de.ellpeck.actuallyadditions.mod.items.ActuallyItems
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput

object HTAARecipeProvider : RagiumRecipeProvider.ModChild("actually_additions") {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Canola Oil
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(ActuallyTags.Items.SEEDS_CANOLA)
            .fluidOutput(InitFluids.CANOLA_OIL, 80)
            .save(output)
        // Refined Canola Oil
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(InitFluids.CANOLA_OIL.get())
            .fluidOutput(InitFluids.REFINED_CANOLA_OIL)
            .save(output)
        // Crystallized Canola Oil
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(ActuallyItems.CRYSTALLIZED_CANOLA_SEED)
            .fluidInput(InitFluids.REFINED_CANOLA_OIL.get())
            .fluidOutput(InitFluids.CRYSTALLIZED_OIL)
            .save(output)
        // Empowered Canola Oil
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(ActuallyItems.EMPOWERED_CANOLA_SEED)
            .fluidInput(InitFluids.CRYSTALLIZED_OIL.get())
            .fluidOutput(InitFluids.EMPOWERED_OIL)
            .save(output)

        // Coffee
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(ActuallyTags.Items.CROPS_COFFEE)
            .itemInput(ActuallyItems.EMPTY_CUP)
            .milkInput()
            .itemOutput(ActuallyItems.COFFEE_CUP)
            .save(output)

        // Basic Coil
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(ActuallyItems.RESTONIA_CRYSTAL, 4)
            .itemInput(ActuallyTags.Items.GEMS_BLACK_QUARTZ)
            .itemOutput(ActuallyItems.BASIC_COIL, 2)
            .saveSuffixed(output, "_aa")
        // Advanced Coil
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(ActuallyItems.BASIC_COIL)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.GOLD)
            .itemOutput(ActuallyItems.ADVANCED_COIL, 2)
            .saveSuffixed(output, "_aa")
    }
}
