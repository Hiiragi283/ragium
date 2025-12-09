package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.impl.data.recipe.HTPlantingRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumPlantingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        crops()
        trees()
    }

    @JvmStatic
    private fun crops() {
        // Beetroot
        cropAndSeed(Items.BEETROOT, Items.BEETROOT)
        // Carrot
        cropAndCrop(Items.CARROT)
        // Potato
        cropAndCrop(Items.POTATO)
        // Wheat
        cropAndSeed(Items.WHEAT_SEEDS, Items.WHEAT)

        // Cactus
        cropAndCrop(Items.CACTUS, 25, itemCreator.fromTagKey(ItemTags.SAND))
        // Sugar Cane
        cropAndCrop(Items.SUGAR_CANE, soil = itemCreator.fromTagKey(RagiumModTags.Items.SOILS_AQUATIC))

        // Melon
        cropAndSeed(Items.MELON_SEEDS, Items.MELON)
        // Pumpkin
        cropAndSeed(Items.PUMPKIN_SEEDS, Items.PUMPKIN)

        // Sweet Berries
        cropAndCrop(Items.SWEET_BERRIES)
        // Glow Berries
        cropAndCrop(Items.GLOW_BERRIES)

        // Water Plants
        mapOf(
            Items.LILY_PAD to 1,
            Items.SEAGRASS to 4,
            Items.SEA_PICKLE to 1,
            Items.KELP to 4,
        ).forEach { (item: ItemLike, count: Int) ->
            HTPlantingRecipeBuilder
                .create(
                    item.toHolderLike(),
                    itemCreator.fromTagKey(RagiumModTags.Items.SOILS_AQUATIC),
                    fluidCreator.water(500),
                    resultHelper.item(item, count),
                ).save(output)
        }

        // Nether Wart
        HTPlantingRecipeBuilder
            .create(
                Items.NETHER_WART.toHolderLike(),
                itemCreator.fromItem(Items.SOUL_SAND),
                fluidCreator.lava(25),
                resultHelper.item(Items.NETHER_WART, 3),
            ).save(output)
        // Warped Wart
        HTPlantingRecipeBuilder
            .create(
                RagiumBlocks.WARPED_WART,
                itemCreator.fromItem(Items.SOUL_SAND),
                fluidCreator.lava(25),
                resultHelper.item(RagiumBlocks.WARPED_WART, 3),
            ).save(output)
        // Exp Berries
        cropAndCrop(RagiumBlocks.EXP_BERRIES)
    }

    @JvmStatic
    private fun trees() {
        tree(Items.OAK_SAPLING, Items.OAK_LOG)
        tree(Items.SPRUCE_SAPLING, Items.SPRUCE_LOG)
        tree(Items.BIRCH_SAPLING, Items.BIRCH_LOG)
        tree(Items.JUNGLE_SAPLING, Items.JUNGLE_LOG)
        tree(Items.ACACIA_SAPLING, Items.ACACIA_LOG)
        tree(Items.CHERRY_SAPLING, Items.CHERRY_LOG)
        tree(Items.DARK_OAK_SAPLING, Items.DARK_OAK_LOG)
        tree(Items.MANGROVE_PROPAGULE, Items.MANGROVE_LOG, itemCreator.fromItem(Items.MUD))
        tree(Items.BAMBOO, Items.BAMBOO)

        tree(Items.CRIMSON_FUNGUS, Items.CRIMSON_STEM, itemCreator.fromItem(Items.CRIMSON_NYLIUM), fluidCreator.lava(250))
        tree(Items.WARPED_FUNGUS, Items.WARPED_STEM, itemCreator.fromItem(Items.WARPED_NYLIUM), fluidCreator.lava(250))

        tree(
            Items.CHORUS_FLOWER,
            Items.CHORUS_FRUIT,
            itemCreator.fromTagKey(Tags.Items.END_STONES),
            fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 25),
        )
    }
}
