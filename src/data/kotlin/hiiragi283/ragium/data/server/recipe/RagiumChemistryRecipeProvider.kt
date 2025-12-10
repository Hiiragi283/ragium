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
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.recipe.machine.HTBioExtractingRecipe
import hiiragi283.ragium.common.variant.HTColoredVariant
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessInputsRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSingleExtraItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSingleRecipeBuilder
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
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DifferenceIngredient

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
                itemCreator.fromItem(Items.MAGMA_CREAM, 3),
                resultHelper.item(RagiumItems.MAGMA_SHARD),
            ).save(output)

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.DRIED_KELP),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALT),
            ).tagCondition(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALT)
            .saveSuffixed(output, "_from_kelp")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER),
            ).tagCondition(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER)
            .saveSuffixed(output, "_from_sandstone")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.WIND_CHARGE),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER),
            ).tagCondition(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER)
            .saveSuffixed(output, "_from_breeze")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(Tags.Items.GUNPOWDERS),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR),
            ).tagCondition(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR)
            .saveSuffixed(output, "_from_gunpowder")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.BLAZE_POWDER),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR),
            ).tagCondition(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR)
            .saveSuffixed(output, "_from_blaze")

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

        for (material: HTColorMaterial in HTColorMaterial.entries) {
            HTItemWithCatalystRecipeBuilder
                .extracting(
                    itemCreator.fromTagKey(CommonMaterialPrefixes.RAW_MATERIAL_DYE, material),
                    resultHelper.item(DyeItem.byColor(material.dyeColor), 2),
                ).saveSuffixed(output, "_from_${material.asMaterialName()}")
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
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.GLASS_BOTTLE))
            .addIngredient(fluidCreator.water(250))
            .setResult(resultHelper.item(HTPotionHelper.createPotion(Items.POTION, Potions.WATER)))
            .save(output, RagiumAPI.id("water_bottle"))
        // Dirt -> Mud
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.DIRT, 8))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.item(Items.MUD, 8))
            .saveSuffixed(output, "_from_dirt")
        // Silt -> Clay
        HTComplexRecipeBuilder
            .mixing()
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
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.WIND_CHARGE, 3))
            .addIngredient(itemCreator.fromItem(Items.PACKED_ICE))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.COOLANT, 1000))
            .save(output)

        // Concretes
        for (color: HTColorMaterial in HTColorMaterial.entries) {
            HTComplexRecipeBuilder
                .mixing()
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
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(RagiumBlocks.EXP_BERRIES),
                null,
                null,
                resultHelper.fluid(RagiumFluidContents.EXPERIENCE, 50),
            ).saveSuffixed(output, "_from_berries")

        // Golden Apple
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.GOLDEN_APPLE))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 8000))
            .setResult(resultHelper.item(Items.ENCHANTED_GOLDEN_APPLE))
            .save(output)
        // Exp Berries
        HTShapelessInputsRecipeBuilder
            .alloying(
                resultHelper.item(RagiumBlocks.EXP_BERRIES),
                itemCreator.fromTagKey(Tags.Items.FOODS_BERRY),
                itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL, 4),
            ).save(output)
        // Blaze Powder
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR, 2))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 250))
            .setResult(resultHelper.item(Items.BLAZE_POWDER))
            .save(output)
        // Wind Charge
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.SNOWBALL, 2))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 250))
            .setResult(resultHelper.item(Items.WIND_CHARGE))
            .save(output)
        // Ghast Tear
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.CHISELED_QUARTZ_BLOCK))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 1000))
            .setResult(resultHelper.item(Items.GHAST_TEAR))
            .save(output)
        // Phantom Membrane
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.LEATHERS))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 250))
            .setResult(resultHelper.item(Items.PHANTOM_MEMBRANE))
            .save(output)
    }

    @JvmStatic
    private fun crimson() {
        // Crimson Nylium
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.NETHERRACKS))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 250))
            .setResult(resultHelper.item(Items.CRIMSON_NYLIUM))
            .save(output)
        // Crimson Fungus
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.RED_MUSHROOM))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 250))
            .setResult(resultHelper.item(Items.CRIMSON_FUNGUS))
            .save(output)
        // Nether Wart
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.CROPS_BEETROOT))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 250))
            .setResult(resultHelper.item(Items.NETHER_WART))
            .save(output)
        // Crimson Soil
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.SOUL_SOIL))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 2000))
            .setResult(resultHelper.item(RagiumBlocks.CRIMSON_SOIL))
            .save(output)
    }

    @JvmStatic
    private fun warped() {
        // Warped Nylium
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.NETHERRACKS))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.DEW_OF_THE_WARP, 250))
            .setResult(resultHelper.item(Items.WARPED_NYLIUM))
            .save(output)
        // Warped Fungus
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.RED_MUSHROOM))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.DEW_OF_THE_WARP, 250))
            .setResult(resultHelper.item(Items.WARPED_FUNGUS))
            .save(output)
        // Warped Wart
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.CROPS_BEETROOT))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.DEW_OF_THE_WARP, 1000))
            .setResult(resultHelper.item(RagiumBlocks.WARPED_WART))
            .save(output)
    }

    @JvmStatic
    private fun eldritch() {
        // Budding Amethyst
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000))
            .setResult(resultHelper.item(Items.BUDDING_AMETHYST))
            .save(output)
        // Budding Quartz
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000))
            .setResult(resultHelper.item(RagiumBlocks.BUDDING_QUARTZ))
            .save(output)

        // Ominous Bottle
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.GLASS_BOTTLE))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 1000))
            .setResult(resultHelper.item(Items.OMINOUS_BOTTLE))
            .save(output)
        // Ominous Trial Key
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.TRIAL_KEY))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000))
            .setResult(resultHelper.item(Items.OMINOUS_TRIAL_KEY))
            .save(output)
        // Crying Obsidian
        HTComplexRecipeBuilder
            .mixing()
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
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.GUNPOWDERS))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.SLIME, 250))
            .setResult(resultHelper.fluid(RagiumFluidContents.GELLED_EXPLOSIVE, 250))
            .save(output)
        // Gelled Explosive -> TNT
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.SANDS, 4))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.GELLED_EXPLOSIVE, 1000))
            .setResult(resultHelper.item(Items.TNT, 8))
            .save(output)
    }

    //    Refining    //

    @JvmStatic
    private fun refining() {
        crudeOil()
        plastic()

        sap()
        biomass()
    }

    @JvmStatic
    private fun crudeOil() {
        // Coal -> Crude Oil
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.COAL),
                resultHelper.fluid(RagiumFluidContents.CRUDE_OIL, 125),
            ).saveSuffixed(output, "_from_coal")
        // Soul XX -> Crude Oil
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKey(ItemTags.SOUL_FIRE_BASE_BLOCKS),
                resultHelper.fluid(RagiumFluidContents.CRUDE_OIL, 500),
            ).saveSuffixed(output, "_from_soul")

        // Crude Oil -> Natural Gas + Naphtha + Tar
        distillation(
            RagiumFluidContents.CRUDE_OIL to 1000,
            resultHelper.item(RagiumItems.TAR),
            resultHelper.fluid(RagiumFluidContents.NAPHTHA, 375) to null,
            resultHelper.fluid(RagiumFluidContents.NATURAL_GAS, 375) to itemCreator.fromTagKey(RagiumModTags.Items.PLASTICS),
        )

        // Naphtha -> Fuel + Sulfur
        distillation(
            RagiumFluidContents.NAPHTHA to 1000,
            resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR),
            resultHelper.fluid(RagiumFluidContents.FUEL, 375) to null,
        )
        // Naphtha + Raginite -> Lubricant
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.NAPHTHA, 1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.LUBRICANT, 1000))
            .save(output)
    }

    @JvmStatic
    private fun plastic() {
        // Slime + Salt -> Polymer Resin
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALT))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.SLIME, 125))
            .setResult(resultHelper.item(RagiumModTags.Items.POLYMER_RESIN))
            .saveSuffixed(output, "_from_slime")
        // Crude Oil + Clay -> Polymer Resin
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.CLAY_BALL))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRUDE_OIL, 125))
            .setResult(resultHelper.item(RagiumModTags.Items.POLYMER_RESIN))
            .saveSuffixed(output, "_from_crude_oil")
        // Natural Gas + Catalyst -> 4x Polymer Resin
        HTComplexRecipeBuilder
            .solidifying()
            .addIngredient(itemCreator.fromItem(RagiumItems.POLYMER_CATALYST))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.NATURAL_GAS, 125))
            .setResult(resultHelper.item(RagiumModTags.Items.POLYMER_RESIN, 4))
            .saveSuffixed(output, "_from_lpg")

        // Plastic Plate
        HTItemWithCatalystRecipeBuilder
            .compressing(
                itemCreator.fromTagKey(RagiumModTags.Items.POLYMER_RESIN),
                resultHelper.item(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.PLASTIC),
                itemCreator.fromItem(HTMoldType.PLATE),
            ).saveSuffixed(output, "_with_mold")
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
        // XX Log (excluded Spruce) -> Wood Dust + Sap
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.from(
                    DifferenceIngredient.of(Ingredient.of(ItemTags.LOGS_THAT_BURN), Ingredient.of(ItemTags.SPRUCE_LOGS)),
                ),
                resultHelper.item(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, 4),
                null,
                resultHelper.fluid(RagiumFluidContents.SAP, 125),
            ).saveSuffixed(output, "_from_log")
        // Sap -> Natural Gas
        distillation(
            RagiumFluidContents.SAP to 1000,
            null,
            resultHelper.fluid(RagiumFluidContents.NATURAL_GAS, 250) to null,
        )

        // Spruce Log -> Spruce Resin
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(ItemTags.SPRUCE_LOGS),
                resultHelper.item(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, 4),
                null,
                resultHelper.fluid(RagiumFluidContents.SPRUCE_RESIN, 250),
            ).saveSuffixed(output, "_from_log")
        // Resin -> Rosin
        distillation(
            RagiumFluidContents.SPRUCE_RESIN to 1000,
            resultHelper.item(RagiumItems.ROSIN),
            resultHelper.fluid(RagiumFluidContents.NATURAL_GAS, 250) to null,
        )

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val base: TagKey<Item>? = data.base
            val sap: HTBasicFluidContent? = data.sap
            // Base -> Sap
            if (base != null && sap != null) {
                HTItemWithCatalystRecipeBuilder
                    .extracting(
                        itemCreator.fromTagKey(base),
                        resultHelper.item(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, 4),
                        null,
                        resultHelper.fluid(sap, RagiumConst.LOG_TO_SAP),
                    ).save(output)
            }
            // Sap -> Molten
            val molten: HTBasicFluidContent = data.molten
            if (sap != null) {
                distillation(
                    sap to 1000,
                    null,
                    resultHelper.fluid(molten, RagiumConst.SAP_TO_MOLTEN) to null,
                )
            }
            // Molten -> Gem
            HTComplexRecipeBuilder
                .solidifying()
                .addIngredient(itemCreator.fromItem(HTMoldType.GEM))
                .addIngredient(fluidCreator.fromHolder(molten, 1000))
                .setResult(resultHelper.item(CommonMaterialPrefixes.GEM, data))
                .save(output)

            HTSingleRecipeBuilder
                .melting(
                    itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, data),
                    resultHelper.fluid(molten, 1000),
                ).saveSuffixed(output, "_from_gem")
        }

        // Crimson Crystal
        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER, 3)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.CRIMSON_CRYSTAL)
            .save(output)
        // Warped Crystal
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL, 3)
            .addIngredient(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.WARPED_CRYSTAL)
            .save(output)

        // Eldritch Pearl
        mixFromData(RagiumMaterialRecipeData.ELDRITCH_FLUX)
    }

    @JvmStatic
    private fun biomass() {
        save(HTBioExtractingRecipe.RECIPE_ID, HTBioExtractingRecipe)
        // Biomass -> Crude Bio
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.FUEL, CommonMaterialKeys.BIO),
                resultHelper.fluid(RagiumFluidContents.CRUDE_BIO, 125),
            ).save(output)

        // Crude Bio -> Bio Fuel
        distillation(
            RagiumFluidContents.CRUDE_BIO to 1000,
            resultHelper.item(Items.CLAY_BALL),
            resultHelper.fluid(RagiumFluidContents.BIOFUEL, 375) to null,
        )

        // Poisonous Potato
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.CROPS_POTATO))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRUDE_BIO, 250))
            .setResult(resultHelper.item(Items.POISONOUS_POTATO))
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
