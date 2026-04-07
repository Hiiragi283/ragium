package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.compareTo
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.util.Either
import hiiragi283.core.common.data.recipe.builder.HTDoubleMultiOutputRecipeBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import org.apache.commons.lang3.math.Fraction

object RagiumPlantingRecipeBuilder : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        crops()
        trees()
        plants()
    }

    @JvmStatic
    private inline fun planting(seed: ItemLike, seedChance: Fraction, builderAction: HTDoubleMultiOutputRecipeBuilder.() -> Unit) {
        planting(seed.toItemLike(), seedChance, builderAction)
    }

    @JvmStatic
    private inline fun planting(
        seed: HTItemHolderLike<*>,
        seedChance: Fraction,
        builderAction: HTDoubleMultiOutputRecipeBuilder.() -> Unit,
    ) {
        RagiumRecipeBuilder.planting(output) {
            base = inputCreator.create(seed)
            builderAction()
            if (seedChance > 0) {
                results += resultCreator.create(seed, chance = seedChance)
            }
        }
    }

    @JvmStatic
    private fun crops() {
        val farmland: HTItemIngredient = inputCreator.create(Items.FARMLAND, amount = 0)
        // Wheat
        planting(Items.WHEAT_SEEDS, fraction(1, 3)) {
            addition = farmland
            results += resultCreator.create(Items.WHEAT)
        }
        // Beetroot
        planting(Items.BEETROOT_SEEDS, fraction(1, 3)) {
            addition = farmland
            results += resultCreator.create(Items.BEETROOT, 3)
        }

        // Carrot, Potato, Berries
        for (seed: Item in listOf(Items.CARROT, Items.POTATO, Items.SWEET_BERRIES, Items.GLOW_BERRIES)) {
            planting(seed, fraction(1, 3)) {
                addition = farmland
                results += resultCreator.create(seed, 3)
            }
        }

        // Melon
        planting(Items.MELON_SEEDS, fraction(1, 3)) {
            addition = farmland
            results += resultCreator.create(Items.MELON)
        }
        // Pumpkin
        planting(Items.PUMPKIN_SEEDS, fraction(1, 3)) {
            addition = farmland
            results += resultCreator.create(Items.PUMPKIN)
        }

        // Cactus, Sugar Cane
        for (item: Item in listOf(Items.CACTUS, Items.SUGAR_CANE)) {
            planting(item, fraction(1, 3)) {
                addition = inputCreator.create(Tags.Items.SANDS, amount = 0)
                results += resultCreator.create(item, 3)
            }
        }

        // Apple
        planting(Items.APPLE, fraction(1, 3)) {
            addition = inputCreator.create(Items.OAK_SAPLING, amount = 0)
            results += resultCreator.create(Items.APPLE, 3)
        }
        // Cocoa Beans
        planting(Items.COCOA_BEANS, fraction(1, 3)) {
            addition = inputCreator.create(ItemTags.JUNGLE_LOGS, amount = 0)
            results += resultCreator.create(Items.COCOA_BEANS, 3)
        }
        // Nether Wart
        planting(Items.NETHER_WART, fraction(1, 3)) {
            addition = inputCreator.create(Items.SOUL_SAND, amount = 0)
            results += resultCreator.create(Items.NETHER_WART, 3)
        }

        // Mushrooms
        for (item: Item in listOf(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM)) {
            planting(item, fraction(1, 3)) {
                addition = farmland
                results += resultCreator.create(item, 3)
            }

            planting(item, fraction(1, 3)) {
                addition = inputCreator.create(Items.MYCELIUM, amount = 0)
                results += resultCreator.create(item, 5)
                recipeId suffix "_with_mycelium"
            }
        }
    }

    @JvmStatic
    private fun trees() {
        // Trees
        val dirt: HTItemIngredient = inputCreator.create(ItemTags.DIRT, amount = 0)
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
            planting(sapling, fraction(1, 6)) {
                addition = dirt
                results += resultCreator.create(log, 6)
            }
        }
        // Fungus
        planting(Items.CRIMSON_FUNGUS, fraction(1, 6)) {
            addition = inputCreator.create(Items.CRIMSON_NYLIUM, amount = 0)
            results += resultCreator.create(Items.CRIMSON_STEM, 6)
        }

        planting(Items.WARPED_FUNGUS, fraction(1, 6)) {
            addition = inputCreator.create(Items.WARPED_NYLIUM, amount = 0)
            results += resultCreator.create(Items.WARPED_STEM, 6)
        }
        // Chorus
        planting(Items.CHORUS_FLOWER, fraction(1, 6)) {
            addition = inputCreator.create(Tags.Items.END_STONES, amount = 0)
            results += resultCreator.create(Items.CHORUS_FRUIT, 6)
        }
    }

    @JvmStatic
    private fun plants() {
        val aquaticSoil: List<TagKey<Item>> = listOf(Tags.Items.GRAVELS, Tags.Items.SANDS)

        mapOf<Item, Either<List<TagKey<Item>>, Item>>(
            Items.VINE to Either.Left(listOf(Tags.Items.COBBLESTONES)),
            Items.LILY_PAD to Either.Left(listOf(Tags.Items.BUCKETS_WATER)),
            Items.SEAGRASS to Either.Left(aquaticSoil),
            Items.SEA_PICKLE to Either.Left(aquaticSoil),
            Items.TUBE_CORAL to Either.Right(Items.TUBE_CORAL_BLOCK),
            Items.BRAIN_CORAL to Either.Right(Items.BRAIN_CORAL_BLOCK),
            Items.BUBBLE_CORAL to Either.Right(Items.BUBBLE_CORAL_BLOCK),
            Items.FIRE_CORAL to Either.Right(Items.FIRE_CORAL_BLOCK),
            Items.HORN_CORAL to Either.Right(Items.HORN_CORAL_BLOCK),
            Items.TUBE_CORAL_FAN to Either.Right(Items.TUBE_CORAL_BLOCK),
            Items.BRAIN_CORAL_FAN to Either.Right(Items.BRAIN_CORAL_BLOCK),
            Items.BUBBLE_CORAL_FAN to Either.Right(Items.BUBBLE_CORAL_BLOCK),
            Items.FIRE_CORAL_FAN to Either.Right(Items.FIRE_CORAL_BLOCK),
            Items.HORN_CORAL_FAN to Either.Right(Items.HORN_CORAL_BLOCK),
        ).forEach { (plant: Item, soil: Either<List<TagKey<Item>>, Item>) ->
            planting(plant, Fraction.ZERO) {
                addition = soil.map(
                    { tagKeys: List<TagKey<Item>> -> inputCreator.create(tagKeys, amount = 0) },
                    { item: Item -> inputCreator.create(item, amount = 0) },
                )
                results += resultCreator.create(plant, 4)
            }
        }

        // Kelp
        planting(Items.KELP, Fraction.ZERO) {
            addition = inputCreator.create(aquaticSoil, amount = 0)
            results += resultCreator.create(Items.KELP, 6)
        }
    }
}
