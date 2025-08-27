package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.data.HTLanguageProvider
import hiiragi283.ragium.api.registry.HTSimpleDeferredBlockHolder
import hiiragi283.ragium.api.storage.HTTransferIO
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import hiiragi283.ragium.data.server.advancement.RagiumAdvancements
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.integration.replication.RagiumReplicationAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumEnchantments
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.variant.HTColorVariant
import hiiragi283.ragium.util.variant.HTDecorationVariant
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.data.PackOutput

class RagiumEnglishProvider(output: PackOutput) : HTLanguageProvider.English(output) {
    override fun addTranslations() {
        advancement()
        block()
        enchantment()
        entity()
        fluid()
        item()
        itemGroup()
        modTags()
        text()
        information()

        delight()
        jade()
        mekanism()
        replication()
    }

    private fun advancement() {
        addAdvancement(RagiumAdvancements.ROOT, "Ragium", "Welcome to Ragium!")
        addAdvancement(RagiumAdvancements.CRAFTABLE_TEMPLATES, "This thing craftable...!", "Craft any Upgrade Templates added by Ragium")
        // Raginite
        addAdvancement(RagiumAdvancements.RAGINITE, "Not a Redstone", "Get Raginite Dust from Raginite Ores in underground")
        addAdvancement(RagiumAdvancements.RAGI_ALLOY, "0xFF003F", "Get Ragi-Alloy Ingot")

        addAdvancement(RagiumAdvancements.RAGI_CHERRY, "Food of twins", "Get Ragi-Cherry")
        addAdvancement(RagiumAdvancements.RAGI_CHERRY_TOAST, "The Last Breakfast", "Get Ragi-Cherry Toast Tower")

        addAdvancement(RagiumAdvancements.ADV_RAGI_ALLOY, "This is red, not orange!", "Get Advanced Ragi-Alloy Ingot")

        addAdvancement(RagiumAdvancements.RAGI_CRYSTAL, "Not a Energium", "Get Ragi-Crystal")
        addAdvancement(
            RagiumAdvancements.RAGI_CRYSTAL_HAMMER,
            "Hammer of Destruction",
            "Get Ragi-Crystal Hammer to break ANY blocks!",
        )
        addAdvancement(RagiumAdvancements.RAGI_TICKET, "Good Old Halcyon Days?", "Get Ragi-Ticket to roll treasure chests")
        // Azure
        addAdvancement(RagiumAdvancements.AZURE_SHARD, "Not a azurite", "Get Azure Shard")
        addAdvancement(RagiumAdvancements.AZURE_STEEL, "The steel is bluish.", "Get Azure Steel Ingot")
        addAdvancement(RagiumAdvancements.AZURE_GEARS, "Wake up! Azure Dragon!", "Get any Azure Steel Tool or Armor")
        // Crimson
        addAdvancement(RagiumAdvancements.CRIMSON_CRYSTAL, "Chao!", "Get Crimson Crystal")
        addAdvancement(
            RagiumAdvancements.CRIMSON_SOIL,
            "The reason why rose is red",
            "Get Crimson Soil by right-clicking Soul Soil with Bloody Ticket",
        )
        // Warped
        addAdvancement(RagiumAdvancements.WARPED_CRYSTAL, "Stabilized Warp", "Get Warped Crystal")
        addAdvancement(RagiumAdvancements.DIM_ANCHOR, "Remote Work", "Place Dimensional Anchor to force loading the chunk")
        addAdvancement(RagiumAdvancements.TELEPORT_KEY, "Lock Open!", "Use teleport key to teleport linked position")
        // Eldritch
        addAdvancement(RagiumAdvancements.ELDRITCH_PEARL, "Not a Primordial", "Get Eldritch Pearl")
        addAdvancement(RagiumAdvancements.ELDRITCH_EGG, "Gotcha!", "Throw Eldritch Egg to capture mobs")
        addAdvancement(
            RagiumAdvancements.MYSTERIOUS_OBSIDIAN,
            "Who is Falling Meteorites?",
            "Get Mysterious Obsidian by right-clicking Crying Obsidian with Eldritch Ticket",
        )
        // Iridescentium
        addAdvancement(RagiumAdvancements.IRIDESCENTIUM, "The sky's the limit", "Get Iridescentium Ingot")
        addAdvancement(RagiumAdvancements.ETERNAL_COMPONENT, "Eternal Requiem", "Get Eternal Component for making tools unbreakable")
    }

