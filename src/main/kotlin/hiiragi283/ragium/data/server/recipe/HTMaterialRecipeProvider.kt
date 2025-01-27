package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.define
import hiiragi283.ragium.data.requires
import hiiragi283.ragium.data.savePrefixed
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder

object HTMaterialRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Ingot/Gem -> Block
        RagiumBlocks.StorageBlocks.entries.forEach { storage: RagiumBlocks.StorageBlocks ->
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, storage)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', storage.parentPrefix, storage.material)
                .unlockedBy("has_input", has(storage.parentPrefix, storage.material))
                .savePrefixed(output)
        }
        // Block -> Ingot
        RagiumItems.Ingots.entries.forEach { ingot: RagiumItems.Ingots ->
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, ingot, 9)
                .requires(HTTagPrefix.STORAGE_BLOCK, ingot.material)
                .unlockedBy("has_ingot", has(HTTagPrefix.STORAGE_BLOCK, ingot.material))
                .savePrefixed(output)
        }
        // Block -> Gem
        RagiumItems.RawResources.entries.forEach { resource: RagiumItems.RawResources ->
            if (resource.tagPrefix != HTTagPrefix.GEM) return@forEach
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, resource, 9)
                .requires(HTTagPrefix.STORAGE_BLOCK, resource.material)
                .unlockedBy("has_gem", has(HTTagPrefix.STORAGE_BLOCK, resource.material))
                .savePrefixed(output)
        }

        // Ingot/Gem -> Gear
        RagiumItems.Gears.entries.forEach { gear: RagiumItems.Gears ->
            val material: HTMaterialKey = gear.material
            val parentPrefix: HTTagPrefix = gear.parentPrefix
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
        RagiumItems.Rods.entries.forEach { rod: RagiumItems.Rods ->
            val material: HTMaterialKey = rod.material
            val parentPrefix: HTTagPrefix = rod.parentPrefix
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
