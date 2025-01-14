package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.define
import hiiragi283.ragium.data.requires
import hiiragi283.ragium.data.savePrefixed
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(output, registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        materialRecipes(recipeOutput)

        storageRecipes(recipeOutput)
    }

    private fun RecipeProvider.has(prefix: HTTagPrefix, material: HTMaterialKey): Criterion<InventoryChangeTrigger.TriggerInstance> =
        has(prefix.createTag(material))

    private fun materialRecipes(output: RecipeOutput) {
        // Ingot/Gem -> Block
        RagiumBlocks.StorageBlocks.entries.forEach { storage: RagiumBlocks.StorageBlocks ->
            val material: HTMaterialKey = storage.material
            val mainPrefix: HTTagPrefix = material.getEntryOrNull()?.type?.getMainPrefix() ?: return@forEach

            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, storage)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', mainPrefix, material)
                .unlockedBy("has_input", has(mainPrefix, material))
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
        RagiumItems.Gems.entries.forEach { ingot: RagiumItems.Gems ->
            ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, ingot, 9)
                .requires(HTTagPrefix.STORAGE_BLOCK, ingot.material)
                .unlockedBy("has_gem", has(HTTagPrefix.STORAGE_BLOCK, ingot.material))
                .savePrefixed(output)
        }

        // Ingot/Gem -> Gear
        RagiumItems.Gears.entries.forEach { gear: RagiumItems.Gears ->
            val material: HTMaterialKey = gear.material
            val mainPrefix: HTTagPrefix = material.getEntryOrNull()?.type?.getMainPrefix() ?: return@forEach

            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, gear)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', mainPrefix, material)
                .define('B', Tags.Items.NUGGETS_IRON)
                .unlockedBy("has_input", has(mainPrefix, material))
                .savePrefixed(output)
        }

        // Raw/Dust -> Ingot
        RagiumItems.Ingots.entries.forEach { ingot: RagiumItems.Ingots ->
            val material: HTMaterialKey = ingot.material
            val entry: HTMaterialRegistry.Entry = material.getEntryOrNull() ?: return@forEach

            if (HTMaterialPropertyKeys.DISABLE_DUST_SMELTING !in entry) {
                // Smelting
                SimpleCookingRecipeBuilder
                    .smelting(
                        Ingredient.of(HTTagPrefix.DUST.createTag(material)),
                        RecipeCategory.MISC,
                        ingot,
                        0.5f,
                        200,
                    ).unlockedBy("has_raw", has(HTTagPrefix.DUST, material))
                    .save(output, ingot.id.withPath { "smelting/${it}_from_dust" })
                // Blasting
                SimpleCookingRecipeBuilder
                    .blasting(
                        Ingredient.of(HTTagPrefix.DUST.createTag(material)),
                        RecipeCategory.MISC,
                        ingot,
                        0.5f,
                        100,
                    ).unlockedBy("has_raw", has(HTTagPrefix.DUST, material))
                    .save(output, ingot.id.withPath { "blasting/${it}_from_dust" })
            }

            if (HTMaterialPropertyKeys.DISABLE_RAW_SMELTING !in entry) {
                // Smelting
                SimpleCookingRecipeBuilder
                    .smelting(
                        Ingredient.of(HTTagPrefix.RAW_MATERIAL.createTag(material)),
                        RecipeCategory.MISC,
                        ingot,
                        0.5f,
                        200,
                    ).unlockedBy("has_raw", has(HTTagPrefix.RAW_MATERIAL, material))
                    .save(output, ingot.id.withPath { "smelting/${it}_from_raw" })
                // Blasting
                SimpleCookingRecipeBuilder
                    .blasting(
                        Ingredient.of(HTTagPrefix.RAW_MATERIAL.createTag(material)),
                        RecipeCategory.MISC,
                        ingot,
                        0.5f,
                        100,
                    ).unlockedBy("has_raw", has(HTTagPrefix.RAW_MATERIAL, material))
                    .save(output, ingot.id.withPath { "blasting/${it}_from_raw" })
            }
        }
    }

    private fun storageRecipes(output: RecipeOutput) {
        // Drum
        RagiumBlocks.Drums.entries.forEach { drum: RagiumBlocks.Drums ->
            // Shaped Crafting
            val mainIngot: TagKey<Item> = HTTagPrefix.INGOT.createTag(drum.tier.getMainMetal())
            ShapedRecipeBuilder
                .shaped(RecipeCategory.TRANSPORTATION, drum)
                .pattern("ABA")
                .pattern("ACA")
                .pattern("ABA")
                .define('A', HTTagPrefix.INGOT, drum.tier.getSubMetal())
                .define('B', mainIngot)
                .define('C', Items.BUCKET)
                .unlockedBy("has_ingot", has(mainIngot))
                .savePrefixed(output)
        }
    }
}
