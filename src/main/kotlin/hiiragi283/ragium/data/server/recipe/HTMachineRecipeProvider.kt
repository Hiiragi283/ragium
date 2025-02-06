package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.*
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumFluidsNew
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTMachineRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        compressor(output)
        extractor(output)
        infuser(output)
        refinery(output)
    }

    //    Compressor    //

    private fun compressor(output: RecipeOutput) {
        // Circuit Board
        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(RagiumItemTags.PLASTICS)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.QUARTZ)
            .itemOutput(RagiumItems.CIRCUIT_BOARD)
            .save(output)
    }

    //    Extractor    //

    private fun extractor(output: RecipeOutput) {
        // Water
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.BUCKETS_WATER)
            .itemOutput(Items.BUCKET)
            .waterOutput()
            .save(output, RagiumAPI.id("water"))

        // Lava
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.BUCKETS_LAVA)
            .itemOutput(Items.BUCKET)
            .fluidOutput(Fluids.LAVA)
            .save(output, RagiumAPI.id("lava"))

        // Milk
        HTExtractorRecipeBuilder()
            .itemInput(Items.MILK_BUCKET)
            .itemOutput(Items.BUCKET)
            .fluidOutput(NeoForgeMod.MILK)
            .save(output, RagiumAPI.id("milk"))

        // Crude Oil
        HTExtractorRecipeBuilder()
            .itemInput(RagiumItems.CRUDE_OIL_BUCKET)
            .itemOutput(Items.BUCKET)
            .fluidOutput(RagiumFluidsNew.CRUDE_OIL)
            .save(output, RagiumAPI.id("crude_oil"))

        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.COALS, 8)
            .fluidOutput(RagiumFluidsNew.CRUDE_OIL)
            .save(output, RagiumAPI.id("crude_oil_from_coal"))

        // Blaze Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Items.BLAZE_POWDER)
            .itemOutput(RagiumItems.BLAZE_REAGENT)
            .save(output)

        // Creeper Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.GUNPOWDERS)
            .itemOutput(RagiumItems.CREEPER_REAGENT)
            .saveSuffixed(output, "_from_powder")

        // Deep Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.COBBLESTONES_DEEPSLATE, 8)
            .itemOutput(RagiumItems.DEEPANT_REAGENT)
            .save(output)

        // Ender Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.ENDER_PEARLS)
            .itemOutput(RagiumItems.ENDER_REAGENT)
            .saveSuffixed(output, "_from_pearl")

        HTExtractorRecipeBuilder()
            .itemInput(Items.ENDER_EYE)
            .itemOutput(RagiumItems.ENDER_REAGENT, 2)
            .saveSuffixed(output, "_from_eye")

        HTExtractorRecipeBuilder()
            .itemInput(Items.END_CRYSTAL)
            .itemOutput(RagiumItems.ENDER_REAGENT, 4)
            .saveSuffixed(output, "_from_crystal")

        // Glow Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Items.GLOW_LICHEN, 8)
            .itemOutput(RagiumItems.GLOW_REAGENT)
            .saveSuffixed(output, "_from_lichen")

        HTExtractorRecipeBuilder()
            .itemInput(Items.GLOW_BERRIES, 4)
            .itemOutput(RagiumItems.GLOW_REAGENT)
            .saveSuffixed(output, "_from_berry")

        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.DUSTS_GLOWSTONE)
            .itemOutput(RagiumItems.GLOW_REAGENT)
            .saveSuffixed(output, "_from_dust")

        HTExtractorRecipeBuilder()
            .itemInput(Items.GLOW_INK_SAC)
            .itemOutput(RagiumItems.GLOW_REAGENT, 2)
            .saveSuffixed(output, "_from_ink")

        // Prismarine Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.GEMS_PRISMARINE)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT)
            .saveSuffixed(output, "_from_crystal")

        HTExtractorRecipeBuilder()
            .itemInput(Items.PRISMARINE_SHARD, 3)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT, 2)
            .saveSuffixed(output, "_from_shard")

        HTExtractorRecipeBuilder()
            .itemInput(Items.NAUTILUS_SHELL)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT, 4)
            .saveSuffixed(output, "_from_nautilus")

        HTExtractorRecipeBuilder()
            .itemInput(Items.HEART_OF_THE_SEA)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT, 64)
            .saveSuffixed(output, "_from_heart")

        // Sculk Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Items.SCULK_VEIN, 8)
            .itemOutput(RagiumItems.SCULK_REAGENT)
            .saveSuffixed(output, "_from_vein")

        HTExtractorRecipeBuilder()
            .itemInput(Items.SCULK)
            .itemOutput(RagiumItems.SCULK_REAGENT)
            .save(output)

        HTExtractorRecipeBuilder()
            .itemInput(Items.SCULK_CATALYST)
            .itemOutput(RagiumItems.SCULK_REAGENT, 4)
            .saveSuffixed(output, "_from_catalyst")

        HTExtractorRecipeBuilder()
            .itemInput(Items.SCULK_SHRIEKER)
            .itemOutput(RagiumItems.SCULK_REAGENT, 16)
            .saveSuffixed(output, "_from_shrieker")

        // Soul Reagent
        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .itemOutput(RagiumItems.SOUL_REAGENT)
            .save(output)

        // Wither Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Items.WITHER_ROSE)
            .itemOutput(RagiumItems.WITHER_REAGENT, 4)
            .saveSuffixed(output, "_from_rose")

        HTExtractorRecipeBuilder()
            .itemInput(Items.WITHER_SKELETON_SKULL)
            .itemOutput(RagiumItems.WITHER_REAGENT, 8)
            .saveSuffixed(output, "_from_skull")
    }

    //    Infuser    //

    private fun infuser(output: RecipeOutput) {
        // Blaze Reagent -> Blaze Acid
        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.BLAZE_REAGENT)
            .waterInput()
            .fluidOutput(RagiumFluids.SULFURIC_ACID)
            .save(output)
    }

    //    Refinery    //

    private fun refinery(output: RecipeOutput) {
        // Soul XX -> Crude Oil
        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .itemOutput(Items.SAND)
            .fluidOutput(RagiumFluidsNew.CRUDE_OIL, FluidType.BUCKET_VOLUME / 2)
            .save(output, RagiumAPI.id("crude_oil_from_soul"))
        // Crude Oil -> Naphtha
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumFluidsNew.CRUDE_OIL)
            .fluidOutput(RagiumFluids.NAPHTHA)
            .save(output)
        // Naphtha -> Polymer Resin + Fuel
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumFluids.NAPHTHA)
            .itemOutput(RagiumItems.POLYMER_RESIN, 2)
            .fluidOutput(RagiumFluids.FUEL)
            .save(output)
        // Polymer Resin -> Plastic
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(RagiumItems.POLYMER_RESIN)
            .itemOutput(RagiumItems.PLASTIC_PLATE)
            .save(output)

        // Biomass -> Alcohol
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumFluids.BIOMASS)
            .fluidOutput(RagiumFluids.ETHANOL)
            .save(output)
        // Alcohol + Plant Oil -> Bio Fuel + Glycerol
        HTMixerRecipeBuilder()
            .fluidInput(RagiumFluids.ETHANOL, FluidType.BUCKET_VOLUME * 4)
            .fluidInput(RagiumFluids.PLANT_OIL)
            .fluidOutput(RagiumFluids.BIODIESEL, FluidType.BUCKET_VOLUME * 4)
            // .fluidOutput(RagiumFluids.GLYCEROL)
            .save(output)

        // XX Log -> Sap + Pulp
        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.WOOD, 4)
            .fluidOutput(RagiumFluids.SAP)
            .save(output)
        // Sap -> Slimeball
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumFluids.SAP)
            .itemOutput(Items.SLIME_BALL)
            .save(output)

        // Crimson Stem -> Crimson Sap
        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.CRIMSON_STEMS)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.WOOD, 4)
            .fluidOutput(RagiumFluids.CRIMSON_SAP)
            .savePrefixed(output, "crimson_")
        // Crimson Sap -> Crimson Crystal + Sap
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumFluids.CRIMSON_SAP)
            .itemOutput(RagiumItems.CRIMSON_CRYSTAL)
            .fluidOutput(RagiumFluids.SAP)
            .save(output)

        // Warped Stem -> Warped Sap
        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.WARPED_STEMS)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.WOOD, 4)
            .fluidOutput(RagiumFluids.WARPED_SAP)
            .savePrefixed(output, "warped_")
        // Warped Sap -> Warped Crystal + Sap
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumFluids.WARPED_SAP)
            .itemOutput(RagiumItems.WARPED_CRYSTAL)
            .fluidOutput(RagiumFluids.SAP)
            .save(output)
    }
}