    private fun block() {
        addBlock(RagiumBlocks.SILT, "Silt")
        addBlock(RagiumBlocks.MYSTERIOUS_OBSIDIAN, "Mysterious Obsidian")
        addBlock(RagiumBlocks.CRIMSON_SOIL, "Crimson Soil")

        addBlock(RagiumBlocks.ASH_LOG, "Ash Log")
        addBlock(RagiumBlocks.EXP_BERRIES, "Exp Berries Bush")
        addItem(RagiumBlocks.EXP_BERRIES.itemHolder, "Exp Berries")
        addBlock(RagiumBlocks.WARPED_WART, "Warped Wart")

        addBlock(RagiumBlocks.RESONANT_DEBRIS, "Resonant Debris")

        addBlock(RagiumBlocks.RAGI_BRICKS, "Ragi-Bricks")
        addBlock(RagiumBlocks.AZURE_TILES, "Azure Tiles")
        addBlock(RagiumBlocks.ELDRITCH_STONE, "Eldritch Stone")
        addBlock(RagiumBlocks.POLISHED_ELDRITCH_STONE, "Polished Eldritch Stone")
        addBlock(RagiumBlocks.POLISHED_ELDRITCH_STONE_BRICKS, "Polished Eldritch Stone Bricks")
        addBlock(RagiumBlocks.PLASTIC_BRICKS, "Plastic Bricks")
        addBlock(RagiumBlocks.PLASTIC_TILES, "Plastic Tiles")
        addBlock(RagiumBlocks.BLUE_NETHER_BRICKS, "Blue Nether Bricks")
        addBlock(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")

        for (variant: HTDecorationVariant in HTDecorationVariant.entries) {
            addBlock(variant.slab, variant.translate(type, "Slab"))
            addBlock(variant.stairs, variant.translate(type, "Stairs"))
            addBlock(variant.wall, variant.translate(type, "Wall"))
        }

        for ((color: HTColorVariant, block: HTSimpleDeferredBlockHolder) in RagiumBlocks.LED_BLOCKS) {
            addBlock(block, "${color.getTranslatedName(type)} LED Block")
        }

        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

        // Dynamo
        // Machine
        addBlock(RagiumBlocks.ADVANCED_MACHINE_FRAME, "Advanced Machine Frame")
        addBlock(RagiumBlocks.BASIC_MACHINE_FRAME, "Basic Machine Frame")
        addBlock(RagiumBlocks.ELITE_MACHINE_FRAME, "Elite Machine Frame")
        // Device
        addBlock(RagiumBlocks.DEVICE_CASING, "Device Casing")
        addBlock(RagiumBlocks.STONE_CASING, "Stone Casing")
        addBlock(RagiumBlocks.REINFORCED_STONE_CASING, "Reinforced Stone Casing")
        addBlock(RagiumBlocks.WOODEN_CASING, "Wooden Casing")
        // Storage
    }

    private fun enchantment() {
        addEnchantment(RagiumEnchantments.CAPACITY, "Capacity", "Increase the capacity of item or fluid storages.")
        addEnchantment(RagiumEnchantments.NOISE_CANCELING, "Noise Canceling", "Increases damage against sculk mobs such as Warden.")
        addEnchantment(RagiumEnchantments.SONIC_PROTECTION, "Sonic Protection", "Immune damage from sonic boom.")
    }

    private fun entity() {
        addEntityType(RagiumEntityTypes.BLAST_CHARGE, "Blast Charge")
        addEntityType(RagiumEntityTypes.ELDRITCH_EGG, "Thrown Eldritch Egg")

        // addEntityType(RagiumEntityTypes.DYNAMITE, "Dynamite")
        // addEntityType(RagiumEntityTypes.DEFOLIANT_DYNAMITE, "Defoliant Dynamite")
        // addEntityType(RagiumEntityTypes.FLATTEN_DYNAMITE, "Flatten Dynamite")
        // addEntityType(RagiumEntityTypes.NAPALM_DYNAMITE, "Napalm Dynamite")
        // addEntityType(RagiumEntityTypes.POISON_DYNAMITE, "Poison Dynamite")
    }

    private fun fluid() {
        addFluid(RagiumFluidContents.HONEY, "Honey")
        addFluid(RagiumFluidContents.EXPERIENCE, "Experience Liquid")
        addFluid(RagiumFluidContents.MUSHROOM_STEW, "Mushroom Stew")

        addFluid(RagiumFluidContents.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluidContents.LPG, "LPG")
        addFluid(RagiumFluidContents.NAPHTHA, "Naphtha")
        addFluid(RagiumFluidContents.DIESEL, "Diesel")
        addFluid(RagiumFluidContents.BLOOD_DIESEL, "Bloo-Diesel")
        addFluid(RagiumFluidContents.LUBRICANT, "Lubricant")

        addFluid(RagiumFluidContents.SAP, "Sap")
        addFluid(RagiumFluidContents.CRIMSON_SAP, "Crimson Sap")
        addFluid(RagiumFluidContents.WARPED_SAP, "Warped Sap")
    }

    private fun item() {
        // Material
        addItem(RagiumItems.BASALT_MESH, "Basalt Mesh")
        addItem(RagiumItems.COMPRESSED_SAWDUST, "Compressed Sawdust")
        addItem(RagiumItems.DEEP_SCRAP, "Deep Scrap")
        addItem(RagiumItems.ELDER_HEART, "Heart of the Elder")
        addItem(RagiumItems.ELDRITCH_GEAR, "Eldritch Gear")
        addItem(RagiumItems.RAGI_COKE, "Ragi-Coke")
        addItem(RagiumItems.RESIN, "Resin")
        addItem(RagiumItems.SILICON, "Indigo Silicon")
        addItem(RagiumItems.TAR, "Tar")

        addPatterned()
        // Armor
        // Tool
        addItem(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE, "Azure Steel Upgrade")
        addItem(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE, "Deep Steel Upgrade")

        addItem(RagiumItems.MEDIUM_DRUM_UPGRADE, "Medium Drum Upgrade")
        addItem(RagiumItems.LARGE_DRUM_UPGRADE, "Large Drum Upgrade")
        addItem(RagiumItems.HUGE_DRUM_UPGRADE, "Huge Drum Upgrade")

        addItem(RagiumItems.DRILL, "Electric Drill")

        addItem(RagiumItems.ETERNAL_COMPONENT, "Eternal Component")

        addItem(RagiumItems.ADVANCED_RAGI_MAGNET, "Advanced Ragi-Magnet")
        addItem(RagiumItems.BLAST_CHARGE, "Blast Charge")
        addItem(RagiumItems.ELDRITCH_EGG, "Eldritch Egg")
        addItem(RagiumItems.POTION_BUNDLE, "Potion Bundle")
        addItem(RagiumItems.RAGI_LANTERN, "Ragi-Lantern")
        addItem(RagiumItems.RAGI_MAGNET, "Ragi-Magnet")
        addItem(RagiumItems.RAGI_TICKET, "Ragi-Ticket")
        addItem(RagiumItems.SLOT_COVER, "Slot Cover")
        addItem(RagiumItems.TELEPORT_KEY, "Teleport Key")
        addItem(RagiumItems.TRADER_CATALOG, "Wandering Trader's Catalog")
        addItem(RagiumItems.UNIVERSAL_BUNDLE, "Universal Backpack")
        // Food
        addItem(RagiumItems.ICE_CREAM, "Ice Cream")
        addItem(RagiumItems.ICE_CREAM_SODA, "Ice Cream Soda")

        addItem(RagiumItems.MINCED_MEAT, "Minced Meat")
        addItem(RagiumItems.CANNED_COOKED_MEAT, "Canned Cooked Meat")

        addItem(RagiumItems.SWEET_BERRIES_CAKE_SLICE, "Slice of Sweet Berries Cake")
        addItem(RagiumItems.MELON_PIE, "Melon Pie")

        addItem(RagiumItems.RAGI_CHERRY, "Ragi-Cherry")
        addItem(RagiumItems.FEVER_CHERRY, "Fever Cherry")

        addItem(RagiumItems.BOTTLED_BEE, "Bottled Bee")
        addItem(RagiumItems.AMBROSIA, "Ambrosia")
        // Parts
        addItem(RagiumItems.ADVANCED_CIRCUIT_BOARD, "Basalt-Reinforced Circuit Board")
        addItem(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        addItem(RagiumItems.LED, "Light Emitting Diode")
        addItem(RagiumItems.LUMINOUS_PASTE, "Luminous Paste")
        addItem(RagiumItems.POLYMER_RESIN, "Polymer Resin")
        addItem(RagiumItems.REDSTONE_BOARD, "Redstone Board")
        addItem(RagiumItems.SOLAR_PANEL, "Solar Panel")
        addItem(RagiumItems.SYNTHETIC_FIBER, "Synthetic Fiber")
        addItem(RagiumItems.SYNTHETIC_LEATHER, "Synthetic Leather")
    }

    private fun itemGroup() {
        addItemGroup(RagiumCreativeTabs.BLOCKS, "Ragium - Blocks")
        addItemGroup(RagiumCreativeTabs.INGREDIENTS, "Ragium - Ingredients")
        addItemGroup(RagiumCreativeTabs.ITEMS, "Ragium - Items")
    }

    /*private fun material() {
        // Common
        addMaterialKey(CommonMaterials.ALUMINA, "Alumina")
        addMaterialKey(CommonMaterials.ALUMINUM, "Aluminum")
        addMaterialKey(CommonMaterials.ANTIMONY, "Antimony")
        addMaterialKey(CommonMaterials.BERYLLIUM, "Beryllium")
        addMaterialKey(CommonMaterials.ASH, "Ash")
        addMaterialKey(CommonMaterials.BAUXITE, "Bauxite")
        addMaterialKey(CommonMaterials.BRASS, "Brass")
        addMaterialKey(CommonMaterials.BRONZE, "Bronze")
        addMaterialKey(CommonMaterials.CADMIUM, "Cadmium")
        addMaterialKey(CommonMaterials.CARBON, "Carbon")
        addMaterialKey(CommonMaterials.CHEESE, "Cheese")
        addMaterialKey(CommonMaterials.CHOCOLATE, "Chocolate")
        addMaterialKey(CommonMaterials.CHROMIUM, "Chromium")
        addMaterialKey(CommonMaterials.COAL_COKE, "Coal Coke")
        addMaterialKey(CommonMaterials.CONSTANTAN, "Constantan")
        addMaterialKey(CommonMaterials.CRYOLITE, "Cryolite")
        addMaterialKey(CommonMaterials.ELECTRUM, "Electrum")
        addMaterialKey(CommonMaterials.FLUORITE, "Fluorite")
        addMaterialKey(CommonMaterials.INVAR, "Invar")
        addMaterialKey(CommonMaterials.IRIDIUM, "Iridium")
        addMaterialKey(CommonMaterials.LEAD, "Lead")
        addMaterialKey(CommonMaterials.NICKEL, "Nickel")
        addMaterialKey(CommonMaterials.NIOBIUM, "Niobium")
        addMaterialKey(CommonMaterials.OSMIUM, "Osmium")
        addMaterialKey(CommonMaterials.PERIDOT, "Peridot")
        addMaterialKey(CommonMaterials.PLATINUM, "Platinum")
        addMaterialKey(CommonMaterials.PLUTONIUM, "Plutonium")
        addMaterialKey(CommonMaterials.PYRITE, "Gold")
        addMaterialKey(CommonMaterials.RUBY, "Ruby")
        addMaterialKey(CommonMaterials.SALT, "Salt")
        addMaterialKey(CommonMaterials.SALTPETER, "Saltpeter")
        addMaterialKey(CommonMaterials.SAPPHIRE, "Sapphire")
        addMaterialKey(CommonMaterials.SILICON, "Silicon")
        addMaterialKey(CommonMaterials.SILVER, "Silver")
        addMaterialKey(CommonMaterials.SOLDERING_ALLOY, "Soldering Alloy")
        addMaterialKey(CommonMaterials.STAINLESS_STEEL, "Stainless Steel")
        addMaterialKey(CommonMaterials.STEEL, "Steel")
        addMaterialKey(CommonMaterials.SULFUR, "Sulfur")
        addMaterialKey(CommonMaterials.SUPERCONDUCTOR, "Superconductor")
        addMaterialKey(CommonMaterials.TIN, "Tin")
        addMaterialKey(CommonMaterials.TITANIUM, "Titanium")
        addMaterialKey(CommonMaterials.TUNGSTEN, "Tungsten")
        addMaterialKey(CommonMaterials.URANIUM, "Uranium")
        addMaterialKey(CommonMaterials.ZINC, "Zinc")
        // AA
        addMaterialKey(IntegrationMaterials.BLACK_QUARTZ, "Black Quartz")
        // AE2
        addMaterialKey(IntegrationMaterials.CERTUS_QUARTZ, "Certus Quartz")
        addMaterialKey(IntegrationMaterials.FLUIX, "Fluix")
        // Create
        addMaterialKey(IntegrationMaterials.ANDESITE_ALLOY, "Andesite Alloy")
        addMaterialKey(IntegrationMaterials.CARDBOARD, "Cardboard")
        addMaterialKey(IntegrationMaterials.ROSE_QUARTZ, "Rose Quartz")
        // EIO
        addMaterialKey(IntegrationMaterials.COPPER_ALLOY, "Copper Alloy")
        addMaterialKey(IntegrationMaterials.ENERGETIC_ALLOY, "Energetic Alloy")
        addMaterialKey(IntegrationMaterials.VIBRANT_ALLOY, "Vibrant Alloy")
        addMaterialKey(IntegrationMaterials.REDSTONE_ALLOY, "Redstone Alloy")
        addMaterialKey(IntegrationMaterials.CONDUCTIVE_ALLOY, "Conductive Alloy")
        addMaterialKey(IntegrationMaterials.PULSATING_ALLOY, "Pulsating Alloy")
        addMaterialKey(IntegrationMaterials.DARK_STEEL, "Dark Steel")
        addMaterialKey(IntegrationMaterials.SOULARIUM, "Soularium")
        addMaterialKey(IntegrationMaterials.END_STEEL, "End Steel")
        // EvilCraft
        addMaterialKey(IntegrationMaterials.DARK_GEM, "Dark Gem")
        // IE
        addMaterialKey(IntegrationMaterials.HOP_GRAPHITE, "HOP Graphite")
        // Mek
        addMaterialKey(IntegrationMaterials.REFINED_GLOWSTONE, "Refined Glowstone")
        addMaterialKey(IntegrationMaterials.REFINED_OBSIDIAN, "Refined Obsidian")
        // Twilight
        addMaterialKey(IntegrationMaterials.CARMINITE, "Carminite")
        addMaterialKey(IntegrationMaterials.FIERY_METAL, "Fiery")
        addMaterialKey(IntegrationMaterials.IRONWOOD, "Ironwood")
        addMaterialKey(IntegrationMaterials.KNIGHTMETAL, "Knightmetal")
        addMaterialKey(IntegrationMaterials.STEELEAF, "Steeleaf")
        // Ragium
        addMaterialKey(RagiumMaterials.ADVANCED_RAGI_ALLOY, "Advanced Ragi-Alloy")
        addMaterialKey(RagiumMaterials.AZURE_STEEL, "Azure Steel")
        addMaterialKey(RagiumMaterials.CRIMSON_CRYSTAL, "Crimson Crystal")
        addMaterialKey(RagiumMaterials.DEEP_STEEL, "Deep Steel")
        addMaterialKey(RagiumMaterials.RAGI_ALLOY, "Ragi-Alloy")
        addMaterialKey(RagiumMaterials.RAGI_CRYSTAL, "Ragi-Crystal")
        addMaterialKey(RagiumMaterials.RAGINITE, "Raginite")
        addMaterialKey(RagiumMaterials.WARPED_CRYSTAL, "Warped Crystal")
        // Vanilla
        addMaterialKey(VanillaMaterials.AMETHYST, "Amethyst")
        addMaterialKey(VanillaMaterials.CALCITE, "Calcite")
        addMaterialKey(VanillaMaterials.COAL, "Coal")
        addMaterialKey(VanillaMaterials.COPPER, "Copper")
        addMaterialKey(VanillaMaterials.DIAMOND, "Diamond")
        addMaterialKey(VanillaMaterials.EMERALD, "Emerald")
        addMaterialKey(VanillaMaterials.ENDER_PEARL, "Ender Pearl")
        addMaterialKey(VanillaMaterials.GLOWSTONE, "Glowstone")
        addMaterialKey(VanillaMaterials.GOLD, "Gold")
        addMaterialKey(VanillaMaterials.IRON, "Iron")
        addMaterialKey(VanillaMaterials.LAPIS, "Lapis")
        addMaterialKey(VanillaMaterials.NETHERITE, "Netherite")
        addMaterialKey(VanillaMaterials.NETHERITE_SCRAP, "Netherite Scrap")
        addMaterialKey(VanillaMaterials.OBSIDIAN, "Obsidian")
        addMaterialKey(VanillaMaterials.QUARTZ, "Quartz")
        addMaterialKey(VanillaMaterials.REDSTONE, "Redstone")
        addMaterialKey(VanillaMaterials.WOOD, "Wood")
    }

    private fun tagPrefix() {
        addTagPrefix(HTTagPrefixes.CLUMP, "%s Clump")
        addTagPrefix(HTTagPrefixes.CRYSTAL, "%s Crystal")
        addTagPrefix(HTTagPrefixes.DIRTY_DUST, "Dirty %s Dust")
        addTagPrefix(HTTagPrefixes.DUST, "%s Dust")
        addTagPrefix(HTTagPrefixes.GEAR, "%s Gear")
        addTagPrefix(HTTagPrefixes.GEM, "%s")
        addTagPrefix(HTTagPrefixes.INGOT, "%s Ingot")
        addTagPrefix(HTTagPrefixes.NUGGET, "%s Nugget")
        addTagPrefix(HTTagPrefixes.ORE, "%s Ore")
        addTagPrefix(HTTagPrefixes.PLATE, "%s Plate")
        addTagPrefix(HTTagPrefixes.RAW_MATERIAL, "Raw %s")
        addTagPrefix(HTTagPrefixes.RAW_STORAGE, "Block of Raw %s")
        addTagPrefix(HTTagPrefixes.ROD, "%s Rod")
        addTagPrefix(HTTagPrefixes.SHARD, "%s Shard")
        addTagPrefix(HTTagPrefixes.SHEETMETAL, "%s Sheetmetal")
        addTagPrefix(HTTagPrefixes.STORAGE_BLOCK, "Block of %s")
        addTagPrefix(HTTagPrefixes.TINY_DUST, "Tiny %s Dust")
        addTagPrefix(HTTagPrefixes.WIRE, "%s Wire")
    }*/

    private fun modTags() {
        add(RagiumModTags.Blocks.LED_BLOCKS, "LED Blocks")
        add(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES, "Replaceable by Resonant Debris")
        add(RagiumModTags.Blocks.WIP, "Work In Progress")

        add(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST, "Blacklisted mobs for capturing")
        add(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS, "Generate Resonant Debris")
        add(RagiumModTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING, "Sensitive to Noise Canceling")

        add(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, "Eldritch Pearl Binders")
        add(RagiumModTags.Items.LED_BLOCKS, "LED Blocks")
        add(RagiumModTags.Items.PLASTICS, "Plastics")
        add(RagiumModTags.Items.POLYMER_RESIN, "Polymer Resins")
        add(RagiumModTags.Items.WIP, "Work In Progress")

        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, "Basic Fluxes for Alloy Smelter")
        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, "Advanced Fluxes for Alloy Smelter")

        add(RagiumModTags.Items.TOOLS_DRILL, "Drills")
        add(RagiumModTags.Items.TOOLS_HAMMER, "Hammers")

        add(RagiumModTags.Items.ENRICHED_RAGINITE, "Enriched Raginite")
        add(RagiumModTags.Items.ENRICHED_AZURE, "Enriched Azure Essence")
    }

