package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.define
import hiiragi283.ragium.api.extension.requires
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

object HTMaterialRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Ingot/Gem -> Block
        RagiumBlocks.STORAGE_BLOCKS.forEach { (key: HTMaterialKey, block: DeferredBlock<Block>) ->
            val parent: HTTagPrefix = RagiumBlocks.getStorageParent(key)
            val coreItem: DeferredItem<out Item> = RagiumItems.getMaterialMap(parent)[key] ?: return@forEach
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, block)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', parent, key)
                .define('B', coreItem)
                .unlockedBy("has_input", has(parent, key))
                .savePrefixed(output)
        }
        // Block -> Ingot
        RagiumItems
            .getMaterialMap(HTTagPrefix.INGOT)
            .forEach { (material: HTMaterialKey, ingot: DeferredItem<out Item>) ->
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, ingot, 9)
                    .requires(HTTagPrefix.STORAGE_BLOCK, material)
                    .unlockedBy("has_ingot", has(HTTagPrefix.STORAGE_BLOCK, material))
                    .savePrefixed(output)
            }
        // Block -> Gem
        RagiumItems.getMaterialMap(HTTagPrefix.GEM).forEach { (material: HTMaterialKey, gem: DeferredItem<out Item>) ->
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, gem, 9)
                .requires(HTTagPrefix.STORAGE_BLOCK, material)
                .unlockedBy("has_gem", has(HTTagPrefix.STORAGE_BLOCK, material))
                .savePrefixed(output)
        }

        // Ingot/Gem -> Gear
        RagiumItems
            .getMaterialMap(HTTagPrefix.GEAR)
            .forEach { (material: HTMaterialKey, gear: DeferredItem<out Item>) ->
                val parentPrefix: HTTagPrefix = RagiumAPI
                    .getInstance()
                    .getMaterialRegistry()
                    .getType(material)
                    .getMainPrefix() ?: return@forEach
                // Shaped Recipe
                ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, gear)
                    .pattern(" A ")
                    .pattern("ABA")
                    .pattern(" A ")
                    .define('A', parentPrefix, material)
                    .define('B', RagiumItems.FORGE_HAMMER)
                    .unlockedBy("has_input", has(parentPrefix, material))
                    .savePrefixed(output)
            }

        // Ingot/Gem -> Rod
        RagiumItems.getMaterialMap(HTTagPrefix.ROD).forEach { (material: HTMaterialKey, rod: DeferredItem<out Item>) ->
            val parentPrefix: HTTagPrefix = RagiumAPI
                .getInstance()
                .getMaterialRegistry()
                .getType(material)
                .getMainPrefix() ?: return@forEach
            // Shaped Recipe
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, rod, 2)
                .pattern("AB")
                .pattern("A ")
                .define('A', parentPrefix, material)
                .define('B', RagiumItems.FORGE_HAMMER)
                .unlockedBy("has_input", has(parentPrefix, material))
                .savePrefixed(output)
        }
    }
}
