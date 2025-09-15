package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
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
                ingredientHelper.item(Items.GLASS_BOTTLE),
                ingredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 1000),
                resultHelper.item(Items.OMINOUS_BOTTLE),
            ).save(output)
    }

    @JvmStatic
    private fun water() {
        // Dirt -> Mud
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Items.DIRT, 8),
                ingredientHelper.water(1000),
                resultHelper.item(Items.MUD, 8),
            ).saveSuffixed(output, "_from_dirt")
        // Silt -> Clay
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(RagiumBlocks.SILT, 8),
                ingredientHelper.water(1000),
                resultHelper.item(Items.CLAY, 8),
            ).saveSuffixed(output, "_from_silt")

        // Ice <-> Water
        meltAndFreeze(
            ingredientHelper.item(Tags.Items.GLASS_BLOCKS),
            Items.ICE,
            HTFluidContent.WATER,
            1000,
        )
        // Water Bottle
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Items.GLASS_BOTTLE),
                ingredientHelper.water(250),
                resultHelper.item(PotionContents.createItemStack(Items.POTION, Potions.WATER)),
            ).save(output, RagiumAPI.id("water_bottle"))

        // Concretes
        for (color: DyeColor in DyeColor.entries) {
            val name: String = color.serializedName
            HTFluidTransformRecipeBuilder
                .infusing(
                    ingredientHelper.item(DeferredItem.createItem<Item>(vanillaId("${name}_concrete_powder")), 8),
                    ingredientHelper.water(1000),
                    resultHelper.item(DeferredItem.createItem<Item>(vanillaId("${name}_concrete")), 8),
                ).saveSuffixed(output, "_from_powder")
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
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Items.GOLDEN_APPLE),
                ingredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 8000),
                resultHelper.item(Items.ENCHANTED_GOLDEN_APPLE),
            ).save(output)
        // Exp Berries
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumBlocks.EXP_BERRIES),
                ingredientHelper.item(Tags.Items.FOODS_BERRY),
                ingredientHelper.item(HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL, 4),
            ).save(output)
        // Blaze Powder
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR),
                ingredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 250),
                resultHelper.item(Items.BLAZE_POWDER),
            ).save(output)
        // Wind Charge
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Items.SNOWBALL),
                ingredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 250),
                resultHelper.item(Items.WIND_CHARGE),
            ).save(output)
        // Ghast Tear
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Items.CHISELED_QUARTZ_BLOCK),
                ingredientHelper.fluid(RagiumFluidContents.EXPERIENCE, 1000),
                resultHelper.item(Items.GHAST_TEAR),
            ).save(output)
    }

    @JvmStatic
    private fun crimson() {
        // Crimson Nylium
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Tags.Items.NETHERRACKS),
                ingredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 250),
                resultHelper.item(Items.CRIMSON_NYLIUM),
            ).save(output)
        // Crimson Fungus
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Items.RED_MUSHROOM),
                ingredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 250),
                resultHelper.item(Items.CRIMSON_FUNGUS),
            ).save(output)
        // Nether Wart
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Tags.Items.CROPS_BEETROOT),
                ingredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 250),
                resultHelper.item(Items.NETHER_WART),
            ).save(output)

        // Crimson Soil
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Items.SOUL_SOIL),
                ingredientHelper.fluid(RagiumFluidContents.CRIMSON_BLOOD, 2000),
                resultHelper.item(RagiumBlocks.CRIMSON_SOIL),
            ).save(output)
    }

    @JvmStatic
    private fun warped() {
        // Warped Nylium
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Tags.Items.NETHERRACKS),
                ingredientHelper.fluid(RagiumFluidContents.DEW_OF_THE_WARP, 250),
                resultHelper.item(Items.WARPED_NYLIUM),
            ).save(output)
        // Warped Fungus
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Items.RED_MUSHROOM),
                ingredientHelper.fluid(RagiumFluidContents.DEW_OF_THE_WARP, 250),
                resultHelper.item(Items.WARPED_FUNGUS),
            ).save(output)
        // Warped Wart
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Tags.Items.CROPS_BEETROOT),
                ingredientHelper.fluid(RagiumFluidContents.DEW_OF_THE_WARP, 1000),
                resultHelper.item(RagiumBlocks.WARPED_WART),
            ).save(output)
    }

    @JvmStatic
    private fun eldritch() {
        // Budding Amethyst
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, HTVanillaMaterialType.AMETHYST),
                ingredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
                resultHelper.item(Items.BUDDING_AMETHYST),
            ).save(output)
        // Ominous Trial Key
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Items.TRIAL_KEY),
                ingredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
                resultHelper.item(Items.OMINOUS_TRIAL_KEY),
            ).save(output)
        // Crying Obsidian
        HTFluidTransformRecipeBuilder
            .infusing(
                ingredientHelper.item(Tags.Items.OBSIDIANS_NORMAL),
                ingredientHelper.fluid(RagiumFluidContents.ELDRITCH_FLUX, 4000),
                resultHelper.item(Items.CRYING_OBSIDIAN),
            ).save(output)
    }
}