    private fun text() {
        add(RagiumTranslationKeys.AZURE_STEEL_UPGRADE, "Azure Steel Upgrade")
        add(RagiumTranslationKeys.AZURE_STEEL_UPGRADE_APPLIES_TO, "Azure Steel Equipment")
        add(RagiumTranslationKeys.AZURE_STEEL_UPGRADE_INGREDIENTS, "Azure Steel Ingot")
        add(RagiumTranslationKeys.AZURE_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION, "Add iron armor, weapon, ot tool")
        add(RagiumTranslationKeys.AZURE_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "Add Azure Steel Ingot")

        add(RagiumTranslationKeys.DEEP_STEEL_UPGRADE, "Deep Steel Upgrade")
        add(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_APPLIES_TO, "Deep Steel Equipment")
        add(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_INGREDIENTS, "Deep Steel Ingot")
        add(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION, "Add diamond armor, weapon, ot tool")
        add(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "Add Deep Steel Ingot")

        add(RagiumTranslationKeys.TOOLTIP_ENERGY_PERCENTAGE, "%s / %s FE")
        add(RagiumTranslationKeys.TOOLTIP_FLUID_NAME, "%s : %s mb")
        add(RagiumTranslationKeys.TOOLTIP_FLUID_NAME_EMPTY, "Empty")
        add(RagiumTranslationKeys.TOOLTIP_INTRINSIC_ENCHANTMENT, "Always has at least %s")
        add(RagiumTranslationKeys.TOOLTIP_EFFECT_RANGE, "Effect Radius: %s blocks")
        add(RagiumTranslationKeys.TOOLTIP_LOOT_TABLE_ID, "Loot Table: %s")
        add(RagiumTranslationKeys.TOOLTIP_SHOW_INFO, "Press Shift to show info")
        add(RagiumTranslationKeys.TOOLTIP_WIP, "This content is work in progress!!")

        add(HTTransferIO.INPUT_ONLY.translationKey, "Mode: Input")
        add(HTTransferIO.OUTPUT_ONLY.translationKey, "Mode: Output")
        add(HTTransferIO.BOTH.translationKey, "Mode: Both")
        add(HTTransferIO.NONE.translationKey, "Mode: None")
    }

