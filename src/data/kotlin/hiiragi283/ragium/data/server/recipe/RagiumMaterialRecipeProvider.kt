package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
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
            .define('A', HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .saveSuffixed(output, "_alt")

        HTCookingRecipeBuilder
            .smelting(RagiumItems.Ingots.RAGI_ALLOY)
            .addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTShapelessRecipeBuilder(RagiumItems.Dusts.RAGI_ALLOY)
            .addIngredient(HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .addIngredient(HTTagPrefix.DUST, VanillaMaterials.COPPER)
            .save(output)
        // Advanced Ragi-Alloy
        HTShapelessRecipeBuilder(RagiumItems.Dusts.ADVANCED_RAGI_ALLOY)
            .addIngredient(HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .addIngredient(HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .addIngredient(HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .addIngredient(HTTagPrefix.DUST, VanillaMaterials.GOLD)
            .save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.RawResources.RAGI_CRYSTAL)
            .hollow8()
            .define('A', HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefix.GEM, VanillaMaterials.DIAMOND)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.StorageBlocks.RAGI_CRYSTAL)
            .hollow8()
            .define('A', HTTagPrefix.RAW_STORAGE, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.DIAMOND)

        // Azure Steel
        HTShapelessRecipeBuilder(RagiumItems.Dusts.AZURE_STEEL, 2)
            .addIngredient(HTTagPrefix.DUST, VanillaMaterials.LAPIS)
            .addIngredient(HTTagPrefix.DUST, VanillaMaterials.AMETHYST)
            .addIngredient(HTTagPrefix.DUST, VanillaMaterials.IRON)
            .addIngredient(HTTagPrefix.DUST, VanillaMaterials.IRON)
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
                .addIngredient(HTTagPrefix.STORAGE_BLOCK, ingot.key)
                .saveSuffixed(output, "_from_block")
        }
        // Block -> Gem
        for (gem: RagiumItems.RawResources in RagiumItems.RawResources.entries) {
            if (gem.prefix != HTTagPrefix.GEM) continue
            HTShapelessRecipeBuilder(gem, 9)
                .addIngredient(HTTagPrefix.STORAGE_BLOCK, gem.key)
                .saveSuffixed(output, "_from_block")
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
