package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags

object RagiumFluidRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        extracting()
        infusing()
        // solidifying()

        // biomass()
        bottle()
        crudeOil()
        // crystal()
        exp()
        sap()
    }

    /*private fun biomass() {
        // Biomass -> Ethanol
        createRefining()
            .fluidOutput(RagiumFluidContents.FUEL, 500)
            .fluidInput(RagiumFluidContents.BIOMASS)
            .saveSuffixed(output, "_from_biomass")
        // Ethanol + Plant Oil -> Fuel + Glycerol
    }*/

    private fun bottle() {
        // Exp Bottle
        createSolidifying()
            .itemOutput(Items.EXPERIENCE_BOTTLE)
            .catalyst(Items.GLASS_BOTTLE)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 250)
            .save(output)

        createMelting()
            .itemOutput(Items.GLASS_BOTTLE)
            .fluidOutput(RagiumFluidContents.EXPERIENCE, 250)
            .itemInput(Items.EXPERIENCE_BOTTLE)
            .saveSuffixed(output, "_from_exp")
        // Honey Bottle
        createSolidifying()
            .itemOutput(Items.HONEY_BOTTLE)
            .catalyst(Items.GLASS_BOTTLE)
            .fluidInput(RagiumFluidContents.HONEY, 250)
            .save(output)

        createMelting()
            .fluidOutput(RagiumFluidContents.HONEY)
            .itemInput(Items.HONEY_BLOCK)
            .saveSuffixed(output, "_from_block")
        // Water Bottle
        createSolidifying()
            .itemOutput(createPotionStack(Potions.WATER))
            .catalyst(Items.GLASS_BOTTLE)
            .waterInput(250)
            .save(output, RagiumAPI.id("water_bottle"))
    }

    private fun crudeOil() {
        // Coal -> Crude Oil
        createMelting()
            .fluidOutput(RagiumFluidContents.CRUDE_OIL, 125)
            .itemInput(Items.COAL)
            .saveSuffixed(output, "_from_coal")
        // Soul XX -> Crude Oil
        createMelting()
            .fluidOutput(RagiumFluidContents.CRUDE_OIL, 500)
            .itemInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .saveSuffixed(output, "_from_soul")

        // Crude Oil -> LPG + Naphtha + Tar
        createRefining()
            .fluidOutput(RagiumFluidContents.LPG, 20)
            .fluidOutput(RagiumFluidContents.NAPHTHA, 40)
            .itemOutput(RagiumItems.TAR)
            .fluidInput(RagiumFluidContents.CRUDE_OIL, 100)
            .save(output)
        // LPG + Clay -> Polymer Resin
        createSolidifying()
            .itemOutput(RagiumModTags.Items.POLYMER_RESIN)
            .catalyst(Items.CLAY_BALL)
            .fluidInput(RagiumFluidContents.LPG, 100)
            .save(output)
        // Naphtha -> Light Fuel + Heavy Fuel + Sulfur
        createRefining()
            .fluidOutput(RagiumFluidContents.LIGHT_FUEL, 40)
            .fluidOutput(RagiumFluidContents.HEAVY_FUEL, 40)
            .itemOutput(RagiumCommonTags.Items.DUSTS_SULFUR)
            .fluidInput(RagiumFluidContents.NAPHTHA, 100)
            .save(output)
        // Light Fuel + Heavy Fuel -> Diesel
    }

    /*private fun crudeOil() {
        // Coal -> Crude Oil
        createExtracting()
            .fluidOutput(RagiumFluidContents.CRUDE_OIL, 125)
            .itemInput(Items.COAL)
            .saveSuffixed(output, "_from_coal")
        // Soul XX -> Crude Oil
        createExtracting()
            .fluidOutput(RagiumFluidContents.CRUDE_OIL, 500)
            .itemInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .saveSuffixed(output, "_from_soul")

        // Crude Oil -> Naphtha + Tar
        createRefining()
            .itemOutput(RagiumItems.TAR)
            .fluidOutput(RagiumFluidContents.NAPHTHA, 750)
            .fluidInput(RagiumFluidContents.CRUDE_OIL)
            .save(output, RagiumAPI.id("naphtha_from_crude_oil"))
        // Naphtha -> Fuel + Sulfur
        createRefining()
            .itemOutput(RagiumItems.SULFUR_DUST)
            .fluidOutput(RagiumFluidContents.FUEL, 750)
            .fluidInput(RagiumFluidContents.NAPHTHA)
            .save(output, RagiumAPI.id("fuel_from_naphtha"))
        // Naphtha + Coal -> Polymer Resin
        createInfusing()
            .itemOutput(RagiumItems.POLYMER_RESIN)
            .itemInput(Items.COAL)
            .fluidInput(RagiumFluidContents.NAPHTHA, 500)
            .save(output, RagiumAPI.id("polymer_resin_from_naphtha"))

        // Tar -> Aromatic Compound
        createExtracting()
            .fluidOutput(RagiumFluidContents.AROMATIC_COMPOUND, 200)
            .itemInput(RagiumItems.TAR)
            .saveSuffixed(output, "_from_tar")
        // Aromatic Compound + Sand -> TNT
        createInfusing()
            .itemOutput(Items.TNT, 8)
            .itemInput(Tags.Items.SANDS)
            .fluidInput(RagiumFluidContents.AROMATIC_COMPOUND, 200)
            .save(output)
    }*/

    /*private fun crystal() {
        // Quartz
        createInfusing()
            .itemOutput(Items.QUARTZ, 2)
            .itemInput(Tags.Items.GEMS_QUARTZ)
            .waterInput(250)
            .saveSuffixed(output, "_from_water")
        // Amethyst
        createInfusing()
            .itemOutput(Items.AMETHYST_SHARD, 2)
            .itemInput(Tags.Items.GEMS_AMETHYST)
            .waterInput(250)
            .saveSuffixed(output, "_from_water")
    }*/

    private fun exp() {
        // Golden Apple
        createSolidifying()
            .itemOutput(Items.ENCHANTED_GOLDEN_APPLE)
            .catalyst(Items.GOLDEN_APPLE)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 8000)
            .save(output)
        // Exp Berries
        createSolidifying()
            .itemOutput(RagiumItems.EXP_BERRIES)
            .catalyst(Tags.Items.FOODS_BERRY)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 1000)
            .save(output)
        // Blaze Powder
        createSolidifying()
            .itemOutput(Items.BLAZE_POWDER)
            .catalyst(RagiumCommonTags.Items.DUSTS_SULFUR)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 250)
            .save(output)
        // Wind Charge
        createSolidifying()
            .itemOutput(Items.WIND_CHARGE)
            .catalyst(Items.SNOWBALL)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 250)
            .save(output)
        // Ghast Tear
        createSolidifying()
            .itemOutput(Items.GHAST_TEAR)
            .catalyst(Items.CHISELED_QUARTZ_BLOCK)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 1000)
            .save(output)
    }

    private fun sap() {
        // XX Log -> Wood Dust + Sap
        createMelting()
            .fluidOutput(RagiumFluidContents.SAP, 125)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .saveSuffixed(output, "_from_log")
        // Sap -> Slime Ball
        /*HTMachineRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(Items.SLIME_BALL)
            .fluidInput(RagiumFluidContents.SAP.commonTag, 1000)
            .saveSuffixed(output, "_from_sap")*/

        // Crimson Stem -> Crimson Sap
        createMelting()
            .fluidOutput(RagiumFluidContents.CRIMSON_SAP, 125)
            .itemInput(ItemTags.CRIMSON_STEMS)
            .saveSuffixed(output, "_from_stems")
        // Crimson Sap -> Sap + Crimson Crystal
        createRefining()
            .itemOutput(RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL)
            .fluidOutput(RagiumFluidContents.SAP, 125)
            .fluidInput(RagiumFluidContents.CRIMSON_SAP.commonTag, 1000)
            .save(output)
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER)
            .addIngredient(RagiumCommonTags.Items.STORAGE_BLOCKS_CRIMSON_CRYSTAL)
            .save(output)

        // Warped Stem -> Warped Sap
        createMelting()
            .fluidOutput(RagiumFluidContents.WARPED_SAP, 125)
            .itemInput(ItemTags.WARPED_STEMS)
            .saveSuffixed(output, "_from_stems")
        // Warped Sap -> Sap + Warped Crystal
        createRefining()
            .itemOutput(RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL)
            .fluidOutput(RagiumFluidContents.SAP, 125)
            .fluidInput(RagiumFluidContents.WARPED_SAP.commonTag, 1000)
            .save(output)
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL)
            .addIngredient(RagiumCommonTags.Items.STORAGE_BLOCKS_WARPED_CRYSTAL)
            .save(output)

        // Eldritch Orb -> Eldritch Goo
        createMelting()
            .fluidOutput(RagiumFluidContents.ELDRITCH_GOO, 125)
            .itemInput(RagiumItems.ELDRITCH_ORB)
            .saveSuffixed(output, "_from_orb")
        // Eldritch Goo -> Eldritch Pearl
        createSolidifying()
            .itemOutput(RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
            .fluidInput(RagiumFluidContents.ELDRITCH_GOO, 500)
            .save(output)
    }

    //    Extracting    //

    private fun extracting() {
        // Magma Block -> Cobblestone + Lava
        createMelting()
            .fluidOutput(Fluids.LAVA, 125)
            .itemInput(Items.MAGMA_BLOCK)
            .saveSuffixed(output, "_from_magma_block")

        // Exp Berries -> Liquid Exp
        createMelting()
            .fluidOutput(RagiumFluidContents.EXPERIENCE, 50)
            .itemInput(RagiumItems.EXP_BERRIES)
            .saveSuffixed(output, "_from_berries")
    }

    //    Infusing    //

    private fun infusing() {
        // Dirt -> Mud
        createSolidifying()
            .itemOutput(Items.MUD)
            .catalyst(Items.DIRT)
            .waterInput(250)
            .saveSuffixed(output, "_from_dirt")
        // Silt -> Clay
        createSolidifying()
            .itemOutput(Items.CLAY)
            .catalyst(RagiumBlocks.SILT)
            .waterInput(250)
            .saveSuffixed(output, "_from_silt")

        // Milk + Snow -> Ice Cream
        createSolidifying()
            .itemOutput(RagiumItems.ICE_CREAM)
            .catalyst(Items.SNOWBALL)
            .milkInput(250)
            .save(output)
    }

    //    Solidifying    //

    /*private fun solidifying() {
        // Water -> Ice
        createSolidifying()
            .itemOutput(Items.ICE)
            .waterInput()
            .catalyst(RagiumItemTags.MOLDS_BLOCK)
            .save(output)
        // Water -> Snowball
        createSolidifying()
            .itemOutput(Items.SNOWBALL)
            .waterInput(250)
            .catalyst(RagiumItemTags.MOLDS_BALL)
            .save(output)

        // Honey -> Honey Block
        createSolidifying()
            .itemOutput(Items.HONEY_BLOCK)
            .fluidInput(RagiumFluidContents.HONEY)
            .catalyst(RagiumItemTags.MOLDS_BLOCK)
            .save(output)

        // Sap -> Slimeball
        createSolidifying()
            .itemOutput(Items.SLIME_BALL)
            .fluidInput(RagiumFluidContents.SAP)
            .catalyst(RagiumItemTags.MOLDS_BALL)
            .save(output)
    }*/
}
