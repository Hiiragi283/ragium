package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.data.lang.HTLanguageProvider
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.client.integration.jade.provider.HTBlockConfigurationDataProvider
import hiiragi283.ragium.client.integration.jade.provider.HTBlockOwnerProvider
import hiiragi283.ragium.client.text.RagiumClientTranslation
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.data.server.advancement.RagiumAdvancements
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDelightContents
import hiiragi283.ragium.setup.RagiumEnchantments
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput

class RagiumEnglishProvider(output: PackOutput) : HTLanguageProvider.English(output) {
    override fun addTranslations() {
        addPatterned()

        advancement()
        block()
        enchantment()
        entity()
        fluid()
        item()
        itemGroup()
        keyMapping()
        modTags()
        recipeType()
        text()

        delight()
        jade()
    }

    private fun advancement() {
        addAdvancement(RagiumAdvancements.ROOT, "Ragium", "Welcome to Ragium!")
        addAdvancement(
            RagiumAdvancements.CRAFTABLE_TEMPLATES,
            "Easy upgrades done dirt cheap",
            "Craft any Upgrade Templates added by Ragium",
        )
        // Raginite
        addAdvancement(RagiumAdvancements.RAGINITE, "Not a Redstone", "Get Raginite Dust from Raginite Ores in underground")
        addAdvancement(RagiumAdvancements.RAGI_CHERRY, "Food of twins", "Eat Ragi-Cherry")
        addAdvancement(RagiumAdvancements.RAGI_CHERRY_TOAST, "The Last Breakfast", "Get Ragi-Cherry Toast Tower")

        addAdvancement(RagiumAdvancements.RAGI_ALLOY, "0xFF003F", "Get Ragi-Alloy Ingot")
        addAdvancement(RagiumAdvancements.ALLOY_SMELTER, "Al-Chemistry", "Get Alloy Smelter")
        addAdvancement(RagiumAdvancements.AMBROSIA, "The Forbidden Fruit", "Eat Ste... Ambrosia")

        addAdvancement(RagiumAdvancements.ADV_RAGI_ALLOY, "This is red, not orange!", "Get Advanced Ragi-Alloy Ingot")
        addAdvancement(RagiumAdvancements.MELTER, "Melting with you", "Get Melter")
        addAdvancement(RagiumAdvancements.REFINERY, "Not a Ref..., It's Refinery!", "Get Refinery")
        addAdvancement(RagiumAdvancements.PLASTIC, "Plus-Tic", "Get Plastic")
        addAdvancement(RagiumAdvancements.POTION_BUNDLE, "Backported Bundle", "Use Potion Bundle")

        addAdvancement(RagiumAdvancements.RAGI_CRYSTAL, "Not a Energium", "Get Ragi-Crystal")
        addAdvancement(RagiumAdvancements.RAGI_CRYSTAL_HAMMER, "Ragi-Disassembler", "Get Ragi-Crystal Hammer")
        addAdvancement(RagiumAdvancements.RAGI_TICKET, "Good Old Halcyon Days?", "Get Ragi-Ticket to roll treasure chests")
        // Azure
        addAdvancement(RagiumAdvancements.BUDDING_AZURE, "Dyed in blue", "Use Blue Knowledge to Budding Amethyst")
        addAdvancement(RagiumAdvancements.AZURE_SHARD, "Not a azurite", "Get Azure Shard")
        addAdvancement(RagiumAdvancements.AZURE_STEEL, "The steel is bluish.", "Get Azure Steel Ingot")
        addAdvancement(RagiumAdvancements.AZURE_GEARS, "Wake up! Azure Dragon!", "Get any Azure Steel Tool or Armor")
        addAdvancement(RagiumAdvancements.SIMULATOR, "1 + 2 + 3 = 1 * 2 * 3", "Get Simulator")
        // Deep
        addAdvancement(RagiumAdvancements.RESONANT_DEBRIS, "Debris in the Ancient", "Get Resonant Debris")
        addAdvancement(RagiumAdvancements.DEEP_STEEL, "Deeper, Deeper, yet Deeper.", "Get Deep Steel")
        addAdvancement(RagiumAdvancements.BEHEAD_MOB, "Not more charged", "Behead mob by weapons with Strike enchantment")

        addAdvancement(RagiumAdvancements.ECHO_STAR, "Shrieking Star", "Get Echo Star")
        // Night Metal
        addAdvancement(RagiumAdvancements.NIGHT_METAL, "Night of Knights", "Get Night Metal Ingot")
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
        addAdvancement(RagiumAdvancements.WARPED_WART, "Industrial Wart", "Eat Warped Wart")
        // Eldritch
        addAdvancement(RagiumAdvancements.ELDRITCH_PEARL, "Not a Primordial", "Get Eldritch Pearl")
        addAdvancement(RagiumAdvancements.ELDRITCH_EGG, "Rotten Egg", "Throw Eldritch Egg to capture mobs")
        addAdvancement(RagiumAdvancements.MYSTERIOUS_OBSIDIAN, "Who is Falling Meteorites?", "")
        // Iridescentium
        addAdvancement(RagiumAdvancements.IRIDESCENTIUM, "The sky's the limit", "Get Iridescentium Ingot")
        addAdvancement(RagiumAdvancements.ETERNAL_COMPONENT, "Eternal Requiem", "Get Eternal Component for making tools unbreakable")
    }

