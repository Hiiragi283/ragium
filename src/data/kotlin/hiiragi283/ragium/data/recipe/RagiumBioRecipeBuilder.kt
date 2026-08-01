package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.compareTo
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.fraction
import hiiragi283.core.api.util.Either
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTPlantingRecipeBuilder
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import org.apache.commons.lang3.math.Fraction

class RagiumBioRecipeBuilder(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        planting()
    }

    override fun getName(): String = "Bio Recipes"

    //    Planting    //

    private fun planting() {
        crops()
        trees()
        plants()
    }

    private inline fun planting(seed: ItemLike, seedChance: Fraction, builderAction: HTPlantingRecipeBuilder.() -> Unit) {
        HTPlantingRecipeBuilder.create {
            plant { +seed }
            builderAction()
            if (seedChance > 0) {
                result {
                    +seed
                    chance = seedChance
                }
            }
        }.save(exporter)
    }

    private fun crops() {
        val farmland: IngredientBuilder.() -> Unit = { +Items.FARMLAND }
        // Wheat
        planting(Items.WHEAT_SEEDS, fraction(1, 3)) {
            soil(farmland)
            result { +Items.WHEAT }
        }
        // Beetroot
        planting(Items.BEETROOT_SEEDS, fraction(1, 3)) {
            soil(farmland)
            result {
                +Items.BEETROOT
                count = 3
            }
        }

        // Carrot, Potato, Berries
        for (seed: Item in listOf(Items.CARROT, Items.POTATO, Items.SWEET_BERRIES, Items.GLOW_BERRIES)) {
            planting(seed, fraction(1, 3)) {
                soil(farmland)
                result {
                    +seed
                    count = 3
                }
            }
        }

        // Melon
        planting(Items.MELON_SEEDS, fraction(1, 3)) {
            soil(farmland)
            result { +Items.MELON }
        }
        // Pumpkin
        planting(Items.PUMPKIN_SEEDS, fraction(1, 3)) {
            soil(farmland)
            result { +Items.PUMPKIN }
        }

        // Cactus, Sugar Cane
        for (item: Item in listOf(Items.CACTUS, Items.SUGAR_CANE)) {
            planting(item, fraction(1, 3)) {
                soil { +Tags.Items.SANDS }
                result {
                    +item
                    count = 3
                }
            }
        }

        // Apple
        planting(Items.APPLE, fraction(1, 3)) {
            soil { +Items.OAK_SAPLING }
            result {
                +Items.APPLE
                count = 3
            }
        }
        // Cocoa Beans
        planting(Items.COCOA_BEANS, fraction(1, 3)) {
            soil { +ItemTags.JUNGLE_LOGS }
            result {
                +Items.COCOA_BEANS
                count = 3
            }
        }
        // Nether Wart
        planting(Items.NETHER_WART, fraction(1, 3)) {
            soil { +Items.SOUL_SAND }
            result {
                +Items.NETHER_WART
                count = 3
            }
        }

        // Mushrooms
        for (item: Item in listOf(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM)) {
            planting(item, fraction(1, 3)) {
                soil(farmland)
                result {
                    +item
                    count = 3
                }
            }

            planting(item, fraction(1, 3)) {
                soil { +Items.MYCELIUM }
                result {
                    +item
                    count = 5
                }
                recipeId suffix "_with_mycelium"
            }
        }
    }

    private fun trees() {
        // Trees
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
                soil { +ItemTags.DIRT }
                result {
                    +log
                    count = 6
                }
            }
        }
        // Fungus
        planting(Items.CRIMSON_FUNGUS, fraction(1, 6)) {
            soil { +Items.CRIMSON_NYLIUM }
            result {
                +Items.CRIMSON_STEM
                count = 6
            }
        }

        planting(Items.WARPED_FUNGUS, fraction(1, 6)) {
            soil { +Items.WARPED_NYLIUM }
            result {
                +Items.WARPED_STEM
                count = 6
            }
        }
        // Chorus
        planting(Items.CHORUS_FLOWER, fraction(1, 6)) {
            soil { +Tags.Items.END_STONES }
            result {
                +Items.CHORUS_FRUIT
                count = 6
            }
        }
    }

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
        ).forEach { (plant: Item, soilTag: Either<List<TagKey<Item>>, Item>) ->
            planting(plant, Fraction.ZERO) {
                soil {
                    soilTag.fold(
                        { +it },
                        { +it },
                    )
                }
                result {
                    +plant
                    count = 4
                }
            }
        }

        // Kelp
        planting(Items.KELP, Fraction.ZERO) {
            soil { +aquaticSoil }
            result {
                +Items.KELP
                count = 6
            }
        }
    }
}
