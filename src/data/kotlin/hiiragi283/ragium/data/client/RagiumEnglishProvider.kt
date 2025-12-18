package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.block.attribute.HTFluidBlockAttribute
import hiiragi283.ragium.api.data.lang.HTLanguageProvider
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.client.integration.jade.provider.HTBlockConfigurationDataProvider
import hiiragi283.ragium.client.integration.jade.provider.HTBlockMachinePropertiesProvider
import hiiragi283.ragium.client.integration.jade.provider.HTBlockOwnerProvider
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.common.upgrade.RagiumUpgradeKeys
import hiiragi283.ragium.data.server.advancement.RagiumAdvancements
import hiiragi283.ragium.setup.RagiumBlocks
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
        keyMapping()
        modTags()
        recipeType()
        text()

        emi()
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
        addAdvancement(RagiumAdvancements.AZURE_SHARD, "Not a azurite", "Get Azure Shard")
        addAdvancement(RagiumAdvancements.AZURE_STEEL, "The steel is bluish.", "Get Azure Steel Ingot")
        addAdvancement(RagiumAdvancements.AZURE_GEARS, "Wake up! Azure Dragon!", "Get any Azure Steel Tool or Armor")
        addAdvancement(RagiumAdvancements.MIXER, "Mix and Mix then Mix", "Get Mixer")
        // Deep
        addAdvancement(RagiumAdvancements.RESONANT_DEBRIS, "Debris in the Ancient", "Get Resonant Debris")
        addAdvancement(RagiumAdvancements.DEEP_STEEL, "Deeper, Deeper, yet Deeper.", "Get Deep Steel")
        addAdvancement(RagiumAdvancements.BEHEAD_MOB, "Not more charged", "Behead mob by weapons with Strike enchantment")

        addAdvancement(RagiumAdvancements.ECHO_STAR, "Sonic the Boom", "Get Echo Star")
        // Night Metal
        addAdvancement(RagiumAdvancements.NIGHT_METAL, "Night of Knights", "Get Night Metal Ingot")
        addAdvancement(RagiumAdvancements.SIMULATOR, "1 + 2 + 3 = 1 * 2 * 3", "Get Simulator")
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
        // Iridescent
        addAdvancement(RagiumAdvancements.IRIDESCENT_POWDER, "The sky's the limit", "Get Iridescent Powder")
        addAdvancement(RagiumAdvancements.ETERNAL_COMPONENT, "Eternal Requiem", "Get Eternal Component for making tools unbreakable")
    }

    private fun block() {
        add(RagiumBlocks.SILT, "Silt")

        add(RagiumBlocks.BUDDING_QUARTZ, "Budding Quartz")
        add(RagiumBlocks.QUARTZ_CLUSTER, "Quartz Cluster")
        add(RagiumBlocks.RESONANT_DEBRIS, "Resonant Debris")
        add(RagiumBlocks.IMITATION_SPAWNER, "Imitation Spawner")
        add(RagiumBlocks.SMOOTH_BLACKSTONE, "Smooth Blackstone")
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

        add(RagiumBlocks.CULINARY_GENERATOR, "Culinary Generator")
        add(RagiumBlocks.MAGMATIC_GENERATOR, "Magmatic Generator")

        add(RagiumBlocks.COMBUSTION_GENERATOR, "Combustion Generator")
        add(RagiumBlocks.SOLAR_PANEL_UNIT, "Solar Panel Unit")
        add(RagiumBlocks.SOLAR_PANEL_CONTROLLER, "Solar Panel Controller")

        add(RagiumBlocks.ENCHANTMENT_GENERATOR, "Enchantment Generator")
        add(RagiumBlocks.NUCLEAR_REACTOR, "Nuclear Reactor")
        // Processors
        add(RagiumBlocks.ALLOY_SMELTER, "Alloy Smelter")
        add(RagiumBlocks.BLOCK_BREAKER, "Block Breaker")
        add(RagiumBlocks.COMPRESSOR, "Compressor")
        add(RagiumBlocks.CUTTING_MACHINE, "Cutting Machine")
        add(RagiumBlocks.ELECTRIC_FURNACE, "Electric Smelter")
        add(RagiumBlocks.EXTRACTOR, "Extractor")
        add(RagiumBlocks.PULVERIZER, "Pulverizer")

        add(RagiumBlocks.CRUSHER, "Crusher")
        add(RagiumBlocks.MELTER, "Melter")
        add(RagiumBlocks.MIXER, "Mixer")
        add(RagiumBlocks.REFINERY, "Refinery")

        add(RagiumBlocks.ADVANCED_MIXER, "Advanced Mixer")
        add(RagiumBlocks.BREWERY, "Brewery")
        add(RagiumBlocks.MULTI_SMELTER, "Multi Smelter")
        add(RagiumBlocks.PLANTER, "Planting Chamber")

        add(RagiumBlocks.ENCHANTER, "Enchanter")
        add(RagiumBlocks.MOB_CRUSHER, "Mob Crusher")
        add(RagiumBlocks.SIMULATOR, "Simulation Chamber")
        // Devices
        add(RagiumBlocks.FLUID_COLLECTOR, "Fluid Collector")
        add(RagiumBlocks.ITEM_COLLECTOR, "Item Collector")

        add(RagiumBlocks.STONE_COLLECTOR, "Stone Collector")

        add(RagiumBlocks.DIM_ANCHOR, "Dimensional Anchor")

        add(RagiumBlocks.TELEPAD, "Telepad")
        // Storage
        add(RagiumBlocks.BATTERY, "Variable Battery")
        add(RagiumBlocks.CRATE, "Variable Crate")
        add(RagiumBlocks.TANK, "Variable Tank")
        add(RagiumBlocks.BUFFER, "Integrated Buffer")

        add(RagiumBlocks.UNIVERSAL_CHEST, "Universal Chest")
    }

    private fun enchantment() {
        addEnchantment(RagiumEnchantments.RANGE, "Extra Range", "Increase the range of collecting.")

        addEnchantment(RagiumEnchantments.NOISE_CANCELING, "Noise Canceling", "Increases damage against sculk mobs such as Warden.")
        addEnchantment(RagiumEnchantments.STRIKE, "Strike", "Drop mob head.")

        addEnchantment(RagiumEnchantments.SONIC_PROTECTION, "Sonic Protection", "Immune damage from sonic boom.")
    }

    private fun entity() {
        add(RagiumEntityTypes.ELDRITCH_EGG, "Thrown Eldritch Egg")
        add(RagiumEntityTypes.TANK_MINECART, "Minecart with Variable Tank")
    }

    private fun fluid() {
        addFluid(RagiumFluidContents.HONEY, "Honey")
        addFluid(RagiumFluidContents.MUSHROOM_STEW, "Mushroom Stew")
        addFluid(RagiumFluidContents.CREAM, "Cream")
        addFluid(RagiumFluidContents.CHOCOLATE, "Chocolate")
        addFluid(RagiumFluidContents.RAGI_CHERRY_JUICE, "Ragi-Cherry Juice")

        addFluid(RagiumFluidContents.SLIME, "Slime")
        addFluid(RagiumFluidContents.GELLED_EXPLOSIVE, "Gelled Explosive")
        addFluid(RagiumFluidContents.CRUDE_BIO, "Crude Bio")
        addFluid(RagiumFluidContents.BIOFUEL, "Biofuel")

        addFluid(RagiumFluidContents.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluidContents.NAPHTHA, "Naphtha")
        addFluid(RagiumFluidContents.FUEL, "Fuel")
        addFluid(RagiumFluidContents.LUBRICANT, "Lubricant")

        addFluid(RagiumFluidContents.SPRUCE_RESIN, "Spruce Resin")
        addFluid(RagiumFluidContents.LATEX, "Latex")
        addFluid(RagiumFluidContents.CRIMSON_SAP, "Crimson Sap")
        addFluid(RagiumFluidContents.WARPED_SAP, "Warped Sap")

        addFluid(RagiumFluidContents.DESTABILIZED_RAGINITE, "Destabilized Raginite")

        addFluid(RagiumFluidContents.EXPERIENCE, "Experience Liquid")
        addFluid(RagiumFluidContents.COOLANT, "Coolant")
        addFluid(RagiumFluidContents.CREOSOTE, "Creosote")
    }

    private fun item() {
        // Material
        add(RagiumItems.BIO_FERTILIZER, "Bio Fertilizer")
        add(RagiumItems.COAL_CHIP, "Coal Chip")
        add(RagiumItems.COAL_CHUNK, "Coal Chunk")
        add(RagiumItems.COMPRESSED_SAWDUST, "Compressed Sawdust")
        add(RagiumItems.ECHO_STAR, "Echo Star")
        add(RagiumItems.ELDER_HEART, "Heart of the Elder")
        add(RagiumItems.IRIDESCENT_POWDER, "Iridescent Powder")
        add(RagiumItems.MAGMA_SHARD, "Magma Shard")
        add(RagiumItems.POTION_DROP, "Potion Drop")
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        add(RagiumItems.RAGIUM_POWDER, "Ragium Powder")
        add(RagiumItems.ROSIN, "Rosin")
        add(RagiumItems.TAR, "Tar")
        add(RagiumItems.WITHER_DOLl, "Wither Doll")
        add(RagiumItems.WITHER_STAR, "Wither Star")

        add(RagiumItems.POTATO_SPROUTS, "Potato Sprouts")
        add(RagiumItems.GREEN_CAKE, "Green Cake")
        add(RagiumItems.GREEN_CAKE_DUST, "Green Cake Dust")
        add(RagiumItems.GREEN_PELLET, "Green Pellet")
        // Armor
        add(RagiumItems.NIGHT_VISION_GOGGLES, "Night Vision Goggles")
        // Tool
        add(RagiumItems.DRILL, "Electric Drill")

        add(RagiumItems.ADVANCED_MAGNET, "Advanced Ragi-Magnet")
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
        add(RagiumItems.CREAM_BOWL, "Cream Bowl")

        add(RagiumItems.ICE_CREAM, "Ice Cream")
        add(RagiumItems.ICE_CREAM_SODA, "Ice Cream Soda")

        add(RagiumItems.CANNED_COOKED_MEAT, "Canned Cooked Meat")

        add(RagiumItems.SWEET_BERRIES_CAKE_SLICE, "Slice of Sweet Berries Cake")
        add(RagiumItems.MELON_PIE, "Melon Pie")

        add(RagiumItems.RAGI_CHERRY, "Ragi-Cherry")
        add(RagiumItems.RAGI_CHERRY_JUICE, "Ragi-Cherry Juice")
        add(RagiumItems.RAGI_CHERRY_JAM, "Ragi-Cherry Jam")
        add(RagiumItems.RAGI_CHERRY_PIE, "Ragi-Cherry Pie")
        add(RagiumItems.RAGI_CHERRY_PIE_SLICE, "Slice of Ragi-Cherry Pie")
        add(RagiumItems.RAGI_CHERRY_TOAST, "Ragi-Cherry Toast")
        add(RagiumItems.FEVER_CHERRY, "Fever Cherry")

        add(RagiumItems.BOTTLED_BEE, "Bottled Bee")
        add(RagiumItems.CHOCOLATE_BREAD, "Chocolate Bread")
        add(RagiumItems.AMBROSIA, "Ambrosia")
        // Parts
        add(RagiumItems.ADVANCED_CIRCUIT, "Advanced Circuit")
        add(RagiumItems.BASIC_CIRCUIT, "Basic Circuit")
        add(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        add(RagiumItems.GRAVITATIONAL_UNIT, "Gravitational Unit")
        add(RagiumItems.LED, "Light Emitting Diode")
        add(RagiumItems.LUMINOUS_PASTE, "Luminous Paste")
        add(RagiumItems.POLYMER_CATALYST, "Polymerization Catalyst")
        add(RagiumItems.REDSTONE_BOARD, "Redstone Board")
        add(RagiumItems.SOLAR_PANEL, "Solar Panel")
        add(RagiumItems.SYNTHETIC_FIBER, "Synthetic Fiber")
        add(RagiumItems.SYNTHETIC_LEATHER, "Synthetic Leather")
        // Vehicles
        add(RagiumItems.TANK_MINECART, "Tank with Minecart")
        // Upgrades
        add(RagiumItems.ETERNAL_COMPONENT, "Eternal Component")
    }

    private fun keyMapping() {
        add(RagiumCommonTranslation.KEY_CATEGORY, "Ragium")

        add(RagiumCommonTranslation.KEY_OPEN_UNIVERSAL_BUNDLE, "Open Universal Bundle")
    }

    private fun modTags() {
        add(RagiumModTags.Blocks.LED_BLOCKS, "LED Blocks")
        add(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES, "Replaceable by Resonant Debris")

        add(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST, "Blacklisted mobs for capturing")
        add(RagiumModTags.EntityTypes.CONFUSION_BLACKLIST, "Blacklisted mobs for confusion")
        add(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS, "Generate Resonant Debris")
        add(RagiumModTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING, "Sensitive to Noise Canceling")

        add(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, "Eldritch Pearl Binders")
        add(RagiumModTags.Items.LED_BLOCKS, "LED Blocks")
        add(RagiumModTags.Items.PLASTICS, "Plastics")
        add(RagiumModTags.Items.POLYMER_RESIN, "Polymer Resins")
        add(RagiumModTags.Items.RAW_MEAT, "Raw Meats")

        add(RagiumModTags.Items.RANGE_ENCHANTABLE, "Range Enchantable")
        add(RagiumModTags.Items.STRIKE_ENCHANTABLE, "Strike Enchantable")

        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, "Basic Fluxes for Alloy Smelter")
        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, "Advanced Fluxes for Alloy Smelter")

        add(RagiumModTags.Items.TOOLS_DRILL, "Drills")
        add(RagiumModTags.Items.TOOLS_HAMMER, "Hammers")

        add(RagiumModTags.Items.GENERATOR_UPGRADABLE, "Generators")
        add(RagiumModTags.Items.PROCESSOR_UPGRADABLE, "Processors")
        add(RagiumModTags.Items.EXTRA_VOIDING_UPGRADABLE, "Processors with Extra Output")
        add(RagiumModTags.Items.EFFICIENT_CRUSHING_UPGRADABLE, "Pulverizer or Crusher")
        add(RagiumModTags.Items.ENERGY_CAPACITY_UPGRADABLE, "Energy Storage")
        add(RagiumModTags.Items.FLUID_CAPACITY_UPGRADABLE, "Fluid Storage")
        add(RagiumModTags.Items.ITEM_CAPACITY_UPGRADABLE, "Item Storage")
        add(RagiumModTags.Items.SMELTING_UPGRADABLE, "Electric / Multi Smelter")

        add(RagiumModTags.Items.EXTRACTOR_EXCLUSIVE, "Upgrades for Extractor")
        add(RagiumModTags.Items.SMELTER_EXCLUSIVE, "Upgrades for Smelters")
    }

    private fun recipeType() {
        add(RagiumRecipeTypes.ALLOYING, "Alloying")
        add(RagiumRecipeTypes.BREWING, "Brewing")
        add(RagiumRecipeTypes.COMPRESSING, "Compressing")
        add(RagiumRecipeTypes.CRUSHING, "Crushing")
        add(RagiumRecipeTypes.CUTTING, "Cutting")
        add(RagiumRecipeTypes.ENCHANTING, "Enchanting")
        add(RagiumRecipeTypes.EXTRACTING, "Extracting")
        add(RagiumRecipeTypes.MELTING, "Melting")
        add(RagiumRecipeTypes.MIXING, "Mixing")
        add(RagiumRecipeTypes.PLANTING, "Planting")
        add(RagiumRecipeTypes.REFINING, "Refining")
        add(RagiumRecipeTypes.SIMULATING, "Simulating")
        add(RagiumRecipeTypes.SOLIDIFYING, "Solidifying")
    }

    private fun text() {
        add(HTAccessConfig.INPUT_ONLY, "Mode: Input")
        add(HTAccessConfig.OUTPUT_ONLY, "Mode: Output")
        add(HTAccessConfig.BOTH, "Mode: Both")
        add(HTAccessConfig.DISABLED, "Mode: Disabled")

        add(HTFluidBlockAttribute.TankType.INPUT, "Input Tank Capacity")
        add(HTFluidBlockAttribute.TankType.OUTPUT, "Output Tank Capacity")
        add(HTFluidBlockAttribute.TankType.FIRST_INPUT, "First Input Tank Capacity")
        add(HTFluidBlockAttribute.TankType.SECOND_INPUT, "Second Input Tank Capacity")

        add(HTUpgradeKeys.BASE_MULTIPLIER, $$"- Base Multiplier: %1$s")
        add(HTUpgradeKeys.IS_CREATIVE, "- Creative")

        add(HTUpgradeKeys.ENERGY_EFFICIENCY, $$"- Energy Efficiency: %1$s")
        add(HTUpgradeKeys.ENERGY_GENERATION, $$"- Energy Generation: %1$s")
        add(HTUpgradeKeys.SPEED, $$"- Speed: %1$s")

        add(HTUpgradeKeys.ENERGY_CAPACITY, $$"- Energy Capacity: %1$s")
        add(HTUpgradeKeys.FLUID_CAPACITY, $$"- Fluid Capacity: %1$s")
        add(HTUpgradeKeys.ITEM_CAPACITY, $$"- Item Capacity: %1$s")

        add(RagiumUpgradeKeys.BLASTING, "- Only process Blasting Recipes")
        add(RagiumUpgradeKeys.SMOKING, "- Only process Smoking Recipes")
        add(RagiumUpgradeKeys.COMPOST_BIO, "- Convert input into Crude Bio")
        add(RagiumUpgradeKeys.VOID_EXTRA, "- Extra output disabled")
        add(RagiumUpgradeKeys.EXTRACT_EXPERIENCE, "- Extract Liquid Experience from enchanted item")
        add(RagiumUpgradeKeys.USE_LUBRICANT, "- Use lubricant per operation")

        add(RagiumUpgradeKeys.COLLECT_EXP, "- Collect Experience Orb instead of Water")
        add(RagiumUpgradeKeys.FISHING, "- Do fishing instead of collecting items")
        add(RagiumUpgradeKeys.CAPTURE_MOB, "- Capture mobs instead of collecting items")
        // API - Constants
        add(RagiumTranslation.ERROR, "Error")
        add(RagiumTranslation.INFINITE, "Infinite")
        add(RagiumTranslation.NONE, "None")
        add(RagiumTranslation.EMPTY, "Empty")
        // API - Error
        add(RagiumTranslation.EMPTY_TAG_KEY, $$"Empty tag key: %1$s")
        add(RagiumTranslation.INVALID_PACKET_S2C, $$"Invalid packet received from server side: %1$s")
        add(RagiumTranslation.INVALID_PACKET_C2S, $$"Invalid packet received from client side: %1$s")

        add(RagiumTranslation.MISSING_SERVER, "Could not find current server")
        add(RagiumTranslation.MISSING_REGISTRY, $$"Missing registry: %1$s")
        add(RagiumTranslation.MISSING_KEY, $$"Missing key: %1$s")
        // API - Item
        add(RagiumTranslation.TOOLTIP_BLOCK_POS, $$"Position: [%1$s, %2$s, %3$s]")
        add(RagiumTranslation.TOOLTIP_CHARGE_POWER, $$"Power: %1$s")
        add(RagiumTranslation.TOOLTIP_DIMENSION, $$"Dimension: %1$s")
        add(RagiumTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"Always has at least %1$s")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, $$"Loot Table: %1$s")
        add(RagiumTranslation.TOOLTIP_UPGRADE_TARGET, $$"Upgrade Targets: %1$s")
        add(RagiumTranslation.TOOLTIP_UPGRADE_EXCLUSIVE, $$"Conflicting Upgrades: %1$s")

        add(RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION, "Press Shift to show description")
        add(RagiumTranslation.TOOLTIP_SHOW_DETAILS, "Press Ctrl to show details")

        add(RagiumTranslation.DATAPACK_WIP, "Enables work in progress contents")
        // Common
        add(RagiumCommonTranslation.WARPED_WART, "Clears one bad effect randomly when eaten.")
        add(RagiumCommonTranslation.EXP_BERRIES, "Gives experience when eaten.")

        add(RagiumCommonTranslation.QUARTZ_GLASS, "A glass which can be harvested without silk Touch.")
        add(RagiumCommonTranslation.OBSIDIAN_GLASS, "A glass which can be used to frame of Nether Portal.")
        add(RagiumCommonTranslation.CRIMSON_GLASS, "A glass which gives damage as same as the Magma Block.")
        add(RagiumCommonTranslation.WARPED_GLASS, "A glass which is passible by only players.")

        add(RagiumCommonTranslation.THERMAL_GENERATOR, "A machine which generates energy from furnace fuels.")
        add(RagiumCommonTranslation.CULINARY_GENERATOR, "A machine which generates energy from foods.")
        add(RagiumCommonTranslation.MAGMATIC_GENERATOR, "A machine which generates energy from hot fluids like Lava.")
        add(RagiumCommonTranslation.COMBUSTION_GENERATOR, "A machine which generates energy from fuel fluids.")
        add(RagiumCommonTranslation.SOLAR_PANEL_UNIT, "A unit to use with Solar Panel Controller.")
        add(RagiumCommonTranslation.SOLAR_PANEL_CONTROLLER, "A machine which generates energy from connected Solar Panel Units.")
        add(RagiumCommonTranslation.ENCHANTMENT_GENERATOR, "A machine which generates energy from enchanted books or experience fluids.")
        add(RagiumCommonTranslation.NUCLEAR_REACTOR, "A machine which generates energy from nuclear fuels.")

        add(RagiumCommonTranslation.ALLOY_SMELTER, "A machine which smelts multiple items into one.")
        add(RagiumCommonTranslation.BLOCK_BREAKER, "A machine which mines the block in front.")
        add(RagiumCommonTranslation.CUTTING_MACHINE, "A machine which cut item into another one.")
        add(RagiumCommonTranslation.COMPRESSOR, "A machine which compresses item into another one.")
        add(RagiumCommonTranslation.ELECTRIC_FURNACE, "A machine which smelts item by energy.")
        add(RagiumCommonTranslation.EXTRACTOR, "A machine which extracts item or fluid from input items.")
        add(RagiumCommonTranslation.PULVERIZER, "A machine which pulverizes item into another one.")

        add(RagiumCommonTranslation.CRUSHER, "An upgraded version of the Pulverizer which also produces byproducts.")
        add(RagiumCommonTranslation.MELTER, "A machine which melts item into fluid.")
        add(RagiumCommonTranslation.MIXER, "A machine which mixes multiple items and fluids.")
        add(RagiumCommonTranslation.REFINERY, "A machine which transform fluid into another.")

        add(RagiumCommonTranslation.BREWERY, "A machine which brews potion from item and fluid.")
        add(RagiumCommonTranslation.MULTI_SMELTER, "A machine which smelts items simultaneously by machine tier.")
        add(RagiumCommonTranslation.PLANTER, "A machine which grows plant from seeds or sapling.")

        add(RagiumCommonTranslation.ENCHANTER, "A machine which creates enchanted books from items.")
        add(RagiumCommonTranslation.MOB_CRUSHER, "A machine which kills mobs from spawn egg and produces its drops.")
        add(RagiumCommonTranslation.SIMULATOR, "A machine which simulates behavior of blocks or mobs to generate resources.")

        add(RagiumCommonTranslation.FLUID_COLLECTOR, "A device which generates Water from surrounded sources or biomes.")

        add(RagiumCommonTranslation.DIM_ANCHOR, "A device which forces to load chunk placed in.")
        add(RagiumCommonTranslation.ENI, "A device which enables to access Energy Network.")

        add(RagiumCommonTranslation.CEU, "A device which provides unlimited amount of energy.")

        add(RagiumCommonTranslation.BATTERY, "A energy storage which capacity is extendable by upgrade.")
        add(RagiumCommonTranslation.CRATE, "A item storage which capacity is extendable by upgrade.")
        add(RagiumCommonTranslation.TANK, "A fluid storage which capacity is extendable by upgrade.")
        add(RagiumCommonTranslation.BUFFER, "A combined storage with 9 slots, 3 tanks, and 1 battery.")
        add(RagiumCommonTranslation.UNIVERSAL_CHEST, "A chest which shares its containment with the same color.")

        add(RagiumCommonTranslation.CONFIG_ENERGY_CAPACITY, "Energy Capacity")
        add(RagiumCommonTranslation.CONFIG_ENERGY_RATE, "Energy Rate")

        add(RagiumCommonTranslation.COMMAND_ENERGY_ADD, $$"Added %1$s FE into the energy network.")
        add(RagiumCommonTranslation.COMMAND_ENERGY_GET, $$"%1$s FE stored in the energy network.")
        add(RagiumCommonTranslation.COMMAND_ENERGY_SET, $$"Set amount of the energy network to %1$s FE.")

        add(RagiumCommonTranslation.NO_DESTINATION, "Not found destination.")
        add(RagiumCommonTranslation.UNKNOWN_DIMENSION, $$"Missing dimension: %1$s")
        add(RagiumCommonTranslation.FUEL_SHORTAGE, $$"Fuel shortage: required %1$s mB")

        add(RagiumCommonTranslation.ELDER_HEART, "Dropped from Elder Guardian.")
        add(RagiumCommonTranslation.IRIDESCENT_POWDER, "Do not expire with time over or any damage.")

        add(RagiumCommonTranslation.BLAST_CHARGE, "A charge which explodes when hit.")
        add(RagiumCommonTranslation.STRIKE_CHARGE, "A charge which strikes thunder when hit.")
        add(RagiumCommonTranslation.NEUTRAL_CHARGE, "A charge which steal equipments from around mobs when hit.")
        add(RagiumCommonTranslation.FISHING_CHARGE, "A charge which does fishing when hit in water.")
        add(RagiumCommonTranslation.TELEPORT_CHARGE, "A charge which teleports around mobs to hit point.")
        add(RagiumCommonTranslation.CONFUSING_CHARGE, "A charge which confuses around mobs when hit.")

        add(RagiumCommonTranslation.DYNAMIC_LANTERN, "Light up darkness in range.")
        add(RagiumCommonTranslation.ELDRITCH_EGG, "Can be throwable by right-click，and capture mobs when hit.")
        add(RagiumCommonTranslation.MAGNET, "Collect dropped items in the effective range")
        add(RagiumCommonTranslation.SLOT_COVER, "Ignored by recipes when placed in machine slot.")
        add(RagiumCommonTranslation.TRADER_CATALOG, "Dropped from Wandering Trader. Right-click to trade with merchant.")
        add(RagiumCommonTranslation.UNIVERSAL_BUNDLE, "A bundle which shares its containment with the same color.")

        add(RagiumCommonTranslation.AMBROSIA, "ALWAYS EDIBLE and NOT CONSUMED!")
        add(RagiumCommonTranslation.ICE_CREAM, "Extinguish fire when eaten.")
        add(RagiumCommonTranslation.RAGI_CHERRY, "Drops from Cherry Leaves as same as Apple.")

        add(RagiumCommonTranslation.EXP_COLLECTOR_UPGRADE, "Allow Fluid Collector to collect Experience Orbs.")
        add(RagiumCommonTranslation.FISHING_UPGRADE, "Allow Item Collector to do fishing from below 3x3 Water Source.")
        add(RagiumCommonTranslation.MOB_CAPTURE_UPGRADE, "Allow Item Collector to capture mobs by Eldritch Egg.")
    }

    //    Addon    //

    private fun emi() {
        add(RagiumCommonTranslation.EMI_MACHINE_UPGRADE_TITLE, "Machine Upgrades")
        add(RagiumCommonTranslation.EMI_COMPOSTING_TITLE, "Composting")
        add(RagiumCommonTranslation.EMI_ROCK_GENERATING, "Rock Generating")

        add(RagiumCommonTranslation.EMI_BLOCK_CATALYST, "Placed below the block.")
    }

    private fun jade() {
        add(HTBlockConfigurationDataProvider, "Access Configuration")
        add(HTBlockMachinePropertiesProvider, "Machine Properties")
        add(HTBlockOwnerProvider, "Block Owner")

        add(RagiumCommonTranslation.JADE_MACHINE_TIER, $$"Tier: %1$s")
    }
}
