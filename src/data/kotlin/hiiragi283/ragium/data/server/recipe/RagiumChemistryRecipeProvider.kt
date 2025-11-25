package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumChemistryRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Plastic Plate
        HTItemWithCatalystRecipeBuilder
            .compressing(
                itemCreator.fromTagKey(RagiumModTags.Items.POLYMER_RESIN),
                resultHelper.item(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.PLASTIC),
            ).save(output)
        // Synthetic Fiber / Leather
        mapOf(
            RagiumItems.SYNTHETIC_FIBER to Tags.Items.STRINGS,
            RagiumItems.SYNTHETIC_LEATHER to Tags.Items.LEATHERS,
        ).forEach { (result: ItemLike, parent: TagKey<Item>) ->
            HTShapelessRecipeBuilder
                .misc(result, 2)
                .addIngredient(RagiumModTags.Items.POLYMER_RESIN)
                .addIngredient(parent)
                .savePrefixed(output, "2x_")

            HTShapedRecipeBuilder
                .create(result, 9)
                .hollow8()
                .define('A', RagiumModTags.Items.POLYMER_RESIN)
                .define('B', parent)
                .savePrefixed(output, "9x_")
        }
        // Synthetic Book
        HTShapelessRecipeBuilder
            .misc(Items.BOOK)
            .addIngredients(Items.PAPER, count = 3)
            .addIngredient(RagiumItems.SYNTHETIC_LEATHER)
            .saveSuffixed(output, "_from_synthetic")

        // Blaze Rod
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Items.BLAZE_ROD),
                itemCreator.fromItem(Items.BLAZE_POWDER, 4),
                itemCreator.fromTagKey(Tags.Items.RODS_WOODEN),
            ).save(output)
        // Breeze Rod
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(Items.BREEZE_ROD),
                itemCreator.fromItem(Items.WIND_CHARGE, 6),
                itemCreator.fromTagKey(Tags.Items.RODS_WOODEN),
            ).save(output)

        // Magma Block <-> Lava
        meltAndFreeze(
            itemCreator.fromItem(HTMoldType.STORAGE_BLOCK),
            Items.MAGMA_BLOCK.toHolderLike(),
            HTFluidHolderLike.LAVA,
            125,
        )

        extracting()
        mixing()
        refining()
    }

    //    Extracting    //

    @JvmStatic
    private fun extracting() {
        // Vanilla
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(Tags.Items.GRAVELS),
                resultHelper.item(Items.FLINT),
            ).saveSuffixed(output, "_from_gravel")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_RED_BLOCKS),
                resultHelper.item(Items.REDSTONE),
            ).saveSuffixed(output, "_from_red_sandstone")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.BROWN_MUSHROOM_BLOCK),
                resultHelper.item(Items.BROWN_MUSHROOM, 3),
            ).saveSuffixed(output, "_from_block")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.RED_MUSHROOM_BLOCK),
                resultHelper.item(Items.RED_MUSHROOM, 3),
            ).saveSuffixed(output, "_from_block")
        // Ragium
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.REDSTONE),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.CINNABAR, 3),
            ).saveSuffixed(output, "_from_redstone")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.DRIED_KELP),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALT),
            ).saveSuffixed(output, "_from_kelp")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER),
            ).saveSuffixed(output, "_from_sandstone")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.WIND_CHARGE),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER),
            ).saveSuffixed(output, "_from_breeze")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(Tags.Items.GUNPOWDERS),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR),
            ).saveSuffixed(output, "_from_gunpowder")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.BLAZE_POWDER),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR),
            ).saveSuffixed(output, "_from_blaze")

        dyes()
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    private fun dyes() {
        // Charcoal -> Brown
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.CHARCOAL),
                resultHelper.item(Items.BROWN_DYE),
            ).saveSuffixed(output, "_from_charcoal")
        // Coal -> Black
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.COAL),
                resultHelper.item(Items.BLACK_DYE),
            ).saveSuffixed(output, "_from_coal")

        for (color: DyeColor in DyeColor.entries) {
            val name: String = color.serializedName
            val dye: DyeItem = DyeItem.byColor(color)

            HTItemWithCatalystRecipeBuilder
                .extracting(
                    itemCreator.fromTagKey(CommonMaterialPrefixes.RAW_MATERIAL.itemTagKey("dyes/$name")),
                    resultHelper.item(dye, 2),
                ).saveSuffixed(output, "_from_$name")
        }
    }

    //    Mixing    //

    @JvmStatic
    private fun mixing() {
        // Water + Lava -> Obsidian
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(fluidCreator.water(1000))
            .addIngredient(fluidCreator.lava(1000))
            .setResult(resultHelper.item(Items.OBSIDIAN))
            .save(output)

        acid()
        explosives()
    }

    @JvmStatic
    private fun acid() {
        // Sulfur + Water -> Sulfuric Acid
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.SULFURIC_ACID, 1000))
            .save(output)
        // Saltpeter + Sulfuric Acid -> Nitric Acid
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.SULFURIC_ACID, 1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.NITRIC_ACID, 1000))
            .save(output)
        // Sulfuric Acid + Nitric Acid -> Mixture Acid
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.SULFURIC_ACID, 500))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.NITRIC_ACID, 500))
            .setResult(resultHelper.fluid(RagiumFluidContents.MIXTURE_ACID, 1000))
            .save(output)
    }

    @JvmStatic
    private fun explosives() {
        // Slime
        meltAndFreeze(
            itemCreator.fromItem(HTMoldType.GEM),
            Items.SLIME_BALL.toHolderLike(),
            RagiumFluidContents.SLIME,
            250,
        )

        meltAndFreeze(
            itemCreator.fromItem(HTMoldType.STORAGE_BLOCK),
            Items.SLIME_BLOCK.toHolderLike(),
            RagiumFluidContents.SLIME,
            250 * 9,
        )
        // Glycerol
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.SUGAR, 2))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.SLIME, 1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.GLYCEROL, 1000))
            .save(output)
    }

    //    Refining    //

    @JvmStatic
    private fun refining() {
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
                fluidCreator.fromHolder(RagiumFluidContents.CRUDE_OIL, 125),
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
        HTComplexRecipeBuilder
            .solidifying(
                itemCreator.fromItem(RagiumItems.POLYMER_CATALYST),
                fluidCreator.fromHolder(RagiumFluidContents.NATURAL_GAS, 125),
                resultHelper.item(RagiumModTags.Items.POLYMER_RESIN, 4),
            ).saveSuffixed(output, "_from_lpg")

        // Naphtha -> Fuel + Sulfur
        distillation(
            RagiumFluidContents.NAPHTHA to 1000,
            resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR),
            resultHelper.fluid(RagiumFluidContents.FUEL, 375) to null,
        )
        // Naphtha + Redstone -> Lubricant
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.NAPHTHA, 1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.LUBRICANT, 1000))
            .save(output)
        // Fuel + Mixture Acid -> Crimson Fuel
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.FUEL, 1000))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.MIXTURE_ACID, 250))
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
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(ItemTags.LOGS_THAT_BURN),
                null,
                null,
                resultHelper.fluid(RagiumFluidContents.SAP, 125),
            ).saveSuffixed(output, "_from_log")
        // Sap -> Resin
        distillation(
            RagiumFluidContents.SAP to 1000,
            resultHelper.item(RagiumItems.RESIN),
            resultHelper.fluid(RagiumFluidContents.NATURAL_GAS, 125) to null,
        )

        // Crimson Crystal
        extractFromData(RagiumMaterialRecipeData.CRIMSON_SAP)
        refiningFromData(RagiumMaterialRecipeData.CRIMSON_BLOOD)
        meltAndFreeze(RagiumMaterialRecipeData.CRIMSON_CRYSTAL)

        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER, 3)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.CRIMSON_CRYSTAL)
            .save(output)
        // Warped Crystal
        extractFromData(RagiumMaterialRecipeData.WARPED_SAP)
        refiningFromData(RagiumMaterialRecipeData.DEW_OF_THE_WARP)
        meltAndFreeze(RagiumMaterialRecipeData.WARPED_CRYSTAL)

        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL, 3)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.WARPED_CRYSTAL)
            .save(output)

        // Eldritch Pearl
        mixFromData(RagiumMaterialRecipeData.ELDRITCH_FLUX)

        meltAndFreeze(RagiumMaterialRecipeData.ELDRITCH_PEARL)
    }

    @JvmStatic
    private fun mutagen() {
        // Organic Mutagen
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.FOODS_FOOD_POISONING))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.ORGANIC_MUTAGEN, 1000))
            .save(output)

        // Poisonous Potato
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(Tags.Items.CROPS_POTATO),
                fluidCreator.fromHolder(RagiumFluidContents.ORGANIC_MUTAGEN, 250),
            ).addResult(resultHelper.item(Items.POISONOUS_POTATO))
            .save(output)
        // Potato Sprouts
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.POISONOUS_POTATO),
                resultHelper.item(RagiumItems.POTATO_SPROUTS),
            ).save(output)
        // Green Cake
        HTItemWithCatalystRecipeBuilder
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
            .create(RagiumItems.GREEN_PELLET, 8)
            .hollow8()
            .define('A', RagiumItems.GREEN_CAKE_DUST)
            .define('B', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL)
            .save(output)
    }

    //    Extensions    //

    @JvmStatic
    private fun extractFromData(data: HTRecipeData) {
        HTItemWithCatalystRecipeBuilder
            .extracting(
                data.getItemIngredients(itemCreator)[0],
                data.getItemResults().getOrNull(0)?.first,
                null,
                data.getFluidResults().getOrNull(0),
            ).saveModified(output, data.operator)
    }

    @JvmStatic
    private fun refiningFromData(data: HTRecipeData) {
        HTComplexRecipeBuilder
            .refining(
                data.getFluidIngredients(fluidCreator)[0],
                data.getFluidResults()[0],
                null,
                data.getItemResults().getOrNull(0)?.first,
            ).saveModified(output, data.operator)
    }
}