    private fun block() {
        add(RagiumBlocks.SILT, "Silt")

        add(RagiumBlocks.BUDDING_AZURE, "Budding Azure")
        add(RagiumBlocks.AZURE_CLUSTER, "Azure Cluster")
        add(RagiumBlocks.RESONANT_DEBRIS, "Resonant Debris")
        add(RagiumBlocks.SOOTY_COBBLESTONE, "Sooty Cobblestone")

        add(RagiumBlocks.CRIMSON_SOIL, "Crimson Soil")

        add(RagiumBlocks.WARPED_WART, "Warped Wart")

        add(RagiumBlocks.EXP_BERRIES, "Exp Berries Bush", "Exp Berries")
        add(RagiumBlocks.MYSTERIOUS_OBSIDIAN, "Mysterious Obsidian")

        add(RagiumBlocks.RAGI_BRICKS, "Ragi-Bricks")
        add(RagiumBlocks.AZURE_TILES, "Azure Tiles")
        add(RagiumBlocks.ELDRITCH_STONE, "Eldritch Stone")
        add(RagiumBlocks.ELDRITCH_STONE_BRICKS, "Eldritch Stone Bricks")
        add(RagiumBlocks.PLASTIC_BRICKS, "Plastic Bricks")
        add(RagiumBlocks.PLASTIC_TILES, "Plastic Tiles")
        add(RagiumBlocks.BLUE_NETHER_BRICKS, "Blue Nether Bricks")
        add(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")

        add(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

        // Parts
        add(RagiumBlocks.DEVICE_CASING, "Device Casing")
        // Generators
        add(RagiumBlocks.THERMAL_GENERATOR, "Thermal Generator")

        add(RagiumBlocks.COMBUSTION_GENERATOR, "Combustion Generator")

        add(RagiumBlocks.SOLAR_PANEL_UNIT, "Solar Panel Unit")
        add(RagiumBlocks.SOLAR_PANEL_CONTROLLER, "Solar Panel Controller")

        add(RagiumBlocks.ENCHANTMENT_GENERATOR, "Enchantment Generator")
        add(RagiumBlocks.NUCLEAR_REACTOR, "Nuclear Reactor")
        // Processors
        add(RagiumBlocks.ELECTRIC_FURNACE, "Electric Smelter")
        add(RagiumBlocks.AUTO_SMITHING_TABLE, "Auto Smithing Table")
        add(RagiumBlocks.AUTO_STONECUTTER, "Auto Stonecutter")

        add(RagiumBlocks.ALLOY_SMELTER, "Alloy Smelter")
        add(RagiumBlocks.BLOCK_BREAKER, "Block Breaker")
        add(RagiumBlocks.COMPRESSOR, "Compressor")
        add(RagiumBlocks.CUTTING_MACHINE, "Cutting Machine")
        add(RagiumBlocks.EXTRACTOR, "Extractor")
        add(RagiumBlocks.PULVERIZER, "Pulverizer")

        add(RagiumBlocks.CRUSHER, "Crusher")
        add(RagiumBlocks.MELTER, "Melter")
        add(RagiumBlocks.REFINERY, "Refinery")
        add(RagiumBlocks.WASHER, "Washer")

        add(RagiumBlocks.BREWERY, "Brewery")
        add(RagiumBlocks.MULTI_SMELTER, "Multi Smelter")
        add(RagiumBlocks.PLANTER, "Planting Chamber")
        add(RagiumBlocks.SIMULATOR, "Simulation Chamber")
        // Devices
        add(RagiumBlocks.ITEM_BUFFER, "Item Buffer")
        add(RagiumBlocks.WATER_COLLECTOR, "Water Collector")

        add(RagiumBlocks.EXP_COLLECTOR, "Exp Collector")
        add(RagiumBlocks.FISHER, "Fisher")

        add(RagiumBlocks.DIM_ANCHOR, "Dimensional Anchor")
        add(RagiumBlocks.ENI, "E.N.I.")

        add(RagiumBlocks.MOB_CAPTURER, "Mob Capturer")
        add(RagiumBlocks.TELEPAD, "Telepad")

        add(RagiumBlocks.CEU, "C.E.U.")
        // Storage
        add(RagiumBlocks.OPEN_CRATE, "Open Crate")

        add(RagiumBlocks.EXP_DRUM, "Experience Drum")
    }

    private fun enchantment() {
        addEnchantment(RagiumEnchantments.CAPACITY, "Capacity", "Increase the capacity of item or fluid storages.")
        addEnchantment(RagiumEnchantments.RANGE, "Extra Range", "Increase the range of collecting.")

        addEnchantment(RagiumEnchantments.NOISE_CANCELING, "Noise Canceling", "Increases damage against sculk mobs such as Warden.")
        addEnchantment(RagiumEnchantments.STRIKE, "Strike", "Drop mob head.")

        addEnchantment(RagiumEnchantments.SONIC_PROTECTION, "Sonic Protection", "Immune damage from sonic boom.")
    }

    private fun entity() {
        add(RagiumEntityTypes.BLAST_CHARGE, "Blast Charge")
        add(RagiumEntityTypes.ELDRITCH_EGG, "Thrown Eldritch Egg")

        for (tier: HTDrumTier in HTDrumTier.entries) {
            val value: String = tier.translate(type, "Minecart with %s")
            add(tier.getEntityType(), value)
            add(tier.getMinecartItem(), value)
        }

        // add(RagiumEntityTypes.DYNAMITE, "Dynamite")
        // add(RagiumEntityTypes.DEFOLIANT_DYNAMITE, "Defoliant Dynamite")
        // add(RagiumEntityTypes.FLATTEN_DYNAMITE, "Flatten Dynamite")
        // add(RagiumEntityTypes.NAPALM_DYNAMITE, "Napalm Dynamite")
        // add(RagiumEntityTypes.POISON_DYNAMITE, "Poison Dynamite")
    }

    private fun fluid() {
        addFluid(RagiumFluidContents.AWKWARD_WATER, "Awkward Water")
        addFluid(RagiumFluidContents.HONEY, "Honey")
        addFluid(RagiumFluidContents.EXPERIENCE, "Experience Liquid")
        addFluid(RagiumFluidContents.MUSHROOM_STEW, "Mushroom Stew")

        addFluid(RagiumFluidContents.CHOCOLATE, "Chocolate")
        addFluid(RagiumFluidContents.MEAT, "Liquid Meat")
        addFluid(RagiumFluidContents.ORGANIC_MUTAGEN, "Organic Mutagen")

        addFluid(RagiumFluidContents.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluidContents.NATURAL_GAS, "Natural Gas")
        addFluid(RagiumFluidContents.NAPHTHA, "Naphtha")
        addFluid(RagiumFluidContents.LUBRICANT, "Lubricant")

        addFluid(RagiumFluidContents.FUEL, "Fuel")
        addFluid(RagiumFluidContents.CRIMSON_FUEL, "Crimson Fuel")
        addFluid(RagiumFluidContents.GREEN_FUEL, "Green Fuel")

        addFluid(RagiumFluidContents.SAP, "Sap")
        addFluid(RagiumFluidContents.CRIMSON_SAP, "Crimson Sap")
        addFluid(RagiumFluidContents.WARPED_SAP, "Warped Sap")
    }

    private fun item() {
        // Material
        add(RagiumItems.COAL_CHIP, "Coal Chip")
        add(RagiumItems.COAL_CHUNK, "Coal Chunk")
        add(RagiumItems.COMPRESSED_SAWDUST, "Compressed Sawdust")
        add(RagiumItems.ECHO_STAR, "Echo Star")
        add(RagiumItems.ELDER_HEART, "Heart of the Elder")
        add(RagiumItems.POTION_DROP, "Potion Drop")
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        add(RagiumItems.RAGI_COKE, "Ragi-Coke")
        add(RagiumItems.RESIN, "Resin")
        add(RagiumItems.TAR, "Tar")
        add(RagiumItems.WITHER_DOLl, "Wither Doll")

        add(RagiumItems.POTATO_SPROUTS, "Potato Sprouts")
        add(RagiumItems.GREEN_CAKE, "Green Cake")
        add(RagiumItems.GREEN_CAKE_DUST, "Green Cake Dust")
        add(RagiumItems.GREEN_PELLET, "Green Pellet")
        // Armor
        add(RagiumItems.NIGHT_VISION_GOGGLES, "Night Vision Goggles")
        // Tool
        add(RagiumItems.MEDIUM_DRUM_UPGRADE, "Medium Drum Upgrade")
        add(RagiumItems.LARGE_DRUM_UPGRADE, "Large Drum Upgrade")
        add(RagiumItems.HUGE_DRUM_UPGRADE, "Huge Drum Upgrade")

        add(RagiumItems.DRILL, "Electric Drill")

        add(RagiumItems.ADVANCED_MAGNET, "Advanced Ragi-Magnet")
        add(RagiumItems.BLAST_CHARGE, "Blast Charge")
        add(RagiumItems.BLUE_KNOWLEDGE, "Blue Knowledge")
        add(RagiumItems.DYNAMIC_LANTERN, "Ragi-Lantern")
        add(RagiumItems.ELDRITCH_EGG, "Eldritch Egg")
        add(RagiumItems.LOOT_TICKET, "Ragi-Ticket")
        add(RagiumItems.MAGNET, "Ragi-Magnet")
        add(RagiumItems.POTION_BUNDLE, "Potion Bundle")
        add(RagiumItems.SLOT_COVER, "Slot Cover")
        add(RagiumItems.TELEPORT_KEY, "Teleport Key")
        add(RagiumItems.TRADER_CATALOG, "Wandering Trader's Catalog")
        add(RagiumItems.UNIVERSAL_BUNDLE, "Universal Backpack")
        // Food
        add(RagiumItems.ICE_CREAM, "Ice Cream")
        add(RagiumItems.ICE_CREAM_SODA, "Ice Cream Soda")

        add(RagiumItems.CANNED_COOKED_MEAT, "Canned Cooked Meat")

        add(RagiumItems.SWEET_BERRIES_CAKE_SLICE, "Slice of Sweet Berries Cake")
        add(RagiumItems.MELON_PIE, "Melon Pie")

        add(RagiumItems.RAGI_CHERRY, "Ragi-Cherry")
        add(RagiumItems.RAGI_CHERRY_JAM, "Ragi-Cherry Jam")
        add(RagiumItems.RAGI_CHERRY_PULP, "Ragi-Cherry Pulp")
        add(RagiumItems.RAGI_CHERRY_TOAST, "Ragi-Cherry Toast")
        add(RagiumItems.FEVER_CHERRY, "Fever Cherry")

        add(RagiumItems.BOTTLED_BEE, "Bottled Bee")
        add(RagiumItems.AMBROSIA, "Ambrosia")
        // Parts
        add(RagiumItems.getMold(CommonMaterialPrefixes.STORAGE_BLOCK), "Block Mold")
        add(RagiumItems.getMold(CommonMaterialPrefixes.GEM), "Gem Mold")
        add(RagiumItems.getMold(CommonMaterialPrefixes.INGOT), "Ingot Mold")

        add(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        add(RagiumItems.GRAVITATIONAL_UNIT, "Gravitational Unit")
        add(RagiumItems.LED, "Light Emitting Diode")
        add(RagiumItems.LUMINOUS_PASTE, "Luminous Paste")
        add(RagiumItems.POLYMER_CATALYST, "Polymerization Catalyst")
        add(RagiumItems.POLYMER_RESIN, "Polymer Resin")
        add(RagiumItems.REDSTONE_BOARD, "Redstone Board")
        add(RagiumItems.SOLAR_PANEL, "Solar Panel")
        add(RagiumItems.SYNTHETIC_FIBER, "Synthetic Fiber")
        add(RagiumItems.SYNTHETIC_LEATHER, "Synthetic Leather")
    }

    private fun itemGroup() {
        add(RagiumCommonTranslation.CREATIVE_TAB_BLOCKS, "Ragium - Blocks")
        add(RagiumCommonTranslation.CREATIVE_TAB_INGREDIENTS, "Ragium - Ingredients")
        add(RagiumCommonTranslation.CREATIVE_TAB_ITEMS, "Ragium - Items")
    }

    private fun keyMapping() {
        add(RagiumClientTranslation.KEY_CATEGORY, "Ragium")

        add(RagiumClientTranslation.KEY_OPEN_UNIVERSAL_BUNDLE, "Open Universal Bundle")
    }

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
        add(RagiumModTags.Items.RAW_MEAT, "Raw Meats")
        add(RagiumModTags.Items.WIP, "Work In Progress")

        add(RagiumModTags.Items.CAPACITY_ENCHANTABLE, "Capacity Enchantable")
        add(RagiumModTags.Items.RANGE_ENCHANTABLE, "Range Enchantable")
        add(RagiumModTags.Items.STRIKE_ENCHANTABLE, "Strike Enchantable")

        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, "Basic Fluxes for Alloy Smelter")
        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, "Advanced Fluxes for Alloy Smelter")

        add(RagiumModTags.Items.TOOLS_DRILL, "Drills")
        add(RagiumModTags.Items.TOOLS_HAMMER, "Hammers")
    }

