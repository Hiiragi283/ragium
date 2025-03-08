package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTAssemblerRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.extension.requires
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
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
        for ((key: HTMaterialKey, block: DeferredBlock<Block>) in RagiumBlocks.STORAGE_BLOCKS) {
            val parent: HTTagPrefix = RagiumBlocks.getStorageParent(key)
            val coreItem: DeferredItem<out Item> = RagiumItems.getMaterialMap(parent)[key] ?: continue
            HTShapedRecipeBuilder(block)
                .hollow8()
                .define('A', parent, key)
                .define('B', coreItem)
                .save(output)
        }
        // Block -> Ingot
        for ((material: HTMaterialKey, ingot: DeferredItem<out Item>) in RagiumItems.getMaterialMap(HTTagPrefix.INGOT)) {
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, ingot, 9)
                .requires(HTTagPrefix.BLOCK, material)
                .unlockedBy("has_ingot", has(HTTagPrefix.BLOCK, material))
                .savePrefixed(output)
        }
        // Block -> Gem
        for ((material: HTMaterialKey, gem: DeferredItem<out Item>) in RagiumItems.getMaterialMap(HTTagPrefix.GEM)) {
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, gem, 9)
                .requires(HTTagPrefix.BLOCK, material)
                .unlockedBy("has_gem", has(HTTagPrefix.BLOCK, material))
                .savePrefixed(output)
        }

        // Ingot/Gem -> Gear
        for ((material: HTMaterialKey, gear: DeferredItem<out Item>) in RagiumItems.getMaterialMap(HTTagPrefix.GEAR)) {
            val parentPrefix: HTTagPrefix = material
                .getType()
                .getMainPrefix() ?: continue
            // Shaped Recipe
            HTShapedRecipeBuilder(gear)
                .hollow4()
                .define('A', parentPrefix, material)
                .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
                .save(output)
        }

        // Ingot -> Coil
        for ((material: HTMaterialKey, coil: DeferredItem<out Item>) in RagiumItems.getMaterialMap(HTTagPrefix.COIL)) {
            // Shaped Crafting
            HTShapedRecipeBuilder(coil, 2)
                .hollow4()
                .define('A', HTTagPrefix.INGOT, material)
                .define('B', HTTagPrefix.ROD, CommonMaterials.STEEL)
                .save(output)
            // Assembler
            HTAssemblerRecipeBuilder(lookup)
                .itemInput(HTTagPrefix.INGOT, material, 4)
                .itemInput(HTTagPrefix.ROD, CommonMaterials.STEEL)
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
