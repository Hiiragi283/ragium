package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
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
        // Magma Block <-> Lava
        meltAndFreeze(
            HTIngredientHelper.item(Tags.Items.GLASS_BLOCKS),
            Items.MAGMA_BLOCK,
            HTFluidContent.LAVA,
            125,
        )

        crudeOil()
        sap()
        mutagen()
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

    @JvmStatic
    private fun mutagen() {
        // Organic Mutagen
        HTFluidTransformRecipeBuilder
            .mixing(
                HTIngredientHelper.item(Tags.Items.FOODS_FOOD_POISONING),
                HTIngredientHelper.water(1000),
                HTResultHelper.INSTANCE.fluid(RagiumFluidContents.ORGANIC_MUTAGEN, 1000),
            ).save(output)

        // Poisonous Potato
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.CROPS_POTATO),
                HTIngredientHelper.fluid(RagiumFluidContents.ORGANIC_MUTAGEN, 250),
                HTResultHelper.INSTANCE.item(Items.POISONOUS_POTATO),
            ).save(output)
        // Potato Sprouts
        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Items.POISONOUS_POTATO),
                HTResultHelper.INSTANCE.item(RagiumItems.POTATO_SPROUTS),
            ).save(output)
        // Green Cake
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(RagiumItems.POTATO_SPROUTS, 16),
                HTResultHelper.INSTANCE.item(RagiumItems.GREEN_CAKE),
            ).save(output)

        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(RagiumItems.GREEN_CAKE),
                HTResultHelper.INSTANCE.item(RagiumItems.GREEN_CAKE_DUST, 8),
            ).save(output)
        // Green Pellet
        HTShapedRecipeBuilder(RagiumItems.GREEN_PELLET, 8)
            .hollow8()
            .define('A', RagiumItems.GREEN_CAKE_DUST)
            .define('B', HTItemMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL)
            .save(output)
    }
}
