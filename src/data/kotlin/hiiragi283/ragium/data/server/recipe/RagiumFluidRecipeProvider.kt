package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTMixingRecipeBuilder
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
            itemCreator.fromItem(RagiumItems.getMold(CommonMaterialPrefixes.STORAGE_BLOCK)),
            Items.MAGMA_BLOCK.toHolderLike(),
            HTFluidContent.LAVA,
            125,
        )
        // Water + Lava -> Obsidian
        HTMixingRecipeBuilder
            .create()
            .addIngredient(fluidCreator.water(1000))
            .addIngredient(fluidCreator.lava(1000))
            .setResult(resultHelper.item(Items.OBSIDIAN))
            .save(output)

        crudeOil()
        sap()
        mutagen()
    }

    @JvmStatic
    private fun crudeOil() {
        // Coal -> Crude Oil
        HTItemToObjRecipeBuilder
            .melting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.COAL),
                resultHelper.fluid(RagiumFluidContents.CRUDE_OIL, 125),
            ).saveSuffixed(output, "_from_coal")
        // Soul XX -> Crude Oil
        HTItemToObjRecipeBuilder
            .melting(
                itemCreator.fromTagKey(ItemTags.SOUL_FIRE_BASE_BLOCKS),
                resultHelper.fluid(RagiumFluidContents.CRUDE_OIL, 500),
            ).saveSuffixed(output, "_from_soul")

        // Crude Oil + Clay -> Polymer Resin
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.CLAY_BALL),
                fluidCreator.fromContent(RagiumFluidContents.CRUDE_OIL, 125),
            ).addResult(resultHelper.item(RagiumModTags.Items.POLYMER_RESIN))
            .saveSuffixed(output, "_from_crude_oil")

        // Crude Oil -> Natural Gas + Naphtha + Tar
        distillation(
            RagiumFluidContents.CRUDE_OIL to 1000,
            resultHelper.item(RagiumItems.TAR),
            resultHelper.fluid(RagiumFluidContents.NAPHTHA, 375) to null,
            resultHelper.fluid(RagiumFluidContents.NATURAL_GAS, 375) to itemCreator.fromTagKey(RagiumModTags.Items.PLASTICS),
        )
        // Natural Gas + Catalyst -> 4x Polymer Resin
        HTFluidTransformRecipeBuilder
            .solidifying(
                itemCreator.fromItem(RagiumItems.POLYMER_CATALYST),
                fluidCreator.fromContent(RagiumFluidContents.NATURAL_GAS, 125),
                resultHelper.item(RagiumModTags.Items.POLYMER_RESIN, 4),
            ).saveSuffixed(output, "_from_lpg")

        // Naphtha -> Fuel + Sulfur
        distillation(
            RagiumFluidContents.NAPHTHA to 1000,
            resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR),
            resultHelper.fluid(RagiumFluidContents.FUEL, 375) to null,
        )
        // Naphtha + Redstone -> Lubricant
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE))
            .addIngredient(fluidCreator.fromContent(RagiumFluidContents.NAPHTHA, 1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.LUBRICANT, 1000))
            .save(output)
        // Fuel + Crimson Crystal -> Crimson Fuel
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.CRIMSON_CRYSTAL))
            .addIngredient(fluidCreator.fromContent(RagiumFluidContents.FUEL, 1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.CRIMSON_FUEL, 1000))
            .save(output)
    }

    @JvmStatic
    private fun sap() {
        // Bio Fuel + Water -> polymer Resin
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(RagiumCommonTags.Items.FUELS_BIO_BLOCK),
                fluidCreator.water(250),
            ).addResult(resultHelper.item(RagiumModTags.Items.POLYMER_RESIN))
            .saveSuffixed(output, "_from_bio")

        // XX Log -> Wood Dust + Sap
        HTItemToObjRecipeBuilder
            .melting(
                itemCreator.fromTagKey(ItemTags.LOGS_THAT_BURN),
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
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.CRIMSON_CRYSTAL)
            .save(output)

        // Warped Crystal -> Elder Pearl
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL, 3)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.WARPED_CRYSTAL)
            .save(output)

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val molten: HTFluidContent<*, *, *> = data.molten
            // molten <-> gem
            meltAndFreeze(
                itemCreator.fromItem(RagiumItems.getMold(CommonMaterialPrefixes.GEM)),
                CommonMaterialPrefixes.GEM.itemTagKey(data),
                molten,
                RagiumConst.MOLTEN_TO_GEM,
            )

            val log: TagKey<Item> = data.log ?: continue
            val sap: HTFluidContent<*, *, *> = data.sap ?: continue
            // log -> sap
            HTItemToObjRecipeBuilder
                .melting(itemCreator.fromTagKey(log), resultHelper.fluid(sap, RagiumConst.LOG_TO_SAP))
                .saveSuffixed(output, "_from_stems")
            // sap -> molten
            distillation(sap to 1000, null, resultHelper.fluid(molten, RagiumConst.SAP_TO_MOLTEN) to null)
        }

        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(RagiumModTags.Items.ELDRITCH_PEARL_BINDER))
            .addIngredient(fluidCreator.fromContent(RagiumFluidContents.CRIMSON_BLOOD, RagiumConst.MOLTEN_TO_GEM))
            .addIngredient(fluidCreator.fromContent(RagiumFluidContents.DEW_OF_THE_WARP, RagiumConst.MOLTEN_TO_GEM))
            .setResult(resultHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, RagiumConst.MOLTEN_TO_GEM))
            .save(output)
    }

    @JvmStatic
    private fun mutagen() {
        // Organic Mutagen
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.FOODS_FOOD_POISONING))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.ORGANIC_MUTAGEN, 1000))
            .save(output)

        // Poisonous Potato
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(Tags.Items.CROPS_POTATO),
                fluidCreator.fromContent(RagiumFluidContents.ORGANIC_MUTAGEN, 250),
            ).addResult(resultHelper.item(Items.POISONOUS_POTATO))
            .save(output)
        // Potato Sprouts
        HTItemToObjRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.POISONOUS_POTATO),
                resultHelper.item(RagiumItems.POTATO_SPROUTS),
            ).save(output)
        // Green Cake
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromItem(RagiumItems.POTATO_SPROUTS, 16),
                resultHelper.item(RagiumItems.GREEN_CAKE),
            ).save(output)

        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromItem(RagiumItems.GREEN_CAKE),
                resultHelper.item(RagiumItems.GREEN_CAKE_DUST, 8),
            ).save(output)
        // Green Pellet
        HTShapedRecipeBuilder
            .misc(RagiumItems.GREEN_PELLET, 8)
            .hollow8()
            .define('A', RagiumItems.GREEN_CAKE_DUST)
            .define('B', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL)
            .save(output)
    }
}