    private fun recipeType() {
        add(RagiumRecipeTypes.ALLOYING, "Alloying")
        add(RagiumRecipeTypes.BREWING, "Brewing")
        add(RagiumRecipeTypes.COMPRESSING, "Compressing")
        add(RagiumRecipeTypes.CRUSHING, "Crushing")
        add(RagiumRecipeTypes.CUTTING, "Cutting")
        add(RagiumRecipeTypes.ENCHANTING, "Enchanting")
        add(RagiumRecipeTypes.EXTRACTING, "Extracting")
        add(RagiumRecipeTypes.FLUID_TRANSFORM, "Fluid Transforming")
        add(RagiumRecipeTypes.MELTING, "Melting")
        add(RagiumRecipeTypes.MIXING, "Mixing")
        add(RagiumRecipeTypes.PLANTING, "Planting")
        add(RagiumRecipeTypes.SIMULATING, "Simulating")
        add(RagiumRecipeTypes.WASHING, "Washing")
    }

    private fun text() {
        add(HTAccessConfig.INPUT_ONLY, "Mode: Input")
        add(HTAccessConfig.OUTPUT_ONLY, "Mode: Output")
        add(HTAccessConfig.BOTH, "Mode: Both")
        add(HTAccessConfig.DISABLED, "Mode: Disabled")

        // API - Constants
        add(RagiumTranslation.ERROR, "Error")
        add(RagiumTranslation.INFINITE, "Infinite")
        add(RagiumTranslation.NONE, "None")
        add(RagiumTranslation.EMPTY, "Empty")
        // API - Error
        add(RagiumTranslation.EMPTY_TAG_KEY, $$"Empty tag key: %1$s")
        add(RagiumTranslation.INVALID_PACKET_S2C, $$"Invalid packet received from server side: %1$s")
        add(RagiumTranslation.INVALID_PACKET_C2S, $$"Invalid packet received from client side: %1$s")
        add(RagiumTranslation.MISSING_REGISTRY, $$"Missing registry: %1$s")
        add(RagiumTranslation.MISSING_KEY, $$"Missing key: %1$s")
        // API - Item
        add(RagiumTranslation.TOOLTIP_BLOCK_POS, $$"Position: [%1$s, %2$s, %3$s]")
        add(RagiumTranslation.TOOLTIP_DIMENSION, $$"Dimension: %1$s")
        add(RagiumTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"Always has at least %1$s")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, $$"Loot Table: %1$s")
        add(RagiumTranslation.TOOLTIP_BLAST_POWER, $$"Power: %1$s")

