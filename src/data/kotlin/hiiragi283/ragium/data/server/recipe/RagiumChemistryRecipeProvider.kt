package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.registry.HTBasicFluidContent
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.data.recipe.HTAlloyingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTCompressingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTExtractingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTFluidWithCatalystRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleExtraItemRecipeBuilder
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.HTColoredVariant
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumChemistryRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        extracting()
        mixing()
        refining()
    }

    //    Extracting    //

    @JvmStatic
    private fun extracting() {
        // Vanilla
        HTExtractingRecipeBuilder
            .create(itemCreator.fromTagKey(Tags.Items.GRAVELS))
            .setResult(resultHelper.item(Items.FLINT))
            .saveSuffixed(output, "_from_gravel")

        HTExtractingRecipeBuilder
            .create(itemCreator.fromTagKey(Tags.Items.SANDSTONE_RED_BLOCKS))
            .setResult(resultHelper.item(Items.REDSTONE))
            .saveSuffixed(output, "_from_red_sandstone")

        HTExtractingRecipeBuilder
            .create(itemCreator.fromItem(Items.BROWN_MUSHROOM_BLOCK))
            .setResult(resultHelper.item(Items.BROWN_MUSHROOM, 3))
            .saveSuffixed(output, "_from_block")

        HTExtractingRecipeBuilder
            .create(itemCreator.fromItem(Items.RED_MUSHROOM_BLOCK))
            .setResult(resultHelper.item(Items.RED_MUSHROOM, 3))
            .saveSuffixed(output, "_from_block")
        // Ragium
        HTExtractingRecipeBuilder
            .create(itemCreator.fromItem(Items.MAGMA_CREAM, 3))
            .setResult(resultHelper.item(RagiumItems.MAGMA_SHARD))
            .save(output)

        HTExtractingRecipeBuilder
            .create(itemCreator.fromItem(Items.DRIED_KELP))
            .setResult(resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALT))
            .tagCondition(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALT)
            .saveSuffixed(output, "_from_kelp")

        HTExtractingRecipeBuilder
            .create(itemCreator.fromTagKey(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS))
            .setResult(resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER))
            .tagCondition(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER)
            .saveSuffixed(output, "_from_sandstone")

        HTExtractingRecipeBuilder
            .create(itemCreator.fromItem(Items.WIND_CHARGE))
            .setResult(resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER))
            .tagCondition(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER)
            .saveSuffixed(output, "_from_breeze")

        HTExtractingRecipeBuilder
            .create(itemCreator.fromTagKey(Tags.Items.GUNPOWDERS))
            .setResult(resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR))
            .tagCondition(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR)
            .saveSuffixed(output, "_from_gunpowder")

        HTExtractingRecipeBuilder
            .create(itemCreator.fromItem(Items.BLAZE_POWDER))
            .setResult(resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR))
            .tagCondition(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR)
            .saveSuffixed(output, "_from_blaze")

        dyes()
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    private fun dyes() {
        // Charcoal -> Brown
        HTExtractingRecipeBuilder
            .create(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.CHARCOAL))
            .setResult(resultHelper.item(Items.BROWN_DYE))
            .saveSuffixed(output, "_from_charcoal")
        // Coal -> Black
        HTExtractingRecipeBuilder
            .create(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.COAL))
            .setResult(resultHelper.item(Items.BLACK_DYE))
            .saveSuffixed(output, "_from_coal")

        for (material: HTColorMaterial in HTColorMaterial.entries) {
            HTExtractingRecipeBuilder
                .create(itemCreator.fromTagKey(CommonMaterialPrefixes.RAW_MATERIAL_DYE, material))
                .setResult(resultHelper.item(DyeItem.byColor(material.dyeColor), 2))
                .saveSuffixed(output, "_from_${material.asMaterialName()}")
        }
    }

    //    Mixing    //

    @JvmStatic
    private fun mixing() {
        // Water + Lava -> Obsidian
        HTMixingRecipeBuilder
            .create()
            .addIngredient(fluidCreator.water(1000))
            .addIngredient(fluidCreator.lava(1000))
            .setResult(resultHelper.item(Items.OBSIDIAN))
            .save(output)
        // Magma Block <-> Lava
        meltAndFreeze(
            HTMoldType.STORAGE_BLOCK,
            Items.MAGMA_BLOCK.toHolderLike(),
            HTFluidHolderLike.LAVA,
            125,
        )

        water()
        exp()

        crimson()
        warped()
        eldritch()

        explosives()
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    private fun water() {
        // Water Bottle
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.GLASS_BOTTLE))
            .addIngredient(fluidCreator.water(250))
            .setResult(resultHelper.item(HTPotionHelper.createPotion(Items.POTION, Potions.WATER)))
            .save(output, RagiumAPI.id("water_bottle"))
        // Dirt -> Mud
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.DIRT, 8))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.item(Items.MUD, 8))
            .saveSuffixed(output, "_from_dirt")
        // Silt -> Clay
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(RagiumBlocks.SILT, 8))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.item(Items.CLAY, 8))
            .saveSuffixed(output, "_from_silt")

        // Ice <-> Water
        meltAndFreeze(
            HTMoldType.STORAGE_BLOCK,
            Items.ICE.toHolderLike(),
            HTFluidHolderLike.WATER,
            1000,
        )
        // Water + Wind Charge + Packed Ice -> Coolant
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.WIND_CHARGE, 3))
            .addIngredient(itemCreator.fromItem(Items.PACKED_ICE))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.COOLANT, 1000))
            .save(output)

        // Concretes
        for (color: HTColorMaterial in HTColorMaterial.entries) {
            HTMixingRecipeBuilder
                .create()
                .addIngredient(itemCreator.fromItem(HTColorMaterial.getColoredItem(HTColoredVariant.CONCRETE_POWDER, color), 8))
                .addIngredient(fluidCreator.water(1000))
                .setResult(resultHelper.item(HTColorMaterial.getColoredItem(HTColoredVariant.CONCRETE, color), 8))
                .saveSuffixed(output, "_from_powder")
        }
    }

    @JvmStatic
    private fun exp() {
        // Exp Bottle
        extractAndInfuse(Items.GLASS_BOTTLE, Items.EXPERIENCE_BOTTLE.toHolderLike(), RagiumFluidContents.EXPERIENCE)
        // Exp Berries -> Liquid Exp
        HTExtractingRecipeBuilder
            .create(itemCreator.fromItem(RagiumBlocks.EXP_BERRIES))
            .setResult(resultHelper.fluid(RagiumFluidContents.EXPERIENCE, 50))
            .saveSuffixed(output, "_from_berries")

        // Golden Apple
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.GOLDEN_APPLE))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 8000))
            .setResult(resultHelper.item(Items.ENCHANTED_GOLDEN_APPLE))
            .save(output)
        // Exp Berries
        HTAlloyingRecipeBuilder
            .create(
                resultHelper.item(RagiumBlocks.EXP_BERRIES),
                itemCreator.fromTagKey(Tags.Items.FOODS_BERRY),
                itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL, 4),
            ).save(output)
        // Blaze Powder
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR, 2))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 250))
            .setResult(resultHelper.item(Items.BLAZE_POWDER))
            .save(output)
        // Wind Charge
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.SNOWBALL, 2))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 250))
            .setResult(resultHelper.item(Items.WIND_CHARGE))
            .save(output)
        // Ghast Tear
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.CHISELED_QUARTZ_BLOCK))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 1000))
            .setResult(resultHelper.item(Items.GHAST_TEAR))
            .save(output)
        // Phantom Membrane
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.LEATHERS))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 250))
            .setResult(resultHelper.item(Items.PHANTOM_MEMBRANE))
            .save(output)
    }

    @JvmStatic
    private fun crimson() {
        // Crimson Nylium
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.NETHERRACKS))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 250))
            .setResult(resultHelper.item(Items.CRIMSON_NYLIUM))
            .save(output)
        // Crimson Fungus
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.RED_MUSHROOM))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 250))
            .setResult(resultHelper.item(Items.CRIMSON_FUNGUS))
            .save(output)
        // Nether Wart
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.CROPS_BEETROOT))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 250))
            .setResult(resultHelper.item(Items.NETHER_WART))
            .save(output)
        // Crimson Soil
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.SOUL_SOIL))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 2000))
            .setResult(resultHelper.item(RagiumBlocks.CRIMSON_SOIL))
            .save(output)
    }

    @JvmStatic
    private fun warped() {
        // Warped Nylium
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.NETHERRACKS))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.DEW_OF_THE_WARP, 250))
            .setResult(resultHelper.item(Items.WARPED_NYLIUM))
            .save(output)
        // Warped Fungus
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.RED_MUSHROOM))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.DEW_OF_THE_WARP, 250))
            .setResult(resultHelper.item(Items.WARPED_FUNGUS))
            .save(output)
        // Warped Wart
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.CROPS_BEETROOT))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.DEW_OF_THE_WARP, 1000))
            .setResult(resultHelper.item(RagiumBlocks.WARPED_WART))
            .save(output)
    }

    @JvmStatic
    private fun eldritch() {
        // Budding Amethyst
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000))
            .setResult(resultHelper.item(Items.BUDDING_AMETHYST))
            .save(output)
        // Budding Quartz
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000))
            .setResult(resultHelper.item(RagiumBlocks.BUDDING_QUARTZ))
            .save(output)

        // Ominous Bottle
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.GLASS_BOTTLE))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 1000))
            .setResult(resultHelper.item(Items.OMINOUS_BOTTLE))
            .save(output)
        // Ominous Trial Key
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.TRIAL_KEY))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000))
            .setResult(resultHelper.item(Items.OMINOUS_TRIAL_KEY))
            .save(output)
        // Crying Obsidian
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.OBSIDIANS_NORMAL))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000))
            .setResult(resultHelper.item(Items.CRYING_OBSIDIAN))
            .save(output)
    }

    @JvmStatic
    private fun explosives() {
        // Slime
        meltAndFreeze(
            HTMoldType.GEM,
            Items.SLIME_BALL.toHolderLike(),
            RagiumFluidContents.SLIME,
            250,
        )

        meltAndFreeze(
            HTMoldType.STORAGE_BLOCK,
            Items.SLIME_BLOCK.toHolderLike(),
            RagiumFluidContents.SLIME,
            250 * 9,
        )
        // Gunpowder + Slime -> Gelled Explosive
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.GUNPOWDERS))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.SLIME, 250))
            .setResult(resultHelper.fluid(RagiumFluidContents.GELLED_EXPLOSIVE, 250))
            .save(output)
        // Gelled Explosive -> TNT
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.SANDS, 4))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.GELLED_EXPLOSIVE, 1000))
            .setResult(resultHelper.item(Items.TNT, 8))
            .save(output)
    }

    //    Refining    //

    @JvmStatic
    private fun refining() {
        cokes()
        crudeOil()
        plastic()

        sap()
        biomass()
    }

    @JvmStatic
    private fun cokes() {
        // Log -> Charcoal + Creosote
        HTExtractingRecipeBuilder
            .create(itemCreator.fromTagKey(ItemTags.LOGS_THAT_BURN))
            .setResult(resultHelper.item(CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL))
            .setResult(resultHelper.fluid(RagiumFluidContents.CREOSOTE, 125))
            .saveSuffixed(output, "_from_logs")
        // Compressed Sawdust -> Charcoal + Creosote
        HTCompressingRecipeBuilder
            .block(
                itemCreator.fromItem(RagiumItems.COMPRESSED_SAWDUST),
                resultHelper.item(CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL),
            ).saveSuffixed(output, "_from_compressed")
        // Sugar -> Charcoal
        HTExtractingRecipeBuilder
            .create(itemCreator.fromItem(Items.SUGAR, 12))
            .setResult(resultHelper.item(CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL))
            .saveSuffixed(output, "_from_sugar")

        // Coal -> Coal Coke + Creosote
        HTExtractingRecipeBuilder
            .create(itemCreator.fromTagKey(CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.COAL))
            .setResult(resultHelper.item(CommonMaterialPrefixes.FUEL, CommonMaterialKeys.COAL_COKE))
            .setResult(resultHelper.fluid(RagiumFluidContents.CREOSOTE, 250))
            .saveSuffixed(output, "_from_coal")
        // Creosote -> Tar + Lubricant
        HTFluidRecipeBuilder
            .refining(
                fluidCreator.fromHolder(RagiumFluidContents.CREOSOTE, 1000),
                resultHelper.fluid(RagiumFluidContents.LUBRICANT, 250),
                resultHelper.item(RagiumItems.TAR, 3),
            ).saveSuffixed(output, "_from_creosote")
    }

    @JvmStatic
    private fun crudeOil() {
        // Coal -> Crude Oil
        HTFluidRecipeBuilder
            .melting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.COAL),
                resultHelper.fluid(RagiumFluidContents.CRUDE_OIL, 125),
            ).saveSuffixed(output, "_from_coal")
        // Soul XX -> Crude Oil
        HTFluidRecipeBuilder
            .melting(
                itemCreator.fromTagKey(ItemTags.SOUL_FIRE_BASE_BLOCKS),
                resultHelper.fluid(RagiumFluidContents.CRUDE_OIL, 500),
            ).saveSuffixed(output, "_from_soul")

        // Crude Oil -> Naphtha + Tar
        HTFluidRecipeBuilder
            .refining(
                fluidCreator.fromHolder(RagiumFluidContents.CRUDE_OIL, 1000),
                resultHelper.fluid(RagiumFluidContents.NAPHTHA, 750),
                resultHelper.item(RagiumItems.TAR),
            ).saveSuffixed(output, "_from_crude_oil")
        // Naphtha + Catalyst -> 4x Polymer Resin
        HTFluidWithCatalystRecipeBuilder
            .solidifying(
                fluidCreator.fromHolder(RagiumFluidContents.NAPHTHA, 1000),
                itemCreator.fromItem(RagiumItems.POLYMER_CATALYST),
                resultHelper.item(RagiumModTags.Items.POLYMER_RESIN, 4),
            ).saveSuffixed(output, "_from_naphtha")
        // Naphtha -> Fuel + Sulfur
        HTFluidRecipeBuilder
            .refining(
                fluidCreator.fromHolder(RagiumFluidContents.NAPHTHA, 1000),
                resultHelper.fluid(RagiumFluidContents.FUEL, 750),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR),
            ).saveSuffixed(output, "_from_naphtha")
        // Naphtha + Raginite -> Lubricant
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE))
            .addIngredient(fluidCreator.fromHolders(RagiumFluidContents.CREOSOTE, RagiumFluidContents.NAPHTHA, amount = 1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.LUBRICANT, 1000))
            .save(output)
    }

    @JvmStatic
    private fun plastic() {
        // Slime + Salt -> Polymer Resin
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALT))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.SLIME, 125))
            .setResult(resultHelper.item(RagiumModTags.Items.POLYMER_RESIN))
            .saveSuffixed(output, "_from_slime")
        // Crude Oil + Clay -> Polymer Resin
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.CLAY_BALL))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRUDE_OIL, 125))
            .setResult(resultHelper.item(RagiumModTags.Items.POLYMER_RESIN))
            .saveSuffixed(output, "_from_crude_oil")

        // Plastic Plate
        HTCompressingRecipeBuilder
            .plate(
                itemCreator.fromTagKey(RagiumModTags.Items.POLYMER_RESIN),
                resultHelper.item(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.PLASTIC, 2),
            ).saveSuffixed(output, "_from_resin")
        // Synthetic Fiber / Leather
        mapOf(
            RagiumItems.SYNTHETIC_FIBER to Tags.Items.STRINGS,
            RagiumItems.SYNTHETIC_LEATHER to Tags.Items.LEATHERS,
        ).forEach { (result: ItemLike, parent: TagKey<Item>) ->
            HTShapelessRecipeBuilder
                .create(result, 2)
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
            .create(Items.BOOK)
            .addIngredients(Items.PAPER, count = 3)
            .addIngredient(RagiumItems.SYNTHETIC_LEATHER)
            .saveSuffixed(output, "_from_synthetic")
    }

    @JvmStatic
    private fun sap() {
        // Spruce Log -> Spruce Resin
        HTExtractingRecipeBuilder
            .create(itemCreator.fromTagKey(ItemTags.SPRUCE_LOGS))
            .setResult(resultHelper.item(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, 4))
            .setResult(resultHelper.fluid(RagiumFluidContents.SPRUCE_RESIN, 250))
            .saveSuffixed(output, "_from_log")
        // Resin -> Rosin
        HTFluidRecipeBuilder
            .refining(
                fluidCreator.fromHolder(RagiumFluidContents.SPRUCE_RESIN, 1000),
                resultHelper.fluid(HTFluidHolderLike.WATER, 500),
                resultHelper.item(RagiumItems.ROSIN),
            ).saveSuffixed(output, "_from_spruce_resin")

        rubber()

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val base: TagKey<Item>? = data.base
            val sap: HTBasicFluidContent? = data.sap
            // Base -> Sap
            if (base != null && sap != null) {
                HTExtractingRecipeBuilder
                    .create(itemCreator.fromTagKey(base))
                    .setResult(resultHelper.item(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, 4))
                    .setResult(resultHelper.fluid(sap, RagiumConst.LOG_TO_SAP))
                    .save(output)
            }
            // Sap -> Molten
            val molten: HTBasicFluidContent = data.molten
            if (sap != null) {
                HTFluidRecipeBuilder
                    .refining(
                        fluidCreator.fromHolder(sap, 1000),
                        resultHelper.fluid(molten, RagiumConst.SAP_TO_MOLTEN),
                    ).saveSuffixed(output, "_from_${sap.getPath()}")
            }
            // Molten -> Gem
            HTFluidWithCatalystRecipeBuilder
                .solidifying(
                    fluidCreator.fromHolder(molten, 1000),
                    itemCreator.fromItem(HTMoldType.GEM),
                    resultHelper.item(CommonMaterialPrefixes.GEM, data),
                ).save(output)

            HTFluidRecipeBuilder
                .melting(
                    itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, data),
                    resultHelper.fluid(molten, 1000),
                ).saveSuffixed(output, "_from_gem")
        }

        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER, 3)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.CRIMSON_CRYSTAL)
            .setExp(0.5f)
            .save(output)
        // Warped Crystal -> Ender Pearl
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL, 3)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.WARPED_CRYSTAL)
            .setExp(0.5f)
            .save(output)

        // Eldritch Pearl
        mixFromData(RagiumMaterialRecipeData.ELDRITCH_FLUX)
    }

    @JvmStatic
    private fun rubber() {
        // Dandelion -> Latex
        HTShapedRecipeBuilder
            .create(RagiumFluidContents.LATEX.bucket)
            .hollow8()
            .define('A', Items.DANDELION)
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_with_dandelion")
        // Acacia / Jungle Log -> Latex
        HTExtractingRecipeBuilder
            .create(itemCreator.fromTagKeys(listOf(ItemTags.ACACIA_LOGS, ItemTags.JUNGLE_LOGS)))
            .setResult(resultHelper.item(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, 4))
            .setResult(resultHelper.fluid(RagiumFluidContents.LATEX, 250))
            .saveSuffixed(output, "_from_log")
        // Latex -> Raw Rubber
        HTCookingRecipeBuilder
            .smelting(RagiumItems.getRaw(CommonMaterialKeys.RUBBER))
            .addIngredient(RagiumFluidContents.LATEX.bucketTag)
            .setExp(0.1f)
            .saveSuffixed(output, "_from_latex")

        meltAndFreeze(RagiumMaterialRecipeData.RAW_RUBBER)
        // Raw Rubber + Sulfur -> Rubber
        alloyFromData(RagiumMaterialRecipeData.RUBBER_SHEET)
        // Latex + Sulfur + Coal -> Rubber
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.COAL))
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.LATEX, 1000))
            .setResult(resultHelper.item(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.RUBBER, 4))
            .save(output)
    }

    @JvmStatic
    private fun biomass() {
        // Biomass -> Crude Bio
        HTFluidRecipeBuilder
            .melting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.FUEL, CommonMaterialKeys.BIO),
                resultHelper.fluid(RagiumFluidContents.CRUDE_BIO, 125),
            ).save(output)

        // Crude Bio -> Bio Fertilizer + Bio Fuel
        HTFluidRecipeBuilder
            .refining(
                fluidCreator.fromHolder(RagiumFluidContents.CRUDE_BIO, 1000),
                resultHelper.fluid(RagiumFluidContents.BIOFUEL, 750),
                resultHelper.item(RagiumItems.BIO_FERTILIZER),
            ).saveSuffixed(output, "_from_crude_bio")

        // Poisonous Potato
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.CROPS_POTATO))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRUDE_BIO, 250))
            .setResult(resultHelper.item(Items.POISONOUS_POTATO))
            .save(output)
        // Potato Sprouts
        HTExtractingRecipeBuilder
            .create(itemCreator.fromItem(Items.POISONOUS_POTATO))
            .setResult(resultHelper.item(RagiumItems.POTATO_SPROUTS))
            .save(output)
        // Green Cake
        HTCompressingRecipeBuilder
            .block(
                itemCreator.fromItem(RagiumItems.POTATO_SPROUTS, 16),
                resultHelper.item(RagiumItems.GREEN_CAKE),
            ).save(output)

        HTSingleExtraItemRecipeBuilder
            .crushing(
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
}
