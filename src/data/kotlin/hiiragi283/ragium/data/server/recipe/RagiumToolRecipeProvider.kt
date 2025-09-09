package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTSmithingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTStonecuttingRecipeBuilder
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.item.HTUniversalBundleItem
import hiiragi283.ragium.common.material.HTTierType
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.util.HTLootTicketHelper
import hiiragi283.ragium.common.variant.HTArmorVariant
import hiiragi283.ragium.common.variant.HTColorMaterial
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.common.variant.HTVanillaToolVariant
import hiiragi283.ragium.integration.delight.HTKnifeToolVariant
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootTable
import net.neoforged.neoforge.common.Tags

object RagiumToolRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        HTShapedRecipeBuilder(RagiumItems.DRILL)
            .pattern(
                " A ",
                "ABA",
                "ACA",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTTierType.BASIC)
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
            .addIngredient(HTItemMaterialVariant.GEM, HTVanillaMaterialType.EMERALD)
            .save(output)

        raginite()
        azureAndDeepSteel()
        molten()

        forgeHammers()

        lootTickets()
    }

    @JvmStatic
    private fun raginite() {
        // Basic
        HTShapedRecipeBuilder(RagiumItems.WRENCH)
            .pattern(
                " A ",
                " BA",
                "B  ",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.MAGNET)
            .pattern(
                "A A",
                "B B",
                " C ",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('C', HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .save(output)

        // Advanced
        createComponentUpgrade(
            HTTierType.ADVANCED,
            RagiumItems.ADVANCED_MAGNET,
            RagiumItems.MAGNET,
        ).save(output)

        // Elite
        HTShapedRecipeBuilder(RagiumItems.DYNAMIC_LANTERN)
            .hollow4()
            .define('A', HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .define('B', Items.LANTERN)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.LOOT_TICKET)
            .cross8()
            .define('A', HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .define('B', Tags.Items.DYES_RED)
            .define('C', Items.PAPER)
            .save(output)
    }

    @JvmStatic
    private fun azureAndDeepSteel() {
        RagiumItems.ARMORS.forEach { (variant: HTArmorVariant, material: HTMaterialType, armor: HTDeferredItem<*>) ->
            val before: HTVanillaMaterialType = when (material) {
                RagiumMaterialType.AZURE_STEEL -> HTVanillaMaterialType.IRON
                RagiumMaterialType.DEEP_STEEL -> HTVanillaMaterialType.DIAMOND
                else -> return@forEach
            }
            val beforeArmor: ItemLike = HTArmorVariant.ARMOR_TABLE.get(variant, before) ?: return@forEach
            when (material) {
                RagiumMaterialType.AZURE_STEEL -> ::addAzureSmithing
                RagiumMaterialType.DEEP_STEEL -> ::addDeepSmithing
                else -> return@forEach
            }(armor, beforeArmor)
        }

        RagiumItems.TOOLS.forEach { (variant: HTToolVariant, material: HTMaterialType, tool: ItemLike) ->
            val before: HTVanillaMaterialType = when (material) {
                RagiumMaterialType.AZURE_STEEL -> HTVanillaMaterialType.IRON
                RagiumMaterialType.DEEP_STEEL -> HTVanillaMaterialType.DIAMOND
                else -> return@forEach
            }
            val beforeTool: ItemLike = when (variant) {
                is HTVanillaToolVariant -> HTVanillaToolVariant.TOOL_TABLE.get(variant, before)
                is HTHammerToolVariant -> RagiumItems.TOOLS.get(variant, before)
                is HTKnifeToolVariant -> RagiumDelightAddon.ALL_KNIFE_MAP[before]?.get()
                else -> null
            } ?: return@forEach
            when (material) {
                RagiumMaterialType.AZURE_STEEL -> ::addAzureSmithing
                RagiumMaterialType.DEEP_STEEL -> ::addDeepSmithing
                else -> return@forEach
            }(tool, beforeTool)
        }

        addTemplate(
            RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE,
            RagiumMaterialType.AZURE_STEEL,
        )
        addTemplate(
            RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE,
            RagiumMaterialType.DEEP_STEEL,
        )
    }

    @JvmStatic
    private fun molten() {
        // Crimson
        HTShapedRecipeBuilder(RagiumItems.BLAST_CHARGE, 8)
            .hollow8()
            .define('A', Tags.Items.GUNPOWDERS)
            .define('B', HTItemMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL)
            .save(output)
        // Warped
        HTShapedRecipeBuilder(RagiumItems.TELEPORT_KEY)
            .cross8()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTItemMaterialVariant.GEM, RagiumMaterialType.WARPED_CRYSTAL)
            .define('C', Items.TRIAL_KEY)
            .save(output)

        resetComponent(RagiumItems.TELEPORT_KEY)
        // Eldritch
        HTShapedRecipeBuilder(RagiumItems.ELDRITCH_EGG)
            .hollow4()
            .define('A', HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .define('B', Tags.Items.EGGS)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.UNIVERSAL_BUNDLE)
            .pattern(
                "ABA",
                "BCB",
                "BBB",
            ).define('A', RagiumItems.SYNTHETIC_FIBER)
            .define('B', RagiumItems.SYNTHETIC_LEATHER)
            .define('C', HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .save(output)

        for (variant: HTColorMaterial in HTColorMaterial.entries) {
            HTShapelessRecipeBuilder(HTUniversalBundleItem.createBundle(variant.color))
                .addIngredient(RagiumItems.UNIVERSAL_BUNDLE)
                .addIngredient(variant.dyeTag)
                .savePrefixed(output, "${variant.serializedName}_")
        }

        resetComponent(RagiumItems.UNIVERSAL_BUNDLE)
    }

    @JvmStatic
    private fun forgeHammers() {
        fun hammer(material: HTMaterialType): ItemLike = RagiumItems.getTool(HTHammerToolVariant, material)

        fun crafting(variant: HTItemMaterialVariant, material: HTMaterialType) {
            HTShapedRecipeBuilder(hammer(material), category = CraftingBookCategory.EQUIPMENT)
                .pattern(
                    " AA",
                    "BBA",
                    " AA",
                ).define('A', variant, material)
                .define('B', Tags.Items.RODS_WOODEN)
                .save(output)
        }

        crafting(HTItemMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
        crafting(HTItemMaterialVariant.GEM, HTVanillaMaterialType.DIAMOND)
        crafting(HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)

        createNetheriteUpgrade(hammer(HTVanillaMaterialType.NETHERITE), hammer(HTVanillaMaterialType.DIAMOND)).save(output)
        createComponentUpgrade(
            HTTierType.ELITE,
            hammer(RagiumMaterialType.RAGI_CRYSTAL),
            hammer(RagiumMaterialType.RAGI_ALLOY),
        ).addIngredient(HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .save(output)
    }

    @JvmStatic
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

    @JvmStatic
    private fun addTemplate(template: ItemLike, material: HTMaterialType) {
        HTShapedRecipeBuilder(template)
            .pattern(
                "A A",
                "A A",
                " A ",
            ).define('A', HTItemMaterialVariant.INGOT, material)
            .save(output)

        HTShapelessRecipeBuilder(template, 2)
            .addIngredient(template)
            .addIngredient(HTItemMaterialVariant.INGOT, material)
            .addIngredient(HTItemMaterialVariant.INGOT, material)
            .saveSuffixed(output, "_duplicate")
    }

    @JvmStatic
    private fun addAzureSmithing(output: ItemLike, ingredient: ItemLike) {
        HTSmithingRecipeBuilder(output)
            .addIngredient(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(ingredient)
            .addIngredient(HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .save(this.output)
    }

    @JvmStatic
    private fun addDeepSmithing(output: ItemLike, ingredient: ItemLike) {
        HTSmithingRecipeBuilder(output)
            .addIngredient(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(ingredient)
            .addIngredient(HTItemMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL)
            .save(this.output)
    }

    @JvmStatic
    private inline fun addLootTicket(lootTableKey: ResourceKey<LootTable>, builderAction: HTShapedRecipeBuilder.() -> Unit) {
        HTShapedRecipeBuilder(HTLootTicketHelper.getLootTicket(lootTableKey))
            .cross8()
            .apply(builderAction)
            .define('C', RagiumItems.LOOT_TICKET)
            .saveSuffixed(output, lootTableKey.location().path.removePrefix("chests"))
    }
}
