package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumAdvancements
import hiiragi283.ragium.api.extension.addAdvancement
import hiiragi283.ragium.api.extension.addEnchantment
import hiiragi283.ragium.api.extension.addFluid
import hiiragi283.ragium.api.extension.addInfo
import hiiragi283.ragium.api.extension.addMatterType
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.integration.replication.RagiumReplicationAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumEnchantments
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumToolTiers
import net.minecraft.data.PackOutput
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Tiers
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumEnglishProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "en_us") {
    override fun addTranslations() {
        advancement()
        block()
        enchantment()
        entity()
        fluid()
        item()
        modTags()
        text()
        information()

        delight()
        jade()
        mekanism()
        replication()
    }

    private fun advancement() {
        addAdvancement(RagiumAdvancements.ROOT, "Ragium", "Start Ragium by getting Blank Ticket")
        addAdvancement(RagiumAdvancements.ETERNAL_TICKET, "Eternal Requiem", "Get Eternal Ticket for making tools unbreakable")
        // Raginite
        addAdvancement(RagiumAdvancements.RAGI_TICKET, "The Red Comet", "Get Ragi-Ticket")
        addAdvancement(RagiumAdvancements.RAGINITE_DUST, "0xFF003F", "Get Raginite Dust by right-clicking Redstone Ore with Ragi-Ticket")
        // Azure
        addAdvancement(RagiumAdvancements.AZURE_TICKET, "The ticket is bluish.", "Get Azure Ticket")
        addAdvancement(
            RagiumAdvancements.AZURE_SHARD,
            "Not a azurite",
            "Get Azure Shard by right-clicking Amethyst Cluster with Azure Ticket",
        )
        addAdvancement(RagiumAdvancements.AZURE_GEARS, "Wake up! Azure Dragon!", "Get any Azure Steel Tool or Armor")
        // Crimson
        addAdvancement(RagiumAdvancements.CRIMSON_CRYSTAL, "Blood Sweat and Saps", "Get Crimson Crystal")
        addAdvancement(
            RagiumAdvancements.CRIMSON_SOIL,
            "The reason why rose is red",
            "Get Crimson Soil by right-clicking Soul Soil with Bloody Ticket",
        )
        // Warped
        addAdvancement(RagiumAdvancements.WARPED_CRYSTAL, "Wrapped Warp", "Get Warped Crystal")
        addAdvancement(RagiumAdvancements.TELEPORT_TICKET, "One-way Ticket", "Use teleport ticket bound with Teleport Anchor")
        // Eldritch
        addAdvancement(RagiumAdvancements.ELDRITCH_PEARL, "Not a Primordial", "Get Eldritch Pearl")
        addAdvancement(
            RagiumAdvancements.MYSTERIOUS_OBSIDIAN,
            "Who is Falling Meteorites？",
            "Get Mysterious Obsidian by right-clicking Crying Obsidian with Eldritch Ticket",
        )
    }

    private fun block() {
        addBlock(RagiumBlocks.SILT, "Silt")
        addBlock(RagiumBlocks.MYSTERIOUS_OBSIDIAN, "Mysterious Obsidian")
        addBlock(RagiumBlocks.CRIMSON_SOIL, "Crimson Soil")

        addBlock(RagiumBlocks.ASH_LOG, "Ash Log")
        addBlock(RagiumBlocks.EXP_BERRY_BUSH, "Exp Berries Bush")

        RagiumBlocks.RAGINITE_ORES.addTranslationEn("Raginite", this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.addTranslationEn("Ragi-Crystal", this)
        addBlock(RagiumBlocks.RESONANT_DEBRIS, "Resonant Debris")

        addBlock(RagiumBlocks.RAGI_CRYSTAL_BLOCK, "Block of Ragi-Crystal")
        addBlock(RagiumBlocks.CRIMSON_CRYSTAL_BLOCK, "Block of Crimson Crystal")
        addBlock(RagiumBlocks.WARPED_CRYSTAL_BLOCK, "Block of Warped Crystal")
        addBlock(RagiumBlocks.ELDRITCH_PEARL_BLOCK, "Block of Eldritch Pearl")

        addBlock(RagiumBlocks.RAGI_ALLOY_BLOCK, "Block of Ragi-Alloy")
        addBlock(RagiumBlocks.ADVANCED_RAGI_ALLOY_BLOCK, "Block of Advanced Ragi-Alloy")
        addBlock(RagiumBlocks.AZURE_STEEL_BLOCK, "Block of Azure Steel")
        addBlock(RagiumBlocks.DEEP_STEEL_BLOCK, "Block of Deep Steel")

        addBlock(RagiumBlocks.CHOCOLATE_BLOCK, "Block of Chocolate")
        addBlock(RagiumBlocks.MEAT_BLOCK, "Block of Meat")
        addBlock(RagiumBlocks.COOKED_MEAT_BLOCK, "Block of Cooked Meat")

        RagiumBlocks.RAGI_STONE_SETS.addTranslationEn("Ragi-Stone", this)
        RagiumBlocks.RAGI_STONE_BRICKS_SETS.addTranslationEn("Ragi-Stone Brick", this)
        RagiumBlocks.RAGI_STONE_SQUARE_SETS.addTranslationEn("Ragi-Stone (Square)", this)
        RagiumBlocks.AZURE_TILE_SETS.addTranslationEn("Azure Tile", this)
        RagiumBlocks.EMBER_STONE_SETS.addTranslationEn("Ember Stone", this)
        RagiumBlocks.PLASTIC_SETS.addTranslationEn("Plastic Block", this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addTranslationEn("Blue Nether Bricks", this)
        RagiumBlocks.SPONGE_CAKE_SETS.addTranslationEn("Sponge Cake", this)

        addBlock(RagiumBlocks.OBSIDIAN_GLASS, "Obsidian Glass")
        addBlock(RagiumBlocks.QUARTZ_GLASS, "Quartz Glass")
        addBlock(RagiumBlocks.SOUL_GLASS, "Soul Glass")

        addBlock(RagiumBlocks.getLedBlock(DyeColor.RED), "Red LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.GREEN), "Green LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.BLUE), "Blue LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.CYAN), "Cyan LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.MAGENTA), "Magenta LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.YELLOW), "Yellow LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.WHITE), "LED Block")

        addBlock(RagiumBlocks.COOKED_MEAT_ON_THE_BONE, "Cooked Meat on the Bone")
        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

        addBlock(RagiumBlocks.ADVANCED_MACHINE_FRAME, "Advanced Machine Frame")
        addBlock(RagiumBlocks.BASIC_MACHINE_FRAME, "Basic Machine Frame")
        addBlock(RagiumBlocks.DEVICE_CASING, "Device Casing")
        addBlock(RagiumBlocks.ELITE_MACHINE_FRAME, "Elite Machine Frame")
        addBlock(RagiumBlocks.STONE_CASING, "Stone Casing")
        addBlock(RagiumBlocks.WOODEN_CASING, "Wooden Casing")
        // Machine
        addBlock(RagiumBlocks.CRUSHER, "Crusher")
        addBlock(RagiumBlocks.BLOCK_BREAKER, "Block Breaker")
        addBlock(RagiumBlocks.EXTRACTOR, "Extractor")

        addBlock(RagiumBlocks.ALLOY_SMELTER, "Alloy Smelter")
        addBlock(RagiumBlocks.FORMING_PRESS, "Forming Press")
        addBlock(RagiumBlocks.MELTER, "Melter")
        addBlock(RagiumBlocks.REFINERY, "Refinery")
        addBlock(RagiumBlocks.SOLIDIFIER, "Solidifier")

        addBlock(RagiumBlocks.INFUSER, "Arcane Infuser")
        // Device
        addBlock(RagiumBlocks.CEU, "C.E.U")
        addBlock(RagiumBlocks.ENI, "E.N.I.")
        addBlock(RagiumBlocks.EXP_COLLECTOR, "Exp Collector")
        addBlock(RagiumBlocks.ITEM_BUFFER, "Item Buffer")
        addBlock(RagiumBlocks.LAVA_COLLECTOR, "Lava Collector")
        addBlock(RagiumBlocks.MILK_DRAIN, "Milk Drain")
        addBlock(RagiumBlocks.SPRINKLER, "Sprinkler")
        addBlock(RagiumBlocks.TELEPORT_ANCHOR, "Teleport Anchor")
        addBlock(RagiumBlocks.WATER_COLLECTOR, "Water Collector")
        // Storage
        addBlock(RagiumBlocks.SMALL_DRUM, "Small Drum")
        addBlock(RagiumBlocks.MEDIUM_DRUM, "Medium Drum")
        addBlock(RagiumBlocks.LARGE_DRUM, "Large Drum")
        addBlock(RagiumBlocks.HUGE_DRUM, "Huge Drum")
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
        addFluid(RagiumFluidContents.CRIMSON_DIESEL, "Crimson Diesel")
        addFluid(RagiumFluidContents.LUBRICANT, "Lubricant")

        addFluid(RagiumFluidContents.SAP, "Sap")
        addFluid(RagiumFluidContents.SYRUP, "Syrup")
        addFluid(RagiumFluidContents.CRIMSON_SAP, "Crimson Sap")
        addFluid(RagiumFluidContents.WARPED_SAP, "Warped Sap")
    }

    private fun item() {
        // Tickets
        addItem(RagiumItems.BLANK_TICKET, "Blank Ticket")

        addItem(RagiumItems.RAGI_TICKET, "Ragi-Ticket")
        addItem(RagiumItems.AZURE_TICKET, "Azure Ticket")
        addItem(RagiumItems.BLOODY_TICKET, "Bloody Ticket")
        addItem(RagiumItems.TELEPORT_TICKET, "Teleport Ticket")
        addItem(RagiumItems.ELDRITCH_TICKET, "Eldritch Ticket")

        addItem(RagiumItems.DAYBREAK_TICKET, "Daybreak Ticket")
        addItem(RagiumItems.ETERNAL_TICKET, "Eternal Ticket")
        // Material
        addItem(RagiumItems.RAGI_COKE, "Ragi-Coke")
        addItem(RagiumItems.AZURE_SHARD, "Azure Shard")
        addItem(RagiumItems.COMPRESSED_SAWDUST, "Compressed Sawdust")
        addItem(RagiumItems.TAR, "Tar")
        addItem(RagiumItems.DEEP_SCRAP, "Deep Scrap")

        addItem(RagiumItems.RAGI_CRYSTAL, "Ragi-Crystal")
        addItem(RagiumItems.CRIMSON_CRYSTAL, "Crimson Crystal")
        addItem(RagiumItems.WARPED_CRYSTAL, "Warped Crystal")
        addItem(RagiumItems.ELDRITCH_ORB, "Eldritch Orb")
        addItem(RagiumItems.ELDRITCH_PEARL, "Eldritch Pearl")

        addItem(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        addItem(RagiumItems.RAGI_ALLOY_INGOT, "Ragi-Alloy Ingot")
        addItem(RagiumItems.RAGI_ALLOY_NUGGET, "Ragi-Alloy Nugget")
        addItem(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND, "Advanced Ragi-Alloy Compound")
        addItem(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT, "Advanced Ragi-Alloy Ingot")
        addItem(RagiumItems.ADVANCED_RAGI_ALLOY_NUGGET, "Advanced Ragi-Alloy Nugget")
        addItem(RagiumItems.AZURE_STEEL_COMPOUND, "Azure Steel Compound")
        addItem(RagiumItems.AZURE_STEEL_INGOT, "Azure Steel Ingot")
        addItem(RagiumItems.AZURE_STEEL_NUGGET, "Azure Steel Nugget")
        addItem(RagiumItems.DEEP_STEEL_INGOT, "Deep Steel Ingot")

        addItem(RagiumItems.CHOCOLATE_INGOT, "Chocolate Ingot")

        addItem(RagiumItems.ASH_DUST, "Ash")
        addItem(RagiumItems.CINNABAR_DUST, "Cinnabar Dust")
        addItem(RagiumItems.OBSIDIAN_DUST, "Obsidian Dust")
        addItem(RagiumItems.QUARTZ_DUST, "Quartz Dust")
        addItem(RagiumItems.RAGINITE_DUST, "Raginite Dust")
        addItem(RagiumItems.SALTPETER_DUST, "Saltpeter Dust")
        addItem(RagiumItems.SAWDUST, "Sawdust")
        addItem(RagiumItems.SULFUR_DUST, "Sulfur Dust")
        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addTranslationEn("Azure Steel", this)
        RagiumItems.DEEP_STEEL_ARMORS.addTranslationEn("Deep Steel", this)
        // Tool
        addItem(RagiumItems.ADVANCED_RAGI_ALLOY_UPGRADE_SMITHING_TEMPLATE, "Advanced Ragi-Alloy Upgrade")
        addItem(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE, "Azure Steel Upgrade")
        addItem(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE, "Deep Steel Upgrade")
        RagiumItems.AZURE_STEEL_TOOLS.addTranslationEn("Azure Steel", this)
        RagiumItems.DEEP_STEEL_TOOLS.addTranslationEn("Deep Steel", this)

        addItem(RagiumItems.getForgeHammer(Tiers.IRON), "Iron Forge Hammer")
        addItem(RagiumItems.getForgeHammer(Tiers.DIAMOND), "Diamond Forge Hammer")
        addItem(RagiumItems.getForgeHammer(Tiers.NETHERITE), "Netherite Forge Hammer")
        addItem(RagiumItems.getForgeHammer(RagiumToolTiers.RAGI_ALLOY), "Ragi-Alloy Forge Hammer")
        addItem(RagiumItems.getForgeHammer(RagiumToolTiers.AZURE_STEEL), "Azure Steel Forge Hammer")
        addItem(RagiumItems.getForgeHammer(RagiumToolTiers.DEEP_STEEL), "Deep Steel Forge Hammer")

        addItem(RagiumItems.ADVANCED_RAGI_MAGNET, "Advanced Ragi-Magnet")
        addItem(RagiumItems.BLAST_CHARGE, "Blast Charge")
        addItem(RagiumItems.ELDRITCH_EGG, "Eldritch Egg")
        addItem(RagiumItems.ENDER_BUNDLE, "Ender Bundle")
        addItem(RagiumItems.POTION_BUNDLE, "Potion Bundle")
        addItem(RagiumItems.RAGI_LANTERN, "Ragi-Lantern")
        addItem(RagiumItems.RAGI_MAGNET, "Ragi-Magnet")
        addItem(RagiumItems.SLOT_COVER, "Slot Cover")
        addItem(RagiumItems.TRADER_CATALOG, "Wandering Trader's Catalog")
        // Food
        addItem(RagiumItems.ICE_CREAM, "Ice Cream")
        addItem(RagiumItems.ICE_CREAM_SODA, "Ice Cream Soda")

        addItem(RagiumItems.MINCED_MEAT, "Minced Meat")
        addItem(RagiumItems.MEAT_INGOT, "Meat Ingot")
        addItem(RagiumItems.COOKED_MEAT_INGOT, "Cooked Meat Ingot")
        addItem(RagiumItems.CANNED_COOKED_MEAT, "Canned Cooked Meat")

        addItem(RagiumItems.SWEET_BERRIES_CAKE_SLICE, "Slice of Sweet Berries Cake")
        addItem(RagiumItems.MELON_PIE, "Melon Pie")

        addItem(RagiumItems.RAGI_CHERRY, "Ragi-Cherry")
        addItem(RagiumItems.RAGI_CHERRY_JAM, "Ragi-Cherry Jam")
        addItem(RagiumItems.FEVER_CHERRY, "Fever Cherry")

        addItem(RagiumItems.BOTTLED_BEE, "Bottled Bee")
        addItem(RagiumItems.EXP_BERRIES, "Exp Berries")
        addItem(RagiumItems.WARPED_WART, "Warped Wart")
        addItem(RagiumItems.AMBROSIA, "Ambrosia")
        // Parts
        addItem(RagiumItems.ADVANCED_CIRCUIT, "Advanced Circuit")
        addItem(RagiumItems.BASIC_CIRCUIT, "Basic Circuit")
        addItem(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        addItem(RagiumItems.ELDER_HEART, "Heart of the Elder")
        addItem(RagiumItems.ELITE_CIRCUIT, "Elite Circuit")
        addItem(RagiumItems.LED, "Light Emitting Diode")
        addItem(RagiumItems.LUMINOUS_PASTE, "Luminous Paste")
        addItem(RagiumItems.PLASTIC_PLATE, "Plastic Plate")
        addItem(RagiumItems.POLYMER_RESIN, "Polymer Resin")
        addItem(RagiumItems.REDSTONE_BOARD, "Redstone Board")
        addItem(RagiumItems.SOLAR_PANEL, "Solar Panel")
        addItem(RagiumItems.SYNTHETIC_FIBER, "Synthetic Fiber")
        addItem(RagiumItems.SYNTHETIC_LEATHER, "Synthetic Leather")
        addItem(RagiumItems.ULTIMATE_CIRCUIT, "Ultimate Circuit")
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
        add(RagiumModTags.Items.POLYMER_RESIN, "Polymer Resins")
        add(RagiumModTags.Items.WIP, "Work In Progress")

        add(RagiumModTags.Items.ENI_UPGRADES, "E.N.I. Upgrades")
        add(RagiumModTags.Items.ENI_UPGRADES_BASIC, "Basic E.N.I. Upgrades")
        add(RagiumModTags.Items.ENI_UPGRADES_ADVANCED, "Advanced E.N.I. Upgrades")
        add(RagiumModTags.Items.ENI_UPGRADES_ELITE, "Elite E.N.I. Upgrades")
        add(RagiumModTags.Items.ENI_UPGRADES_ULTIMATE, "Ultimate E.N.I. Upgrades")

        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, "Basic Fluxes for Alloy Smelter")
        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, "Advanced Fluxes for Alloy Smelter")
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
    }

    private fun information() {
        addInfo(RagiumBlocks.ASH_LOG, "Drop Ash Dust when harvested.")
        addInfo(RagiumBlocks.CRIMSON_SOIL, "Apply damages from fake player for above mobs.")
        addInfo(RagiumBlocks.OBSIDIAN_GLASS, "As the same blast resistance as Obsidian, can be harvested without Silk Touch.")
        addInfo(RagiumBlocks.QUARTZ_GLASS, "This glass block can be harvested without Silk Touch.")
        addInfo(RagiumBlocks.SOUL_GLASS, "Only passable with Players, can be harvested without Silk Touch.")

        addInfo(RagiumItems.AMBROSIA, "ALWAYS EDIBLE and NOT CONSUMED!")
        addInfo(RagiumItems.BLAST_CHARGE, "Can be upgraded by gunpowders in Crafting Table.")
        addInfo(RagiumItems.ELDER_HEART, "Dropped from Elder Guardian.")
        addInfo(RagiumItems.ELDRITCH_EGG, "Can be throwable by right-click，then capture mobs when hit.")
        addInfo(RagiumItems.ICE_CREAM, "Extinguish fire when eaten.")
        addInfo(RagiumItems.RAGI_CHERRY, "Drops from Cherry Leaves as same as Apple.")
        addInfo(RagiumItems.RAGI_LANTERN, "Light up darkness in range.")
        addInfo(RagiumItems.RAGI_MAGNET, "Collect dropped items in the effective range")
        addInfo(RagiumItems.SLOT_COVER, "Ignored by recipes when placed in machine slot.")
        addInfo(RagiumItems.TRADER_CATALOG, "Dropped from Wandering Trader.")
        addInfo(RagiumItems.WARPED_WART, "Clear one bad effect randomly when eaten.")
    }

    private fun delight() {
        addItem(RagiumDelightAddon.RAGI_CHERRY_PULP, "Ragi-Cherry Pulp")
    }

    private fun jade() {
        add("config.jade.plugin_ragium.output_side", "Output Side")

        add(RagiumTranslationKeys.JADE_OUTPUT_SIDE, "Output Side: %s")
    }

    private fun mekanism() {
        add(RagiumMekanismAddon.CHEMICAL_RAGINITE.translationKey, "Raginite")
        add(RagiumMekanismAddon.CHEMICAL_AZURE.translationKey, "Azure Essence")
        add(RagiumMekanismAddon.CHEMICAL_CRIMSON_SAP.translationKey, "Crimson Sap")
        add(RagiumMekanismAddon.CHEMICAL_WARPED_SAP.translationKey, "Warped Sap")

        addItem(RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE, "Enriched Raginite")
        addItem(RagiumMekanismAddon.ITEM_ENRICHED_AZURE, "Enriched Azure Essence")
    }

    private fun replication() {
        addMatterType(RagiumReplicationAddon.MATTER_RAGIUM.get(), "Ragium")
    }
}
