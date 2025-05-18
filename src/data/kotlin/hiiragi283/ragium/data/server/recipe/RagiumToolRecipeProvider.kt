package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.recipe.custom.HTEternalTicketRecipe
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.neoforged.neoforge.common.Tags

object RagiumToolRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addRecipes(output, provider)
        // Tool
        RagiumItems.RAGI_ALLOY_TOOLS.addRecipes(output, provider)
        RagiumItems.AZURE_STEEL_TOOLS.addRecipes(output, provider)

        HTShapedRecipeBuilder(RagiumItems.ENDER_BUNDLE)
            .pattern(
                " A ",
                "ABA",
                "AAA",
            ).define('A', Tags.Items.LEATHERS)
            .define('B', Tags.Items.CHESTS_ENDER)
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

        molds()
        tickets()
    }

    private fun molds() {
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
        // Ragi
        HTShapedRecipeBuilder(RagiumItems.RAGI_TICKET)
            .cross8()
            .define('A', Tags.Items.DUSTS_REDSTONE)
            .define('B', Tags.Items.DYES_RED)
            .define('C', RagiumItems.BLANK_TICKET)
            .save(output)
        // Azure
        HTShapedRecipeBuilder(RagiumItems.AZURE_TICKET)
            .cross8()
            .define('A', Tags.Items.GLASS_BLOCKS)
            .define('B', Tags.Items.DYES_BLUE)
            .define('C', RagiumItems.BLANK_TICKET)
            .save(output)
        // Deep
        HTShapedRecipeBuilder(RagiumItems.DEEP_TICKET)
            .cross8()
            .define('A', Items.SCULK)
            .define('B', Tags.Items.DYES_GREEN)
            .define('C', RagiumItems.BLANK_TICKET)
            .save(output)

        // Eternal
        HTShapelessRecipeBuilder(RagiumItems.ETERNAL_TICKET)
            .addIngredient(RagiumItems.BLANK_TICKET)
            .addIngredient(Tags.Items.NETHER_STARS)
            .addIngredient(Tags.Items.DYES_WHITE)
            .save(output)

        save(
            RagiumAPI.id("smithing/eternal_ticket"),
            HTEternalTicketRecipe,
        )
    }
}
