package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.recipe.custom.HTEternalTicketRecipe
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumToolRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_HAMMER, category = CraftingBookCategory.EQUIPMENT)
            .pattern(" AA")
            .pattern("BBA")
            .pattern(" AA")
            .define('A', RagiumItemTags.INGOTS_RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ENDER_BUNDLE)
            .pattern(
                " A ",
                "ABA",
                "AAA",
            ).define('A', Tags.Items.LEATHERS)
            .define('B', RagiumItemTags.GEMS_ELDRITCH_PEARL)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ITEM_MAGNET)
            .pattern(
                "A A",
                "B B",
                " C ",
            ).define('A', RagiumItemTags.INGOTS_AZURE_STEEL)
            .define('B', RagiumItemTags.INGOTS_RAGI_ALLOY)
            .define('C', RagiumItemTags.GEMS_RAGI_CRYSTAL)
            .save(output)

        HTSmithingRecipeBuilder(RagiumItems.EXP_MAGNET)
            .addIngredient(Tags.Items.STORAGE_BLOCKS_EMERALD)
            .addIngredient(RagiumItems.ITEM_MAGNET)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.TRADER_CATALOG)
            .addIngredient(Items.BOOK)
            .addIngredient(Tags.Items.GEMS_EMERALD)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_LANTERN)
            .hollow4()
            .define('A', RagiumItemTags.GEMS_RAGI_CRYSTAL)
            .define('B', Items.LANTERN)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ELDRITCH_EGG)
            .hollow4()
            .define('A', RagiumItemTags.GEMS_ELDRITCH_PEARL)
            .define('B', Tags.Items.EGGS)
            .save(output)

        // molds()
        azure()
        tickets()
    }

    /*private fun molds() {
        HTShapedRecipeBuilder(RagiumItems.Molds.BLANK)
            .pattern(
                "AA",
                "AA",
                "B ",
            ).define('A', RagiumItemTags.INGOTS_AZURE_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)

        for (mold: RagiumItems.Molds in RagiumItems.Molds.entries) {
            save(
                RagiumAPI.id("stonecutting/${mold.path}"),
                StonecutterRecipe(
                    "mold",
                    Ingredient.of(RagiumItemTags.MOLDS_BLANK),
                    mold.toStack(),
                ),
            )
        }
    }*/

    private fun azure() {
        // Armor
        HTShapedRecipeBuilder(RagiumItems.AZURE_STEEL_HELMET, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "AAA",
                "ABA",
            ).define('A', RagiumItemTags.INGOTS_AZURE_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Chestplate
        HTShapedRecipeBuilder(RagiumItems.AZURE_STEEL_CHESTPLATE, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "ABA",
                "AAA",
                "AAA",
            ).define('A', RagiumItemTags.INGOTS_AZURE_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Leggings
        HTShapedRecipeBuilder(RagiumItems.AZURE_STEEL_LEGGINGS, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "AAA",
                "ABA",
                "A A",
            ).define('A', RagiumItemTags.INGOTS_AZURE_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Boots
        HTShapedRecipeBuilder(RagiumItems.AZURE_STEEL_BOOTS, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "A A",
                "ABA",
            ).define('A', RagiumItemTags.INGOTS_AZURE_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)
        // Tool
        RagiumItems.AZURE_STEEL_TOOLS.addRecipes(output, provider)

        // Iron -> Azure Steel
        HTShapedRecipeBuilder(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .pattern(
                "A A",
                "A A",
                " A ",
            ).define('A', RagiumItemTags.INGOTS_AZURE_STEEL)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE, 2)
            .addIngredient(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(RagiumItemTags.INGOTS_AZURE_STEEL)
            .addIngredient(RagiumItemTags.INGOTS_AZURE_STEEL)
            .saveSuffixed(output, "_duplicate")

        mapOf(
            Items.IRON_HELMET to RagiumItems.AZURE_STEEL_HELMET,
            Items.IRON_CHESTPLATE to RagiumItems.AZURE_STEEL_CHESTPLATE,
            Items.IRON_LEGGINGS to RagiumItems.AZURE_STEEL_LEGGINGS,
            Items.IRON_BOOTS to RagiumItems.AZURE_STEEL_BOOTS,
        ).forEach { (base: Item, result) ->
            HTSmithingRecipeBuilder(result)
                .addIngredient(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
                .addIngredient(base)
                .addIngredient(RagiumItemTags.INGOTS_AZURE_STEEL)
                .save(output)
        }
    }

    private fun tickets() {
        // Blank
        HTShapedRecipeBuilder(RagiumItems.BLANK_TICKET, 6)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', RagiumItemTags.PAPER)
            .define('B', Tags.Items.DYES_BLACK)
            .define('C', Tags.Items.GEMS_DIAMOND)
            .save(output)
        // Ragi from Redstone
        HTShapedRecipeBuilder(RagiumItems.RAGI_TICKET)
            .cross8()
            .define('A', Tags.Items.STORAGE_BLOCKS_REDSTONE)
            .define('B', Tags.Items.DYES_RED)
            .define('C', RagiumItems.BLANK_TICKET)
            .saveSuffixed(output, "_from_redstone")
        // Azure from Lapis
        HTShapedRecipeBuilder(RagiumItems.AZURE_TICKET)
            .cross8()
            .define('A', Tags.Items.STORAGE_BLOCKS_LAPIS)
            .define('B', Tags.Items.DYES_BLUE)
            .define('C', RagiumItems.BLANK_TICKET)
            .saveSuffixed(output, "_from_lapis")

        fun register(ticket: ItemLike, corner: TagKey<Item>, dye: TagKey<Item>) {
            HTShapedRecipeBuilder(ticket)
                .cross8()
                .define('A', corner)
                .define('B', dye)
                .define('C', RagiumItems.BLANK_TICKET)
                .save(output)
        }

        register(RagiumItems.RAGI_TICKET, RagiumItemTags.DUSTS_RAGINITE, Tags.Items.DYES_RED)
        register(RagiumItems.AZURE_TICKET, RagiumItemTags.INGOTS_AZURE_STEEL, Tags.Items.DYES_BLUE)
        register(RagiumItems.BLOODY_TICKET, RagiumItemTags.GEMS_CRIMSON_CRYSTAL, Tags.Items.DYES_BROWN)
        register(RagiumItems.TELEPORT_TICKET, RagiumItemTags.GEMS_WARPED_CRYSTAL, Tags.Items.DYES_CYAN)
        register(RagiumItems.ELDRITCH_TICKET, RagiumItemTags.GEMS_ELDRITCH_PEARL, Tags.Items.DYES_PURPLE)

        register(RagiumItems.DAYBREAK_TICKET, RagiumItemTags.INGOTS_ADVANCED_RAGI_ALLOY, Tags.Items.DYES_ORANGE)
        register(RagiumItems.ETERNAL_TICKET, Tags.Items.NETHER_STARS, Tags.Items.DYES_WHITE)

        save(
            RagiumAPI.id("smithing/eternal_ticket"),
            HTEternalTicketRecipe,
        )
    }
}
