package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.material.RagiumFoodMaterials
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.setup.RagiumItems
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
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Fuels.COAL),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.COAL, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SULFUR), 0.25f)
            .setExp(0.3f)
            .saveSuffixed(output, "_from_ore")

        // Redstone
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Minerals.REDSTONE),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.REDSTONE, 8),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.CINNABAR))
            .setExp(0.5f)
            .saveSuffixed(output, "_from_ore")
        // Cinnabar
        // Salt
        // Saltpeter
        // Sulfur
        // Lapis
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Gems.LAPIS),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Gems.LAPIS, 12),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SALTPETER))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Quartz
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Gems.QUARTZ),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Gems.QUARTZ, 6),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SULFUR), 0.25f)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Amethyst
        // Diamond
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Gems.DIAMOND),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Gems.DIAMOND, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Fuels.COAL), 0.25f)
            .setExp(1f)
            .saveSuffixed(output, "_from_ore")
        // Emerald
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Gems.EMERALD),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Gems.EMERALD, 2),
            ).setExp(1f)
            .saveSuffixed(output, "_from_ore")

        // Copper
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.COPPER),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.COPPER, 3),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.GOLD), 0.25f)
            .setExp(0.3f)
            .saveSuffixed(output, "_from_ore")
        // Iron
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.IRON),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.IRON, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HTMaterialKey.of("tin")), 0.25f)
            .setExp(0.5f)
            .saveSuffixed(output, "_from_ore")
        // Gold
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.GOLD),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.GOLD, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.COPPER), 0.25f)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Silver
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.SILVER),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.SILVER, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HTMaterialKey.of("lead")), 0.25f)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Netherite
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.ORES_NETHERITE_SCRAP),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.NETHERITE, 2),
            ).setExp(1f)
            .saveSuffixed(output, "_from_ore")
        // Deep Steel

        // Raginite
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, RagiumMaterial.RAGINITE),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, RagiumMaterial.RAGINITE, 8),
            ).setExp(0.5f)
            .saveSuffixed(output, "_from_ore")
        // Ragi-Crystal
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, RagiumMaterial.RAGI_CRYSTAL),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, RagiumMaterial.RAGI_CRYSTAL, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, RagiumMaterial.RAGINITE), 0.25f)
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
            ).setTime(20 * 5)
            .saveSuffixed(output, "_from_gravel")
        // Sandstone -> Sand + Saltpeter
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                itemResult.create(Items.SAND, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SALTPETER), 0.25f)
            .saveSuffixed(output, "_from_sandstone")

        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_RED_BLOCKS),
                itemResult.create(Items.RED_SAND, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SALTPETER), 0.25f)
            .saveSuffixed(output, "_from_sandstone")
        // Obsidian Dust
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.OBSIDIANS),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Obsidian),
            ).save(output)
    }

    @JvmStatic
    private fun planks() {
        // Wood Dust
        fun wood(tagKey: TagKey<Item>, input: Int, output: Int) {
            HTChancedRecipeBuilder
                .crushing(
                    itemCreator.fromTagKey(tagKey, input),
                    RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Wood, output),
                ).saveSuffixed(RagiumCrushingRecipeProvider.output, "_from_${tagKey.location().path}")
        }

        wood(ItemTags.BOATS, 1, 5)
        wood(ItemTags.LOGS_THAT_BURN, 1, 6)
        wood(ItemTags.PLANKS, 1, 1)
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
        // Minced Meat
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.FOODS_RAW_MEAT),
                itemResult.create(RagiumItems.MEAT_DUST, HCMaterialPrefixes.DUST, RagiumFoodMaterials.MEAT),
            ).saveSuffixed(output, "_from_raw")

        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.INGOT, RagiumFoodMaterials.MEAT),
                itemResult.create(RagiumItems.MEAT_DUST, HCMaterialPrefixes.DUST, RagiumFoodMaterials.MEAT),
            ).saveSuffixed(output, "_from_ingot")

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
