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
        // Ragi-Crystal
        HTShapedRecipeBuilder(RagiumItems.RAGI_CRYSTAL)
            .hollow8()
            .define('A', RagiumItemTags.DUSTS_RAGINITE)
            .define('B', Tags.Items.GEMS_DIAMOND)
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
