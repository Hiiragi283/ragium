package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.common.init.*
import net.minecraft.data.PackOutput
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumEnglishProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "en_us") {
    override fun addTranslations() {
        block()
        enchantment()
        entity()
        fluid()
        item()
        material()
        tagPrefix()
        tag()
        tooltips()
        misc()

        mekanism()
        jade()
        emi()
    }

    private fun block() {
        addBlock(RagiumBlocks.SILT, "Silt")
        addBlock(RagiumBlocks.CRUDE_OIL, "Crude Oil")
        addBlock(RagiumBlocks.ASH_LOG, "Ash Log")

        RagiumBlocks.RAGI_BRICK_SETS.addTranslationEn("Ragi-Brick", this)
        RagiumBlocks.AZURE_TILE_SETS.addTranslationEn("Azure Tile", this)
        RagiumBlocks.EMBER_STONE_SETS.addTranslationEn("Ember Stone", this)
        RagiumBlocks.PLASTIC_SETS.addTranslationEn("Plastic Block", this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addTranslationEn("Blue Nether Bricks", this)

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

        addBlock(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")
        addBlock(RagiumBlocks.SPONGE_CAKE_SLAB, "Sponge Cake Slab")
        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

        addBlock(RagiumBlocks.ADVANCED_MACHINE_CASING, "Advanced Machine Casing")
        addBlock(RagiumBlocks.DEVICE_CASING, "Device Casing")
        addBlock(RagiumBlocks.MACHINE_CASING, "Machine Casing")
        addBlock(RagiumBlocks.STONE_CASING, "Stone Casing")
        addBlock(RagiumBlocks.WOODEN_CASING, "Wooden Casing")

        // addBlock(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")
        // addBlock(RagiumBlocks.TELEPORT_ANCHOR, "Teleport Anchor")

        addBlock(RagiumBlocks.CRUSHER, "Crusher")
        addBlock(RagiumBlocks.EXTRACTOR, "Extractor")

        addBlock(RagiumBlocks.CENTRIFUGE, "Centrifuge")
        addBlock(RagiumBlocks.INFUSER, "Infuser")

        addBlock(RagiumBlocks.WATER_WELL, "Water Well")
        addBlock(RagiumBlocks.LAVA_WELL, "Lava Well")
        addBlock(RagiumBlocks.MILK_DRAIN, "Milk Drain")

        addBlock(RagiumBlocks.ENI, "E.N.I.")
        addBlock(RagiumBlocks.SPRINKLER, "Sprinkler")
    }

    private fun enchantment() {
        addEnchantment(RagiumEnchantments.CAPACITY, "Capacity", "Increase the capacity of item or fluid storages")
    }

    private fun entity() {
        // addEntityType(RagiumEntityTypes.DYNAMITE, "Dynamite")
        // addEntityType(RagiumEntityTypes.DEFOLIANT_DYNAMITE, "Defoliant Dynamite")
        // addEntityType(RagiumEntityTypes.FLATTEN_DYNAMITE, "Flatten Dynamite")
        // addEntityType(RagiumEntityTypes.NAPALM_DYNAMITE, "Napalm Dynamite")
        // addEntityType(RagiumEntityTypes.POISON_DYNAMITE, "Poison Dynamite")
    }

    private fun fluid() {
        addFluid(RagiumFluids.HONEY, "Honey")
        addFluid(RagiumFluids.SNOW, "Powder Snow")

        addFluid(RagiumVirtualFluids.EXPERIENCE, "Experience Liquid")

        addFluid(RagiumVirtualFluids.CHOCOLATE, "Chocolate")
        addFluid(RagiumVirtualFluids.MUSHROOM_STEW, "Mushroom Stew")

        addFluid(RagiumVirtualFluids.AIR, "Air")

        addFluid(RagiumVirtualFluids.HYDROGEN, "Hydrogen")

        addFluid(RagiumVirtualFluids.NITROGEN, "Nitrogen")
        addFluid(RagiumVirtualFluids.AMMONIA, "Ammonia")
        addFluid(RagiumVirtualFluids.NITRIC_ACID, "Nitric Acid")
        addFluid(RagiumVirtualFluids.MIXTURE_ACID, "Mixture Acid")

        addFluid(RagiumVirtualFluids.OXYGEN, "Oxygen")
        addFluid(RagiumVirtualFluids.ROCKET_FUEL, "Rocket Fuel")

        addFluid(RagiumVirtualFluids.ALKALI_SOLUTION, "Alkali Solution")

        addFluid(RagiumVirtualFluids.SULFUR_DIOXIDE, "Sulfur Dioxide")
        addFluid(RagiumVirtualFluids.SULFUR_TRIOXIDE, "Sulfur Trioxide")
        addFluid(RagiumVirtualFluids.SULFURIC_ACID, "Sulfuric Acid")

        addFluid(RagiumVirtualFluids.NAPHTHA, "Naphtha")
        addFluid(RagiumVirtualFluids.FUEL, "Fuel")
        addFluid(RagiumVirtualFluids.NITRO_FUEL, "Nitro Fuel")

        addFluid(RagiumVirtualFluids.AROMATIC_COMPOUND, "Aromatic Compound")

        addFluid(RagiumVirtualFluids.PLANT_OIL, "Plant Oil")
        addFluid(RagiumVirtualFluids.BIOMASS, "Biomass")
        addFluid(RagiumVirtualFluids.ETHANOL, "Ethanol")

        addFluid(RagiumVirtualFluids.CRUDE_BIODIESEL, "Crude Biodiesel")
        addFluid(RagiumVirtualFluids.BIODIESEL, "Biodiesel")
        addFluid(RagiumVirtualFluids.GLYCEROL, "Glycerol")
        addFluid(RagiumVirtualFluids.NITROGLYCERIN, "Nitroglycerin")

        addFluid(RagiumVirtualFluids.SAP, "Sap")
        addFluid(RagiumVirtualFluids.CRIMSON_SAP, "Crimson Sap")
        addFluid(RagiumVirtualFluids.WARPED_SAP, "Warped Sap")

        addFluid(RagiumVirtualFluids.RAGIUM_SOLUTION, "Ragium Solution")
    }

    private fun item() {
        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addTranslationEn("Azure Steel", this)
        // Tool
        RagiumItems.RAGI_ALLOY_TOOLS.addTranslationEn("Ragi-Alloy", this)
        RagiumItems.AZURE_STEEL_TOOLS.addTranslationEn("Azure Steel", this)

        addItem(RagiumItems.ENDER_BUNDLE, "Ender Bundle")
        addItem(RagiumItems.ITEM_MAGNET, "Item Magnet")
        addItem(RagiumItems.TRADER_CATALOG, "Wandering Trader's Catalog")
        // Food
        addItem(RagiumItems.SWEET_BERRIES_CAKE_PIECE, "A piece of Sweet Berries Cake")
        addItem(RagiumItems.MELON_PIE, "Melon Pie")

        addItem(RagiumItems.BUTTER, "Butter")
        addItem(RagiumItems.ICE_CREAM, "Ice Cream")

        addItem(RagiumItems.DOUGH, "Dough")
        addItem(RagiumItems.FLOUR, "Flour")

        addItem(RagiumItems.CHOCOLATE_APPLE, "Chocolate Apple")
        addItem(RagiumItems.CHOCOLATE_BREAD, "Chocolate Bread")
        addItem(RagiumItems.CHOCOLATE_COOKIE, "Chocolate Cookie")

        addItem(RagiumItems.MINCED_MEAT, "Minced Meat")
        addItem(RagiumItems.MEAT_INGOT, "Meat Ingot")
        addItem(RagiumItems.COOKED_MEAT_INGOT, "Cooked Meat Ingot")
        addItem(RagiumItems.CANNED_COOKED_MEAT, "Canned Cooked Meat")
        addItem(RagiumItems.MEAT_SANDWICH, "Meat Sandwich")

        addItem(RagiumItems.WARPED_WART, "Warped Wart")

        addItem(RagiumItems.AMBROSIA, "Ambrosia")

        /*addItem(RagiumItems.POTION_BUNDLE, "Potion Bundle")
        addItem(RagiumItems.DURALUMIN_CASE, "Duralumin Case")
        addItem(RagiumItems.RAGI_LANTERN, "Ragi-Lantern")

        addItem(RagiumItems.ITEM_MAGNET, "Item Magnet")
        addItem(RagiumItems.EXP_MAGNET, "Exp Magnet")

        addItem(RagiumItems.DYNAMITE, "Dynamite")
        addItem(RagiumItems.DEFOLIANT_DYNAMITE, "Defoliant Dynamite")
        addItem(RagiumItems.FLATTEN_DYNAMITE, "Flatten Dynamite")
        addItem(RagiumItems.NAPALM_DYNAMITE, "Napalm Dynamite")
        addItem(RagiumItems.POISON_DYNAMITE, "Poison Dynamite")*/
        // Mold
        addMold(RagiumItems.Molds.BLANK, "Mold (Blank)")
        addMold(RagiumItems.Molds.BALL, "Mold (Ball)")
        addMold(RagiumItems.Molds.BLOCK, "Mold (Block)")
        addMold(RagiumItems.Molds.GEAR, "Mold (Gear)")
        addMold(RagiumItems.Molds.INGOT, "Mold (Ingot)")
        addMold(RagiumItems.Molds.PLATE, "Mold (Plate)")
        addMold(RagiumItems.Molds.ROD, "Mold (Rod)")
        addMold(RagiumItems.Molds.WIRE, "Mold (Wire)")
        // Parts
        // addItem(RagiumItems.BEE_WAX, "Bee Wax")
        addItem(RagiumItems.ADVANCED_CIRCUIT, "Advanced Circuit")
        addItem(RagiumItems.BASIC_CIRCUIT, "Basic Circuit")
        addItem(RagiumItems.CRUDE_OIL_BUCKET, "Crude Oil Bucket")
        addItem(RagiumItems.ENGINE, "V8 Engine")
        addItem(RagiumItems.LED, "L.E.D.")
        addItem(RagiumItems.PLASTIC_PLATE, "Plastic Plate")
        addItem(RagiumItems.POLYMER_RESIN, "Polymer Resin")
        addItem(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        addItem(RagiumItems.SOAP, "Soap")
        addItem(RagiumItems.SOLAR_PANEL, "Solar Panel")
        addItem(RagiumItems.STONE_BOARD, "Stone Board")
        addItem(RagiumItems.TAR, "Tar")
        addItem(RagiumItems.YELLOW_CAKE, "Yellow Cake")
        addItem(RagiumItems.YELLOW_CAKE_PIECE, "A piece of Yellow Cake")

        // addItem(RagiumItems.BLANK_TICKET, "Blank Ticket")
        // addItem(RagiumItems.TELEPORT_TICKET, "Teleport Ticket")
        // addItem(RagiumItems.RAGI_TICKET, "Ragi-Ticket")
    }

    private fun material() {
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
        addMaterialKey(RagiumMaterials.RAGIUM, "Ragium")
        addMaterialKey(RagiumMaterials.WARPED_CRYSTAL, "Warped Crystal")
        // Vanilla
        addMaterialKey(VanillaMaterials.AMETHYST, "Amethyst")
        addMaterialKey(VanillaMaterials.CALCITE, "Calcite")
        addMaterialKey(VanillaMaterials.COAL, "Coal")
        addMaterialKey(VanillaMaterials.COPPER, "Copper")
        addMaterialKey(VanillaMaterials.DIAMOND, "Diamond")
        addMaterialKey(VanillaMaterials.EMERALD, "Emerald")
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
        addTagPrefix(HTTagPrefix.CLUMP, "%s Clump")
        addTagPrefix(HTTagPrefix.CRYSTAL, "%s Crystal")
        addTagPrefix(HTTagPrefix.DIRTY_DUST, "Dirty %s Dust")
        addTagPrefix(HTTagPrefix.DUST, "%s Dust")
        addTagPrefix(HTTagPrefix.GEAR, "%s Gear")
        addTagPrefix(HTTagPrefix.GEM, "%s")
        addTagPrefix(HTTagPrefix.INGOT, "%s Ingot")
        addTagPrefix(HTTagPrefix.NUGGET, "%s Nugget")
        addTagPrefix(HTTagPrefix.ORE, "%s Ore")
        addTagPrefix(HTTagPrefix.PLATE, "%s Plate")
        addTagPrefix(HTTagPrefix.RAW_MATERIAL, "Raw %s")
        addTagPrefix(HTTagPrefix.RAW_STORAGE, "Block of Raw %s")
        addTagPrefix(HTTagPrefix.ROD, "%s Rod")
        addTagPrefix(HTTagPrefix.SHARD, "%s Shard")
        addTagPrefix(HTTagPrefix.SHEETMETAL, "%s Sheetmetal")
        addTagPrefix(HTTagPrefix.STORAGE_BLOCK, "Block of %s")
        addTagPrefix(HTTagPrefix.TINY_DUST, "Tiny %s Dust")
        addTagPrefix(HTTagPrefix.WIRE, "%s Wire")
    }

    private fun tag() {
        // Fluid
        add(RagiumFluidTags.CHOCOLATES, "Chocolate")
        add(RagiumFluidTags.CREOSOTE, "Creosote")
        add(RagiumFluidTags.MEAT, "Meat")
        add(RagiumFluidTags.STEAM, "Steam")

        add(RagiumFluidTags.NITRO_FUEL, "Nitro Fuel")
        add(RagiumFluidTags.NON_NITRO_FUEL, "Non-Nitro Fuel")
        add(RagiumFluidTags.THERMAL_FUEL, "Thermal Fuel")
        // Item
        add(RagiumItemTags.COAL_COKE, "Coal Coke")
        add(RagiumItemTags.PAPER, "Paper")
        add(RagiumItemTags.PLASTICS, "Plastic")
        add(RagiumItemTags.SILICON, "Silicon")
        add(RagiumItemTags.SLAG, "Slag")
        add(RagiumItemTags.TOOLS_FORGE_HAMMER, "Forge Hammer")
        add(RagiumItemTags.BUCKETS_CRUDE_OIL, "Crude Oil Bucket")

        add(RagiumItemTags.CROPS_WARPED_WART, "Warped Wart")
        add(RagiumItemTags.FLOURS, "Flours")
        add(RagiumItemTags.FOOD_BUTTER, "Butter")
        add(RagiumItemTags.FOOD_CHEESE, "Cheese")
        add(RagiumItemTags.FOOD_CHOCOLATE, "Chocolate")
        add(RagiumItemTags.FOOD_DOUGH, "Dough")

        add(RagiumItemTags.CIRCUITS, "Circuit")
        add(RagiumItemTags.CIRCUITS_BASIC, "Basic Circuit")
        add(RagiumItemTags.CIRCUITS_ADVANCED, "Advanced Circuit")
        add(RagiumItemTags.CIRCUITS_ELITE, "Elite Circuit")

        add(RagiumItemTags.GLASS_BLOCKS_OBSIDIAN, "Obsidian Glass")
        add(RagiumItemTags.GLASS_BLOCKS_QUARTZ, "Quartz Glass")

        add(RagiumItemTags.DYNAMITES, "Dynamite")
        add(RagiumItemTags.LED_BLOCKS, "LED Block")

        add(RagiumItemTags.MOLDS, "Press Mold")
        add(RagiumItemTags.MOLDS_BALL, "Press Mold (Ball)")
        add(RagiumItemTags.MOLDS_BLANK, "Press Mold (Blank)")
        add(RagiumItemTags.MOLDS_BLOCK, "Press Mold (Block)")
        add(RagiumItemTags.MOLDS_GEAR, "Press Mold (Gear)")
        add(RagiumItemTags.MOLDS_INGOT, "Press Mold (Ingot)")
        add(RagiumItemTags.MOLDS_PLATE, "Press Mold (Plate)")
        add(RagiumItemTags.MOLDS_ROD, "Press Mold (Rod)")
        add(RagiumItemTags.MOLDS_WIRE, "Press Mold (Wire)")

        add(RagiumItemTags.DIRT_SOILS, "Dirt Soil")
        add(RagiumItemTags.END_SOILS, "End Soil")
        add(RagiumItemTags.MUSHROOM_SOILS, "Mushroom Soil")
        add(RagiumItemTags.NETHER_SOILS, "Nether Soil")
    }

    private fun tooltips() {}

    private fun misc() {
        // Ore Variant
        addOreVariant(HTOreVariant.OVERWORLD, "%s Ore")
        addOreVariant(HTOreVariant.DEEPSLATE, "Deepslate %s Ore")
        addOreVariant(HTOreVariant.NETHER, "Nether %s Ore")
        addOreVariant(HTOreVariant.END, "End %s Ore")
    }

    private fun mekanism() {
        // add(RagiumMekAddon.RAGINITE_SLURRY.cleanSlurry.translationKey, "Clean Raginite Slurry")
        // add(RagiumMekAddon.RAGINITE_SLURRY.dirtySlurry.translationKey, "Dirty Raginite Slurry")
    }

    private fun jade() {
        add("config.jade.plugin_ragium.boiler", "Boiler")
        add("config.jade.plugin_ragium.enchantable_block", "Enchantable Block")
        add("config.jade.plugin_ragium.energy_network", "Show Energy Network")
        add("config.jade.plugin_ragium.error_message", "Show Error Message")
        add("config.jade.plugin_ragium.firebox", "Firebox")
        add("config.jade.plugin_ragium.heat_source", "Heat Source")
        add("config.jade.plugin_ragium.machine_info", "Show Machine Info")
        add("config.jade.plugin_ragium.rock_generator", "Rock Generator")
        add("config.jade.plugin_ragium.steam_furnace", "Steam Furnace")
    }

    private fun emi() {
        add(RagiumTranslationKeys.EMI_ASH_LOG, "Drop Ash Dust when harvested.")
        add(RagiumTranslationKeys.EMI_HARVESTABLE_GLASS, "This glass block can be harvested without Silk Touch.")
        add(RagiumTranslationKeys.EMI_OBSIDIAN_GLASS, "As the same blast resistance as Obsidian.")
        add(RagiumTranslationKeys.EMI_SOUL_GLASS, "Only passable with Players.")

        add(RagiumTranslationKeys.EMI_ITEM_MAGNET, "Collect dropped items around 5 m.")
        add(RagiumTranslationKeys.EMI_TRADER_CATALOG, "Also obtained by killing Wandering Trader.")

        add(RagiumTranslationKeys.EMI_AMBROSIA, "Always edible and not consumed!")
        add(RagiumTranslationKeys.EMI_ICE_CREAM, "Extinguish fire when eaten.")
        add(RagiumTranslationKeys.EMI_WARPED_WART, "Clear one bad effect randomly when eaten.")
    }
}