        add(RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION, "Press Shift to show description")
        add(RagiumTranslation.TOOLTIP_SHOW_DETAILS, "Press Ctrl to show details")
        add(RagiumTranslation.TOOLTIP_WIP, "This content is work in progress!!")

        // Common
        add(RagiumCommonTranslation.WARPED_WART, "Clears one bad effect randomly when eaten.")
        add(RagiumCommonTranslation.EXP_BERRIES, "Gives experience when eaten.")

        add(RagiumCommonTranslation.THERMAL_GENERATOR, "A machine which generates energy from furnace fuels or hot fluids.")
        add(RagiumCommonTranslation.COMBUSTION_GENERATOR, "A machine which generates energy from fuel fluids.")
        add(RagiumCommonTranslation.SOLAR_PANEL_UNIT, "A unit to use with Solar Panel Controller.")
        add(RagiumCommonTranslation.SOLAR_PANEL_CONTROLLER, "A machine which generates energy from connected Solar Panel Units.")
        add(RagiumCommonTranslation.ENCHANTMENT_GENERATOR, "A machine which generates energy from enchanted books or experience fluids.")
        add(RagiumCommonTranslation.NUCLEAR_REACTOR, "A machine which generates energy from nuclear fuels.")

        add(RagiumCommonTranslation.ALLOY_SMELTER, "A machine which smelts multiple items into one.")
        add(RagiumCommonTranslation.BLOCK_BREAKER, "A machine which mines the block in front.")
        add(RagiumCommonTranslation.CUTTING_MACHINE, "A machine which cut item into another one.")
        add(RagiumCommonTranslation.COMPRESSOR, "A machine which compresses item into another one.")
        add(RagiumCommonTranslation.EXTRACTOR, "A machine which extracts item or fluid from input items.")
        add(RagiumCommonTranslation.PULVERIZER, "A machine which pulverizes item into another one.")