    private fun information() {
        addInfo(RagiumBlocks.ASH_LOG, "Drop Ash Dust when harvested.")
        addInfo(RagiumBlocks.CRIMSON_SOIL, "Mobs killed on this block also drop experience.")
        addInfo(RagiumBlocks.WARPED_WART, "Clear one bad effect randomly when eaten.")

        addInfo(HTDeviceVariant.CEU, "Unlimited Power")
        addInfo(HTDeviceVariant.DIM_ANCHOR, "Always load chunk which placed in.")
        addInfo(HTDeviceVariant.ENI, "Enabled to access Energy Network.")
        addInfo(HTDeviceVariant.EXP_COLLECTOR, "Collects around Exp Orbs.")
        addInfo(HTDeviceVariant.ITEM_BUFFER, "Item buffer with 9 slots.")
        addInfo(
            HTDeviceVariant.LAVA_COLLECTOR,
            "Generates Lava when ALL below conditions are met.",
            "- Placed in the Nether",
            "- Surrounded by 4 Lava sources",
        )
        addInfo(HTDeviceVariant.MILK_COLLECTOR, "Milking a cow on this.")
        addInfo(
            HTDeviceVariant.WATER_COLLECTOR,
            "Generates Lava when ANY below conditions are met.",
            "- Placed in Ocean-like or River-like biomes",
            "- Surrounded by 2 or more Water Sources",
        )

        val nonSilkTouch = "Can be harvested without Silk Touch."
        addInfo(RagiumBlocks.getGlass(HTVanillaMaterialType.OBSIDIAN), "As the same blast resistance as Obsidian.", "And $nonSilkTouch")
        addInfo(RagiumBlocks.getGlass(HTVanillaMaterialType.QUARTZ), nonSilkTouch)
        addInfo(RagiumBlocks.getGlass(HTVanillaMaterialType.SOUL), "Only passable with Players.", "And $nonSilkTouch")

        addInfo(RagiumItems.AMBROSIA, "ALWAYS EDIBLE and NOT CONSUMED!")
        addInfo(RagiumItems.BLAST_CHARGE, "Can be upgraded by gunpowders in Crafting Table.")
        addInfo(RagiumItems.ELDER_HEART, "Dropped from Elder Guardian.")
        addInfo(
            RagiumItems.ELDRITCH_EGG,
            "Can be throwable by right-click，and capture mobs when hit.",
            "Dropped Spawn Egg will teleport your inventory.",
        )
        addInfo(RagiumItems.ICE_CREAM, "Extinguish fire when eaten.")
        addInfo(RagiumItems.RAGI_CHERRY, "Drops from Cherry Leaves as same as Apple.")
        addInfo(RagiumItems.RAGI_LANTERN, "Light up darkness in range.")
        addInfo(RagiumItems.RAGI_MAGNET, "Collect dropped items in the effective range")
        addInfo(RagiumItems.SLOT_COVER, "Ignored by recipes when placed in machine slot.")
        addInfo(RagiumItems.TRADER_CATALOG, "Dropped from Wandering Trader.")
    }

