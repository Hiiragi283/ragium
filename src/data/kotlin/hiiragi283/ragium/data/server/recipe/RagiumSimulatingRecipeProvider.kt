package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumSimulatingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Amethyst
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                null,
                HTIngredientHelper.item(Items.BUDDING_AMETHYST),
                HTResultHelper.item(Items.AMETHYST_SHARD, 4),
            ).save(output)
        // Echo Shard
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                null,
                HTIngredientHelper.item(Items.SCULK_CATALYST),
                HTResultHelper.item(Items.ECHO_SHARD),
            ).save(output)

        mobExtracting()
    }

    @JvmStatic
    fun mobExtracting() {
        // Armadillo Scute
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                null,
                HTIngredientHelper.item(Items.ARMADILLO_SPAWN_EGG),
                HTResultHelper.item(Items.ARMADILLO_SCUTE),
            ).save(output)

        // Honeycomb
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                null,
                HTIngredientHelper.item(Items.BEE_NEST),
                HTResultHelper.item(Items.HONEYCOMB),
            ).save(output)
        // Honey Bottle
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                HTIngredientHelper.item(Items.GLASS_BOTTLE),
                HTIngredientHelper.item(Items.BEE_NEST),
                HTResultHelper.item(Items.HONEY_BOTTLE),
            ).save(output)

        // Egg
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                HTIngredientHelper.item(Tags.Items.SEEDS),
                HTIngredientHelper.item(Items.CHICKEN_SPAWN_EGG),
                HTResultHelper.item(Items.EGG),
            ).save(output)

        // Dragon Breath
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                HTIngredientHelper.item(Items.GLASS_BOTTLE),
                HTIngredientHelper.item(Items.DRAGON_EGG),
                HTResultHelper.item(Items.DRAGON_BREATH),
            ).save(output)

        // Poppy
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                HTIngredientHelper.item(Tags.Items.FERTILIZERS),
                HTIngredientHelper.item(Items.IRON_GOLEM_SPAWN_EGG),
                HTResultHelper.item(Items.POPPY),
            ).save(output)

        // Ancient Debris
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                HTIngredientHelper.item(Tags.Items.NETHERRACKS, 16),
                HTIngredientHelper.item(Items.PIGLIN_BRUTE_SPAWN_EGG),
                HTResultHelper.item(Items.ANCIENT_DEBRIS),
            ).save(output)

        // Wool
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                null,
                HTIngredientHelper.item(Items.SHEEP_SPAWN_EGG),
                HTResultHelper.item(Items.WHITE_WOOL),
            ).save(output)

        // Turtle Scute
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                HTIngredientHelper.item(Items.SEAGRASS, 8),
                HTIngredientHelper.item(Items.TURTLE_SPAWN_EGG),
                HTResultHelper.item(Items.TURTLE_SCUTE),
            ).save(output)

        // Resonant Debris
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                HTIngredientHelper.item(Items.DEEPSLATE, 8),
                HTIngredientHelper.item(Items.WARDEN_SPAWN_EGG),
                HTResultHelper.item(RagiumBlocks.RESONANT_DEBRIS),
            ).save(output)

        // Nether Star
        HTShapedRecipeBuilder(RagiumItems.WITHER_DOLl)
            .pattern(
                "AAA",
                "BBB",
                " B ",
            ).define('A', Items.WITHER_SKELETON_SKULL)
            .define('B', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .save(output)

        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                HTIngredientHelper.item(RagiumItems.WITHER_DOLl),
                HTIngredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.IRIDESCENTIUM),
                HTResultHelper.item(Tags.Items.NETHER_STARS),
            ).save(output)
    }
}
