package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTItemWithFluidToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluids

object RagiumFluidRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        melting()

        crudeOil()
        sap()
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

        // Crude Oil + clay -> Polymer Resin
        HTItemWithFluidToItemRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.CLAY_BALL),
                HTIngredientHelper.fluid(RagiumFluidContents.CRUDE_OIL, 125),
                HTResultHelper.item(RagiumModTags.Items.POLYMER_RESIN),
            ).saveSuffixed(output, "_from_crude_oil")

        // Crude Oil -> LPG + Naphtha + Tar
        createDistillation()
            .fluidOutput(RagiumFluidContents.NAPHTHA, 375)
            .fluidOutput(RagiumFluidContents.LPG, 375)
            .itemOutput(RagiumItems.TAR)
            .fluidInput(RagiumFluidContents.CRUDE_OIL, 1000)
            .saveSuffixed(output, "_from_crude_oil")
        // LPG + Coal -> 4x Polymer Resin
        HTItemWithFluidToItemRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.COAL),
                HTIngredientHelper.fluid(RagiumFluidContents.LPG, 125),
                HTResultHelper.item(RagiumModTags.Items.POLYMER_RESIN, 4),
            ).saveSuffixed(output, "_from_lpg")
        // Naphtha -> Diesel + Sulfur
        createDistillation()
            .fluidOutput(RagiumFluidContents.DIESEL, 375)
            .itemOutput(RagiumCommonTags.Items.DUSTS_SULFUR)
            .fluidInput(RagiumFluidContents.NAPHTHA, 1000)
            .saveSuffixed(output, "_from_naphtha")
        // Diesel + Crimson Crystal -> Crimson Fuel
    }

    private fun sap() {
        // XX Log -> Wood Dust + Sap
        createMelting()
            .fluidOutput(RagiumFluidContents.SAP, 125)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .saveSuffixed(output, "_from_log")
        // Sap -> Syrup
        createDistillation()
            .itemOutput(Items.SLIME_BALL)
            .fluidOutput(RagiumFluidContents.SYRUP, 750)
            .fluidOutput(Fluids.WATER, 250)
            .fluidInput(RagiumFluidContents.SAP, 1000)
            .saveSuffixed(output, "_from_sap")
        // Syrup -> Sugar
        HTItemWithFluidToItemRecipeBuilder
            .solidifying(
                null,
                HTIngredientHelper.fluid(RagiumFluidContents.SYRUP, 250),
                HTResultHelper.item(Items.SUGAR),
            ).saveSuffixed(output, "_from_syrup")

        // Crimson Stem -> Crimson Sap
        createMelting()
            .fluidOutput(RagiumFluidContents.CRIMSON_SAP, 125)
            .itemInput(ItemTags.CRIMSON_STEMS)
            .saveSuffixed(output, "_from_stems")
        // Crimson Sap -> Sap + Crimson Crystal
        createDistillation()
            .itemOutput(RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL)
            .fluidOutput(RagiumFluidContents.SAP, 125)
            .fluidInput(RagiumFluidContents.CRIMSON_SAP.commonTag, 1000)
            .saveSuffixed(output, "_from_crimson_sap")
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER, onlyBlasting = true)
            .addIngredient(RagiumCommonTags.Items.STORAGE_BLOCKS_CRIMSON_CRYSTAL)
            .save(output)

        // Warped Stem -> Warped Sap
        createMelting()
            .fluidOutput(RagiumFluidContents.WARPED_SAP, 125)
            .itemInput(ItemTags.WARPED_STEMS)
            .saveSuffixed(output, "_from_stems")
        // Warped Sap -> Sap + Warped Crystal
        createDistillation()
            .itemOutput(RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL)
            .fluidOutput(RagiumFluidContents.SAP, 125)
            .fluidInput(RagiumFluidContents.WARPED_SAP.commonTag, 1000)
            .saveSuffixed(output, "_from_warped_sap")
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL, onlyBlasting = true)
            .addIngredient(RagiumCommonTags.Items.STORAGE_BLOCKS_WARPED_CRYSTAL)
            .save(output)
    }

    //    Extracting    //

    private fun melting() {
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
}
