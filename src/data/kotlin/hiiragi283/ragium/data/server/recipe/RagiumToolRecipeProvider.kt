package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.item.tool.HTUniversalBundleItem
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.recipe.HTUpgradeBlastChargeRecipe
import hiiragi283.ragium.common.tier.HTCircuitTier
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.util.HTDefaultLootTickets
import hiiragi283.ragium.common.variant.HTArmorVariant
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
            ).define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .define('B', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('C', CommonMaterialPrefixes.CIRCUIT, HTCircuitTier.BASIC)
            .save(output)

        HTShapedRecipeBuilder
            .equipment(RagiumItems.POTION_BUNDLE)
            .pattern(
                " A ",
                "BBB",
            ).define('A', RagiumItems.SYNTHETIC_FIBER)
            .define('B', Items.GLASS_BOTTLE)
            .save(output)

        HTSingleItemRecipeBuilder
            .stonecutter(RagiumItems.SLOT_COVER, 3)
            .addIngredient(Items.SMOOTH_STONE_SLAB)
            .save(output)

        HTShapelessRecipeBuilder
            .equipment(RagiumItems.TRADER_CATALOG)
            .addIngredient(Items.BOOK)
            .addIngredient(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.EMERALD)
            .save(output)

        HTShapedRecipeBuilder
            .equipment(RagiumItems.ECHO_STAR)
            .cross8()
            .define('A', CommonMaterialPrefixes.DUST, VanillaMaterialKeys.ECHO)
            .define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.ECHO)
            .define('C', CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.DEEP_STEEL)
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
            .equipment(RagiumItems.MAGNET)
            .pattern(
                "A A",
                "B B",
                " C ",
            ).define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .define('B', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('C', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
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
            .define('A', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .define('B', Items.LANTERN)
            .save(output)

        HTShapedRecipeBuilder
            .equipment(RagiumItems.LOOT_TICKET)
            .cross8()
            .define('A', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .define('B', Tags.Items.DYES_RED)
            .define('C', Items.PAPER)
            .save(output)

        HTShapedRecipeBuilder
            .equipment(RagiumItems.NIGHT_VISION_GOGGLES)
            .pattern(
                "AAA",
                "ABA",
            ).define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .save(output)
    }

    @JvmStatic
    private fun azureAndDeepSteel() {
        HTShapedRecipeBuilder
            .misc(RagiumItems.BLUE_KNOWLEDGE)
            .cross8()
            .define('A', CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.LAPIS)
            .define('B', CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST)
            .define('C', Items.BOOK)
            .save(output)

        HTShapedRecipeBuilder
            .misc(RagiumItems.BLUE_KNOWLEDGE)
            .cross8()
            .define('A', CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.LAPIS)
            .define('B', CommonMaterialPrefixes.GEM, VanillaMaterialKeys.AMETHYST)
            .define('C', Items.ENCHANTED_BOOK)
            .saveSuffixed(output, "_alt")

        addEquipments(RagiumMaterialKeys.AZURE_STEEL, VanillaMaterialKeys.IRON)
        addEquipments(RagiumMaterialKeys.DEEP_STEEL, VanillaMaterialKeys.DIAMOND)
        addEquipments(RagiumMaterialKeys.NIGHT_METAL, VanillaMaterialKeys.GOLD)

        HTShapedRecipeBuilder
            .misc(Items.HEAVY_CORE)
            .cross8()
            .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL)
            .define('B', CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.NETHERITE)
            .define('C', CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.NIGHT_METAL)
            .save(output)
    }

    @JvmStatic
    private fun addEquipments(material: HTMaterialLike, beforeKey: HTMaterialKey) {
        // Template
        val upgrade: ItemLike = RagiumItems.getSmithingTemplate(material)
        HTShapedRecipeBuilder
            .equipment(upgrade)
            .pattern(
                "A A",
                "A A",
                " A ",
            ).define('A', CommonMaterialPrefixes.INGOT, material)
            .save(output)

        HTShapelessRecipeBuilder
            .equipment(upgrade, 2)
            .addIngredient(upgrade)
            .addIngredients(CommonMaterialPrefixes.INGOT, material, 2)
            .saveSuffixed(output, "_duplicate")
        // Armor
        for ((variant: HTArmorVariant, armor: ItemLike) in RagiumItems.getArmorMap(material)) {
            val beforeArmor: ItemLike = VanillaMaterialKeys.ARMOR_TABLE[variant, beforeKey] ?: continue
            HTSmithingRecipeBuilder
                .create(armor)
                .addIngredient(upgrade)
                .addIngredient(beforeArmor)
                .addIngredient(CommonMaterialPrefixes.INGOT, material)
                .save(this.output)
        }
        // Tool
        for ((variant: HTToolVariant, tool: ItemLike) in RagiumItems.getToolMap(material)) {
            val beforeTool: ItemLike = VanillaMaterialKeys.TOOL_TABLE[variant, beforeKey] ?: continue
            HTSmithingRecipeBuilder
                .create(tool)
                .addIngredient(upgrade)
                .addIngredient(beforeTool)
                .addIngredient(CommonMaterialPrefixes.INGOT, material)
                .save(this.output)
        }
    }

    @JvmStatic
    private fun molten() {
        // Crimson
        HTShapedRecipeBuilder
            .equipment(RagiumItems.BLAST_CHARGE, 8)
            .hollow8()
            .define('A', Tags.Items.GUNPOWDERS)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.CRIMSON_CRYSTAL)
            .save(output)

        save(
            RagiumAPI.id("shapeless/upgrade_blast_charge"),
            HTUpgradeBlastChargeRecipe(CraftingBookCategory.EQUIPMENT),
        )
        // Warped
        HTShapedRecipeBuilder
            .equipment(RagiumItems.TELEPORT_KEY)
            .cross8()
            .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .define('B', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.WARPED_CRYSTAL)
            .define('C', Items.TRIAL_KEY)
            .save(output)

        resetComponent(RagiumItems.TELEPORT_KEY, RagiumDataComponents.FLUID_CONTENT, RagiumDataComponents.TELEPORT_POS)
        // Eldritch
        HTShapedRecipeBuilder
            .equipment(RagiumItems.ELDRITCH_EGG)
            .hollow4()
            .define('A', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
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
            .define('C', CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
            .save(output)

        for (variant: HTColorMaterial in HTColorMaterial.entries) {
            val bundle: ImmutableItemStack = HTUniversalBundleItem.createBundle(variant.dyeColor).toImmutable() ?: return
            HTShapelessRecipeBuilder(CraftingBookCategory.EQUIPMENT, bundle)
                .addIngredient(RagiumItems.UNIVERSAL_BUNDLE)
                .addIngredient(variant.dyeTag)
                .savePrefixed(output, "${variant.asMaterialName()}_")
        }

        resetComponent(RagiumItems.UNIVERSAL_BUNDLE, RagiumDataComponents.COLOR)
    }

    @JvmStatic
    private fun forgeHammers() {
        fun crafting(prefix: HTPrefixLike, key: HTMaterialLike) {
            HTShapedRecipeBuilder
                .equipment(RagiumItems.getHammer(key))
                .pattern(
                    " AA",
                    "BBA",
                    " AA",
                ).define('A', prefix, key)
                .define('B', Tags.Items.RODS_WOODEN)
                .save(output)
        }
        crafting(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)

        createComponentUpgrade(
            HTComponentTier.ELITE,
            RagiumItems.getHammer(RagiumMaterialKeys.RAGI_CRYSTAL),
            RagiumItems.getHammer(RagiumMaterialKeys.RAGI_ALLOY),
        ).addIngredient(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
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
            addIngredient(CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.GOLD)
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
            addIngredient(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.EMERALD)
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
            addIngredient(CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.GOLD)
        }
        // Ancient City
        addLootTicket(HTDefaultLootTickets.ANCIENT_CITY) {
            addIngredient(Items.DEEPSLATE_TILES)
            addIngredient(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.ECHO)
        }
        // Ruined Portal
        addLootTicket(HTDefaultLootTickets.RUINED_PORTAL) {
            addIngredient(Tags.Items.OBSIDIANS_NORMAL)
            addIngredient(Tags.Items.CROPS_NETHER_WART)
        }
    }

    //    Extension    //

    @JvmStatic
    private inline fun addLootTicket(lootTicket: HTDefaultLootTickets, builderAction: HTShapelessRecipeBuilder.() -> Unit) {
        val ticket: ImmutableItemStack = HTDefaultLootTickets.getLootTicket(lootTicket).toImmutable() ?: return
        HTShapelessRecipeBuilder(CraftingBookCategory.EQUIPMENT, ticket)
            .addIngredient(RagiumItems.LOOT_TICKET)
            .apply(builderAction)
            .saveSuffixed(output, "/${lootTicket.name.lowercase()}")
    }
}
