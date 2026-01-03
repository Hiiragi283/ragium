package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTPlantingRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumPlantingRecipeBuilder : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        crops()
        trees()
        plants()
    }

    @JvmStatic
    private fun crops() {
        val farmland: HTItemIngredient = itemCreator.fromItem(Items.FARMLAND)
        // Wheat
        HTPlantingRecipeBuilder
            .create(
                Items.WHEAT_SEEDS,
                farmland,
                itemResult.create(Items.WHEAT),
            ).save(output)
        // Beetroot
        HTPlantingRecipeBuilder
            .create(
                Items.BEETROOT_SEEDS,
                farmland,
                itemResult.create(Items.BEETROOT, 3),
            ).save(output)

        // Carrot, Potato, Berries
        for (item: Item in listOf(Items.CARROT, Items.POTATO, Items.SWEET_BERRIES, Items.GLOW_BERRIES)) {
            HTPlantingRecipeBuilder
                .create(
                    item,
                    farmland,
                    itemResult.create(item, 3),
                ).save(output)
        }

        // Melon
        HTPlantingRecipeBuilder
            .create(
                Items.MELON_SEEDS,
                farmland,
                itemResult.create(Items.MELON),
            ).save(output)
        // Pumpkin
        HTPlantingRecipeBuilder
            .create(
                Items.PUMPKIN_SEEDS,
                farmland,
                itemResult.create(Items.PUMPKIN),
            ).save(output)

        // Cactus, Sugar Cane
        for (item: Item in listOf(Items.CACTUS, Items.SUGAR_CANE)) {
            HTPlantingRecipeBuilder
                .create(
                    item,
                    itemCreator.fromTagKey(Tags.Items.SANDS),
                    itemResult.create(item, 3),
                ).save(output)
        }

        // Apple
        HTPlantingRecipeBuilder
            .create(
                Items.APPLE,
                itemCreator.fromItem(Items.OAK_SAPLING),
                itemResult.create(Items.APPLE, 3),
            ).save(output)
        // Cocoa Beans
        HTPlantingRecipeBuilder
            .create(
                Items.COCOA_BEANS,
                itemCreator.fromTagKey(ItemTags.JUNGLE_LOGS),
                itemResult.create(Items.COCOA_BEANS, 3),
            ).save(output)
        // Nether Wart
        HTPlantingRecipeBuilder
            .create(
                Items.NETHER_WART,
                itemCreator.fromItem(Items.SOUL_SAND),
                itemResult.create(Items.NETHER_WART, 3),
            ).save(output)

        // Mushrooms
        for (item: Item in listOf(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM)) {
            HTPlantingRecipeBuilder
                .create(
                    item,
                    farmland,
                    itemResult.create(item, 3),
                ).save(output)

            HTPlantingRecipeBuilder
                .create(
                    item,
                    itemCreator.fromItem(Items.MYCELIUM),
                    itemResult.create(item, 5),
                ).saveSuffixed(output, "_with_mycelium")
        }
    }

    @JvmStatic
    private fun trees() {
        // Trees
        val dirt: HTItemIngredient = itemCreator.fromTagKey(ItemTags.DIRT)
        mapOf(
            Items.OAK_SAPLING to Items.OAK_LOG,
            Items.SPRUCE_SAPLING to Items.SPRUCE_LOG,
            Items.BIRCH_SAPLING to Items.BIRCH_LOG,
            Items.JUNGLE_SAPLING to Items.JUNGLE_LOG,
            Items.ACACIA_SAPLING to Items.ACACIA_LOG,
            Items.DARK_OAK_SAPLING to Items.DARK_OAK_LOG,
            Items.CHERRY_SAPLING to Items.CHERRY_LOG,
            Items.MANGROVE_PROPAGULE to Items.MANGROVE_LOG,
            Items.BAMBOO to Items.BAMBOO,
        ).forEach { (sapling: Item, log: Item) ->
            HTPlantingRecipeBuilder
                .create(
                    sapling,
                    dirt,
                    itemResult.create(log, 6),
                ).save(output)
        }
        // Fungus
        HTPlantingRecipeBuilder
            .create(
                Items.CRIMSON_FUNGUS,
                itemCreator.fromItem(Items.CRIMSON_NYLIUM),
                itemResult.create(Items.CRIMSON_STEM, 6),
            ).save(output)

        HTPlantingRecipeBuilder
            .create(
                Items.WARPED_FUNGUS,
                itemCreator.fromItem(Items.WARPED_NYLIUM),
                itemResult.create(Items.WARPED_STEM, 6),
            ).save(output)
        // Chorus
        HTPlantingRecipeBuilder
            .create(
                Items.CHORUS_FLOWER,
                itemCreator.fromTagKey(Tags.Items.END_STONES),
                itemResult.create(Items.CHORUS_FRUIT, 6),
            ).save(output)
    }

    @JvmStatic
    private fun plants() {
        val aquaticSoil: HTItemIngredient = itemCreator.fromTagKeys(listOf(Tags.Items.GRAVELS, Tags.Items.SANDS))

        mapOf(
            Items.VINE to itemCreator.fromTagKey(Tags.Items.COBBLESTONES),
            Items.LILY_PAD to itemCreator.fromTagKey(Tags.Items.BUCKETS_WATER),
            Items.SEAGRASS to aquaticSoil,
            Items.SEA_PICKLE to aquaticSoil,
            Items.TUBE_CORAL to itemCreator.fromItem(Items.TUBE_CORAL_BLOCK),
            Items.BRAIN_CORAL to itemCreator.fromItem(Items.BRAIN_CORAL_BLOCK),
            Items.BUBBLE_CORAL to itemCreator.fromItem(Items.BUBBLE_CORAL_BLOCK),
            Items.FIRE_CORAL to itemCreator.fromItem(Items.FIRE_CORAL_BLOCK),
            Items.HORN_CORAL to itemCreator.fromItem(Items.HORN_CORAL_BLOCK),
            Items.TUBE_CORAL_FAN to itemCreator.fromItem(Items.TUBE_CORAL_BLOCK),
            Items.BRAIN_CORAL_FAN to itemCreator.fromItem(Items.BRAIN_CORAL_BLOCK),
            Items.BUBBLE_CORAL_FAN to itemCreator.fromItem(Items.BUBBLE_CORAL_BLOCK),
            Items.FIRE_CORAL_FAN to itemCreator.fromItem(Items.FIRE_CORAL_BLOCK),
            Items.HORN_CORAL_FAN to itemCreator.fromItem(Items.HORN_CORAL_BLOCK),
        ).forEach { (plant: Item, soil: HTItemIngredient) ->
            HTPlantingRecipeBuilder
                .create(
                    plant,
                    soil,
                    itemResult.create(plant, 3),
                ).save(output)
        }

        // Kelp
        HTPlantingRecipeBuilder
            .create(
                Items.KELP,
                aquaticSoil,
                itemResult.create(Items.KELP, 6),
            ).save(output)
    }
}
