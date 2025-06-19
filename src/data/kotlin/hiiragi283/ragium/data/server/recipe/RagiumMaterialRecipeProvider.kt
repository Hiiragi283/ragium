package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.common.recipe.HTCauldronDroppingRecipeImpl
import hiiragi283.ragium.data.server.RagiumMaterialFamilies
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags

object RagiumMaterialRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', RagiumItemTags.DUSTS_RAGINITE)
            .define('B', Tags.Items.INGOTS_COPPER)
            .save(output)

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

        createAlloying()
            .itemOutput(RagiumItems.RAGI_ALLOY_INGOT)
            .itemInput(Tags.Items.INGOTS_COPPER)
            .itemInput(RagiumItemTags.DUSTS_RAGINITE, 3)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_COKE)
            .hollow4()
            .define('A', RagiumItemTags.DUSTS_RAGINITE)
            .define('B', ItemTags.COALS)
            .save(output)
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

        createAlloying()
            .itemOutput(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT)
            .itemInput(Tags.Items.INGOTS_GOLD)
            .itemInput(RagiumItemTags.DUSTS_RAGINITE, 3)
            .save(output)
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.RAGI_CRYSTAL)
            .hollow8()
            .define('A', RagiumItemTags.DUSTS_RAGINITE)
            .define('B', Tags.Items.GEMS_DIAMOND)
            .save(output)

        createAlloying()
            .itemOutput(RagiumItems.RAGI_CRYSTAL)
            .itemInput(Tags.Items.GEMS_DIAMOND)
            .itemInput(RagiumItemTags.DUSTS_RAGINITE, 6)
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

        createAlloying()
            .itemOutput(RagiumItems.AZURE_SHARD, 2)
            .itemInput(Tags.Items.GEMS_AMETHYST)
            .itemInput(Tags.Items.GEMS_LAPIS)
            .save(output)

        createAlloying()
            .itemOutput(RagiumItems.AZURE_STEEL_INGOT)
            .itemInput(Tags.Items.INGOTS_IRON)
            .itemInput(RagiumItems.AZURE_SHARD, 3)
            .save(output)
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
        // Crimson Crystal
        save(
            RagiumAPI.id("cauldron/crimson_crystal"),
            HTCauldronDroppingRecipeImpl(
                RagiumFluidContents.CRIMSON_SAP.get(),
                3,
                Ingredient.of(Tags.Items.GEMS_QUARTZ),
                RagiumItems.CRIMSON_CRYSTAL.toStack(),
            ),
        )
        // Warped Crystal
        save(
            RagiumAPI.id("cauldron/warped_crystal"),
            HTCauldronDroppingRecipeImpl(
                RagiumFluidContents.WARPED_SAP.get(),
                3,
                Ingredient.of(Tags.Items.GEMS_QUARTZ),
                RagiumItems.WARPED_CRYSTAL.toStack(),
            ),
        )
        // Eldritch Pearl
        HTShapedRecipeBuilder(RagiumItems.ELDRITCH_PEARL)
            .cross4()
            .define('A', RagiumItemTags.GEMS_CRIMSON_CRYSTAL)
            .define('B', RagiumItemTags.GEMS_WARPED_CRYSTAL)
            .define('C', RagiumItemTags.ELDRITCH_PEARL_BINDER)
            .save(output)

        createAlloying()
            .itemOutput(RagiumItems.ELDRITCH_PEARL, 6)
            .itemInput(RagiumItemTags.STORAGE_BLOCKS_CRIMSON_CRYSTAL)
            .itemInput(RagiumItemTags.STORAGE_BLOCKS_WARPED_CRYSTAL)
            .save(output)
        // Misc
        oreToRaw()

        HTShapelessRecipeBuilder(Items.GUNPOWDER, 3)
            .addIngredient(RagiumItemTags.DUSTS_SULFUR)
            .addIngredient(RagiumItemTags.DUSTS_SALTPETER)
            .addIngredient(Items.CHARCOAL)
            .addIngredient(RagiumItemTags.TOOLS_FORGE_HAMMER)
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
            .itemOutput(RagiumItems.CINNABAR_DUST, 2, 1 / 2f)
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
            .itemOutput(RagiumItems.RAGINITE_DUST, 8)
            .itemOutput(RagiumItems.RAGINITE_DUST, 4, 1 / 2f)
            .itemOutput(RagiumItems.RAGI_CRYSTAL, 2, 1 / 4f)
            .itemInput(RagiumItemTags.ORES_RAGINITE)
            .saveSuffixed(output, "_from_ore")
        // Ragi-Crystal
        createCrushing()
            .itemOutput(RagiumItems.RAGI_CRYSTAL, 2)
            .itemInput(RagiumItemTags.ORES_RAGI_CRYSTAL)
            .saveSuffixed(output, "_from_ore")
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
