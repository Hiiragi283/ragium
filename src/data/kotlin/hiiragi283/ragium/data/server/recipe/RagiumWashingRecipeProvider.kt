package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTColoredVariant
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
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
                ingredientHelper.item(Items.GLASS_BOTTLE),
                ingredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 1000),
            ).addResult(resultHelper.item(Items.OMINOUS_BOTTLE))
            .save(output)
    }

    @JvmStatic
    private fun water() {
        // Dirt -> Mud
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Items.DIRT, 8),
                ingredientHelper.water(1000),
            ).addResult(resultHelper.item(Items.MUD, 8))
            .saveSuffixed(output, "_from_dirt")
        // Gravel -> Flint
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                HTIngredientHelper.INSTANCE.item(Tags.Items.GRAVELS),
                HTIngredientHelper.INSTANCE.water(250),
            ).addResult(HTResultHelper.INSTANCE.item(Items.FLINT))
            .addResult(HTResultHelper.INSTANCE.item(Items.FLINT), 1 / 2f)
            .save(output)
        // Silt -> Clay
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(RagiumBlocks.SILT, 8),
                ingredientHelper.water(1000),
            ).addResult(resultHelper.item(Items.CLAY, 8))
            .saveSuffixed(output, "_from_silt")

        // Ice <-> Water
        meltAndFreeze(
            ingredientHelper.item(Tags.Items.GLASS_BLOCKS),
            Items.ICE,
            HTFluidContent.WATER,
            1000,
        )
        // Water Bottle
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Items.GLASS_BOTTLE),
                ingredientHelper.water(250),
            ).addResult(resultHelper.item(PotionContents.createItemStack(Items.POTION, Potions.WATER)))
            .save(output, RagiumAPI.id("water_bottle"))

        // Concretes
        for (color: HTColorMaterial in HTColorMaterial.entries) {
            HTItemWithFluidToChancedItemRecipeBuilder
                .washing(
                    ingredientHelper.item(HTColorMaterial.getColoredItem(HTColoredVariant.CONCRETE_POWDER, color), 8),
                    ingredientHelper.water(1000),
                ).addResult(resultHelper.item(HTColorMaterial.getColoredItem(HTColoredVariant.CONCRETE, color), 8))
                .saveSuffixed(output, "_from_powder")
        }
    }

    @JvmStatic
    private fun exp() {
        // Exp Bottle
        extractAndInfuse(
            ingredientHelper.item(Items.GLASS_BOTTLE),
            Items.EXPERIENCE_BOTTLE,
            RagiumFluidContents.EXPERIENCE,
            250,
        )
        // Exp Berries -> Liquid Exp
        HTItemToObjRecipeBuilder
            .melting(
                ingredientHelper.item(RagiumBlocks.EXP_BERRIES),
                resultHelper.fluid(RagiumFluidContents.EXPERIENCE, 50),
            ).saveSuffixed(output, "_from_berries")

        // Golden Apple
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Items.GOLDEN_APPLE),
                ingredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 8000),
            ).addResult(resultHelper.item(Items.ENCHANTED_GOLDEN_APPLE))
            .save(output)
        // Exp Berries
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumBlocks.EXP_BERRIES),
                ingredientHelper.item(Tags.Items.FOODS_BERRY),
                ingredientHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL, 4),
            ).save(output)
        // Blaze Powder
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR),
                ingredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 250),
            ).addResult(resultHelper.item(Items.BLAZE_POWDER))
            .save(output)
        // Wind Charge
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Items.SNOWBALL),
                ingredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 250),
            ).addResult(resultHelper.item(Items.WIND_CHARGE))
            .save(output)
        // Ghast Tear
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Items.CHISELED_QUARTZ_BLOCK),
                ingredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 1000),
            ).addResult(resultHelper.item(Items.GHAST_TEAR))
            .save(output)
    }

    @JvmStatic
    private fun crimson() {
        // Crimson Nylium
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Tags.Items.NETHERRACKS),
                ingredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 250),
            ).addResult(resultHelper.item(Items.CRIMSON_NYLIUM))
            .save(output)
        // Crimson Fungus
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Items.RED_MUSHROOM),
                ingredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 250),
            ).addResult(resultHelper.item(Items.CRIMSON_FUNGUS))
            .save(output)
        // Nether Wart
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Tags.Items.CROPS_BEETROOT),
                ingredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 250),
            ).addResult(resultHelper.item(Items.NETHER_WART))
            .save(output)

        // Crimson Soil
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Items.SOUL_SOIL),
                ingredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 2000),
            ).addResult(resultHelper.item(RagiumBlocks.CRIMSON_SOIL))
            .save(output)
    }

    @JvmStatic
    private fun warped() {
        // Warped Nylium
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Tags.Items.NETHERRACKS),
                ingredientHelper.fluid(RagiumFluidContents.DEW_OF_THE_WARP, 250),
            ).addResult(resultHelper.item(Items.WARPED_NYLIUM))
            .save(output)
        // Warped Fungus
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Items.RED_MUSHROOM),
                ingredientHelper.fluid(RagiumFluidContents.DEW_OF_THE_WARP, 250),
            ).addResult(resultHelper.item(Items.WARPED_FUNGUS))
            .save(output)
        // Warped Wart
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Tags.Items.CROPS_BEETROOT),
                ingredientHelper.fluid(RagiumFluidContents.DEW_OF_THE_WARP, 1000),
            ).addResult(resultHelper.item(RagiumBlocks.WARPED_WART))
            .save(output)
    }

    @JvmStatic
    private fun eldritch() {
        // Budding Amethyst
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, HTVanillaMaterialType.AMETHYST),
                ingredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
            ).addResult(resultHelper.item(Items.BUDDING_AMETHYST))
            .save(output)
        // Ominous Trial Key
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Items.TRIAL_KEY),
                ingredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
            ).addResult(resultHelper.item(Items.OMINOUS_TRIAL_KEY))
            .save(output)
        // Crying Obsidian
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(Tags.Items.OBSIDIANS_NORMAL),
                ingredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
            ).addResult(resultHelper.item(Items.CRYING_OBSIDIAN))
            .save(output)
    }
}
