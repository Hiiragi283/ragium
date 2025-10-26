package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.data.lang.HTLanguageProvider
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.client.RagiumKeyMappings
import hiiragi283.ragium.client.integration.jade.provider.HTBlockConfigurationDataProvider
import hiiragi283.ragium.client.integration.jade.provider.HTBlockOwnerProvider
import hiiragi283.ragium.common.integration.food.RagiumDelightAddon
import hiiragi283.ragium.common.integration.food.RagiumFoodAddon
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.data.server.advancement.RagiumAdvancements
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCreativeTabs
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
        information()

        food()
        delight()
        jade()
    }

    private fun advancement() {
        addAdvancement(RagiumAdvancements.ROOT, "Ragium", "Welcome to Ragium!")
        addAdvancement(RagiumAdvancements.CRAFTABLE_TEMPLATES, "This thing craftable...!", "Craft any Upgrade Templates added by Ragium")
        // Raginite
        addAdvancement(RagiumAdvancements.RAGINITE, "Not a Redstone", "Get Raginite Dust from Raginite Ores in underground")
        addAdvancement(RagiumAdvancements.RAGI_CHERRY, "Food of twins", "Get Ragi-Cherry")
        addAdvancement(RagiumAdvancements.RAGI_CHERRY_TOAST, "The Last Breakfast", "Get Ragi-Cherry Toast Tower")
        addAdvancement(RagiumAdvancements.RAGI_ALLOY, "0xFF003F", "Get Ragi-Alloy Ingot")
        addAdvancement(RagiumAdvancements.ALLOY_SMELTER, "Al-Chemistry", "Get Alloy Smelter")

        addAdvancement(RagiumAdvancements.ADV_RAGI_ALLOY, "This is red, not orange!", "Get Advanced Ragi-Alloy Ingot")
        addAdvancement(RagiumAdvancements.MELTER, "Melty Kiss", "Get Melter")

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
        addAdvancement(RagiumAdvancements.SIMULATOR, "1 + 2 + 3 = 1 * 2 * 3", "Get Simulator")
        // Deep
        addAdvancement(RagiumAdvancements.RESONANT_DEBRIS, "Debris in the Ancient", "Get Resonant Debris")
        addAdvancement(RagiumAdvancements.DEEP_STEEL, "Deeper, Deeper, yet Deeper.", "Get Deep Steel")
        addAdvancement(RagiumAdvancements.DEEP_GEARS, "Black Hazard", "Get any Deep Steel Tool or Armor")

        addAdvancement(RagiumAdvancements.ECHO_STAR, "Shrieking Star", "Get Echo Star")
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
        addAdvancement(RagiumAdvancements.ELDRITCH_EGG, "Rotten Egg", "Throw Eldritch Egg to capture mobs")
        addAdvancement(RagiumAdvancements.MYSTERIOUS_OBSIDIAN, "Who is Falling Meteorites?", "")
        // Iridescentium
        addAdvancement(RagiumAdvancements.IRIDESCENTIUM, "The sky's the limit", "Get Iridescentium Ingot")
        addAdvancement(RagiumAdvancements.ETERNAL_COMPONENT, "Eternal Requiem", "Get Eternal Component for making tools unbreakable")
    }

    private fun block() {
        add(RagiumBlocks.SILT, "Silt")
        add(RagiumBlocks.AZURE_CLUSTER, "Azure Cluster")
        add(RagiumBlocks.MYSTERIOUS_OBSIDIAN, "Mysterious Obsidian")
        add(RagiumBlocks.CRIMSON_SOIL, "Crimson Soil")

        add(RagiumBlocks.EXP_BERRIES, "Exp Berries Bush", "Exp Berries")
        add(RagiumBlocks.WARPED_WART, "Warped Wart")

        add(RagiumBlocks.RESONANT_DEBRIS, "Resonant Debris")

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
        add(RagiumBlocks.STONE_CASING, "Stone Casing")
        add(RagiumBlocks.REINFORCED_STONE_CASING, "Reinforced Stone Casing")
        add(RagiumBlocks.WOODEN_CASING, "Wooden Casing")
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
        addFluid(RagiumFluidContents.HONEY, "Honey")
        addFluid(RagiumFluidContents.EXPERIENCE, "Experience Liquid")
        addFluid(RagiumFluidContents.MUSHROOM_STEW, "Mushroom Stew")

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

        addFluid(RagiumFluidContents.GILDED_LAVA, "Gilded Lava")
    }

    private fun item() {
        // Material
        add(RagiumItems.BASALT_MESH, "Basalt Mesh")
        add(RagiumItems.COMPRESSED_SAWDUST, "Compressed Sawdust")
        add(RagiumItems.ECHO_STAR, "Echo Star")
        add(RagiumItems.ELDER_HEART, "Heart of the Elder")
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
        add(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE, "Azure Steel Upgrade")
        add(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE, "Deep Steel Upgrade")

        add(RagiumItems.MEDIUM_DRUM_UPGRADE, "Medium Drum Upgrade")
        add(RagiumItems.LARGE_DRUM_UPGRADE, "Large Drum Upgrade")
        add(RagiumItems.HUGE_DRUM_UPGRADE, "Huge Drum Upgrade")

        add(RagiumItems.DRILL, "Electric Drill")

        add(RagiumItems.ADVANCED_MAGNET, "Advanced Ragi-Magnet")
        add(RagiumItems.BLAST_CHARGE, "Blast Charge")
        add(RagiumItems.DYNAMIC_LANTERN, "Ragi-Lantern")
        add(RagiumItems.ELDRITCH_EGG, "Eldritch Egg")
        add(RagiumItems.LOOT_TICKET, "Ragi-Ticket")
        add(RagiumItems.MAGNET, "Ragi-Magnet")
        add(RagiumItems.POTION_BUNDLE, "Potion Bundle")
        add(RagiumItems.SLOT_COVER, "Slot Cover")
        add(RagiumItems.TELEPORT_KEY, "Teleport Key")
        add(RagiumItems.TRADER_CATALOG, "Wandering Trader's Catalog")
        add(RagiumItems.UNIVERSAL_BUNDLE, "Universal Backpack")
        add(RagiumItems.WRENCH, "Ragi-Wrench")
        // Food
        add(RagiumItems.ICE_CREAM, "Ice Cream")
        add(RagiumItems.ICE_CREAM_SODA, "Ice Cream Soda")

        add(RagiumItems.CANNED_COOKED_MEAT, "Canned Cooked Meat")

        add(RagiumItems.SWEET_BERRIES_CAKE_SLICE, "Slice of Sweet Berries Cake")
        add(RagiumItems.MELON_PIE, "Melon Pie")

        add(RagiumItems.RAGI_CHERRY, "Ragi-Cherry")
        add(RagiumItems.FEVER_CHERRY, "Fever Cherry")

        add(RagiumItems.BOTTLED_BEE, "Bottled Bee")
        add(RagiumItems.AMBROSIA, "Ambrosia")
        // Parts
        add(RagiumItems.ADVANCED_CIRCUIT_BOARD, "Basalt-Reinforced Circuit Board")
        add(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        add(RagiumItems.GRAVITATIONAL_UNIT, "Gravitational Unit")
        add(RagiumItems.LED, "Light Emitting Diode")
        add(RagiumItems.LUMINOUS_PASTE, "Luminous Paste")
        add(RagiumItems.PLATING_CATALYST, "Plating Catalyst")
        add(RagiumItems.POLYMER_CATALYST, "Polymerization Catalyst")
        add(RagiumItems.POLYMER_RESIN, "Polymer Resin")
        add(RagiumItems.REDSTONE_BOARD, "Redstone Board")
        add(RagiumItems.SOLAR_PANEL, "Solar Panel")
        add(RagiumItems.SYNTHETIC_FIBER, "Synthetic Fiber")
        add(RagiumItems.SYNTHETIC_LEATHER, "Synthetic Leather")
    }

    private fun itemGroup() {
        addItemGroup(RagiumCreativeTabs.BLOCKS, "Ragium - Blocks")
        addItemGroup(RagiumCreativeTabs.INGREDIENTS, "Ragium - Ingredients")
        addItemGroup(RagiumCreativeTabs.ITEMS, "Ragium - Items")
    }

    private fun keyMapping() {
        add(RagiumTranslation.KEY_CATEGORY, "Ragium")

        add(RagiumTranslation.RECIPE_CUTTING, "Cutting")

        add(RagiumKeyMappings.OPEN_POTION_BUNDLE, "Open Potion Bundle")
        add(RagiumKeyMappings.OPEN_UNIVERSAL_BUNDLE, "Open Universal Bundle")
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
        add(RagiumRecipeTypes.COMPRESSING, "Compressing")
        add(RagiumRecipeTypes.CRUSHING, "Crushing")
        add(RagiumRecipeTypes.ENCHANTING, "Enchanting")
        add(RagiumRecipeTypes.EXTRACTING, "Extracting")
        add(RagiumRecipeTypes.FLUID_TRANSFORM, "Fluid Transforming")
        add(RagiumRecipeTypes.MELTING, "Melting")
        add(RagiumRecipeTypes.PLANTING, "Planting")
        add(RagiumRecipeTypes.SIMULATING, "Simulating")
        add(RagiumRecipeTypes.WASHING, "Washing")
    }

    private fun text() {
        add(RagiumTranslation.AZURE_STEEL_UPGRADE, "Azure Steel Upgrade")
        add(RagiumTranslation.AZURE_STEEL_UPGRADE_APPLIES_TO, "Azure Steel Equipment")
        add(RagiumTranslation.AZURE_STEEL_UPGRADE_INGREDIENTS, "Azure Steel Ingot")
        add(RagiumTranslation.AZURE_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION, "Add iron armor, weapon, ot tool")
        add(RagiumTranslation.AZURE_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "Add Azure Steel Ingot")

        add(RagiumTranslation.DEEP_STEEL_UPGRADE, "Deep Steel Upgrade")
        add(RagiumTranslation.DEEP_STEEL_UPGRADE_APPLIES_TO, "Deep Steel Equipment")
        add(RagiumTranslation.DEEP_STEEL_UPGRADE_INGREDIENTS, "Deep Steel Ingot")
        add(RagiumTranslation.DEEP_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION, "Add diamond armor, weapon, ot tool")
        add(RagiumTranslation.DEEP_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "Add Deep Steel Ingot")

        add(RagiumTranslation.ITEM_POTION, "Potion of %s")

        add(RagiumTranslation.TOOLTIP_ENERGY_PERCENTAGE, "%s / %s FE")
        add(RagiumTranslation.TOOLTIP_FLUID_NAME, "%s : %s mb")
        add(RagiumTranslation.TOOLTIP_FLUID_NAME_EMPTY, "Empty")
        add(RagiumTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, "Always has at least %s")
        add(RagiumTranslation.TOOLTIP_EFFECT_RANGE, "Effect Radius: %s blocks")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, "Loot Table: %s")
        add(RagiumTranslation.TOOLTIP_SHOW_INFO, "Press Shift to show info")
        add(RagiumTranslation.TOOLTIP_WIP, "This content is work in progress!!")

        add(HTAccessConfig.INPUT_ONLY, "Mode: Input")
        add(HTAccessConfig.OUTPUT_ONLY, "Mode: Output")
        add(HTAccessConfig.BOTH, "Mode: Both")
        add(HTAccessConfig.NONE, "Mode: None")
    }

    private fun information() {
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
        addInfo(RagiumItems.DYNAMIC_LANTERN, "Light up darkness in range.")
        addInfo(RagiumItems.MAGNET, "Collect dropped items in the effective range")
        addInfo(RagiumItems.SLOT_COVER, "Ignored by recipes when placed in machine slot.")
        addInfo(RagiumItems.TRADER_CATALOG, "Dropped from Wandering Trader.")
    }

    //    Addon    //

    private fun food() {
        add(RagiumFoodAddon.RAGI_CHERRY_JAM, "Ragi-Cherry Jam")
        add(RagiumFoodAddon.RAGI_CHERRY_PULP, "Ragi-Cherry Pulp")
    }

    private fun delight() {
        add(RagiumDelightAddon.RAGI_CHERRY_PIE, "Ragi-Cherry Pie")
        add(RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK, "Ragi-Cherry Toast Tower")

        add(RagiumDelightAddon.RAGI_CHERRY_PIE_SLICE, "Slice of Ragi-Cherry Pie")
        add(RagiumDelightAddon.RAGI_CHERRY_TOAST, "Ragi-Cherry Toast")
    }

    private fun jade() {
        add(HTBlockConfigurationDataProvider, "Access Configuration")
        add(HTBlockOwnerProvider, "Block Owner")
    }
}
