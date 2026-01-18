package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.HTMaterialResultHelper
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        ores()
        stones()
        planks()
        foods()
    }

    @JvmStatic
    private fun ores() {
        // Coal
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, VanillaMaterialKeys.COAL),
                HTMaterialResultHelper.item(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, 2),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR), 0.25f)
            .setExp(0.3f)
            .saveSuffixed(output, "_from_ore")

        // Redstone
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, VanillaMaterialKeys.REDSTONE),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE, 8),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, CommonMaterialKeys.CINNABAR))
            .setExp(0.5f)
            .saveSuffixed(output, "_from_ore")
        // Cinnabar
        // Salt
        // Saltpeter
        // Sulfur
        // Lapis
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, VanillaMaterialKeys.LAPIS),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.LAPIS, 12),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Quartz
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, VanillaMaterialKeys.QUARTZ),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.QUARTZ, 6),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR), 0.25f)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Amethyst
        // Diamond
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, VanillaMaterialKeys.DIAMOND),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.DIAMOND, 2),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL), 0.25f)
            .setExp(1f)
            .saveSuffixed(output, "_from_ore")
        // Emerald
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, VanillaMaterialKeys.EMERALD),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.EMERALD, 2),
            ).setExp(1f)
            .saveSuffixed(output, "_from_ore")

        // Copper
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, VanillaMaterialKeys.COPPER),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.COPPER, 3),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.GOLD), 0.25f)
            .setExp(0.3f)
            .saveSuffixed(output, "_from_ore")
        // Iron
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, VanillaMaterialKeys.IRON),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON, 2),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, CommonMaterialKeys.TIN), 0.25f)
            .setExp(0.5f)
            .saveSuffixed(output, "_from_ore")
        // Gold
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, VanillaMaterialKeys.GOLD),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.GOLD, 2),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.COPPER), 0.25f)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Silver
        // Netherite
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.ORES_NETHERITE_SCRAP),
                HTMaterialResultHelper.item(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2),
            ).setExp(1f)
            .saveSuffixed(output, "_from_ore")
        // Deep Steel

        // Raginite
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, RagiumMaterialKeys.RAGINITE),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 8),
            ).setExp(0.5f)
            .saveSuffixed(output, "_from_ore")
        // Ragi-Crystal
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(CommonTagPrefixes.ORE, RagiumMaterialKeys.RAGI_CRYSTAL),
                HTMaterialResultHelper.item(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL, 2),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE), 0.25f)
            .setExp(1f)
            .saveSuffixed(output, "_from_ore")
    }

    @JvmStatic
    private fun stones() {
        // Stone -> Cobblestone
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromItem(Items.STONE),
                itemResult.create(Items.COBBLESTONE),
            ).saveSuffixed(output, "_from_stone")
        // Cobblestone -> Gravel
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromItem(Items.COBBLESTONE),
                itemResult.create(Items.GRAVEL),
            ).saveSuffixed(output, "_from_cobblestone")
        // Gravel -> Sand
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.GRAVELS),
                itemResult.create(Items.SAND),
            ).saveSuffixed(output, "_from_gravel")
        // Sandstone -> Sand + Saltpeter
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                itemResult.create(Items.SAND, 2),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER), 0.25f)
            .saveSuffixed(output, "_from_sandstone")

        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_RED_BLOCKS),
                itemResult.create(Items.RED_SAND, 2),
            ).addResult(HTMaterialResultHelper.item(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER), 0.25f)
            .saveSuffixed(output, "_from_sandstone")
    }

    @JvmStatic
    private fun planks() {
        // Wood Dust
        fun wood(tagKey: TagKey<Item>, input: Int, output: Int) {
            HTChancedRecipeBuilder
                .crushing(
                    itemCreator.fromTagKey(tagKey, input),
                    HTMaterialResultHelper.item(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD, output),
                ).saveSuffixed(RagiumCrushingRecipeProvider.output, "_from_${tagKey.location().path}")
        }

        wood(ItemTags.BOATS, 1, 5)
        wood(ItemTags.LOGS_THAT_BURN, 1, 6)
        wood(ItemTags.WOODEN_BUTTONS, 1, 1)
        wood(ItemTags.WOODEN_DOORS, 1, 2)
        wood(ItemTags.WOODEN_PRESSURE_PLATES, 1, 2)
        wood(ItemTags.WOODEN_SLABS, 2, 1)
        wood(ItemTags.WOODEN_STAIRS, 4, 3)
        wood(ItemTags.WOODEN_TRAPDOORS, 1, 3)
        wood(Tags.Items.BARRELS_WOODEN, 1, 7)
        wood(Tags.Items.CHESTS_WOODEN, 1, 8)
        wood(Tags.Items.FENCE_GATES_WOODEN, 1, 4)
        wood(Tags.Items.FENCES_WOODEN, 1, 5)
        wood(Tags.Items.RODS_WOODEN, 2, 1)
    }

    @JvmStatic
    private fun foods() {
        // Wheat Flour
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.CROPS_WHEAT),
                itemResult.create(HCItems.WHEAT_FLOUR, HiiragiCoreTags.Items.FLOURS_WHEAT),
            ).save(output)

        // Pulped Fish
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.FOODS_RAW_FISH),
                itemResult.create(HCItems.PULPED_FISH),
            ).save(output)
        // Pulped Seed
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.SEEDS),
                itemResult.create(HCItems.PULPED_SEED),
            ).save(output)
    }
}
