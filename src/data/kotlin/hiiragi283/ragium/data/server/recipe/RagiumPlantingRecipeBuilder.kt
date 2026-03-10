package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.common.data.recipe.builder.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import org.apache.commons.lang3.math.Fraction

object RagiumPlantingRecipeBuilder : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        crops()
        trees()
        plants()
    }

    @JvmStatic
    private inline fun planting(seed: ItemLike, seedChance: Fraction, builderAction: HTItemToChancedRecipeBuilder.() -> Unit) {
        planting(seed.toItemLike(), seedChance, builderAction)
    }

    @JvmStatic
    private inline fun planting(seed: HTItemHolderLike<*>, seedChance: Fraction, builderAction: HTItemToChancedRecipeBuilder.() -> Unit) {
        RagiumRecipeBuilder.planting(output) {
            ingredient = inputCreator.create(seed)
            extraResult += resultCreator.create(seed) to seedChance
            builderAction()
        }
    }

    @JvmStatic
    private fun crops() {
        // Wheat
        planting(Items.WHEAT_SEEDS, fraction(1, 3)) {
            result = resultCreator.create(Items.WHEAT)
        }
        // Beetroot
        planting(Items.BEETROOT_SEEDS, fraction(1, 3)) {
            result = resultCreator.create(Items.BEETROOT, 3)
        }

        // Carrot, Potato, Berries
        for (seed: Item in listOf(Items.CARROT, Items.POTATO, Items.SWEET_BERRIES, Items.GLOW_BERRIES)) {
            planting(seed, fraction(1, 3)) {
                result = resultCreator.create(seed, 3)
            }
        }

        // Melon
        planting(Items.MELON_SEEDS, fraction(1, 3)) {
            result = resultCreator.create(Items.MELON)
        }
        // Pumpkin
        planting(Items.PUMPKIN_SEEDS, fraction(1, 3)) {
            result = resultCreator.create(Items.PUMPKIN)
        }

        // Cactus, Sugar Cane
        for (item: Item in listOf(Items.CACTUS, Items.SUGAR_CANE)) {
            planting(item, fraction(1, 3)) {
                result = resultCreator.create(item, 3)
            }
        }

        // Apple
        planting(Items.APPLE, fraction(1, 3)) {
            result = resultCreator.create(Items.APPLE, 3)
        }
        // Cocoa Beans
        planting(Items.COCOA_BEANS, fraction(1, 3)) {
            result = resultCreator.create(Items.COCOA_BEANS, 3)
        }
        // Nether Wart
        planting(Items.NETHER_WART, fraction(1, 3)) {
            result = resultCreator.create(Items.NETHER_WART, 3)
        }

        // Mushrooms
        for (item: Item in listOf(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM)) {
            planting(item, fraction(1, 3)) {
                result = resultCreator.create(item, 3)
            }
        }
    }

    @JvmStatic
    private fun trees() {
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
            Items.CRIMSON_FUNGUS to Items.CRIMSON_STEM,
            Items.WARPED_FUNGUS to Items.WARPED_STEM,
            Items.CHORUS_FLOWER to Items.CHORUS_FRUIT,
        ).forEach { (sapling: Item, log: Item) ->
            planting(sapling, fraction(1, 6)) {
                result = resultCreator.create(log, 6)
            }
        }
    }

    @JvmStatic
    private fun plants() {
        listOf(
            Items.VINE,
            Items.LILY_PAD,
            Items.SEAGRASS,
            Items.SEA_PICKLE,
            Items.TUBE_CORAL,
            Items.BRAIN_CORAL,
            Items.BUBBLE_CORAL,
            Items.FIRE_CORAL,
            Items.HORN_CORAL,
            Items.TUBE_CORAL_FAN,
            Items.BRAIN_CORAL_FAN,
            Items.BUBBLE_CORAL_FAN,
            Items.FIRE_CORAL_FAN,
            Items.HORN_CORAL_FAN,
        ).forEach { crop: Item ->
            planting(crop, fraction(1, 3)) {
                result = resultCreator.create(crop)
            }
        }

        planting(Items.KELP, fraction(1, 3)) {
            result = resultCreator.create(Items.KELP, 6)
        }
    }
}
