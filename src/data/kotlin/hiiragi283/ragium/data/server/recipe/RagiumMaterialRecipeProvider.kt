package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterial
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumMaterialRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow8()
            .define('A', HTTagPrefixes.RAW_MATERIAL, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefixes.INGOT, VanillaMaterials.COPPER)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', HTTagPrefixes.DUST, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefixes.INGOT, VanillaMaterials.COPPER)
            .saveSuffixed(output, "_alt")

        HTCookingRecipeBuilder
            .smelting(RagiumItems.Ingots.RAGI_ALLOY)
            .addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCookingRecipeBuilder
            .blasting(RagiumItems.Ingots.RAGI_ALLOY)
            .addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")
        // Advanced Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .cross8()
            .define('A', HTTagPrefixes.DUST, VanillaMaterials.GLOWSTONE)
            .define('B', HTTagPrefixes.DUST, RagiumMaterials.RAGINITE)
            .define('C', HTTagPrefixes.INGOT, VanillaMaterials.GOLD)
            .save(output)

        HTCookingRecipeBuilder
            .smelting(RagiumItems.Ingots.ADVANCED_RAGI_ALLOY)
            .addIngredient(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCookingRecipeBuilder
            .blasting(RagiumItems.Ingots.ADVANCED_RAGI_ALLOY)
            .addIngredient(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.RawResources.RAGI_CRYSTAL)
            .hollow8()
            .define('A', HTTagPrefixes.DUST, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefixes.GEM, VanillaMaterials.DIAMOND)
            .save(output)
        // Ragium
        HTShapelessRecipeBuilder(RagiumItems.CHIPPED_RAGIUM_ESSENCE, 16)
            .addIngredient(RagiumItems.RAGIUM_ESSENCE)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_CHERRY)
            .hollow8()
            .define('A', RagiumItems.CHIPPED_RAGIUM_ESSENCE)
            .define('B', Tags.Items.FOODS_FRUIT)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.RAGI_COKE)
            .addIngredient(ItemTags.COALS)
            .addIngredient(RagiumItems.CHIPPED_RAGIUM_ESSENCE)
            .addIngredient(RagiumItems.CHIPPED_RAGIUM_ESSENCE)
            .save(output)

        // Azure Steel
        HTShapedRecipeBuilder(RagiumItems.AZURE_STEEL_COMPOUND)
            .cross4()
            .define('A', HTTagPrefixes.DUST, VanillaMaterials.LAPIS)
            .define('B', HTTagPrefixes.DUST, VanillaMaterials.AMETHYST)
            .define('C', HTTagPrefixes.INGOT, VanillaMaterials.IRON)
            .save(output)

        HTCookingRecipeBuilder
            .smelting(RagiumItems.Ingots.AZURE_STEEL)
            .addIngredient(RagiumItems.AZURE_STEEL_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCookingRecipeBuilder
            .blasting(RagiumItems.Ingots.AZURE_STEEL)
            .addIngredient(RagiumItems.AZURE_STEEL_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        // Sawdust
        HTShapedRecipeBuilder(RagiumItems.COMPRESSED_SAWDUST)
            .hollow8()
            .define('A', HTTagPrefixes.DUST, VanillaMaterials.WOOD)
            .define('B', RagiumItems.SAWDUST)
            .save(output)

        HTCookingRecipeBuilder
            .smelting(Items.CHARCOAL)
            .addIngredient(RagiumItems.COMPRESSED_SAWDUST)
            .setExp(0.15f)
            .saveSuffixed(output, "_from_pellet")

        registerPatterns()
    }

    private fun registerPatterns() {
        // Ingot/Gem -> Block
        for (block: RagiumBlocks.StorageBlocks in RagiumBlocks.StorageBlocks.entries) {
            val baseItem: HTMaterialItemLike = block.baseItem
            HTShapedRecipeBuilder(block)
                .hollow8()
                .define('A', baseItem.prefix, block)
                .define('B', baseItem)
                .saveSuffixed(output, "_from_base")
        }
        // Block -> Ingot
        for (ingot: RagiumItems.Ingots in RagiumItems.Ingots.entries) {
            HTShapelessRecipeBuilder(ingot, 9)
                .addIngredient(HTTagPrefixes.STORAGE_BLOCK, ingot)
                .saveSuffixed(output, "_from_block")
        }
        // Block -> Gem
        for (gem: RagiumItems.RawResources in RagiumItems.RawResources.entries) {
            if (gem.prefix != HTTagPrefixes.GEM) continue
            HTShapelessRecipeBuilder(gem, 9)
                .addIngredient(HTTagPrefixes.STORAGE_BLOCK, gem)
                .saveSuffixed(output, "_from_block")
        }

        // Gem/Ingot/Raw -> Dust
        // Tier 1 の素材だけハンマーで粉砕可能
        for (dust: RagiumItems.Dusts in RagiumItems.Dusts.entries) {
            val inputPrefix: HTTagPrefix = when (dust) {
                RagiumItems.Dusts.COAL -> HTTagPrefixes.GEM
                RagiumItems.Dusts.COPPER -> HTTagPrefixes.INGOT
                RagiumItems.Dusts.IRON -> HTTagPrefixes.INGOT
                RagiumItems.Dusts.LAPIS -> HTTagPrefixes.GEM
                RagiumItems.Dusts.QUARTZ -> continue
                RagiumItems.Dusts.GOLD -> continue
                RagiumItems.Dusts.DIAMOND -> continue
                RagiumItems.Dusts.EMERALD -> continue
                RagiumItems.Dusts.AMETHYST -> HTTagPrefixes.GEM
                RagiumItems.Dusts.ENDER_PEARL -> HTTagPrefixes.GEM
                RagiumItems.Dusts.OBSIDIAN -> continue
                RagiumItems.Dusts.RAGINITE -> HTTagPrefixes.RAW_MATERIAL
                RagiumItems.Dusts.RAGI_ALLOY -> HTTagPrefixes.INGOT
                RagiumItems.Dusts.ADVANCED_RAGI_ALLOY -> continue
                RagiumItems.Dusts.RAGI_CRYSTAL -> continue
                RagiumItems.Dusts.AZURE_STEEL -> HTTagPrefixes.INGOT
                RagiumItems.Dusts.DEEP_STEEL -> continue
                RagiumItems.Dusts.ASH -> continue
                RagiumItems.Dusts.SALTPETER -> HTTagPrefixes.RAW_MATERIAL
                RagiumItems.Dusts.SULFUR -> HTTagPrefixes.RAW_MATERIAL
            }

            HTShapelessRecipeBuilder(dust)
                .addIngredient(inputPrefix, dust)
                .addIngredient(RagiumItemTags.TOOLS_FORGE_HAMMER)
                .saveSuffixed(output, "_with_hammer")
        }

        // Dust -> Ingot
        fun dustToIngot(material: HTMaterial, ingot: ItemLike) {
            val dust: RagiumItems.Dusts =
                RagiumItems.Dusts.entries.firstOrNull { dustIn: RagiumItems.Dusts -> dustIn == material } ?: return
            HTCookingRecipeBuilder
                .smelting(ingot)
                .addIngredient(dust)
                .setExp(0.7f)
                .saveSuffixed(output, "_from_dust")
        }

        dustToIngot(VanillaMaterials.IRON, Items.IRON_INGOT)
        dustToIngot(VanillaMaterials.GOLD, Items.GOLD_INGOT)
        dustToIngot(VanillaMaterials.COPPER, Items.COPPER_INGOT)
        dustToIngot(VanillaMaterials.NETHERITE, Items.NETHERITE_INGOT)

        for (ingot: RagiumItems.Ingots in RagiumItems.Ingots.entries) {
            dustToIngot(ingot, ingot)
        }

        oreToRaw()
    }

    // Ore -> Raw/Gem
    private fun oreToRaw() {
        // Coal
        createCrushing()
            .itemOutput(Items.COAL, 2)
            .itemOutput(RagiumItems.Dusts.SULFUR)
            .itemInput(HTTagPrefixes.ORE, VanillaMaterials.COAL)
            .saveSuffixed(output, "_from_ore")
        // Copper
        createCrushing()
            .itemOutput(Items.RAW_COPPER, 4)
            .itemOutput(Items.GOLD_NUGGET, 3)
            .itemInput(HTTagPrefixes.ORE, VanillaMaterials.COPPER)
            .saveSuffixed(output, "_from_ore")
        // Iron
        createCrushing()
            .itemOutput(Items.RAW_IRON, 2)
            .itemOutput(Items.FLINT)
            .itemInput(HTTagPrefixes.ORE, VanillaMaterials.IRON)
            .saveSuffixed(output, "_from_ore")
        // Gold
        createCrushing()
            .itemOutput(Items.RAW_GOLD, 2)
            .itemInput(HTTagPrefixes.ORE, VanillaMaterials.GOLD)
            .saveSuffixed(output, "_from_ore")
        // Redstone
        createCrushing()
            .itemOutput(Items.REDSTONE, 12)
            .itemInput(HTTagPrefixes.ORE, VanillaMaterials.REDSTONE)
            .saveSuffixed(output, "_from_ore")
        // Lapis
        createCrushing()
            .itemOutput(Items.LAPIS_LAZULI, 8)
            .itemInput(HTTagPrefixes.ORE, VanillaMaterials.LAPIS)
            .saveSuffixed(output, "_from_ore")
        // Quartz
        createCrushing()
            .itemOutput(Items.QUARTZ, 4)
            .itemInput(HTTagPrefixes.ORE, VanillaMaterials.QUARTZ)
            .saveSuffixed(output, "_from_ore")
        // Diamond
        createCrushing()
            .itemOutput(Items.DIAMOND, 2)
            .itemInput(HTTagPrefixes.ORE, VanillaMaterials.DIAMOND)
            .saveSuffixed(output, "_from_ore")
        // Emerald
        createCrushing()
            .itemOutput(Items.EMERALD, 2)
            .itemInput(HTTagPrefixes.ORE, VanillaMaterials.EMERALD)
            .saveSuffixed(output, "_from_ore")
        // Netherite
        createCrushing()
            .itemOutput(Items.NETHERITE_SCRAP, 2)
            .itemInput(Tags.Items.ORES_NETHERITE_SCRAP)
            .saveSuffixed(output, "_from_ore")
    }
}
