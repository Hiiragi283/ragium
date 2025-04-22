package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTDefinitionRecipeBuilder
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.common.recipe.HTBucketExtractingRecipe
import hiiragi283.ragium.common.recipe.HTBucketFillingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags

object RagiumFluidRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        extracting(output, holderLookup)
        infusing(output, holderLookup)
    }

    //    Extracting    //

    private fun extracting(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Magma Block -> Cobblestone + Lava
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(Items.COBBLESTONE)
            .fluidOutput(Fluids.LAVA, 100)
            .itemInput(Items.MAGMA_BLOCK)
            .saveSuffixed(output, "_from_magma_block")

        // Exp Berries -> Liquid Exp
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.EXPERIENCE, 50)
            .itemInput(RagiumItems.EXP_BERRIES)
            .saveSuffixed(output, "_from_berries")

        crudeOil(output)
        biomass(output)
        sap(output)
        ragium(output)

        output.accept(
            RagiumAPI.id("extracting/buckets"),
            HTBucketExtractingRecipe,
            null,
        )
        output.accept(
            RagiumAPI.id("infusing/buckets"),
            HTBucketFillingRecipe,
            null,
        )
    }

    private fun crudeOil(output: RecipeOutput) {
        // Coal -> Crude Oil
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.CRUDE_OIL, 125)
            .itemInput(HTTagPrefixes.GEM, VanillaMaterials.COAL)
            .saveSuffixed(output, "_from_coal")
        // Soul XX -> Crude Oil
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.CRUDE_OIL, 500)
            .itemInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .saveSuffixed(output, "_from_soul")

        // Crude Oil -> Naphtha + Tar
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.REFINING)
            .itemOutput(RagiumItems.TAR)
            .fluidOutput(RagiumFluidContents.NAPHTHA, 750)
            .fluidInput(RagiumFluidContents.CRUDE_OIL)
            .save(output, RagiumAPI.id("naphtha_from_crude_oil"))
        // Naphtha -> Fuel + Sulfur
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.REFINING)
            .itemOutput(RagiumItems.Dusts.SULFUR)
            .fluidOutput(RagiumFluidContents.FUEL, 750)
            .fluidInput(RagiumFluidContents.NAPHTHA)
            .save(output, RagiumAPI.id("fuel_from_naphtha"))

        // Tar -> Aromatic Compound
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.AROMATIC_COMPOUND, 200)
            .itemInput(RagiumItems.TAR)
            .saveSuffixed(output, "_from_tar")
        // Aromatic Compound + Sand -> TNT
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.TNT, 8)
            .itemInput(Tags.Items.SANDS)
            .fluidInput(RagiumFluidContents.AROMATIC_COMPOUND, 200)
            .save(output)
    }

    private fun biomass(output: RecipeOutput) {
        // Biomass -> Ethanol
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.REFINING)
            .fluidOutput(RagiumFluidContents.FUEL, 500)
            .fluidInput(RagiumFluidContents.BIOMASS)
            .saveSuffixed(output, "_from_biomass")
        // Ethanol + Plant Oil -> Fuel + Glycerol
    }

    private fun sap(output: RecipeOutput) {
        // XX Log -> Wood Dust + Sap
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluidContents.SAP, 100)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .saveSuffixed(output, "_from_log")
        // Sap -> Slime Ball
        /*HTMachineRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(Items.SLIME_BALL)
            .fluidInput(RagiumFluidContents.SAP.commonTag, 1000)
            .saveSuffixed(output, "_from_sap")*/

        // Crimson Stem -> Wood Dust + Crimson Sap
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluidContents.CRIMSON_SAP, 100)
            .itemInput(ItemTags.CRIMSON_STEMS)
            .saveSuffixed(output, "_from_crimson")
        // Crimson Sap -> Sap + Crimson Crystal
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.REFINING)
            .itemOutput(RagiumItems.RawResources.CRIMSON_CRYSTAL)
            .fluidOutput(RagiumFluidContents.SAP, 100)
            .fluidInput(RagiumFluidContents.CRIMSON_SAP.commonTag, 1000)
            .save(output)
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER)
            .addIngredient(HTTagPrefixes.STORAGE_BLOCK, RagiumMaterials.CRIMSON_CRYSTAL)
            .save(output)

        // Warped Stem -> Wood Dust + Warped Sap
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluidContents.WARPED_SAP, 100)
            .itemInput(ItemTags.WARPED_STEMS)
            .saveSuffixed(output, "_from_warped")
        // Warped Sap -> Sap + Warped Crystal
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.REFINING)
            .itemOutput(RagiumItems.RawResources.WARPED_CRYSTAL)
            .fluidOutput(RagiumFluidContents.SAP, 100)
            .fluidInput(RagiumFluidContents.WARPED_SAP.commonTag, 1000)
            .save(output)
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL)
            .addIngredient(HTTagPrefixes.STORAGE_BLOCK, RagiumMaterials.WARPED_CRYSTAL)
            .save(output)
    }

    private fun ragium(output: RecipeOutput) {
        // Raginite -> 20 mb
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.MOLTEN_RAGIUM, 20)
            .itemInput(HTTagPrefixes.DUST, RagiumMaterials.RAGINITE)
            .saveSuffixed(output, "_from_raginite")
        // Advanced Ragi-Alloy -> 100 mb
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.MOLTEN_RAGIUM, 100)
            .itemInput(HTTagPrefixes.DUST, RagiumMaterials.RAGI_ALLOY)
            .saveSuffixed(output, "_from_alloy")
        // Advanced Ragi-Alloy -> 125 mb
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.MOLTEN_RAGIUM, 125)
            .itemInput(HTTagPrefixes.DUST, RagiumMaterials.ADVANCED_RAGI_ALLOY)
            .saveSuffixed(output, "_from_advanced_alloy")
        // Ragi-Crystal -> 250 mb
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.MOLTEN_RAGIUM, 250)
            .itemInput(HTTagPrefixes.DUST, RagiumMaterials.RAGI_CRYSTAL)
            .saveSuffixed(output, "_from_crystal")

        // Ragium Essence
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(RagiumItems.RAGIUM_ESSENCE)
            .itemInput(HTTagPrefixes.DUST, VanillaMaterials.QUARTZ)
            .fluidInput(RagiumFluidContents.MOLTEN_RAGIUM)
            .save(output)
    }

    //    Infusing    //

    private fun infusing(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Water Bottle
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(createPotionStack(Potions.WATER, 3))
            .itemInput(Items.GLASS_BOTTLE, 3)
            .waterInput()
            .save(output, RagiumAPI.id("water_bottle"))

        // Dirt -> Mud
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.MUD)
            .itemInput(Items.DIRT)
            .waterInput(250)
            .saveSuffixed(output, "_from_dirt")
        // Silt -> Clay
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.CLAY)
            .itemInput(RagiumBlocks.SILT)
            .waterInput(250)
            .saveSuffixed(output, "_from_silt")

        // Milk + Snow -> Ice Cream
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(RagiumItems.ICE_CREAM)
            .itemInput(Items.SNOWBALL)
            .milkInput(250)
            .save(output)

        crystal(output)
        exp(output)
    }

    private fun crystal(output: RecipeOutput) {
        // Quartz
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.QUARTZ, 2)
            .itemInput(HTTagPrefixes.DUST, VanillaMaterials.QUARTZ)
            .waterInput(250)
            .saveSuffixed(output, "_from_water")
        // Amethyst
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.AMETHYST_SHARD, 2)
            .itemInput(HTTagPrefixes.DUST, VanillaMaterials.AMETHYST)
            .waterInput(250)
            .saveSuffixed(output, "_from_water")
    }

    private fun exp(output: RecipeOutput) {
        // Exp Bottle
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.EXPERIENCE_BOTTLE)
            .itemInput(Items.GLASS_BOTTLE)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 250)
            .save(output)
        // Golden Apple
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.ENCHANTED_GOLDEN_APPLE)
            .itemInput(Items.GOLDEN_APPLE)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 8000)
            .save(output)
        // Exp Berries
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(RagiumItems.EXP_BERRIES)
            .itemInput(Tags.Items.FOODS_BERRY)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 1000)
            .save(output)
        // Blaze Powder
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.BLAZE_POWDER)
            .itemInput(HTTagPrefixes.DUST, CommonMaterials.SULFUR)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 250)
            .save(output)
        // Wind Charge
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.WIND_CHARGE)
            .itemInput(Items.SNOWBALL)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 250)
            .save(output)
    }
}
