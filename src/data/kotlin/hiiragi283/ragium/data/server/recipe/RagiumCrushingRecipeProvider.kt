package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
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
    }

    @JvmStatic
    private fun ores() {
        // Coal
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Fuels.COAL),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.COAL, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Minerals.SULFUR, 3))
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
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Minerals.SULFUR, 3))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Amethyst
        // Diamond
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Gems.DIAMOND),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Gems.DIAMOND, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Fuels.COAL, 3))
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
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Metals.GOLD, 3))
            .setExp(0.3f)
            .saveSuffixed(output, "_from_ore")

        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.COPPER),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.COPPER),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Metals.GOLD))
            .setExp(0.3f)
            .saveSuffixed(output, "_from_raw")
        // Iron
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.IRON),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.IRON, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HTMaterialKey.of("tin"), 3))
            .setExp(0.5f)
            .saveSuffixed(output, "_from_ore")

        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.IRON),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.IRON),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HTMaterialKey.of("tin")))
            .setExp(0.5f)
            .saveSuffixed(output, "_from_raw")
        // Gold
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.GOLD),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.GOLD, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Metals.COPPER, 3))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")

        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.GOLD),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.GOLD),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Metals.COPPER))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_raw")
        // Silver
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.SILVER),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.SILVER, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HTMaterialKey.of("lead"), 3))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")

        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.SILVER),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.SILVER),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HTMaterialKey.of("lead")))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_raw")

        // Netherite
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.ORES_NETHERITE_SCRAP),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.NETHERITE, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Alloys.NETHERITE, 3))
            .setExp(1f)
            .saveSuffixed(output, "_from_ore")
        // Deep Steel
    }

    @JvmStatic
    private fun stones() {
        // Gravel -> Sand + Flint
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.GRAVELS, 3),
                itemResult.create(Items.SAND, 2),
            ).addResult(itemResult.create(Items.FLINT))
            .saveSuffixed(output, "_from_gravel")

        // Sandstone -> Sand + Saltpeter
        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                itemResult.create(Items.SAND, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Minerals.SALTPETER, 3))
            .saveSuffixed(output, "_from_sandstone")

        HTChancedRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_RED_BLOCKS),
                itemResult.create(Items.RED_SAND, 2),
            ).addResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Minerals.SALTPETER, 3))
            .saveSuffixed(output, "_from_sandstone")
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
}
