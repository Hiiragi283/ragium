package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.recipe.custom.HTEternalTicketRecipe
import hiiragi283.ragium.common.util.HTLootTicketHelper
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumToolTiers
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootTable
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredItem

object RagiumToolRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        HTShapedRecipeBuilder(RagiumItems.ENDER_BUNDLE)
            .pattern(
                " A ",
                "ABA",
                "AAA",
            ).define('A', Tags.Items.LEATHERS)
            .define('B', RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ITEM_MAGNET)
            .pattern(
                "A A",
                "B B",
                " C ",
            ).define('A', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('B', RagiumCommonTags.Items.INGOTS_RAGI_ALLOY)
            .define('C', RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL)
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
            .define('A', RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL)
            .define('B', Items.LANTERN)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ELDRITCH_EGG)
            .hollow4()
            .define('A', RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
            .define('B', Tags.Items.EGGS)
            .save(output)

        // molds()
        azureSteel()
        deepSteel()
        forgeHammers()

        tickets()
        lootTickets()
    }

    /*private fun molds() {
        HTShapedRecipeBuilder(RagiumItems.Molds.BLANK)
            .pattern(
                "AA",
                "AA",
                "B ",
            ).define('A', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .define('B', RagiumCommonTags.Items.TOOLS_FORGE_HAMMER)
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

    private fun azureSteel() {
        HTShapedRecipeBuilder(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .pattern(
                "A A",
                "A A",
                " A ",
            ).define('A', RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE, 2)
            .addIngredient(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .saveSuffixed(output, "_duplicate")

        EQUIPMENT_SUFFIXES
            .associate { suffix: String ->
                "iron$suffix" to RagiumConstantValues.AZURE_STEEL + suffix
            }.forEach { (base: String, result: String) ->
                HTSmithingRecipeBuilder(DeferredItem.createItem<Item>(RagiumAPI.id(result)))
                    .addIngredient(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
                    .addIngredient(DeferredItem.createItem<Item>(vanillaId(base)))
                    .addIngredient(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
                    .save(output)
            }
    }

    private fun deepSteel() {
        HTShapedRecipeBuilder(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .pattern(
                "A A",
                "A A",
                " A ",
            ).define('A', RagiumCommonTags.Items.INGOTS_DEEP_STEEL)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE, 2)
            .addIngredient(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(RagiumCommonTags.Items.INGOTS_DEEP_STEEL)
            .addIngredient(RagiumCommonTags.Items.INGOTS_DEEP_STEEL)
            .saveSuffixed(output, "_duplicate")

        EQUIPMENT_SUFFIXES
            .associate { suffix: String ->
                "diamond$suffix" to RagiumConstantValues.DEEP_STEEL + suffix
            }.forEach { (base: String, result: String) ->
                HTSmithingRecipeBuilder(DeferredItem.createItem<Item>(RagiumAPI.id(result)))
                    .addIngredient(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
                    .addIngredient(DeferredItem.createItem<Item>(vanillaId(base)))
                    .addIngredient(RagiumCommonTags.Items.INGOTS_DEEP_STEEL)
                    .save(output)
            }
    }

    private fun forgeHammers() {
        fun crafting(tier: Tier, input: TagKey<Item>) {
            HTShapedRecipeBuilder(RagiumItems.getForgeHammer(tier), category = CraftingBookCategory.EQUIPMENT)
                .pattern(
                    " AA",
                    "BBA",
                    " AA",
                ).define('A', input)
                .define('B', Tags.Items.RODS_WOODEN)
                .save(output)
        }

        crafting(Tiers.IRON, Tags.Items.INGOTS_IRON)
        crafting(Tiers.DIAMOND, Tags.Items.GEMS_DIAMOND)
        crafting(RagiumToolTiers.RAGI_ALLOY, RagiumCommonTags.Items.INGOTS_RAGI_ALLOY)

        fun smithing(
            tier: Tier,
            upgrade: ItemLike,
            oldTier: Tier,
            input: TagKey<Item>,
        ) {
            HTSmithingRecipeBuilder(RagiumItems.getForgeHammer(tier))
                .addIngredient(upgrade)
                .addIngredient(RagiumItems.getForgeHammer(oldTier))
                .addIngredient(input)
                .save(output)
        }

        smithing(Tiers.NETHERITE, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Tiers.DIAMOND, Tags.Items.INGOTS_NETHERITE)
        smithing(
            RagiumToolTiers.AZURE_STEEL,
            RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE,
            Tiers.IRON,
            RagiumCommonTags.Items.INGOTS_AZURE_STEEL,
        )
        smithing(
            RagiumToolTiers.DEEP_STEEL,
            RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE,
            RagiumToolTiers.AZURE_STEEL,
            RagiumCommonTags.Items.INGOTS_DEEP_STEEL,
        )
    }

    private fun tickets() {
        // Blank
        HTShapedRecipeBuilder(RagiumItems.BLANK_TICKET, 6)
            .pattern(
                "AAA",
                "BCB",
                "AAA",
            ).define('A', RagiumCommonTags.Items.PAPER)
            .define('B', Tags.Items.DYES_BLACK)
            .define('C', Tags.Items.GEMS_DIAMOND)
            .save(output)
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

        register(RagiumItems.RAGI_TICKET, RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL, Tags.Items.DYES_RED)
        register(RagiumItems.AZURE_TICKET, RagiumCommonTags.Items.INGOTS_AZURE_STEEL, Tags.Items.DYES_BLUE)
        register(RagiumItems.BLOODY_TICKET, RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL, Tags.Items.DYES_BROWN)
        register(RagiumItems.TELEPORT_TICKET, RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL, Tags.Items.DYES_CYAN)
        register(RagiumItems.ELDRITCH_TICKET, RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL, Tags.Items.DYES_PURPLE)

        register(RagiumItems.DAYBREAK_TICKET, RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY, Tags.Items.DYES_ORANGE)
        register(RagiumItems.ETERNAL_TICKET, Tags.Items.NETHER_STARS, Tags.Items.DYES_WHITE)

        save(
            RagiumAPI.id("smithing/eternal_ticket"),
            HTEternalTicketRecipe,
        )
    }

    private fun lootTickets() {
        fun builder(lootTableKey: ResourceKey<LootTable>, builderAction: HTShapedRecipeBuilder.() -> Unit) {
            HTShapedRecipeBuilder(HTLootTicketHelper.getLootTicket(lootTableKey))
                .cross8()
                .apply(builderAction)
                .define('C', RagiumItems.RAGI_TICKET)
                .saveSuffixed(output, lootTableKey.location().path.removePrefix("chests"))
        }

        // End City
        builder(BuiltInLootTables.END_CITY_TREASURE) {
            define('A', Items.PURPUR_BLOCK)
            define('B', Items.SHULKER_SHELL)
        }
        // Simple Dungeon
        builder(BuiltInLootTables.SIMPLE_DUNGEON) {
            define('A', Tags.Items.COBBLESTONES_MOSSY)
            define('B', Items.ROTTEN_FLESH)
        }
        // Mineshaft
        builder(BuiltInLootTables.ABANDONED_MINESHAFT) {
            define('A', ItemTags.PLANKS)
            define('B', ItemTags.RAILS)
        }
        // Nether Fortress
        builder(BuiltInLootTables.NETHER_BRIDGE) {
            define('A', Items.NETHER_BRICKS)
            define('B', Tags.Items.CROPS_NETHER_WART)
        }
    }
}
