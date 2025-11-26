package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.util.HTPotionHelper
import hiiragi283.ragium.common.variant.HTColoredVariant
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags

object RagiumWashingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        bottle()

        water()
        exp()

        crimson()
        warped()
        eldritch()
    }

    @JvmStatic
    private fun bottle() {
        // Ominous Bottle
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 1000),
            ).addResult(resultHelper.item(Items.OMINOUS_BOTTLE))
            .save(output)
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    private fun water() {
        // Dirt -> Mud
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.DIRT, 8),
                fluidCreator.water(1000),
            ).addResult(resultHelper.item(Items.MUD, 8))
            .saveSuffixed(output, "_from_dirt")
        // Gravel -> Flint
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(Tags.Items.GRAVELS),
                fluidCreator.water(250),
            ).addResult(resultHelper.item(Items.FLINT))
            .addResult(resultHelper.item(Items.FLINT), 1 / 2f)
            .save(output)
        // Silt -> Clay
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(RagiumBlocks.SILT, 8),
                fluidCreator.water(1000),
            ).addResult(resultHelper.item(Items.CLAY, 8))
            .saveSuffixed(output, "_from_silt")

        // Ice <-> Water
        meltAndFreeze(
            HTMoldType.STORAGE_BLOCK,
            Items.ICE.toHolderLike(),
            HTFluidHolderLike.WATER,
            1000,
        )
        // Water Bottle
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                fluidCreator.water(250),
            ).addResult(resultHelper.item(HTPotionHelper.createPotion(Items.POTION, Potions.WATER)))
            .save(output, RagiumAPI.id("water_bottle"))

        // Concretes
        for (color: HTColorMaterial in HTColorMaterial.entries) {
            HTItemWithFluidToChancedItemRecipeBuilder
                .washing(
                    itemCreator.fromItem(HTColorMaterial.getColoredItem(HTColoredVariant.CONCRETE_POWDER, color), 8),
                    fluidCreator.water(1000),
                ).addResult(resultHelper.item(HTColorMaterial.getColoredItem(HTColoredVariant.CONCRETE, color), 8))
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
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.GOLDEN_APPLE),
                fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 8000),
            ).addResult(resultHelper.item(Items.ENCHANTED_GOLDEN_APPLE))
            .save(output)
        // Exp Berries
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumBlocks.EXP_BERRIES),
                itemCreator.fromTagKey(Tags.Items.FOODS_BERRY),
                itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL, 4),
            ).save(output)
        // Blaze Powder
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR),
                fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 250),
            ).addResult(resultHelper.item(Items.BLAZE_POWDER))
            .save(output)
        // Wind Charge
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.SNOWBALL),
                fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 250),
            ).addResult(resultHelper.item(Items.WIND_CHARGE))
            .save(output)
        // Ghast Tear
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.CHISELED_QUARTZ_BLOCK),
                fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 1000),
            ).addResult(resultHelper.item(Items.GHAST_TEAR))
            .save(output)
        // Phantom Membrane
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(Tags.Items.LEATHERS),
                fluidCreator.fromHolder(RagiumFluidContents.EXPERIENCE, 250),
            ).addResult(resultHelper.item(Items.PHANTOM_MEMBRANE))
            .save(output)
    }

    @JvmStatic
    private fun crimson() {
        // Crimson Nylium
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(Tags.Items.NETHERRACKS),
                fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 250),
            ).addResult(resultHelper.item(Items.CRIMSON_NYLIUM))
            .save(output)
        // Crimson Fungus
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.RED_MUSHROOM),
                fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 250),
            ).addResult(resultHelper.item(Items.CRIMSON_FUNGUS))
            .save(output)
        // Nether Wart
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(Tags.Items.CROPS_BEETROOT),
                fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 250),
            ).addResult(resultHelper.item(Items.NETHER_WART))
            .save(output)

        // Crimson Soil
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.SOUL_SOIL),
                fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 2000),
            ).addResult(resultHelper.item(RagiumBlocks.CRIMSON_SOIL))
            .save(output)
    }

    @JvmStatic
    private fun warped() {
        // Warped Nylium
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(Tags.Items.NETHERRACKS),
                fluidCreator.fromHolder(RagiumFluidContents.DEW_OF_THE_WARP, 250),
            ).addResult(resultHelper.item(Items.WARPED_NYLIUM))
            .save(output)
        // Warped Fungus
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.RED_MUSHROOM),
                fluidCreator.fromHolder(RagiumFluidContents.DEW_OF_THE_WARP, 250),
            ).addResult(resultHelper.item(Items.WARPED_FUNGUS))
            .save(output)
        // Warped Wart
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(Tags.Items.CROPS_BEETROOT),
                fluidCreator.fromHolder(RagiumFluidContents.DEW_OF_THE_WARP, 1000),
            ).addResult(resultHelper.item(RagiumBlocks.WARPED_WART))
            .save(output)
    }

    @JvmStatic
    private fun eldritch() {
        // Budding Amethyst
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST),
                fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000),
            ).addResult(resultHelper.item(Items.BUDDING_AMETHYST))
            .save(output)
        // Budding Quartz
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ),
                fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000),
            ).addResult(resultHelper.item(RagiumBlocks.BUDDING_QUARTZ))
            .save(output)

        // Ominous Trial Key
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.TRIAL_KEY),
                fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000),
            ).addResult(resultHelper.item(Items.OMINOUS_TRIAL_KEY))
            .save(output)
        // Crying Obsidian
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(Tags.Items.OBSIDIANS_NORMAL),
                fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 4000),
            ).addResult(resultHelper.item(Items.CRYING_OBSIDIAN))
            .save(output)
    }
}
