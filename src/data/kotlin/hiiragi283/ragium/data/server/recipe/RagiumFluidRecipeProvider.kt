package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTMoltenCrystalData
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumFluidRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        melting()

        crudeOil()
        sap()
    }

    @JvmStatic
    private fun melting() {
        // Magma Block <-> Lava
        meltAndFreeze(
            HTIngredientHelper.item(Tags.Items.GLASS_BLOCKS),
            Items.MAGMA_BLOCK,
            HTFluidContent.LAVA,
            125,
        )
    }

    @JvmStatic
    private fun crudeOil() {
        // Coal -> Crude Oil
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.fuelOrDust(HTVanillaMaterialType.COAL),
                HTResultHelper.INSTANCE.fluid(RagiumFluidContents.CRUDE_OIL, 125),
            ).saveSuffixed(output, "_from_coal")
        // Soul XX -> Crude Oil
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(ItemTags.SOUL_FIRE_BASE_BLOCKS),
                HTResultHelper.INSTANCE.fluid(RagiumFluidContents.CRUDE_OIL, 500),
            ).saveSuffixed(output, "_from_soul")

        // Crude Oil + Clay -> Polymer Resin
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.CLAY_BALL),
                HTIngredientHelper.fluid(RagiumFluidContents.CRUDE_OIL, 125),
                HTResultHelper.INSTANCE.item(RagiumModTags.Items.POLYMER_RESIN),
            ).saveSuffixed(output, "_from_crude_oil")

        // Crude Oil -> Natural Gas + Naphtha + Tar
        distillation(
            RagiumFluidContents.CRUDE_OIL to 1000,
            HTResultHelper.INSTANCE.item(RagiumItems.TAR),
            HTResultHelper.INSTANCE.fluid(RagiumFluidContents.NAPHTHA, 375) to null,
            HTResultHelper.INSTANCE.fluid(RagiumFluidContents.NATURAL_GAS, 375) to HTIngredientHelper.item(RagiumModTags.Items.PLASTICS),
        )
        // Natural Gas + Catalyst -> 4x Polymer Resin
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(RagiumItems.POLYMER_CATALYST),
                HTIngredientHelper.fluid(RagiumFluidContents.NATURAL_GAS, 125),
                HTResultHelper.INSTANCE.item(RagiumModTags.Items.POLYMER_RESIN, 4),
            ).saveSuffixed(output, "_from_lpg")
        // Naphtha -> Diesel + Sulfur
        distillation(
            RagiumFluidContents.NAPHTHA to 1000,
            HTResultHelper.INSTANCE.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR),
            HTResultHelper.INSTANCE.fluid(RagiumFluidContents.FUEL, 375) to null,
        )
        // Diesel + Crimson Crystal -> Bloo-Diesel
        HTFluidTransformRecipeBuilder
            .mixing(
                HTIngredientHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL),
                HTIngredientHelper.fluid(RagiumFluidContents.FUEL, 1000),
                HTResultHelper.INSTANCE.fluid(RagiumFluidContents.CRIMSON_FUEL, 1000),
            ).save(output)
    }

    @JvmStatic
    private fun sap() {
        // Bio Fuel + Water -> polymer Resin
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(RagiumCommonTags.Items.FUELS_BIO_BLOCK),
                HTIngredientHelper.water(250),
                HTResultHelper.INSTANCE.item(RagiumModTags.Items.POLYMER_RESIN),
            ).saveSuffixed(output, "_from_bio")

        // XX Log -> Wood Dust + Sap
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(ItemTags.LOGS_THAT_BURN),
                HTResultHelper.INSTANCE.fluid(RagiumFluidContents.SAP, 125),
            ).saveSuffixed(output, "_from_log")
        // Sap -> Resin
        distillation(
            RagiumFluidContents.SAP to 1000,
            HTResultHelper.INSTANCE.item(RagiumItems.RESIN),
            HTResultHelper.INSTANCE.fluid(RagiumFluidContents.NATURAL_GAS, 125) to null,
        )

        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER, onlyBlasting = true)
            .addIngredient(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.CRIMSON_CRYSTAL)
            .save(output)

        // Warped Crystal -> Elder Pearl
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL, onlyBlasting = true)
            .addIngredient(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.WARPED_CRYSTAL)
            .save(output)

        for (data: HTMoltenCrystalData in HTMoltenCrystalData.entries) {
            val molten: HTFluidContent<*, *, *> = data.molten
            val material: HTMaterialType = data.material
            // molten <-> gem
            meltAndFreeze(
                null,
                HTItemMaterialVariant.GEM.itemTagKey(material),
                molten,
                HTMoltenCrystalData.MOLTEN_TO_GEM,
            )

            val log: TagKey<Item> = data.log ?: continue
            val sap: HTFluidContent<*, *, *> = data.sap ?: continue
            // log -> sap
            HTItemToObjRecipeBuilder
                .melting(HTIngredientHelper.item(log), HTResultHelper.INSTANCE.fluid(sap, HTMoltenCrystalData.LOG_TO_SAP))
                .saveSuffixed(output, "_from_stems")
            // sap -> molten
            distillation(sap to 1000, null, HTResultHelper.INSTANCE.fluid(molten, HTMoltenCrystalData.SAP_TO_MOLTEN) to null)
        }
    }
}
