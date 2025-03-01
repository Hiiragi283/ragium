package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.extension.requires
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

object HTMaterialRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Ingot/Gem -> Block
        RagiumBlocks.STORAGE_BLOCKS.forEach { (key: HTMaterialKey, block: DeferredBlock<Block>) ->
            val parent: HTTagPrefix = RagiumBlocks.getStorageParent(key)
            val coreItem: DeferredItem<out Item> = RagiumItems.getMaterialMap(parent)[key] ?: return@forEach
            HTShapedRecipeBuilder(block)
                .hollow8()
                .define('A', parent, key)
                .define('B', coreItem)
                .save(output)
        }
        // Block -> Ingot
        RagiumItems
            .getMaterialMap(HTTagPrefix.INGOT)
            .forEach { (material: HTMaterialKey, ingot: DeferredItem<out Item>) ->
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, ingot, 9)
                    .requires(HTTagPrefix.BLOCK, material)
                    .unlockedBy("has_ingot", has(HTTagPrefix.BLOCK, material))
                    .savePrefixed(output)
            }
        // Block -> Gem
        RagiumItems.getMaterialMap(HTTagPrefix.GEM).forEach { (material: HTMaterialKey, gem: DeferredItem<out Item>) ->
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, gem, 9)
                .requires(HTTagPrefix.BLOCK, material)
                .unlockedBy("has_gem", has(HTTagPrefix.BLOCK, material))
                .savePrefixed(output)
        }

        // Ingot/Gem -> Gear
        RagiumItems
            .getMaterialMap(HTTagPrefix.GEAR)
            .forEach { (material: HTMaterialKey, gear: DeferredItem<out Item>) ->
                val parentPrefix: HTTagPrefix = material
                    .getType()
                    .getMainPrefix() ?: return@forEach
                // Shaped Recipe
                HTShapedRecipeBuilder(gear)
                    .hollow4()
                    .define('A', parentPrefix, material)
                    .define('B', RagiumItems.FORGE_HAMMER)
                    .save(output)
            }

        // Ingot -> Coil
        RagiumItems.getMaterialMap(HTTagPrefix.COIL).forEach { (material: HTMaterialKey, coil: DeferredItem<out Item>) ->
            // Shaped Crafting
            HTShapedRecipeBuilder(coil, 2)
                .pattern(" A ")
                .pattern("BCB")
                .pattern("BCB")
                .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
                .define('B', HTTagPrefix.INGOT, material)
                .define('C', RagiumBlocks.SHAFT)
                .save(output)
            // Assembler
            HTMultiItemRecipeBuilder
                .assembler(lookup)
                .itemInput(HTTagPrefix.INGOT, material, 4)
                .itemInput(RagiumBlocks.SHAFT)
                .itemOutput(coil, 4)
                .save(output)
        }

        // Dust -> Ingot
        fun dustToIngot(key: HTMaterialKey, ingot: ItemLike) {
            val dust: DeferredItem<out Item> = RagiumItems.getMaterialMap(HTTagPrefix.DUST)[key] ?: return
            HTCookingRecipeBuilder
                .create(
                    Ingredient.of(dust),
                    ingot,
                    exp = 0.7f,
                    types = HTCookingRecipeBuilder.BLASTING_TYPES,
                ).save(output)
        }

        dustToIngot(VanillaMaterials.IRON, Items.IRON_INGOT)
        dustToIngot(VanillaMaterials.GOLD, Items.GOLD_INGOT)
        dustToIngot(VanillaMaterials.COPPER, Items.COPPER_INGOT)
        dustToIngot(VanillaMaterials.NETHERITE, Items.NETHERITE_INGOT)

        RagiumItems.getMaterialMap(HTTagPrefix.INGOT).forEach(::dustToIngot)
    }
}