        add(RagiumCommonTranslation.CRUSHER, "An upgraded version of the Pulverizer which also produces byproducts.")
        add(RagiumCommonTranslation.MELTER, "A machine which melts item into fluid.")
        add(RagiumCommonTranslation.MIXER, "A machine which mixes multiple items and fluids.")
        add(RagiumCommonTranslation.REFINERY, "A machine which transform fluid into another.")
        add(RagiumCommonTranslation.WASHER, "A machine which washes item with fluid.")

        add(RagiumCommonTranslation.BREWERY, "A machine which brews potion from item and fluid.")
        add(RagiumCommonTranslation.MULTI_SMELTER, "A machine which smelts items simultaneously.")
        add(RagiumCommonTranslation.PLANTER, "A machine which grows plant from seeds or sapling.")
        add(RagiumCommonTranslation.SIMULATOR, "A machine which simulates behavior of blocks or mobs to generate resources.")

        add(RagiumCommonTranslation.ITEM_BUFFER, "A device which collects items around.")
        add(RagiumCommonTranslation.WATER_COLLECTOR, "A device which generates Water from surrounded sources or biomes.")

        add(RagiumCommonTranslation.EXP_COLLECTOR, "A device which collects Experience Orbs and converts into liquid.")
        add(RagiumCommonTranslation.FISHER, "A device which do fishing from below 3x3 water sources.")

