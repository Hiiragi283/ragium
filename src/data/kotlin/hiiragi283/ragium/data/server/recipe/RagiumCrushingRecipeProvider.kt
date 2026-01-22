package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
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
    }

    @JvmStatic
    private fun ores() {
        // Coal
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.COAL),
                itemResult.create(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, 2),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR), 0.25f)
            .setExp(0.3f)
            .saveSuffixed(output, "_from_ore")

        // Redstone
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.REDSTONE),
                itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE, 8),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, CommonMaterialKeys.CINNABAR))
            .setExp(0.5f)
            .saveSuffixed(output, "_from_ore")
        // Cinnabar
        // Salt
        // Saltpeter
        // Sulfur
        // Lapis
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.LAPIS),
                itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.LAPIS, 12),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER))
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Quartz
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.QUARTZ),
                itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.QUARTZ, 6),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR), 0.25f)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Amethyst
        // Diamond
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.DIAMOND),
                itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.DIAMOND, 2),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL), 0.25f)
            .setExp(1f)
            .saveSuffixed(output, "_from_ore")
        // Emerald
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.EMERALD),
                itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.EMERALD, 2),
            ).setExp(1f)
            .saveSuffixed(output, "_from_ore")

        // Copper
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.COPPER),
                itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COPPER, 3),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.GOLD), 0.25f)
            .setExp(0.3f)
            .saveSuffixed(output, "_from_ore")
        // Iron
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.IRON),
                itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON, 2),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, CommonMaterialKeys.TIN), 0.25f)
            .setExp(0.5f)
            .saveSuffixed(output, "_from_ore")
        // Gold
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, VanillaMaterialKeys.GOLD),
                itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.GOLD, 2),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COPPER), 0.25f)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_ore")
        // Silver
        // Netherite
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(Tags.Items.ORES_NETHERITE_SCRAP),
                itemResult.create(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2),
            ).setExp(1f)
            .saveSuffixed(output, "_from_ore")
        // Deep Steel

        // Raginite
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, RagiumMaterialKeys.RAGINITE),
                itemResult.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 8),
            ).setExp(0.5f)
            .saveSuffixed(output, "_from_ore")
        // Ragi-Crystal
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(CommonTagPrefixes.ORE, RagiumMaterialKeys.RAGI_CRYSTAL),
                itemResult.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL, 2),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE), 0.25f)
            .setExp(1f)
            .saveSuffixed(output, "_from_ore")
    }

    @JvmStatic
    private fun stones() {
        // Stone -> Cobblestone
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(Items.STONE),
                itemResult.create(Items.COBBLESTONE),
            ).saveSuffixed(output, "_from_stone")
        // Cobblestone -> Gravel
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(Items.COBBLESTONE),
                itemResult.create(Items.GRAVEL),
            ).saveSuffixed(output, "_from_cobblestone")
        // Gravel -> Sand
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(Tags.Items.GRAVELS),
                itemResult.create(Items.SAND),
            ).saveSuffixed(output, "_from_gravel")
        // Sandstone -> Sand + Saltpeter
        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                itemResult.create(Items.SAND, 2),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER), 0.25f)
            .saveSuffixed(output, "_from_sandstone")

        HTChancedRecipeBuilder
            .crushing(
                inputCreator.create(Tags.Items.SANDSTONE_RED_BLOCKS),
                itemResult.create(Items.RED_SAND, 2),
            ).addResult(itemResult.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER), 0.25f)
            .saveSuffixed(output, "_from_sandstone")
    }

    @JvmStatic
    private fun planks() {
        // Wood Dust
        fun wood(tagKey: TagKey<Item>, input: Int, output: Int) {
            HTChancedRecipeBuilder
                .crushing(
                    inputCreator.create(tagKey, input),
                    itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD, output),
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
}
