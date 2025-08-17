package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTStonecuttingRecipeBuilder
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.common.recipe.HTBlastChargeRecipe
import hiiragi283.ragium.common.recipe.HTEternalTicketRecipe
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTLootTicketHelper
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.material.RagiumTierType
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

object RagiumToolRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        HTShapedRecipeBuilder(RagiumItems.DRILL)
            .pattern(
                " A ",
                "ABA",
                "ACA",
            ).define('A', HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', HTMaterialVariant.CIRCUIT, RagiumTierType.BASIC)
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
            .addIngredient(HTMaterialVariant.GEM, HTVanillaMaterialType.EMERALD)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.BLAST_CHARGE, 8)
            .hollow8()
            .define('A', Tags.Items.GUNPOWDERS)
            .define('B', HTMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ENDER_BUNDLE)
            .pattern(
                " A ",
                "ABA",
                "AAA",
            ).define('A', Tags.Items.LEATHERS)
            .define('B', HTMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ELDRITCH_EGG)
            .hollow4()
            .define('A', HTMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
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
            ).define('A', HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('C', HTMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .save(output)

        // Advanced
        createAdvUpgrade(RagiumItems.ADVANCED_RAGI_MAGNET, RagiumItems.RAGI_MAGNET).save(output)

        // Elite
        HTShapedRecipeBuilder(RagiumItems.RAGI_LANTERN)
            .hollow4()
            .define('A', HTMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .define('B', Items.LANTERN)
            .save(output)
    }

    private fun azureSteel() {
        addTemplate(
            RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE,
            RagiumMaterialType.AZURE_STEEL,
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
            RagiumMaterialType.DEEP_STEEL,
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
        fun hammer(material: HTMaterialType): DeferredItem<*> = RagiumItems.getForgeHammer(material)

        fun crafting(variant: HTMaterialVariant, material: HTMaterialType) {
            HTShapedRecipeBuilder(hammer(material), category = CraftingBookCategory.EQUIPMENT)
                .pattern(
                    " AA",
                    "BBA",
                    " AA",
                ).define('A', variant, material)
                .define('B', Tags.Items.RODS_WOODEN)
                .save(output)
        }

        crafting(HTMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
        crafting(HTMaterialVariant.GEM, HTVanillaMaterialType.DIAMOND)
        crafting(HTMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)

        createNetheriteUpgrade(hammer(HTVanillaMaterialType.NETHERITE), hammer(HTVanillaMaterialType.DIAMOND)).save(output)
        addAzureSmithing(hammer(RagiumMaterialType.AZURE_STEEL), hammer(HTVanillaMaterialType.IRON))
        addDeepSmithing(hammer(RagiumMaterialType.DEEP_STEEL), hammer(RagiumMaterialType.AZURE_STEEL))

        HTSmithingRecipeBuilder(hammer(RagiumMaterialType.RAGI_CRYSTAL))
            .addIngredient(HTMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .addIngredient(hammer(RagiumMaterialType.RAGI_ALLOY))
            .save(output)
    }

    private fun tickets() {
        addTicket(
            RagiumItems.RAGI_TICKET,
            HTMaterialVariant.GEM.itemTagKey(RagiumMaterialType.RAGI_CRYSTAL),
            Tags.Items.DYES_RED,
        )
        addTicket(
            RagiumItems.TELEPORT_TICKET,
            HTMaterialVariant.GEM.itemTagKey(RagiumMaterialType.WARPED_CRYSTAL),
            Tags.Items.DYES_CYAN,
        )
        addTicket(RagiumItems.ETERNAL_TICKET, Tags.Items.NETHER_STARS, Tags.Items.DYES_WHITE)

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
    private fun addTemplate(template: ItemLike, material: HTMaterialType) {
        HTShapedRecipeBuilder(template)
            .pattern(
                "A A",
                "A A",
                " A ",
            ).define('A', HTMaterialVariant.INGOT, material)
            .save(output)

        HTShapelessRecipeBuilder(template, 2)
            .addIngredient(template)
            .addIngredient(HTMaterialVariant.INGOT, material)
            .addIngredient(HTMaterialVariant.INGOT, material)
            .saveSuffixed(output, "_duplicate")
    }

    @JvmStatic
    private fun addAzureSmithing(output: ItemLike, ingredient: ItemLike) {
        HTSmithingRecipeBuilder(output)
            .addIngredient(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(ingredient)
            .addIngredient(HTMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .save(this.output)
    }

    @JvmStatic
    private fun addDeepSmithing(output: ItemLike, ingredient: ItemLike) {
        HTSmithingRecipeBuilder(output)
            .addIngredient(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(ingredient)
            .addIngredient(HTMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL)
            .save(this.output)
    }

    @JvmStatic
    private fun addTicket(ticket: ItemLike, corner: TagKey<Item>, dye: TagKey<Item>) {
        HTShapedRecipeBuilder(ticket)
            .cross8()
            .define('A', corner)
            .define('B', dye)
            .define('C', Items.PAPER)
            .save(output)
    }

    @JvmStatic
    private fun addLootTicket(lootTableKey: ResourceKey<LootTable>, builderAction: HTShapedRecipeBuilder.() -> Unit) {
        HTShapedRecipeBuilder(HTLootTicketHelper.getLootTicket(lootTableKey))
            .cross8()
            .apply(builderAction)
            .define('C', RagiumItems.RAGI_TICKET)
            .saveSuffixed(output, lootTableKey.location().path.removePrefix("chests"))
    }
}
