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
        val farmland: HTItemIngredient = inputCreator.create(Items.FARMLAND)
        // Wheat
        HTPlantingRecipeBuilder.create(output) {
            seed = Items.WHEAT_SEEDS
            soil = farmland
            crop = resultCreator.create(Items.WHEAT)
        }
        // Beetroot
        HTPlantingRecipeBuilder.create(output) {
            seed = Items.BEETROOT_SEEDS
            soil = farmland
            crop = resultCreator.create(Items.BEETROOT, 3)
        }

        // Carrot, Potato, Berries
        for (item: Item in listOf(Items.CARROT, Items.POTATO, Items.SWEET_BERRIES, Items.GLOW_BERRIES)) {
            HTPlantingRecipeBuilder.create(output) {
                seed = Items.MELON_SEEDS
                soil = farmland
                crop = resultCreator.create(item, 3)
            }
        }

        // Melon
        HTPlantingRecipeBuilder.create(output) {
            seed = Items.MELON_SEEDS
            soil = farmland
            crop = resultCreator.create(Items.MELON)
        }
        // Pumpkin
        HTPlantingRecipeBuilder.create(output) {
            seed = Items.PUMPKIN_SEEDS
            soil = farmland
            crop = resultCreator.create(Items.PUMPKIN)
        }

        // Cactus, Sugar Cane
        for (item: Item in listOf(Items.CACTUS, Items.SUGAR_CANE)) {
            HTPlantingRecipeBuilder.create(output) {
                seed = item
                soil = inputCreator.create(Tags.Items.SANDS)
                crop = resultCreator.create(item, 3)
            }
        }

        // Apple
        HTPlantingRecipeBuilder.create(output) {
            seed = Items.APPLE
            soil = inputCreator.create(Items.OAK_SAPLING)
            crop = resultCreator.create(Items.APPLE, 3)
        }
        // Cocoa Beans
        HTPlantingRecipeBuilder.create(output) {
            seed = Items.COCOA_BEANS
            soil = inputCreator.create(ItemTags.JUNGLE_LOGS)
            crop = resultCreator.create(Items.COCOA_BEANS, 3)
        }
        // Nether Wart
        HTPlantingRecipeBuilder.create(output) {
            seed = Items.NETHER_WART
            soil = inputCreator.create(Items.SOUL_SAND)
            crop = resultCreator.create(Items.NETHER_WART, 3)
        }

        // Mushrooms
        for (item: Item in listOf(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM)) {
            HTPlantingRecipeBuilder.create(output) {
                seed = item
                soil = farmland
                crop = resultCreator.create(item, 3)
            }

            HTPlantingRecipeBuilder.create(output) {
                seed = item
                soil = inputCreator.create(Items.MYCELIUM)
                crop = resultCreator.create(item, 5)
                recipeId suffix "_with_mycelium"
            }
        }
    }

    @JvmStatic
    private fun trees() {
        // Trees
        val dirt: HTItemIngredient = inputCreator.create(ItemTags.DIRT)
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
            HTPlantingRecipeBuilder.create(output) {
                seed = sapling
                soil = dirt
                crop = resultCreator.create(log, 6)
            }
        }
        // Fungus
        HTPlantingRecipeBuilder.create(output) {
            seed = Items.CRIMSON_FUNGUS
            soil = inputCreator.create(Items.CRIMSON_NYLIUM)
            crop = resultCreator.create(Items.CRIMSON_STEM, 6)
        }

        HTPlantingRecipeBuilder.create(output) {
            seed = Items.WARPED_FUNGUS
            soil = inputCreator.create(Items.WARPED_NYLIUM)
            crop = resultCreator.create(Items.WARPED_STEM, 6)
        }
        // Chorus
        HTPlantingRecipeBuilder.create(output) {
            seed = Items.CHORUS_FLOWER
            soil = inputCreator.create(Tags.Items.END_STONES)
            crop = resultCreator.create(Items.CHORUS_FRUIT, 6)
        }
    }

    @JvmStatic
    private fun plants() {
        val aquaticSoil: HTItemIngredient = inputCreator.create(listOf(Tags.Items.GRAVELS, Tags.Items.SANDS))

        mapOf(
            Items.VINE to inputCreator.create(Tags.Items.COBBLESTONES),
            Items.LILY_PAD to inputCreator.create(Tags.Items.BUCKETS_WATER),
            Items.SEAGRASS to aquaticSoil,
            Items.SEA_PICKLE to aquaticSoil,
            Items.TUBE_CORAL to inputCreator.create(Items.TUBE_CORAL_BLOCK),
            Items.BRAIN_CORAL to inputCreator.create(Items.BRAIN_CORAL_BLOCK),
            Items.BUBBLE_CORAL to inputCreator.create(Items.BUBBLE_CORAL_BLOCK),
            Items.FIRE_CORAL to inputCreator.create(Items.FIRE_CORAL_BLOCK),
            Items.HORN_CORAL to inputCreator.create(Items.HORN_CORAL_BLOCK),
            Items.TUBE_CORAL_FAN to inputCreator.create(Items.TUBE_CORAL_BLOCK),
            Items.BRAIN_CORAL_FAN to inputCreator.create(Items.BRAIN_CORAL_BLOCK),
            Items.BUBBLE_CORAL_FAN to inputCreator.create(Items.BUBBLE_CORAL_BLOCK),
            Items.FIRE_CORAL_FAN to inputCreator.create(Items.FIRE_CORAL_BLOCK),
            Items.HORN_CORAL_FAN to inputCreator.create(Items.HORN_CORAL_BLOCK),
        ).forEach { (plant: Item, soil: HTItemIngredient) ->
            HTPlantingRecipeBuilder.create(output) {
                this.seed = plant
                this.soil = soil
                this.crop = resultCreator.create(plant, 3)
            }
        }

        // Kelp
        HTPlantingRecipeBuilder.create(output) {
            seed = Items.KELP
            soil = aquaticSoil
            crop = resultCreator.create(Items.KELP, 6)
        }
    }
}
