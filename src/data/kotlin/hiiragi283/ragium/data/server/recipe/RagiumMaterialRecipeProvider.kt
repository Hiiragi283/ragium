package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object RagiumMaterialRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
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
        // Advanced Ragi-Alloy
        HTShapelessRecipeBuilder(RagiumItems.Dusts.ADVANCED_RAGI_ALLOY)
            .addIngredient(HTTagPrefixes.DUST, RagiumMaterials.RAGINITE)
            .addIngredient(HTTagPrefixes.DUST, RagiumMaterials.RAGINITE)
            .addIngredient(HTTagPrefixes.DUST, RagiumMaterials.RAGINITE)
            .addIngredient(HTTagPrefixes.DUST, VanillaMaterials.GOLD)
            .save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.RawResources.RAGI_CRYSTAL)
            .hollow8()
            .define('A', HTTagPrefixes.DUST, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefixes.GEM, VanillaMaterials.DIAMOND)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.StorageBlocks.RAGI_CRYSTAL)
            .hollow8()
            // .define('A', HTTagPrefixes.RAW_STORAGE, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefixes.STORAGE_BLOCK, VanillaMaterials.DIAMOND)

        // Azure Steel
        HTShapelessRecipeBuilder(RagiumItems.Dusts.AZURE_STEEL, 2)
            .addIngredient(HTTagPrefixes.DUST, VanillaMaterials.LAPIS)
            .addIngredient(HTTagPrefixes.DUST, VanillaMaterials.AMETHYST)
            .addIngredient(HTTagPrefixes.DUST, VanillaMaterials.IRON)
            .addIngredient(HTTagPrefixes.DUST, VanillaMaterials.IRON)
            .save(output)
        // Deep Steel

        registerPatterns(output)
    }

    private fun registerPatterns(output: RecipeOutput) {
        // Ingot/Gem -> Block
        for (block: RagiumBlocks.StorageBlocks in RagiumBlocks.StorageBlocks.entries) {
            val baseItem: HTMaterialItemLike = block.baseItem
            HTShapedRecipeBuilder(block)
                .hollow8()
                .define('A', baseItem.prefix, block.key)
                .define('B', baseItem)
                .saveSuffixed(output, "_from_base")
        }
        // Block -> Ingot
        for (ingot: RagiumItems.Ingots in RagiumItems.Ingots.entries) {
            HTShapelessRecipeBuilder(ingot, 9)
                .addIngredient(HTTagPrefixes.STORAGE_BLOCK, ingot.key)
                .saveSuffixed(output, "_from_block")
        }
        // Block -> Gem
        for (gem: RagiumItems.RawResources in RagiumItems.RawResources.entries) {
            if (gem.prefix != HTTagPrefixes.GEM) continue
            HTShapelessRecipeBuilder(gem, 9)
                .addIngredient(HTTagPrefixes.STORAGE_BLOCK, gem.key)
                .saveSuffixed(output, "_from_block")
        }

        // Gem/Ingot/Raw -> Dust
        // Tier 1 の素材だけハンマーで粉砕可能
        for (dust: RagiumItems.Dusts in RagiumItems.Dusts.entries) {
            val inputPrefix: HTTagPrefix = when (dust) {
                RagiumItems.Dusts.WOOD -> continue
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
                .addIngredient(inputPrefix, dust.key)
                .addIngredient(RagiumItemTags.TOOLS_FORGE_HAMMER)
                .saveSuffixed(output, "_with_hammer")
        }

        // Dust -> Ingot
        fun dustToIngot(key: HTMaterialKey, ingot: ItemLike) {
            val dust: RagiumItems.Dusts =
                RagiumItems.Dusts.entries.firstOrNull { dustIn: RagiumItems.Dusts -> dustIn.key == key } ?: return
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
            dustToIngot(ingot.key, ingot)
        }
    }
}
