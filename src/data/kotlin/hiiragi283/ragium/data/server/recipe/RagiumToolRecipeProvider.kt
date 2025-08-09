package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStonecuttingRecipeBuilder
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.recipe.HTBlastChargeRecipe
import hiiragi283.ragium.common.recipe.HTEternalTicketRecipe
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTLootTicketHelper
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootTable
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredItem

object RagiumToolRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        HTShapedRecipeBuilder(RagiumItems.DRILL)
            .pattern(
                " A ",
                "ABA",
                "ACA",
            ).define('A', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('B', RagiumCommonTags.Items.DUSTS_RAGINITE)
            .define('C', RagiumCommonTags.Items.CIRCUITS_BASIC)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.POTION_BUNDLE)
            .pattern(
                " A ",
                "BBB",
            ).define('A', Tags.Items.STRINGS)
            .define('B', Items.GLASS_BOTTLE)
            .save(output)

        HTStonecuttingRecipeBuilder(RagiumItems.SLOT_COVER, 3)
            .addIngredient(Items.SMOOTH_STONE_SLAB)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.TRADER_CATALOG)
            .addIngredient(Items.BOOK)
            .addIngredient(Tags.Items.GEMS_EMERALD)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.BLAST_CHARGE, 8)
            .hollow8()
            .define('A', Tags.Items.GUNPOWDERS)
            .define('B', RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ENDER_BUNDLE)
            .pattern(
                " A ",
                "ABA",
                "AAA",
            ).define('A', Tags.Items.LEATHERS)
            .define('B', RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ELDRITCH_EGG)
            .hollow4()
            .define('A', RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
            .define('B', Tags.Items.EGGS)
            .save(output)

        ragiAlloy()
        azureSteel()
        deepSteel()

        forgeHammers()

        tickets()
        lootTickets()
    }

    private fun ragiAlloy() {
        // Basic
        HTShapedRecipeBuilder(RagiumItems.RAGI_MAGNET)
            .pattern(
                "A A",
                "B B",
                " C ",
            ).define('A', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('B', RagiumCommonTags.Items.INGOTS_RAGI_ALLOY)
            .define('C', RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL)
            .save(output)

        // Advanced
        addTemplate(
            RagiumItems.ADVANCED_RAGI_ALLOY_UPGRADE_SMITHING_TEMPLATE,
            RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY,
        )

        HTSmithingRecipeBuilder(RagiumItems.ADVANCED_RAGI_MAGNET)
            .addIngredient(RagiumItems.ADVANCED_RAGI_ALLOY_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(RagiumItems.RAGI_MAGNET)
            .addIngredient(RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY)
            .save(output)

        // Elite
        HTShapedRecipeBuilder(RagiumItems.RAGI_LANTERN)
            .hollow4()
            .define('A', RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL)
            .define('B', Items.LANTERN)
            .save(output)
    }

    private fun azureSteel() {
        addTemplate(
            RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE,
            RagiumCommonTags.Items.INGOTS_AZURE_STEEL,
        )

        EQUIPMENT_SUFFIXES
            .associate { suffix: String ->
                "iron$suffix" to RagiumConst.AZURE_STEEL + suffix
            }.forEach { (base: String, result: String) ->
                addAzureSmithing(
                    DeferredItem.createItem<Item>(RagiumAPI.id(result)),
                    DeferredItem.createItem<Item>(vanillaId(base)),
                )
            }
    }

    private fun deepSteel() {
        addTemplate(
            RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE,
            RagiumCommonTags.Items.INGOTS_DEEP_STEEL,
        )

        EQUIPMENT_SUFFIXES
            .associate { suffix: String ->
                "diamond$suffix" to RagiumConst.DEEP_STEEL + suffix
            }.forEach { (base: String, result: String) ->
                addDeepSmithing(
                    DeferredItem.createItem<Item>(RagiumAPI.id(result)),
                    DeferredItem.createItem<Item>(vanillaId(base)),
                )
            }
    }

    private fun forgeHammers() {
        fun crafting(hammer: ItemLike, input: TagKey<Item>) {
            HTShapedRecipeBuilder(hammer, category = CraftingBookCategory.EQUIPMENT)
                .pattern(
                    " AA",
                    "BBA",
                    " AA",
                ).define('A', input)
                .define('B', Tags.Items.RODS_WOODEN)
                .save(output)
        }

        crafting(RagiumItems.ForgeHammers.IRON, Tags.Items.INGOTS_IRON)
        crafting(RagiumItems.ForgeHammers.DIAMOND, Tags.Items.GEMS_DIAMOND)
        crafting(RagiumItems.ForgeHammers.RAGI_ALLOY, RagiumCommonTags.Items.INGOTS_RAGI_ALLOY)

        createNetheriteUpgrade(RagiumItems.ForgeHammers.NETHERITE, RagiumItems.ForgeHammers.DIAMOND).save(output)
        addAzureSmithing(RagiumItems.ForgeHammers.AZURE_STEEL, RagiumItems.ForgeHammers.IRON)
        addDeepSmithing(RagiumItems.ForgeHammers.DEEP_STEEL, RagiumItems.ForgeHammers.AZURE_STEEL)
    }

    private fun tickets() {
        // Blank
        HTShapedRecipeBuilder(RagiumItems.Tickets.BLANK, 6)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', RagiumCommonTags.Items.PAPER)
            .define('B', Tags.Items.DYES_BLACK)
            .define('C', Tags.Items.GEMS_DIAMOND)
            .save(output)
        // Azure from Lapis
        HTShapedRecipeBuilder(RagiumItems.Tickets.AZURE)
            .cross8()
            .define('A', Tags.Items.STORAGE_BLOCKS_LAPIS)
            .define('B', Tags.Items.DYES_BLUE)
            .define('C', RagiumItems.Tickets.BLANK)
            .saveSuffixed(output, "_from_lapis")

        addTicket(RagiumItems.Tickets.RAGI, RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL, Tags.Items.DYES_RED)
        addTicket(RagiumItems.Tickets.AZURE, RagiumCommonTags.Items.INGOTS_AZURE_STEEL, Tags.Items.DYES_BLUE)
        addTicket(RagiumItems.Tickets.BLOODY, RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL, Tags.Items.DYES_BROWN)
        addTicket(RagiumItems.Tickets.TELEPORT, RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL, Tags.Items.DYES_CYAN)
        addTicket(RagiumItems.Tickets.ELDRITCH, RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL, Tags.Items.DYES_PURPLE)

        addTicket(RagiumItems.Tickets.DAYBREAK, RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY, Tags.Items.DYES_ORANGE)
        addTicket(RagiumItems.Tickets.ETERNAL, Tags.Items.NETHER_STARS, Tags.Items.DYES_WHITE)

        save(
            RagiumAPI.id("shapeless/blast_charge"),
            HTBlastChargeRecipe(CraftingBookCategory.EQUIPMENT),
        )
        save(
            RagiumAPI.id("shapeless/eternal_ticket"),
            HTEternalTicketRecipe(CraftingBookCategory.MISC),
        )
    }

    private fun lootTickets() {
        // End City
        addLootTicket(BuiltInLootTables.END_CITY_TREASURE) {
            define('A', Items.PURPUR_BLOCK)
            define('B', Items.SHULKER_SHELL)
        }
        // Simple Dungeon
        addLootTicket(BuiltInLootTables.SIMPLE_DUNGEON) {
            define('A', Tags.Items.COBBLESTONES_MOSSY)
            define('B', Items.ROTTEN_FLESH)
        }
        // Mineshaft
        addLootTicket(BuiltInLootTables.ABANDONED_MINESHAFT) {
            define('A', ItemTags.PLANKS)
            define('B', ItemTags.RAILS)
        }
        // Nether Fortress
        addLootTicket(BuiltInLootTables.NETHER_BRIDGE) {
            define('A', Items.NETHER_BRICKS)
            define('B', Tags.Items.CROPS_NETHER_WART)
        }
    }

    //    Extension    //

    @JvmField
    val EQUIPMENT_SUFFIXES: List<String> = listOf(
        // Armor
        "_helmet",
        "_chestplate",
        "_leggings",
        "_boots",
        // Tool
        "_shovel",
        "_pickaxe",
        "_axe",
        "_hoe",
        "_sword",
    )

    @JvmStatic
    private fun addTemplate(template: ItemLike, input: TagKey<Item>) {
        HTShapedRecipeBuilder(template)
            .pattern(
                "A A",
                "A A",
                " A ",
            ).define('A', input)
            .save(output)

        HTShapelessRecipeBuilder(template, 2)
            .addIngredient(template)
            .addIngredient(input)
            .addIngredient(input)
            .saveSuffixed(output, "_duplicate")
    }

    @JvmStatic
    private fun addAzureSmithing(output: ItemLike, ingredient: ItemLike) {
        HTSmithingRecipeBuilder(output)
            .addIngredient(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(ingredient)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .save(this.output)
    }

    @JvmStatic
    private fun addDeepSmithing(output: ItemLike, ingredient: ItemLike) {
        HTSmithingRecipeBuilder(output)
            .addIngredient(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(ingredient)
            .addIngredient(RagiumCommonTags.Items.INGOTS_DEEP_STEEL)
            .save(this.output)
    }

    @JvmStatic
    private fun addTicket(ticket: ItemLike, corner: TagKey<Item>, dye: TagKey<Item>) {
        HTShapedRecipeBuilder(ticket)
            .cross8()
            .define('A', corner)
            .define('B', dye)
            .define('C', RagiumItems.Tickets.BLANK)
            .save(output)
    }

    @JvmStatic
    private fun addLootTicket(lootTableKey: ResourceKey<LootTable>, builderAction: HTShapedRecipeBuilder.() -> Unit) {
        HTShapedRecipeBuilder(HTLootTicketHelper.getLootTicket(lootTableKey))
            .cross8()
            .apply(builderAction)
            .define('C', RagiumItems.Tickets.RAGI)
            .saveSuffixed(output, lootTableKey.location().path.removePrefix("chests"))
    }
}
