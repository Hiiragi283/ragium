package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredItem

object RagiumInfusingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        bottle()

        water()
        exp()

        crimson()
        warped()
        eldritch()
    }

    private fun bottle() {
        // Ominous Bottle
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.GLASS_BOTTLE),
                HTIngredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 1000),
                HTResultHelper.item(Items.OMINOUS_BOTTLE),
            ).save(output)
    }

    private fun water() {
        // Dirt -> Mud
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.DIRT, 8),
                HTIngredientHelper.water(1000),
                HTResultHelper.item(Items.MUD, 8),
            ).saveSuffixed(output, "_from_dirt")
        // Silt -> Clay
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(RagiumBlocks.SILT, 8),
                HTIngredientHelper.water(1000),
                HTResultHelper.item(Items.CLAY, 8),
            ).saveSuffixed(output, "_from_silt")

        // Ice <-> Water
        meltAndFreeze(
            HTIngredientHelper.item(Tags.Items.GLASS_BLOCKS),
            Items.ICE,
            HTFluidContent.WATER,
            1000,
        )
        // Water Bottle
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.GLASS_BOTTLE),
                HTIngredientHelper.water(250),
                HTResultHelper.item(createPotionStack(Potions.WATER)),
            ).save(output, RagiumAPI.id("water_bottle"))

        // Concretes
        for (color: DyeColor in DyeColor.entries) {
            val name: String = color.serializedName
            HTItemWithFluidToObjRecipeBuilder
                .infusing(
                    HTIngredientHelper.item(DeferredItem.createItem<Item>(vanillaId("${name}_concrete_powder")), 8),
                    HTIngredientHelper.water(1000),
                    HTResultHelper.item(DeferredItem.createItem<Item>(vanillaId("${name}_concrete")), 8),
                ).saveSuffixed(output, "_from_powder")
        }
    }

    private fun exp() {
        // Exp Bottle
        extractAndInfuse(
            HTIngredientHelper.item(Items.GLASS_BOTTLE),
            Items.EXPERIENCE_BOTTLE,
            RagiumFluidContents.EXPERIENCE,
            250,
        )
        // Exp Berries -> Liquid Exp
        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(RagiumItems.EXP_BERRIES),
                HTResultHelper.fluid(RagiumFluidContents.EXPERIENCE, 50),
            ).saveSuffixed(output, "_from_berries")

        // Golden Apple
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.GOLDEN_APPLE),
                HTIngredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 8000),
                HTResultHelper.item(Items.ENCHANTED_GOLDEN_APPLE),
            ).save(output)
        // Exp Berries
        HTItemWithCatalystToItemRecipeBuilder
            .pressing(
                HTIngredientHelper.item(RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL, 4),
                HTIngredientHelper.item(Tags.Items.FOODS_BERRY),
                HTResultHelper.item(RagiumItems.EXP_BERRIES),
            ).save(output)
        // Blaze Powder
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(RagiumCommonTags.Items.DUSTS_SULFUR),
                HTIngredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 250),
                HTResultHelper.item(Items.BLAZE_POWDER),
            ).save(output)
        // Wind Charge
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.SNOWBALL),
                HTIngredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 250),
                HTResultHelper.item(Items.WIND_CHARGE),
            ).save(output)
        // Ghast Tear
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.CHISELED_QUARTZ_BLOCK),
                HTIngredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 1000),
                HTResultHelper.item(Items.GHAST_TEAR),
            ).save(output)
    }

    private fun crimson() {
        // Crimson Nylium
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.NETHERRACKS),
                HTIngredientHelper.fluid(RagiumFluidContents.CRIMSON_SAP, 250),
                HTResultHelper.item(Items.CRIMSON_NYLIUM),
            ).save(output)
        // Crimson Fungus
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.RED_MUSHROOM),
                HTIngredientHelper.fluid(RagiumFluidContents.CRIMSON_SAP, 250),
                HTResultHelper.item(Items.CRIMSON_FUNGUS),
            ).save(output)
        // Nether Wart
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.CROPS_BEETROOT),
                HTIngredientHelper.fluid(RagiumFluidContents.CRIMSON_SAP, 250),
                HTResultHelper.item(Items.NETHER_WART),
            ).save(output)

        // Crimson Soil
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.SOUL_SOIL),
                HTIngredientHelper.fluid(RagiumFluidContents.CRIMSON_SAP, 250),
                HTResultHelper.item(RagiumBlocks.CRIMSON_SOIL),
            ).save(output)
    }

    private fun warped() {
        // Warped Nylium
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.NETHERRACKS),
                HTIngredientHelper.fluid(RagiumFluidContents.WARPED_SAP, 250),
                HTResultHelper.item(Items.WARPED_NYLIUM),
            ).save(output)
        // Warped Fungus
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.RED_MUSHROOM),
                HTIngredientHelper.fluid(RagiumFluidContents.WARPED_SAP, 250),
                HTResultHelper.item(Items.WARPED_FUNGUS),
            ).save(output)
        // Warped Wart
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.CROPS_BEETROOT),
                HTIngredientHelper.fluid(RagiumFluidContents.WARPED_SAP, 250),
                HTResultHelper.item(RagiumItems.WARPED_WART),
            ).save(output)
    }

    private fun eldritch() {
        // Ominous Trial Key
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.TRIAL_KEY),
                HTIngredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
                HTResultHelper.item(Items.OMINOUS_TRIAL_KEY),
            ).save(output)
        // Crying Obsidian
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.OBSIDIANS_NORMAL),
                HTIngredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
                HTResultHelper.item(Items.CRYING_OBSIDIAN),
            ).save(output)
    }
}
