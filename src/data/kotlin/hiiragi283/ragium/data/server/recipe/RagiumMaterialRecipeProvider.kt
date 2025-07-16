package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.data.server.RagiumMaterialFamilies
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumMaterialRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('B', Tags.Items.INGOTS_COPPER)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.RAGI_ALLOY_INGOT)
            .addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        createAlloying()
            .itemOutput(RagiumCommonTags.Items.INGOTS_RAGI_ALLOY)
            .itemInput(Tags.Items.INGOTS_COPPER)
            .itemInput(RagiumCommonTags.Items.DUSTS_RAGINITE, 3)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_COKE)
            .hollow4()
            .define('A', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('B', ItemTags.COALS)
            .save(output)
        // Advanced Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .cross8()
            .define('A', Tags.Items.DUSTS_GLOWSTONE)
            .define('B', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('C', Tags.Items.INGOTS_GOLD)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT)
            .addIngredient(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        createAlloying()
            .itemOutput(RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY)
            .itemInput(Tags.Items.INGOTS_GOLD)
            .itemInput(RagiumCommonTags.Items.DUSTS_RAGINITE, 3)
            .save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.RAGI_CRYSTAL)
            .hollow8()
            .define('A', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('B', Tags.Items.GEMS_DIAMOND)
            .save(output)

        createAlloying()
            .itemOutput(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL)
            .itemInput(Tags.Items.GEMS_DIAMOND)
            .itemInput(RagiumCommonTags.Items.DUSTS_RAGINITE, 6)
            .save(output)
        // Azure Steel
        HTShapedRecipeBuilder(RagiumItems.AZURE_STEEL_COMPOUND)
            .hollow4()
            .define('A', RagiumItems.AZURE_SHARD)
            .define('B', Tags.Items.INGOTS_IRON)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(RagiumItems.AZURE_STEEL_INGOT)
            .addIngredient(RagiumItems.AZURE_STEEL_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")

        createAlloying()
            .itemOutput(RagiumItems.AZURE_SHARD, 2)
            .itemInput(Tags.Items.GEMS_AMETHYST)
            .itemInput(Tags.Items.GEMS_LAPIS)
            .save(output)

        createAlloying()
            .itemOutput(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .itemInput(Tags.Items.INGOTS_IRON)
            .itemInput(RagiumItems.AZURE_SHARD, 3)
            .save(output)
        // Sawdust
        HTShapedRecipeBuilder(RagiumItems.COMPRESSED_SAWDUST)
            .hollow8()
            .define('A', RagiumCommonTags.Items.DUSTS_WOOD)
            .define('B', RagiumItems.SAWDUST)
            .save(output)

        HTCookingRecipeBuilder
            .blasting(Items.CHARCOAL, onlyBlasting = true)
            .addIngredient(RagiumItems.COMPRESSED_SAWDUST)
            .setExp(0.15f)
            .saveSuffixed(output, "_from_pellet")
        // Eldritch Pearl
        HTShapedRecipeBuilder(RagiumItems.ELDRITCH_ORB)
            .cross4()
            .define('A', RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL)
            .define('B', RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL)
            .define('C', RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .save(output)

        createAlloying()
            .itemOutput(RagiumItems.ELDRITCH_ORB, 6)
            .itemInput(RagiumCommonTags.Items.STORAGE_BLOCKS_CRIMSON_CRYSTAL)
            .itemInput(RagiumCommonTags.Items.STORAGE_BLOCKS_WARPED_CRYSTAL)
            .save(output)
        // Misc
        oreToRaw()
        rawToIngot()

        HTShapelessRecipeBuilder(Items.GUNPOWDER, 3)
            .addIngredient(RagiumCommonTags.Items.DUSTS_SULFUR)
            .addIngredient(RagiumCommonTags.Items.DUSTS_SALTPETER)
            .addIngredient(Items.CHARCOAL)
            .addIngredient(RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
            .saveSuffixed(output, "_with_hammer")

        register(RagiumMaterialFamilies.RAGI_CRYSTAL)
        register(RagiumMaterialFamilies.CRIMSON_CRYSTAL)
        register(RagiumMaterialFamilies.WARPED_CRYSTAL)
        register(RagiumMaterialFamilies.ELDRITCH_PEARL)

        register(RagiumMaterialFamilies.RAGI_ALLOY)
        register(RagiumMaterialFamilies.ADVANCED_RAGI_ALLOY)
        register(RagiumMaterialFamilies.AZURE_STEEL)
        register(RagiumMaterialFamilies.DEEP_STEEL)

        register(RagiumMaterialFamilies.CHOCOLATE)
        register(RagiumMaterialFamilies.MEAT)
        register(RagiumMaterialFamilies.COOKED_MEAT)
    }

    // Ore -> Raw/Gem
    private fun oreToRaw() {
        // Coal
        createCrushing()
            .itemOutput(Items.COAL, 2)
            .itemOutput(RagiumItems.SULFUR_DUST, chance = 1 / 4f)
            .itemInput(Tags.Items.ORES_COAL)
            .saveSuffixed(output, "_from_ore")
        // Copper
        createCrushing()
            .itemOutput(Items.RAW_COPPER, 4)
            .itemOutput(Items.RAW_GOLD, chance = 1 / 4f)
            .itemInput(Tags.Items.ORES_COPPER)
            .saveSuffixed(output, "_from_ore")
        // Iron
        createCrushing()
            .itemOutput(Items.RAW_IRON, 2)
            .itemOutput(Items.FLINT, chance = 1 / 4f)
            .itemInput(Tags.Items.ORES_IRON)
            .saveSuffixed(output, "_from_ore")
        // Gold
        createCrushing()
            .itemOutput(Items.RAW_GOLD, 2)
            .itemOutput(Items.RAW_COPPER, chance = 1 / 4f)
            .itemInput(Tags.Items.ORES_GOLD)
            .saveSuffixed(output, "_from_ore")
        // Redstone
        createCrushing()
            .itemOutput(Items.REDSTONE, 8)
            .itemOutput(Items.REDSTONE, 4, 1 / 2f)
            .itemOutput(RagiumCommonTags.Items.DUSTS_CINNABAR, 2, 1 / 2f)
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

        // Raginite
        createCrushing()
            .itemOutput(RagiumCommonTags.Items.DUSTS_RAGINITE, 8)
            .itemOutput(RagiumCommonTags.Items.DUSTS_RAGINITE, 4, 1 / 2f)
            .itemOutput(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL, 2, 1 / 4f)
            .itemInput(RagiumCommonTags.Items.ORES_RAGINITE)
            .saveSuffixed(output, "_from_ore")
        // Ragi-Crystal
        createCrushing()
            .itemOutput(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL, 2)
            .itemInput(RagiumCommonTags.Items.ORES_RAGI_CRYSTAL)
            .saveSuffixed(output, "_from_ore")
    }

    // Raw -> Ingot
    private fun rawToIngot() {
        fun register(result: TagKey<Item>, input: TagKey<Item>) {
            createAlloying()
                .itemOutput(result, 3)
                .itemInput(input, 2)
                .itemInput(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC)
                .saveSuffixed(output, "_with_basic_flux")

            createAlloying()
                .itemOutput(result, 2)
                .itemInput(input)
                .itemInput(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED)
                .saveSuffixed(output, "_with_advanced_flux")
        }

        register(Tags.Items.INGOTS_COPPER, Tags.Items.RAW_MATERIALS_COPPER)
        register(Tags.Items.INGOTS_IRON, Tags.Items.RAW_MATERIALS_IRON)
        register(Tags.Items.INGOTS_GOLD, Tags.Items.RAW_MATERIALS_GOLD)
    }

    private fun register(family: HTMaterialFamily) {
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

        val nugget: HTMaterialFamily.Entry? = family[HTMaterialFamily.Variant.NUGGETS]
        if (nugget != null) {
            // Nugget -> Base
            HTShapedRecipeBuilder(baseEntry)
                .hollow8()
                .define('A', nugget.tagKey)
                .define('B', nugget)
                .saveSuffixed(output, "_from_nugget")
            // Base -> Nugget
            HTShapelessRecipeBuilder(nugget, 9)
                .addIngredient(baseEntry.tagKey)
                .saveSuffixed(output, "_from_base")
        }
    }
}
