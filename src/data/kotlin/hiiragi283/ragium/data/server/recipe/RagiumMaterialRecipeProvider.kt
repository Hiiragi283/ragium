package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.data.server.RagiumMaterialFamilies
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumMaterialRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow8()
            .define('A', RagiumItemTags.RAW_MATERIALS_RAGINITE)
            .define('B', Tags.Items.INGOTS_COPPER)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', RagiumItemTags.DUSTS_RAGINITE)
            .define('B', Tags.Items.INGOTS_COPPER)
            .saveSuffixed(output, "_alt")

        HTCookingRecipeBuilder
            .smelting(RagiumItems.RAGI_ALLOY_INGOT)
            .addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCookingRecipeBuilder
            .blasting(RagiumItems.RAGI_ALLOY_INGOT)
            .addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTShapelessRecipeBuilder(RagiumItems.RAGINITE_DUST)
            .addIngredient(RagiumItemTags.RAW_MATERIALS_RAGINITE)
            .addIngredient(RagiumItemTags.TOOLS_FORGE_HAMMER)
            .saveSuffixed(output, "_with_hammer")
        // Advanced Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .cross8()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', RagiumItemTags.DUSTS_RAGINITE)
            .define('C', Tags.Items.INGOTS_GOLD)
            .save(output)

        HTCookingRecipeBuilder
            .smelting(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT)
            .addIngredient(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCookingRecipeBuilder
            .blasting(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT)
            .addIngredient(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.RAGI_CRYSTAL)
            .hollow8()
            .define('A', RagiumItemTags.DUSTS_RAGINITE)
            .define('B', Tags.Items.GEMS_DIAMOND)
            .save(output)
        // Ragium
        HTShapelessRecipeBuilder(RagiumItems.CHIPPED_RAGIUM_ESSENCE, 16)
            .addIngredient(RagiumItems.RAGIUM_ESSENCE)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.RAGI_COKE)
            .addIngredient(ItemTags.COALS)
            .addIngredient(RagiumItems.CHIPPED_RAGIUM_ESSENCE)
            .addIngredient(RagiumItems.CHIPPED_RAGIUM_ESSENCE)
            .save(output)

        // Azure Steel
        HTShapedRecipeBuilder(RagiumItems.AZURE_STEEL_COMPOUND)
            .hollow4()
            .define('A', RagiumItems.AZURE_SHARD)
            .define('B', Tags.Items.INGOTS_IRON)
            .save(output)

        HTCookingRecipeBuilder
            .smelting(RagiumItems.AZURE_STEEL_INGOT)
            .addIngredient(RagiumItems.AZURE_STEEL_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        HTCookingRecipeBuilder
            .blasting(RagiumItems.AZURE_STEEL_INGOT)
            .addIngredient(RagiumItems.AZURE_STEEL_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        // Sawdust
        HTShapedRecipeBuilder(RagiumItems.COMPRESSED_SAWDUST)
            .hollow8()
            .define('A', RagiumItemTags.DUSTS_WOOD)
            .define('B', RagiumItems.SAWDUST)
            .save(output)

        HTCookingRecipeBuilder
            .smelting(Items.CHARCOAL)
            .addIngredient(RagiumItems.COMPRESSED_SAWDUST)
            .setExp(0.15f)
            .saveSuffixed(output, "_from_pellet")

        oreToRaw()

        register(RagiumMaterialFamilies.RAGI_CRYSTAL)
        register(RagiumMaterialFamilies.CRIMSON_CRYSTAL)
        register(RagiumMaterialFamilies.WARPED_CRYSTAL)

        register(RagiumMaterialFamilies.RAGI_ALLOY)
        register(RagiumMaterialFamilies.ADVANCED_RAGI_ALLOY)
        register(RagiumMaterialFamilies.AZURE_STEEL)
        register(RagiumMaterialFamilies.DEEP_STEEL)

        register(RagiumMaterialFamilies.CHEESE)
        register(RagiumMaterialFamilies.CHOCOLATE)
    }

    // Ore -> Raw/Gem
    private fun oreToRaw() {
        // Coal
        createCrushing()
            .itemOutput(Items.COAL, 2)
            .itemOutput(RagiumItems.SULFUR_DUST)
            .itemInput(Tags.Items.ORES_COAL)
            .saveSuffixed(output, "_from_ore")
        // Copper
        createCrushing()
            .itemOutput(Items.RAW_COPPER, 4)
            .itemOutput(Items.GOLD_NUGGET, 3)
            .itemInput(Tags.Items.ORES_COPPER)
            .saveSuffixed(output, "_from_ore")
        // Iron
        createCrushing()
            .itemOutput(Items.RAW_IRON, 2)
            .itemOutput(Items.FLINT)
            .itemInput(Tags.Items.ORES_IRON)
            .saveSuffixed(output, "_from_ore")
        // Gold
        createCrushing()
            .itemOutput(Items.RAW_GOLD, 2)
            .itemInput(Tags.Items.ORES_GOLD)
            .saveSuffixed(output, "_from_ore")
        // Redstone
        createCrushing()
            .itemOutput(Items.REDSTONE, 12)
            .itemOutput(RagiumItems.RAGINITE_DUST, 2)
            .itemInput(Tags.Items.ORES_REDSTONE)
            .saveSuffixed(output, "_from_ore")
        // Lapis
        createCrushing()
            .itemOutput(Items.LAPIS_LAZULI, 8)
            .itemInput(Tags.Items.ORES_LAPIS)
            .saveSuffixed(output, "_from_ore")
        // Quartz
        createCrushing()
            .itemOutput(Items.QUARTZ, 4)
            .itemOutput(Items.AMETHYST_SHARD)
            .itemInput(Tags.Items.ORES_QUARTZ)
            .saveSuffixed(output, "_from_ore")
        // Diamond
        createCrushing()
            .itemOutput(Items.DIAMOND, 2)
            .itemInput(Tags.Items.ORES_DIAMOND)
            .saveSuffixed(output, "_from_ore")
        // Emerald
        createCrushing()
            .itemOutput(Items.EMERALD, 2)
            .itemInput(Tags.Items.ORES_EMERALD)
            .saveSuffixed(output, "_from_ore")
        // Netherite
        createCrushing()
            .itemOutput(Items.NETHERITE_SCRAP, 2)
            .itemInput(Tags.Items.ORES_NETHERITE_SCRAP)
            .saveSuffixed(output, "_from_ore")
    }

    private fun register(family: HTMaterialFamily) {
        val baseVariant: HTMaterialFamily.Variant = family.baseVariant
        val baseEntry: HTMaterialFamily.Entry = family.baseEntry

        val block: HTMaterialFamily.Entry? = family[HTMaterialFamily.Variant.STORAGE_BLOCK]
        if (block != null) {
            // Base -> Block
            HTShapedRecipeBuilder(block)
                .hollow8()
                .define('A', baseEntry.tagKey)
                .define('B', baseEntry)
                .saveSuffixed(output, "_from_base")
            // Block -> Base
            HTShapelessRecipeBuilder(baseEntry, 9)
                .addIngredient(block.tagKey)
                .saveSuffixed(output, "_from_block")
        }
    }
}