        add(RagiumCommonTranslation.DIM_ANCHOR, "A device which forces to load chunk placed in.")
        add(RagiumCommonTranslation.ENI, "A device which enables to access Energy Network.")

        add(RagiumCommonTranslation.MOB_CAPTURER, "A device which captures mobs into Eldritch Egg.")

        add(RagiumCommonTranslation.CEU, "A device which provides unlimited amount of energy.")

        add(RagiumCommonTranslation.CRATE, "A storage which stores one type of item.")
        add(RagiumCommonTranslation.OPEN_CRATE, "A storage which drops inserted items below.")
        add(RagiumCommonTranslation.DRUM, "A storage which stores one type of fluid.")
        add(RagiumCommonTranslation.EXP_DRUM, "A storage which stores only Experience Liquid.")

        add(RagiumCommonTranslation.COMMAND_ENERGY_ADD, $$"Added %1$s FE into the energy network.")
        add(RagiumCommonTranslation.COMMAND_ENERGY_GET, $$"%1$s FE stored in the energy network.")
        add(RagiumCommonTranslation.COMMAND_ENERGY_SET, $$"Set amount of the energy network to %1$s FE.")

        add(RagiumCommonTranslation.NO_DESTINATION, "Not found destination.")
        add(RagiumCommonTranslation.UNKNOWN_DIMENSION, $$"Missing dimension: %1$s")
        add(RagiumCommonTranslation.FUEL_SHORTAGE, $$"Fuel shortage: required %1$s mB")

