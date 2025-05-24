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
        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addRecipes(output, provider)
        // Tool
        RagiumItems.AZURE_STEEL_TOOLS.addRecipes(output, provider)

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

        HTShapedRecipeBuilder(RagiumItems.RAGI_LANTERN)
            .hollow4()
            .define('A', RagiumItemTags.GEMS_RAGI_CRYSTAL)
            .define('B', Items.LANTERN)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_EGG)
            .hollow4()
            .define('A', RagiumItemTags.GEMS_RAGI_CRYSTAL)
            .define('B', Tags.Items.EGGS)
            .save(output)

        // molds()
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

        fun register(ticket: ItemLike, corner: TagKey<Item>, dye: TagKey<Item>) {
            HTShapedRecipeBuilder(ticket)
                .cross8()
                .define('A', corner)
                .define('B', dye)
                .define('C', RagiumItems.BLANK_TICKET)
                .save(output)
        }

        register(RagiumItems.RAGI_TICKET, RagiumItemTags.DUSTS_RAGINITE, Tags.Items.DYES_RED)
        register(RagiumItems.AZURE_TICKET, Tags.Items.GLASS_BLOCKS, Tags.Items.DYES_BLUE)
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
