package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.impl.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
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
            ingredientHelper.item(Tags.Items.GLASS_BLOCKS),
            HTItemHolderLike.fromItem(Items.MAGMA_BLOCK),
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
                ingredientHelper.fuelOrDust(HTVanillaMaterialType.COAL),
                resultHelper.fluid(RagiumFluidContents.CRUDE_OIL, 125),
            ).saveSuffixed(output, "_from_coal")
        // Soul XX -> Crude Oil
        HTItemToObjRecipeBuilder
            .melting(
                ingredientHelper.item(ItemTags.SOUL_FIRE_BASE_BLOCKS),
                resultHelper.fluid(RagiumFluidContents.CRUDE_OIL, 500),
            ).saveSuffixed(output, "_from_soul")

        // Crude Oil + Clay -> Polymer Resin
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Items.CLAY_BALL),
                ingredientHelper.fluid(RagiumFluidContents.CRUDE_OIL, 125),
            ).addResult(resultHelper.item(RagiumModTags.Items.POLYMER_RESIN))
            .saveSuffixed(output, "_from_crude_oil")

        // Crude Oil -> Natural Gas + Naphtha + Tar
        distillation(
            RagiumFluidContents.CRUDE_OIL to 1000,
            resultHelper.item(RagiumItems.TAR),
            resultHelper.fluid(RagiumFluidContents.NAPHTHA, 375) to null,
            resultHelper.fluid(RagiumFluidContents.NATURAL_GAS, 375) to ingredientHelper.item(RagiumModTags.Items.PLASTICS),
        )
        // Natural Gas + Catalyst -> 4x Polymer Resin
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(RagiumItems.POLYMER_CATALYST),
                ingredientHelper.fluid(RagiumFluidContents.NATURAL_GAS, 125),
            ).addResult(resultHelper.item(RagiumModTags.Items.POLYMER_RESIN, 4))
            .saveSuffixed(output, "_from_lpg")

        // Naphtha -> Fuel + Sulfur
        distillation(
            RagiumFluidContents.NAPHTHA to 1000,
            resultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR),
            resultHelper.fluid(RagiumFluidContents.FUEL, 375) to null,
        )
        // Naphtha + Redstone -> Lubricant
        distillation(
            RagiumFluidContents.NAPHTHA to 1000,
            resultHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
            resultHelper.fluid(RagiumFluidContents.LUBRICANT, 1000) to null,
        )
        // Fuel + Crimson Crystal -> Crimson Fuel
        HTFluidTransformRecipeBuilder
            .mixing(
                ingredientHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL),
                ingredientHelper.fluid(RagiumFluidContents.FUEL, 1000),
                resultHelper.fluid(RagiumFluidContents.CRIMSON_FUEL, 1000),
            ).save(output)
    }

    @JvmStatic
    private fun sap() {
        // Bio Fuel + Water -> polymer Resin
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(RagiumCommonTags.Items.FUELS_BIO_BLOCK),
                ingredientHelper.water(250),
            ).addResult(resultHelper.item(RagiumModTags.Items.POLYMER_RESIN))
            .saveSuffixed(output, "_from_bio")

        // XX Log -> Wood Dust + Sap
        HTItemToObjRecipeBuilder
            .melting(
                ingredientHelper.item(ItemTags.LOGS_THAT_BURN),
                resultHelper.fluid(RagiumFluidContents.SAP, 125),
            ).saveSuffixed(output, "_from_log")
        // Sap -> Resin
        distillation(
            RagiumFluidContents.SAP to 1000,
            resultHelper.item(RagiumItems.RESIN),
            resultHelper.fluid(RagiumFluidContents.NATURAL_GAS, 125) to null,
        )

        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER, 3)
            .addIngredient(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.CRIMSON_CRYSTAL)
            .save(output)

        // Warped Crystal -> Elder Pearl
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL, 3)
            .addIngredient(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.WARPED_CRYSTAL)
            .save(output)

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val molten: HTFluidContent<*, *, *> = data.molten
            val material: HTMaterialType = data.material
            // molten <-> gem
            meltAndFreeze(
                null,
                HTItemMaterialVariant.GEM.itemTagKey(material),
                molten,
                RagiumConst.MOLTEN_TO_GEM,
            )

            val log: TagKey<Item> = data.log ?: continue
            val sap: HTFluidContent<*, *, *> = data.sap ?: continue
            // log -> sap
            HTItemToObjRecipeBuilder
                .melting(ingredientHelper.item(log), resultHelper.fluid(sap, RagiumConst.LOG_TO_SAP))
                .saveSuffixed(output, "_from_stems")
            // sap -> molten
            distillation(sap to 1000, null, resultHelper.fluid(molten, RagiumConst.SAP_TO_MOLTEN) to null)
        }
    }

    @JvmStatic
    private fun mutagen() {
        // Organic Mutagen
        HTFluidTransformRecipeBuilder
            .mixing(
                ingredientHelper.item(Tags.Items.FOODS_FOOD_POISONING),
                ingredientHelper.water(1000),
                resultHelper.fluid(RagiumFluidContents.ORGANIC_MUTAGEN, 1000),
            ).save(output)

        // Poisonous Potato
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Tags.Items.CROPS_POTATO),
                ingredientHelper.fluid(RagiumFluidContents.ORGANIC_MUTAGEN, 250),
            ).addResult(resultHelper.item(Items.POISONOUS_POTATO))
            .save(output)
        // Potato Sprouts
        HTItemToObjRecipeBuilder
            .extracting(
                ingredientHelper.item(Items.POISONOUS_POTATO),
                resultHelper.item(RagiumItems.POTATO_SPROUTS),
            ).save(output)
        // Green Cake
        HTItemToObjRecipeBuilder
            .compressing(
                ingredientHelper.item(RagiumItems.POTATO_SPROUTS, 16),
                resultHelper.item(RagiumItems.GREEN_CAKE),
            ).save(output)

        HTItemToObjRecipeBuilder
            .pulverizing(
                ingredientHelper.item(RagiumItems.GREEN_CAKE),
                resultHelper.item(RagiumItems.GREEN_CAKE_DUST, 8),
            ).save(output)
        // Green Pellet
        HTShapedRecipeBuilder
            .misc(RagiumItems.GREEN_PELLET, 8)
            .hollow8()
            .define('A', RagiumItems.GREEN_CAKE_DUST)
            .define('B', HTItemMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL)
            .save(output)
    }
}
