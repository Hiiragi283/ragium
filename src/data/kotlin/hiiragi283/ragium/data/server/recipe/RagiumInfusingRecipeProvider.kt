package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
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

    @JvmStatic
    private fun bottle() {
        // Ominous Bottle
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.GLASS_BOTTLE),
                HTIngredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 1000),
                HTResultHelper.INSTANCE.item(Items.OMINOUS_BOTTLE),
            ).save(output)
    }

    @JvmStatic
    private fun water() {
        // Dirt -> Mud
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.DIRT, 8),
                HTIngredientHelper.water(1000),
                HTResultHelper.INSTANCE.item(Items.MUD, 8),
            ).saveSuffixed(output, "_from_dirt")
        // Silt -> Clay
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(RagiumBlocks.SILT, 8),
                HTIngredientHelper.water(1000),
                HTResultHelper.INSTANCE.item(Items.CLAY, 8),
            ).saveSuffixed(output, "_from_silt")

        // Ice <-> Water
        meltAndFreeze(
            HTIngredientHelper.item(Tags.Items.GLASS_BLOCKS),
            Items.ICE,
            HTFluidContent.WATER,
            1000,
        )
        // Water Bottle
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.GLASS_BOTTLE),
                HTIngredientHelper.water(250),
                HTResultHelper.INSTANCE.item(createPotionStack(Potions.WATER)),
            ).save(output, RagiumAPI.id("water_bottle"))

        // Concretes
        for (color: DyeColor in DyeColor.entries) {
            val name: String = color.serializedName
            HTFluidTransformRecipeBuilder
                .infusing(
                    HTIngredientHelper.item(DeferredItem.createItem<Item>(vanillaId("${name}_concrete_powder")), 8),
                    HTIngredientHelper.water(1000),
                    HTResultHelper.INSTANCE.item(DeferredItem.createItem<Item>(vanillaId("${name}_concrete")), 8),
                ).saveSuffixed(output, "_from_powder")
        }
    }

    @JvmStatic
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
                HTIngredientHelper.item(RagiumBlocks.EXP_BERRIES),
                HTResultHelper.INSTANCE.fluid(RagiumFluidContents.EXPERIENCE, 50),
            ).saveSuffixed(output, "_from_berries")

        // Golden Apple
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.GOLDEN_APPLE),
                HTIngredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 8000),
                HTResultHelper.INSTANCE.item(Items.ENCHANTED_GOLDEN_APPLE),
            ).save(output)
        // Exp Berries
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.INSTANCE.item(RagiumBlocks.EXP_BERRIES),
                HTIngredientHelper.item(Tags.Items.FOODS_BERRY),
                HTIngredientHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL, 4),
            ).save(output)
        // Blaze Powder
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR),
                HTIngredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 250),
                HTResultHelper.INSTANCE.item(Items.BLAZE_POWDER),
            ).save(output)
        // Wind Charge
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.SNOWBALL),
                HTIngredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 250),
                HTResultHelper.INSTANCE.item(Items.WIND_CHARGE),
            ).save(output)
        // Ghast Tear
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.CHISELED_QUARTZ_BLOCK),
                HTIngredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 1000),
                HTResultHelper.INSTANCE.item(Items.GHAST_TEAR),
            ).save(output)
    }

    @JvmStatic
    private fun crimson() {
        // Crimson Nylium
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.NETHERRACKS),
                HTIngredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 250),
                HTResultHelper.INSTANCE.item(Items.CRIMSON_NYLIUM),
            ).save(output)
        // Crimson Fungus
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.RED_MUSHROOM),
                HTIngredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 250),
                HTResultHelper.INSTANCE.item(Items.CRIMSON_FUNGUS),
            ).save(output)
        // Nether Wart
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.CROPS_BEETROOT),
                HTIngredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 250),
                HTResultHelper.INSTANCE.item(Items.NETHER_WART),
            ).save(output)

        // Crimson Soil
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.SOUL_SOIL),
                HTIngredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 250),
                HTResultHelper.INSTANCE.item(RagiumBlocks.CRIMSON_SOIL),
            ).save(output)
    }

    @JvmStatic
    private fun warped() {
        // Warped Nylium
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.NETHERRACKS),
                HTIngredientHelper.fluid(RagiumFluidContents.DEW_OF_THE_WARP, 250),
                HTResultHelper.INSTANCE.item(Items.WARPED_NYLIUM),
            ).save(output)
        // Warped Fungus
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.RED_MUSHROOM),
                HTIngredientHelper.fluid(RagiumFluidContents.DEW_OF_THE_WARP, 250),
                HTResultHelper.INSTANCE.item(Items.WARPED_FUNGUS),
            ).save(output)
        // Warped Wart
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.CROPS_BEETROOT),
                HTIngredientHelper.fluid(RagiumFluidContents.DEW_OF_THE_WARP, 250),
                HTResultHelper.INSTANCE.item(RagiumBlocks.WARPED_WART),
            ).save(output)
    }

    @JvmStatic
    private fun eldritch() {
        // Budding Amethyst
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.AMETHYST_BLOCK),
                HTIngredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
                HTResultHelper.INSTANCE.item(Items.BUDDING_AMETHYST),
            ).save(output)
        // Ominous Trial Key
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.TRIAL_KEY),
                HTIngredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
                HTResultHelper.INSTANCE.item(Items.OMINOUS_TRIAL_KEY),
            ).save(output)
        // Crying Obsidian
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.OBSIDIANS_NORMAL),
                HTIngredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
                HTResultHelper.INSTANCE.item(Items.CRYING_OBSIDIAN),
            ).save(output)
    }
}
