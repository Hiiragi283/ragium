package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTBlockMaterialVariant
import hiiragi283.ragium.common.variant.HTItemMaterialVariant
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
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
                itemCreator.fromItem(Items.BUDDING_AMETHYST),
                resultHelper.item(Items.AMETHYST_SHARD, 4),
            ).save(output)
        // Echo Shard
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                itemCreator.fromTagKey(HTItemMaterialVariant.GEM, HTVanillaMaterialType.AMETHYST),
                itemCreator.fromItem(Items.SCULK_CATALYST),
                resultHelper.item(Items.ECHO_SHARD),
            ).save(output)

        mobExtracting()
    }

    @JvmStatic
    fun mobExtracting() {
        // Armadillo Scute
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                null,
                itemCreator.fromItem(Items.ARMADILLO_SPAWN_EGG),
                resultHelper.item(Items.ARMADILLO_SCUTE),
            ).save(output)

        // Honeycomb
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                null,
                itemCreator.fromItem(Items.BEE_NEST),
                resultHelper.item(Items.HONEYCOMB),
            ).save(output)
        // Honey Bottle
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                itemCreator.fromItem(Items.BEE_NEST),
                resultHelper.item(Items.HONEY_BOTTLE),
            ).save(output)

        // Egg
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                itemCreator.fromTagKey(Tags.Items.SEEDS),
                itemCreator.fromItem(Items.CHICKEN_SPAWN_EGG),
                resultHelper.item(Items.EGG),
            ).save(output)

        // Heart of the Sea
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                itemCreator.fromItem(RagiumItems.ELDER_HEART),
                itemCreator.fromItem(Items.ELDER_GUARDIAN_SPAWN_EGG),
                resultHelper.item(Items.HEART_OF_THE_SEA),
            ).save(output)

        // Dragon Breath
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                itemCreator.fromItem(Items.DRAGON_HEAD),
                resultHelper.item(Items.DRAGON_BREATH),
            ).save(output)

        // Poppy
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                itemCreator.fromTagKey(Tags.Items.FERTILIZERS),
                itemCreator.fromItem(Items.IRON_GOLEM_SPAWN_EGG),
                resultHelper.item(Items.POPPY),
            ).save(output)

        // Ancient Debris
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.NETHER_BRICKS, 64),
                itemCreator.fromItem(Items.PIGLIN_BRUTE_SPAWN_EGG),
                resultHelper.item(Items.ANCIENT_DEBRIS),
            ).save(output)

        // Wool
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                null,
                itemCreator.fromItem(Items.SHEEP_SPAWN_EGG),
                resultHelper.item(Items.WHITE_WOOL),
            ).save(output)

        // Turtle Scute
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.SEAGRASS, 8),
                itemCreator.fromItem(Items.TURTLE_SPAWN_EGG),
                resultHelper.item(Items.TURTLE_SCUTE),
            ).save(output)

        // Resonant Debris
        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.DEEPSLATE, 8),
                itemCreator.fromItem(Items.WARDEN_SPAWN_EGG),
                resultHelper.item(RagiumBlocks.RESONANT_DEBRIS),
            ).save(output)

        // Nether Star
        HTShapedRecipeBuilder
            .misc(RagiumItems.WITHER_DOLl)
            .pattern(
                "AAA",
                "BBB",
                " B ",
            ).define('A', Items.WITHER_SKELETON_SKULL)
            .define('B', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .save(output)

        HTItemWithCatalystToItemRecipeBuilder
            .simulating(
                itemCreator.fromItem(RagiumItems.WITHER_DOLl),
                itemCreator.fromTagKey(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.IRIDESCENTIUM),
                resultHelper.item(Tags.Items.NETHER_STARS),
            ).save(output)
    }
}
