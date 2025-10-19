package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.integration.RagiumDelightAddon
import hiiragi283.ragium.common.item.HTUniversalBundleItem
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.tier.HTCircuitTier
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.util.HTDefaultLootTickets
import hiiragi283.ragium.common.variant.HTArmorVariant
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.common.variant.HTKnifeToolVariant
import hiiragi283.ragium.common.variant.HTVanillaToolVariant
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSmithingRecipeBuilder
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumToolRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        HTShapedRecipeBuilder
            .equipment(RagiumItems.DRILL)
            .pattern(
                " A ",
                "ABA",
                "ACA",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .define('C', HTItemMaterialVariant.CIRCUIT, HTCircuitTier.BASIC)
            .save(output)

        HTShapedRecipeBuilder
            .equipment(RagiumItems.POTION_BUNDLE)
            .pattern(
                " A ",
                "BBB",
            ).define('A', Tags.Items.STRINGS)
            .define('B', Items.GLASS_BOTTLE)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(RagiumItems.SLOT_COVER, 3)
            .addIngredient(Items.SMOOTH_STONE_SLAB)
            .save(output)

        HTShapelessRecipeBuilder
            .equipment(RagiumItems.TRADER_CATALOG)
            .addIngredient(Items.BOOK)
            .addIngredient(HTItemMaterialVariant.GEM, HTVanillaMaterialType.EMERALD)
            .save(output)

        HTShapedRecipeBuilder
            .equipment(RagiumItems.ECHO_STAR)
            .cross8()
            .define('A', HTItemMaterialVariant.DUST, HTVanillaMaterialType.ECHO)
            .define('B', HTItemMaterialVariant.GEM, HTVanillaMaterialType.ECHO)
            .define('C', HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.DEEP_STEEL)
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
        HTShapedRecipeBuilder
            .equipment(RagiumItems.WRENCH)
            .pattern(
                " A ",
                " BA",
                "B  ",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .save(output)

        HTShapedRecipeBuilder
            .equipment(RagiumItems.MAGNET)
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
            HTComponentTier.ADVANCED,
            RagiumItems.ADVANCED_MAGNET,
            RagiumItems.MAGNET,
        ).save(output)

        // Elite
        HTShapedRecipeBuilder
            .equipment(RagiumItems.DYNAMIC_LANTERN)
            .hollow4()
            .define('A', HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .define('B', Items.LANTERN)
            .save(output)

        HTShapedRecipeBuilder
            .equipment(RagiumItems.LOOT_TICKET)
            .cross8()
            .define('A', HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .define('B', Tags.Items.DYES_RED)
            .define('C', Items.PAPER)
            .save(output)

        HTShapedRecipeBuilder
            .equipment(RagiumItems.NIGHT_VISION_GOGGLES)
            .pattern(
                "AAA",
                "ABA",
            ).define('A', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .define('B', HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .save(output)
    }

    @JvmStatic
    private fun azureAndDeepSteel() {
        addEquipments(
            RagiumItems.AZURE_ARMORS,
            RagiumMaterialType.AZURE_STEEL,
            HTVanillaMaterialType.IRON,
            RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE,
            ::addAzureSmithing,
        )

        addEquipments(
            RagiumItems.DEEP_ARMORS,
            RagiumMaterialType.DEEP_STEEL,
            HTVanillaMaterialType.DIAMOND,
            RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE,
            ::addDeepSmithing,
        )
    }

    @JvmStatic
    private fun addEquipments(
        armors: Map<HTArmorVariant, HTDeferredItem<*>>,
        material: HTMaterialType,
        beforeMaterial: HTVanillaMaterialType,
        upgrade: HTDeferredItem<*>,
        upgradeFactory: (ItemLike, ItemLike) -> Unit,
    ) {
        // Template
        addTemplate(upgrade, material)
        // Armor
        for ((variant: HTArmorVariant, armor: HTDeferredItem<*>) in armors) {
            val beforeArmor: ItemLike = HTArmorVariant.ARMOR_TABLE[variant, beforeMaterial] ?: continue
            upgradeFactory(armor, beforeArmor)
        }
        // Tool
        for ((variant: HTToolVariant, tool: HTDeferredItem<*>) in RagiumItems.TOOLS.column(material)) {
            val beforeTool: ItemLike = when (variant) {
                is HTVanillaToolVariant -> HTVanillaToolVariant.TOOL_TABLE[variant, beforeMaterial]
                is HTHammerToolVariant -> RagiumItems.TOOLS[variant, beforeMaterial]
                is HTKnifeToolVariant -> RagiumDelightAddon.KNIFE_MAP[beforeMaterial]
                else -> null
            } ?: continue
            upgradeFactory(tool, beforeTool)
        }
    }

    @JvmStatic
    private fun molten() {
        // Crimson
        HTShapedRecipeBuilder
            .equipment(RagiumItems.BLAST_CHARGE, 8)
            .hollow8()
            .define('A', Tags.Items.GUNPOWDERS)
            .define('B', HTItemMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL)
            .save(output)
        // Warped
        HTShapedRecipeBuilder
            .equipment(RagiumItems.TELEPORT_KEY)
            .cross8()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .define('B', HTItemMaterialVariant.GEM, RagiumMaterialType.WARPED_CRYSTAL)
            .define('C', Items.TRIAL_KEY)
            .save(output)

        resetComponent(RagiumItems.TELEPORT_KEY, RagiumDataComponents.FLUID_CONTENT, RagiumDataComponents.TELEPORT_POS)
        // Eldritch
        HTShapedRecipeBuilder
            .equipment(RagiumItems.ELDRITCH_EGG)
            .hollow4()
            .define('A', HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .define('B', Tags.Items.EGGS)
            .save(output)

        HTShapedRecipeBuilder
            .equipment(RagiumItems.UNIVERSAL_BUNDLE)
            .pattern(
                "ABA",
                "BCB",
                "BBB",
            ).define('A', RagiumItems.SYNTHETIC_FIBER)
            .define('B', RagiumItems.SYNTHETIC_LEATHER)
            .define('C', HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .save(output)

        for (variant: HTColorMaterial in HTColorMaterial.entries) {
            HTShapelessRecipeBuilder(CraftingBookCategory.EQUIPMENT, HTUniversalBundleItem.createBundle(variant.dyeColor).toImmutable())
                .addIngredient(RagiumItems.UNIVERSAL_BUNDLE)
                .addIngredient(variant.dyeTag)
                .savePrefixed(output, "${variant.materialName()}_")
        }

        resetComponent(RagiumItems.UNIVERSAL_BUNDLE, RagiumDataComponents.COLOR)
    }

    @JvmStatic
    private fun forgeHammers() {
        fun hammer(material: HTMaterialType): ItemLike = RagiumItems.getTool(HTHammerToolVariant, material)

        fun crafting(variant: HTMaterialVariant.ItemTag, material: HTMaterialType) {
            HTShapedRecipeBuilder
                .equipment(hammer(material))
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
            HTComponentTier.ELITE,
            hammer(RagiumMaterialType.RAGI_CRYSTAL),
            hammer(RagiumMaterialType.RAGI_ALLOY),
        ).addIngredient(HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .save(output)
    }

    @JvmStatic
    private fun lootTickets() {
        // End City
        addLootTicket(HTDefaultLootTickets.END_CITY) {
            addIngredient(Items.PURPUR_BLOCK)
            addIngredient(Items.SHULKER_SHELL)
        }
        // Simple Dungeon
        addLootTicket(HTDefaultLootTickets.DUNGEON) {
            addIngredient(Tags.Items.COBBLESTONES_MOSSY)
            addIngredient(Items.ROTTEN_FLESH)
        }
        // Mineshaft
        addLootTicket(HTDefaultLootTickets.MINESHAFT) {
            addIngredient(ItemTags.PLANKS)
            addIngredient(ItemTags.RAILS)
        }
        // Nether Fortress
        addLootTicket(HTDefaultLootTickets.NETHER_FORTRESS) {
            addIngredient(Items.NETHER_BRICKS)
            addIngredient(Tags.Items.CROPS_NETHER_WART)
        }

        // Desert Pyramid
        addLootTicket(HTDefaultLootTickets.DESERT_PYRAMID) {
            addIngredient(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            addIngredient(HTItemMaterialVariant.INGOT, HTVanillaMaterialType.GOLD)
        }
        // Jungle Temple
        addLootTicket(HTDefaultLootTickets.TEMPLE) {
            addIngredient(Tags.Items.COBBLESTONES_MOSSY)
            addIngredient(Items.VINE)
        }
        // Igloo Chest
        addLootTicket(HTDefaultLootTickets.IGLOO) {
            addIngredient(Items.SNOW_BLOCK)
            addIngredient(ItemTags.BEDS)
        }
        // Mansion
        addLootTicket(HTDefaultLootTickets.MANSION) {
            addIngredient(Items.DARK_OAK_PLANKS)
            addIngredient(HTItemMaterialVariant.GEM, HTVanillaMaterialType.EMERALD)
        }

        // Buried Treasure
        addLootTicket(HTDefaultLootTickets.BURIED_TREASURE) {
            addIngredient(Tags.Items.SANDS_COLORLESS)
            addIngredient(Items.PUFFERFISH)
        }
        // Shipwreck
        addLootTicket(HTDefaultLootTickets.SHIPWRECK) {
            addIngredient(Tags.Items.CHESTS_WOODEN)
            addIngredient(Items.KELP)
        }
        // Bastion Remnant
        addLootTicket(HTDefaultLootTickets.BASTION_REMNANT) {
            addIngredient(Items.BLACKSTONE)
            addIngredient(HTItemMaterialVariant.INGOT, HTVanillaMaterialType.GOLD)
        }
        // Ancient City
        addLootTicket(HTDefaultLootTickets.ANCIENT_CITY) {
            addIngredient(Items.DEEPSLATE_TILES)
            addIngredient(HTItemMaterialVariant.GEM, HTVanillaMaterialType.ECHO)
        }
        // Ruined Portal
        addLootTicket(HTDefaultLootTickets.RUINED_PORTAL) {
            addIngredient(Tags.Items.OBSIDIANS_NORMAL)
            addIngredient(Tags.Items.CROPS_NETHER_WART)
        }
    }

    //    Extension    //

    @JvmStatic
    private fun addTemplate(template: ItemLike, material: HTMaterialType) {
        HTShapedRecipeBuilder
            .equipment(template)
            .pattern(
                "A A",
                "A A",
                " A ",
            ).define('A', HTItemMaterialVariant.INGOT, material)
            .save(output)

        HTShapelessRecipeBuilder
            .equipment(template, 2)
            .addIngredient(template)
            .addIngredient(HTItemMaterialVariant.INGOT, material)
            .addIngredient(HTItemMaterialVariant.INGOT, material)
            .saveSuffixed(output, "_duplicate")
    }

    @JvmStatic
    private fun addAzureSmithing(output: ItemLike, ingredient: ItemLike) {
        HTSmithingRecipeBuilder
            .create(output)
            .addIngredient(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(ingredient)
            .addIngredient(HTItemMaterialVariant.INGOT, RagiumMaterialType.AZURE_STEEL)
            .save(this.output)
    }

    @JvmStatic
    private fun addDeepSmithing(output: ItemLike, ingredient: ItemLike) {
        HTSmithingRecipeBuilder
            .create(output)
            .addIngredient(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .addIngredient(ingredient)
            .addIngredient(HTItemMaterialVariant.INGOT, RagiumMaterialType.DEEP_STEEL)
            .save(this.output)
    }

    @JvmStatic
    private inline fun addLootTicket(lootTicket: HTDefaultLootTickets, builderAction: HTShapelessRecipeBuilder.() -> Unit) {
        HTShapelessRecipeBuilder(CraftingBookCategory.EQUIPMENT, HTDefaultLootTickets.getLootTicket(lootTicket).toImmutable())
            .addIngredient(RagiumItems.LOOT_TICKET)
            .apply(builderAction)
            .saveSuffixed(output, "/${lootTicket.name.lowercase()}")
    }
}
