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
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
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
        /*HTInfusingRecipeBuilder()
            .itemOutput(Items.ENCHANTED_GOLDEN_APPLE)
            .addIngredient(Items.GOLDEN_APPLE)
            .setCost(15 * 4)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.HEART_OF_THE_SEA)
            .addIngredient(RagiumItems.ELDER_HEART)
            .setCost(15 * 4)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.REINFORCED_DEEPSLATE)
            .addIngredient(Items.DEEPSLATE)
            .setCost(15 * 4)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.GILDED_BLACKSTONE)
            .addIngredient(Items.BLACKSTONE)
            .setCost(5)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.BUDDING_AMETHYST)
            .addIngredient(Items.AMETHYST_BLOCK)
            .setCost(15)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.CHORUS_FLOWER)
            .addIngredient(ItemTags.SMALL_FLOWERS)
            .setCost(15 * 2)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.DRAGON_EGG)
            .addIngredient(Tags.Items.EGGS)
            .setCost(15 * 4)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.HEAVY_CORE)
            .addIngredient(RagiumCommonTags.Items.STORAGE_BLOCKS_DEEP_STEEL)
            .setCost(15 * 2)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.OMINOUS_TRIAL_KEY)
            .addIngredient(Items.TRIAL_KEY)
            .setCost(15 * 2)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
            .addIngredient(RagiumItems.ELDRITCH_ORB)
            .setCost(15 * 2)
            .save(output)*/

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
        // Milk + Snow -> Ice Cream
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.SNOWBALL),
                HTIngredientHelper.milk(250),
                HTResultHelper.item(RagiumItems.ICE_CREAM),
            ).save(output)

        for (color: DyeColor in DyeColor.entries) {
            val name: String = color.serializedName
            HTItemWithFluidToObjRecipeBuilder
                .infusing(
                    HTIngredientHelper.item(DeferredItem.createItem<Item>(vanillaId("${name}_concrete_powder")), 8),
                    HTIngredientHelper.water(1000),
                    HTResultHelper.item(DeferredItem.createItem<Item>(vanillaId("${name}_concrete")), 8),
                ).saveSuffixed(output, "_from_powder")
        }

        bio()
        bottle()
        exp()
    }

    private fun bio() {
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(RagiumCommonTags.Items.FUELS_BIO_BLOCK),
                HTIngredientHelper.water(250),
                HTResultHelper.item(RagiumModTags.Items.POLYMER_RESIN),
            ).saveSuffixed(output, "_from_bio")
    }

    private fun bottle() {
        // Exp Bottle
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.GLASS_BOTTLE),
                HTIngredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 250),
                HTResultHelper.item(Items.EXPERIENCE_BOTTLE),
            ).save(output)

        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(Items.EXPERIENCE_BOTTLE),
                HTResultHelper.fluid(RagiumFluidContents.EXPERIENCE, 250),
            ).saveSuffixed(output, "_from_exp")
        // Honey Bottle
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.GLASS_BOTTLE),
                HTIngredientHelper.fluid(RagiumFluidContents.HONEY, 250),
                HTResultHelper.item(Items.HONEY_BOTTLE),
            ).save(output)
        // Water Bottle
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.GLASS_BOTTLE),
                HTIngredientHelper.water(250),
                HTResultHelper.item(createPotionStack(Potions.WATER)),
            ).save(output, RagiumAPI.id("water_bottle"))
    }

    private fun exp() {
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
}
