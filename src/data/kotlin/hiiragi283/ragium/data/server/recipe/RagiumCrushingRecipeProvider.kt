package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTExtraProcessingRecipeBuilder
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCrushingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        ores()
        stones()
    }

    @JvmStatic
    private fun ores() {
        // Coal
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Fuels.COAL),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.COAL, 2),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Minerals.SULFUR, 3))
            .setExp(0.3f)
            .saveSuffixed(output, "_from_ore")

        // Redstone
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Minerals.REDSTONE),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.REDSTONE, 8),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.CINNABAR))
            .setExp(0.5f)
            .saveSuffixed(output, "_from_ore")
        // Cinnabar
        // Salt
        // Saltpeter
        // Sulfur
        // Lapis
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Gems.LAPIS),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Gems.LAPIS, 12),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SALTPETER))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Quartz
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Gems.QUARTZ),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Gems.QUARTZ, 6),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Minerals.SULFUR, 3))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Amethyst
        // Diamond
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Gems.DIAMOND),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Gems.DIAMOND, 2),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Fuels.COAL, 3))
            .setExp(1f)
            .saveSuffixed(output, "_from_ore")
        // Emerald
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Gems.EMERALD),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Gems.EMERALD, 2),
            ).setExp(1f)
            .saveSuffixed(output, "_from_ore")

        // Copper
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.COPPER),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.COPPER, 3),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Metals.GOLD, 3))
            .setExp(0.3f)
            .saveSuffixed(output, "_from_ore")

        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.COPPER),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.COPPER),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Metals.GOLD))
            .setExp(0.3f)
            .saveSuffixed(output, "_from_raw")
        // Iron
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.IRON),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.IRON, 2),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HTMaterialKey.of("tin"), 3))
            .setExp(0.5f)
            .saveSuffixed(output, "_from_ore")

        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.IRON),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.IRON),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HTMaterialKey.of("tin")))
            .setExp(0.5f)
            .saveSuffixed(output, "_from_raw")
        // Gold
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.GOLD),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.GOLD, 2),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Metals.COPPER, 3))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")

        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.GOLD),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.GOLD),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Metals.COPPER))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_raw")
        // Silver
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.ORE, HCMaterial.Metals.SILVER),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.SILVER, 2),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HTMaterialKey.of("lead"), 3))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")

        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.SILVER),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Metals.SILVER),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HTMaterialKey.of("lead")))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_raw")

        // Netherite
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.ORES_NETHERITE_SCRAP),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.NETHERITE, 2),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Alloys.NETHERITE, 3))
            .setExp(1f)
            .saveSuffixed(output, "_from_ore")
        // Deep Steel
    }

    @JvmStatic
    private fun stones() {
        // Gravel -> Sand + Flint
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.GRAVELS, 3),
                itemResult.create(Items.SAND, 2),
            ).setExtra(itemResult.create(Items.FLINT))
            .saveSuffixed(output, "_from_gravel")

        // Sandstone -> Sand + Saltpeter
        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                itemResult.create(Items.SAND, 2),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Minerals.SALTPETER, 3))
            .saveSuffixed(output, "_from_sandstone")

        HTExtraProcessingRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_RED_BLOCKS),
                itemResult.create(Items.RED_SAND, 2),
            ).setExtra(RagiumMaterialResultHelper.item(HCMaterialPrefixes.TINY_DUST, HCMaterial.Minerals.SALTPETER, 3))
            .saveSuffixed(output, "_from_sandstone")
    }
}