    private fun delight() {
        addBlock(RagiumDelightAddon.RAGI_CHERRY_PIE, "Ragi-Cherry Pie")
        addBlock(RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCk, "Ragi-Cherry Toast Tower")

        addItem(RagiumDelightAddon.RAGI_CHERRY_JAM, "Ragi-Cherry Jam")
        addItem(RagiumDelightAddon.RAGI_CHERRY_PIE_SLICE, "Slice of Ragi-Cherry Pie")
        addItem(RagiumDelightAddon.RAGI_CHERRY_PULP, "Ragi-Cherry Pulp")
        addItem(RagiumDelightAddon.RAGI_CHERRY_TOAST, "Ragi-Cherry Toast")
    }

    private fun jade() {
        add("config.jade.plugin_ragium.output_side", "Output Side")

        add(RagiumTranslationKeys.JADE_OUTPUT_SIDE, "Output Side: %s")
    }

    private fun mekanism() {
        addChemical(RagiumMekanismAddon.CHEMICAL_RAGINITE, "Raginite")
        addChemical(RagiumMekanismAddon.CHEMICAL_AZURE, "Azure Essence")

        addItem(RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE, "Enriched Raginite")
        addItem(RagiumMekanismAddon.ITEM_ENRICHED_AZURE, "Enriched Azure Essence")
    }

    private fun replication() {
        addMatterType(RagiumReplicationAddon.MATTER_RAGIUM.get(), "Ragium")
    }
}
