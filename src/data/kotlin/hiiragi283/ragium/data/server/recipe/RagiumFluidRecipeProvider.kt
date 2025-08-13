package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTFluidWithCatalystToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags

object RagiumFluidRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        melting()

        crudeOil()
        sap()
    }

    private fun melting() {
        // Magma Block <-> Lava
        meltAndFreeze(
            HTIngredientHelper.item(Tags.Items.GLASS_BLOCKS),
            Items.MAGMA_BLOCK,
            HTFluidContent.LAVA,
            125,
        )
    }

    private fun crudeOil() {
        // Coal -> Crude Oil
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(HTIngredientHelper.coal()),
                HTResultHelper.fluid(RagiumFluidContents.CRUDE_OIL, 125),
            ).saveSuffixed(output, "_from_coal")
        // Soul XX -> Crude Oil
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(ItemTags.SOUL_FIRE_BASE_BLOCKS),
                HTResultHelper.fluid(RagiumFluidContents.CRUDE_OIL, 500),
            ).saveSuffixed(output, "_from_soul")

        // Crude Oil + clay -> Polymer Resin
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.CLAY_BALL),
                HTIngredientHelper.fluid(RagiumFluidContents.CRUDE_OIL, 125),
                HTResultHelper.item(RagiumModTags.Items.POLYMER_RESIN),
            ).saveSuffixed(output, "_from_crude_oil")

        // Crude Oil -> LPG + Naphtha + Tar
        distillation(
            output,
            RagiumFluidContents.CRUDE_OIL.commonTag to 1000,
            HTResultHelper.item(RagiumItems.TAR),
            HTResultHelper.fluid(RagiumFluidContents.NAPHTHA, 375),
            HTResultHelper.fluid(RagiumFluidContents.LPG, 375),
        )
        // LPG + Coal -> 4x Polymer Resin
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(HTIngredientHelper.coal()),
                HTIngredientHelper.fluid(RagiumFluidContents.LPG, 125),
                HTResultHelper.item(RagiumModTags.Items.POLYMER_RESIN, 4),
            ).saveSuffixed(output, "_from_lpg")
        // Naphtha -> Diesel + Sulfur
        distillation(
            output,
            RagiumFluidContents.NAPHTHA.commonTag to 1000,
            HTResultHelper.item(RagiumCommonTags.Items.DUSTS_SULFUR),
            HTResultHelper.fluid(RagiumFluidContents.DIESEL, 375),
        )
        // Diesel + Crimson Crystal -> Crimson Fuel
    }

    private fun sap() {
        // Bio Fuel + Water -> polymer Resin
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(RagiumCommonTags.Items.FUELS_BIO_BLOCK),
                HTIngredientHelper.water(250),
                HTResultHelper.item(RagiumModTags.Items.POLYMER_RESIN),
            ).saveSuffixed(output, "_from_bio")

        // XX Log -> Wood Dust + Sap
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(ItemTags.LOGS_THAT_BURN),
                HTResultHelper.fluid(RagiumFluidContents.SAP, 125),
            ).saveSuffixed(output, "_from_log")
        // Sap -> Syrup
        distillation(
            output,
            RagiumFluidContents.SAP.commonTag to 1000,
            null,
            HTResultHelper.fluid(RagiumFluidContents.SYRUP, 750),
            HTResultHelper.fluid(Fluids.WATER, 250),
        )
        // Syrup -> Sugar
        HTFluidWithCatalystToObjRecipeBuilder
            .solidifying(
                null,
                HTIngredientHelper.fluid(RagiumFluidContents.SYRUP, 250),
                HTResultHelper.item(Items.SUGAR),
            ).saveSuffixed(output, "_from_syrup")

        // Crimson Stem -> Crimson Sap
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(ItemTags.CRIMSON_STEMS),
                HTResultHelper.fluid(RagiumFluidContents.CRIMSON_SAP, 125),
            ).saveSuffixed(output, "_from_stems")
        // Crimson Sap -> Sap + Crimson Crystal
        distillation(
            output,
            RagiumFluidContents.CRIMSON_SAP.commonTag to 1000,
            HTResultHelper.item(RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL),
            HTResultHelper.fluid(RagiumFluidContents.SAP, 125),
        )
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER, onlyBlasting = true)
            .addIngredient(RagiumCommonTags.Items.STORAGE_BLOCKS_CRIMSON_CRYSTAL)
            .save(output)

        // Warped Stem -> Warped Sap
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(ItemTags.WARPED_STEMS),
                HTResultHelper.fluid(RagiumFluidContents.WARPED_SAP, 125),
            ).saveSuffixed(output, "_from_stems")
        // Warped Sap -> Sap + Warped Crystal
        distillation(
            output,
            RagiumFluidContents.WARPED_SAP.commonTag to 1000,
            HTResultHelper.item(RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL),
            HTResultHelper.fluid(RagiumFluidContents.SAP, 125),
        )
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL, onlyBlasting = true)
            .addIngredient(RagiumCommonTags.Items.STORAGE_BLOCKS_WARPED_CRYSTAL)
            .save(output)
    }
}
