package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeBuilder
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluids

object RagiumFluidRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        centrifuging(output, holderLookup)
        infusing(output, holderLookup)
    }

    //    Centrifuging    //

    private fun centrifuging(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Magma Block -> Cobblestone + Lava
        HTMachineRecipeBuilder(RagiumRecipes.CENTRIFUGING)
            .itemOutput(Items.COBBLESTONE)
            .fluidOutput(Fluids.LAVA, 100)
            .itemInput(Items.MAGMA_BLOCK)
            .saveSuffixed(output, "_from_magma_block")

        sap(output)
    }

    private fun sap(output: RecipeOutput) {
        // XX Log -> Wood Dust + Sap
        HTMachineRecipeBuilder(RagiumRecipes.CENTRIFUGING)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumVirtualFluids.SAP, 100)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .saveSuffixed(output, "_from_log")
        // Sap -> Slime Ball
        HTMachineRecipeBuilder(RagiumRecipes.CENTRIFUGING)
            .itemOutput(Items.SLIME_BALL)
            .fluidInput(RagiumVirtualFluids.SAP.commonTag, 1000)
            .saveSuffixed(output, "_from_sap")

        // Crimson Stem -> Wood Dust + Crimson Sap
        HTMachineRecipeBuilder(RagiumRecipes.CENTRIFUGING)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumVirtualFluids.CRIMSON_SAP, 100)
            .itemInput(ItemTags.CRIMSON_STEMS)
            .saveSuffixed(output, "_from_crimson")
        // Crimson Sap -> Sap + Crimson Crystal
        HTMachineRecipeBuilder(RagiumRecipes.CENTRIFUGING)
            .itemOutput(RagiumItems.RawResources.CRIMSON_CRYSTAL)
            .fluidOutput(RagiumVirtualFluids.SAP, 100)
            .fluidInput(RagiumVirtualFluids.CRIMSON_SAP.commonTag, 1000)
            .save(output)
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER)
            .addIngredient(HTTagPrefixes.STORAGE_BLOCK, RagiumMaterials.CRIMSON_CRYSTAL)
            .save(output)

        // Warped Stem -> Wood Dust + Warped Sap
        HTMachineRecipeBuilder(RagiumRecipes.CENTRIFUGING)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumVirtualFluids.WARPED_SAP, 100)
            .itemInput(ItemTags.WARPED_STEMS)
            .saveSuffixed(output, "_from_warped")
        // Warped Sap -> Sap + Warped Crystal
        HTMachineRecipeBuilder(RagiumRecipes.CENTRIFUGING)
            .itemOutput(RagiumItems.RawResources.WARPED_CRYSTAL)
            .fluidOutput(RagiumVirtualFluids.SAP, 100)
            .fluidInput(RagiumVirtualFluids.WARPED_SAP.commonTag, 1000)
            .save(output)
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL)
            .addIngredient(HTTagPrefixes.STORAGE_BLOCK, RagiumMaterials.WARPED_CRYSTAL)
            .save(output)
    }

    //    Infusing    //

    private fun infusing(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Dirt -> Mud
        HTMachineRecipeBuilder(RagiumRecipes.INFUSING)
            .itemOutput(Items.MUD)
            .itemInput(Items.DIRT)
            .waterInput(250)
            .saveSuffixed(output, "_from_dirt")
        // Silt -> Clay
        HTMachineRecipeBuilder(RagiumRecipes.INFUSING)
            .itemOutput(Items.CLAY)
            .itemInput(RagiumBlocks.SILT)
            .waterInput(250)
            .saveSuffixed(output, "_from_silt")
    }
}