        add(RagiumCommonTranslation.ELDER_HEART, "Dropped from Elder Guardian.")

        add(RagiumCommonTranslation.BLAST_CHARGE, "Can be upgraded by gunpowders in Crafting Table.")
        add(RagiumCommonTranslation.DYNAMIC_LANTERN, "Light up darkness in range.")
        add(RagiumCommonTranslation.ELDRITCH_EGG, "Can be throwable by right-click，and capture mobs when hit.")
        add(RagiumCommonTranslation.MAGNET, "Collect dropped items in the effective range")
        add(RagiumCommonTranslation.SLOT_COVER, "Ignored by recipes when placed in machine slot.")
        add(RagiumCommonTranslation.TRADER_CATALOG, "Dropped from Wandering Trader. Right-click to trade with merchant.")

        add(RagiumCommonTranslation.AMBROSIA, "ALWAYS EDIBLE and NOT CONSUMED!")
        add(RagiumCommonTranslation.ICE_CREAM, "Extinguish fire when eaten.")
        add(RagiumCommonTranslation.RAGI_CHERRY, "Drops from Cherry Leaves as same as Apple.")
    }

    //    Addon    //

    private fun delight() {
        add(RagiumDelightContents.RAGI_CHERRY_PIE, "Ragi-Cherry Pie")
        add(RagiumDelightContents.RAGI_CHERRY_TOAST_BLOCK, "Ragi-Cherry Toast Tower")

        add(RagiumDelightContents.RAGI_CHERRY_PIE_SLICE, "Slice of Ragi-Cherry Pie")
    }

    private fun jade() {
        add(HTBlockConfigurationDataProvider, "Access Configuration")
        add(HTBlockOwnerProvider, "Block Owner")

        add(RagiumClientTranslation.JADE_EXP_STORAGE, "Experience: %s")
    }
}
