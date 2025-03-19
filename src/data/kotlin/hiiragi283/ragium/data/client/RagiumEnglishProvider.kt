package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.extension.addEnchantment
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.*
import net.minecraft.data.PackOutput
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumEnglishProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "en_us") {
    override fun addTranslations() {
        block()
        enchantment()
        entity()
        // fluid()
        item()
        material()
        tagPrefix()
        tag()
        tooltips()
        misc()

        mekanism()
        jade()
    }

    private fun block() {
        addBlock(RagiumBlocks.SILT, "Silt")

        RagiumBlocks.RAGI_BRICK_SETS.addTranslationEn("Ragi-Brick", this)
        RagiumBlocks.AZURE_TILE_SETS.addTranslationEn("Azure Tile", this)
        RagiumBlocks.PLASTIC_SETS.addTranslationEn("Plastic Block", this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addTranslationEn("Blue Nether Bricks", this)

        addBlock(RagiumBlocks.OBSIDIAN_GLASS, "Obsidian Glass")
        addBlock(RagiumBlocks.QUARTZ_GLASS, "Quartz Glass")
        addBlock(RagiumBlocks.SOUL_GLASS, "Soul Glass")

        addBlock(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")
        addBlock(RagiumBlocks.SPONGE_CAKE_SLAB, "Sponge Cake Slab")
        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

        addBlock(RagiumBlocks.DEVICE_CASING, "Device Casing")
        addBlock(RagiumBlocks.MACHINE_CASING, "Machine Casing")
        addBlock(RagiumBlocks.STONE_CASING, "Stone Casing")
        addBlock(RagiumBlocks.WOODEN_CASING, "Wooden Casing")

        // addBlock(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")
        // addBlock(RagiumBlocks.TELEPORT_ANCHOR, "Teleport Anchor")

        addBlock(RagiumBlocks.getLedBlock(DyeColor.RED), "Red LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.GREEN), "Green LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.BLUE), "Blue LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.CYAN), "Cyan LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.MAGENTA), "Magenta LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.YELLOW), "Yellow LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.WHITE), "LED Block")
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

    /*private fun fluid() {
        addFluid(RagiumFluids.GLASS, "Molten Glass")
        addFluid(RagiumFluids.HONEY, "Honey")
        addFluid(RagiumFluids.SNOW, "Powder Snow")

        addFluid(RagiumVirtualFluids.CHOCOLATE, "Chocolate")
        addFluid(RagiumVirtualFluids.MUSHROOM_STEW, "Mushroom Stew")

        addFluid(RagiumVirtualFluids.AIR, "Air")
        addFluid(RagiumVirtualFluids.STEAM, "Steam")

        addFluid(RagiumVirtualFluids.HYDROGEN, "Hydrogen")

        addFluid(RagiumVirtualFluids.NITROGEN, "Nitrogen")
        addFluid(RagiumVirtualFluids.AMMONIA, "Ammonia")
        addFluid(RagiumVirtualFluids.NITRIC_ACID, "Nitric Acid")
        addFluid(RagiumVirtualFluids.MIXTURE_ACID, "Mixture Acid")

        addFluid(RagiumVirtualFluids.OXYGEN, "Oxygen")
        addFluid(RagiumVirtualFluids.ROCKET_FUEL, "Rocket Fuel")

        addFluid(RagiumVirtualFluids.HYDROFLUORIC_ACID, "Hydrofluoric Acid")

        addFluid(RagiumVirtualFluids.ALKALI_SOLUTION, "Alkali Solution")

        addFluid(RagiumVirtualFluids.ALUMINA_SOLUTION, "Alumina Solution")

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
        addFluid(RagiumVirtualFluids.LATEX, "Latex")

        addFluid(RagiumVirtualFluids.LIQUID_GLOW, "Liquid Glow")
        addFluid(RagiumVirtualFluids.RAGIUM_SOLUTION, "Ragium Solution")
    }*/

    private fun item() {
        addItem(RagiumItems.SWEET_BERRIES_CAKE_PIECE, "A piece of Sweet Berries Cake")
        addItem(RagiumItems.MELON_PIE, "Melon Pie")

        addItem(RagiumItems.BUTTER, "Butter")
        addItem(RagiumItems.DOUGH, "Dough")
        addItem(RagiumItems.FLOUR, "Flour")

        addItem(RagiumItems.CHOCOLATE, "Chocolate")
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

        /*addItem(RagiumItems.DIVING_GOGGLE, "Diving Goggles")
        addItem(RagiumItems.JETPACK, "Jetpack")
        addItem(RagiumItems.CLIMBING_LEGGINGS, "Climbing Leggings")
        addItem(RagiumItems.SLIME_BOOTS, "Slime Boots")

        RagiumItems.AZURE_STEEL_ARMORS.addTranslationEn("Azure Steel", this)
        RagiumItems.DURALUMIN_ARMORS.addTranslationEn("Duralumin", this)

        addItem(RagiumItems.FEVER_PICKAXE, "Fever Pickaxe")
        addItem(RagiumItems.SILKY_PICKAXE, "Silky Pickaxe")

        RagiumItems.RAGI_ALLOY_TOOLS.addTranslationEn("Ragi-Alloy", this)
        RagiumItems.AZURE_STEEL_TOOLS.addTranslationEn("Azure Steel", this)
        RagiumItems.DURALUMIN_TOOLS.addTranslationEn("Duralumin", this)

        addItem(RagiumItems.POTION_BUNDLE, "Potion Bundle")
        addItem(RagiumItems.DURALUMIN_CASE, "Duralumin Case")
        addItem(RagiumItems.RAGI_LANTERN, "Ragi-Lantern")

        addItem(RagiumItems.ITEM_MAGNET, "Item Magnet")
        addItem(RagiumItems.EXP_MAGNET, "Exp Magnet")

        addItem(RagiumItems.DYNAMITE, "Dynamite")
        addItem(RagiumItems.DEFOLIANT_DYNAMITE, "Defoliant Dynamite")
        addItem(RagiumItems.FLATTEN_DYNAMITE, "Flatten Dynamite")
        addItem(RagiumItems.NAPALM_DYNAMITE, "Napalm Dynamite")
        addItem(RagiumItems.POISON_DYNAMITE, "Poison Dynamite")*/

        addItem(RagiumItems.AZURE_STEEL_COMPOUND, "Azure Steel Compound")
        // addItem(RagiumItems.BEE_WAX, "Bee Wax")
        // addItem(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        // addItem(RagiumItems.CRUDE_OIL_BUCKET, "Crude Oil Bucket")
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
        add(CommonMaterials.ALUMINA, "Alumina")
        add(CommonMaterials.ALUMINUM, "Aluminum")
        add(CommonMaterials.ANTIMONY, "Antimony")
        add(CommonMaterials.BERYLLIUM, "Beryllium")
        add(CommonMaterials.ASH, "Ash")
        add(CommonMaterials.BAUXITE, "Bauxite")
        add(CommonMaterials.BRASS, "Brass")
        add(CommonMaterials.BRONZE, "Bronze")
        add(CommonMaterials.CADMIUM, "Cadmium")
        add(CommonMaterials.CARBON, "Carbon")
        add(CommonMaterials.CHROMIUM, "Chromium")
        add(CommonMaterials.COAL_COKE, "Coal Coke")
        add(CommonMaterials.CONSTANTAN, "Constantan")
        add(CommonMaterials.CRYOLITE, "Cryolite")
        add(CommonMaterials.ELECTRUM, "Electrum")
        add(CommonMaterials.FLUORITE, "Fluorite")
        add(CommonMaterials.INVAR, "Invar")
        add(CommonMaterials.IRIDIUM, "Iridium")
        add(CommonMaterials.LEAD, "Lead")
        add(CommonMaterials.NICKEL, "Nickel")
        add(CommonMaterials.NIOBIUM, "Niobium")
        add(CommonMaterials.OSMIUM, "Osmium")
        add(CommonMaterials.PERIDOT, "Peridot")
        add(CommonMaterials.PLATINUM, "Platinum")
        add(CommonMaterials.PLUTONIUM, "Plutonium")
        add(CommonMaterials.PYRITE, "Gold")
        add(CommonMaterials.RUBY, "Ruby")
        add(CommonMaterials.SALT, "Salt")
        add(CommonMaterials.SALTPETER, "Saltpeter")
        add(CommonMaterials.SAPPHIRE, "Sapphire")
        add(CommonMaterials.SILICON, "Silicon")
        add(CommonMaterials.SILVER, "Silver")
        add(CommonMaterials.SOLDERING_ALLOY, "Soldering Alloy")
        add(CommonMaterials.STAINLESS_STEEL, "Stainless Steel")
        add(CommonMaterials.STEEL, "Steel")
        add(CommonMaterials.SULFUR, "Sulfur")
        add(CommonMaterials.SUPERCONDUCTOR, "Superconductor")
        add(CommonMaterials.TIN, "Tin")
        add(CommonMaterials.TITANIUM, "Titanium")
        add(CommonMaterials.TUNGSTEN, "Tungsten")
        add(CommonMaterials.URANIUM, "Uranium")
        add(CommonMaterials.ZINC, "Zinc")
        // AA
        add(IntegrationMaterials.BLACK_QUARTZ, "Black Quartz")
        // AE2
        add(IntegrationMaterials.CERTUS_QUARTZ, "Certus Quartz")
        add(IntegrationMaterials.FLUIX, "Fluix")
        // Create
        add(IntegrationMaterials.ANDESITE_ALLOY, "Andesite Alloy")
        add(IntegrationMaterials.CARDBOARD, "Cardboard")
        add(IntegrationMaterials.ROSE_QUARTZ, "Rose Quartz")
        // EIO
        add(IntegrationMaterials.COPPER_ALLOY, "Copper Alloy")
        add(IntegrationMaterials.ENERGETIC_ALLOY, "Energetic Alloy")
        add(IntegrationMaterials.VIBRANT_ALLOY, "Vibrant Alloy")
        add(IntegrationMaterials.REDSTONE_ALLOY, "Redstone Alloy")
        add(IntegrationMaterials.CONDUCTIVE_ALLOY, "Conductive Alloy")
        add(IntegrationMaterials.PULSATING_ALLOY, "Pulsating Alloy")
        add(IntegrationMaterials.DARK_STEEL, "Dark Steel")
        add(IntegrationMaterials.SOULARIUM, "Soularium")
        add(IntegrationMaterials.END_STEEL, "End Steel")
        // EvilCraft
        add(IntegrationMaterials.DARK_GEM, "Dark Gem")
        // IE
        add(IntegrationMaterials.HOP_GRAPHITE, "HOP Graphite")
        // Mek
        add(IntegrationMaterials.REFINED_GLOWSTONE, "Refined Glowstone")
        add(IntegrationMaterials.REFINED_OBSIDIAN, "Refined Obsidian")
        // Twilight
        add(IntegrationMaterials.CARMINITE, "Carminite")
        add(IntegrationMaterials.FIERY_METAL, "Fiery")
        add(IntegrationMaterials.IRONWOOD, "Ironwood")
        add(IntegrationMaterials.KNIGHTMETAL, "Knightmetal")
        add(IntegrationMaterials.STEELEAF, "Steeleaf")
        // Ragium
        add(RagiumMaterials.ADVANCED_RAGI_ALLOY, "Advanced Ragi-Alloy")
        add(RagiumMaterials.AZURE_STEEL, "Azure Steel")
        add(RagiumMaterials.CRIMSON_CRYSTAL, "Crimson Crystal")
        add(RagiumMaterials.DEEP_STEEL, "Deep Steel")
        add(RagiumMaterials.RAGI_ALLOY, "Ragi-Alloy")
        add(RagiumMaterials.RAGI_CRYSTAL, "Ragi-Crystal")
        add(RagiumMaterials.RAGINITE, "Raginite")
        add(RagiumMaterials.RAGIUM, "Ragium")
        add(RagiumMaterials.WARPED_CRYSTAL, "Warped Crystal")
        // Vanilla
        add(VanillaMaterials.AMETHYST, "Amethyst")
        add(VanillaMaterials.CALCITE, "Calcite")
        add(VanillaMaterials.COAL, "Coal")
        add(VanillaMaterials.COPPER, "Copper")
        add(VanillaMaterials.DIAMOND, "Diamond")
        add(VanillaMaterials.EMERALD, "Emerald")
        add(VanillaMaterials.GLOWSTONE, "Glowstone")
        add(VanillaMaterials.GOLD, "Gold")
        add(VanillaMaterials.IRON, "Iron")
        add(VanillaMaterials.LAPIS, "Lapis")
        add(VanillaMaterials.NETHERITE, "Netherite")
        add(VanillaMaterials.NETHERITE_SCRAP, "Netherite Scrap")
        add(VanillaMaterials.OBSIDIAN, "Obsidian")
        add(VanillaMaterials.QUARTZ, "Quartz")
        add(VanillaMaterials.REDSTONE, "Redstone")
        add(VanillaMaterials.WOOD, "Wood")
    }

    private fun tagPrefix() {
        add(HTTagPrefix.CLUMP, "%s Clump")
        add(HTTagPrefix.CRYSTAL, "%s Crystal")
        add(HTTagPrefix.DIRTY_DUST, "Dirty %s Dust")
        add(HTTagPrefix.DUST, "%s Dust")
        add(HTTagPrefix.GEAR, "%s Gear")
        add(HTTagPrefix.GEM, "%s")
        add(HTTagPrefix.INGOT, "%s Ingot")
        add(HTTagPrefix.NUGGET, "%s Nugget")
        add(HTTagPrefix.ORE, "%s Ore")
        add(HTTagPrefix.PLATE, "%s Plate")
        add(HTTagPrefix.RAW_MATERIAL, "Raw %s")
        add(HTTagPrefix.RAW_STORAGE, "Block of Raw %s")
        add(HTTagPrefix.ROD, "%s Rod")
        add(HTTagPrefix.SHARD, "%s Shard")
        add(HTTagPrefix.SHEETMETAL, "%s Sheetmetal")
        add(HTTagPrefix.STORAGE_BLOCK, "Block of %s")
        add(HTTagPrefix.TINY_DUST, "Tiny %s Dust")
        add(HTTagPrefix.WIRE, "%s Wire")
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

        add(RagiumItemTags.CROPS_WARPED_WART, "Warped Wart")
        add(RagiumItemTags.FLOURS, "Flours")
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
        add(HTOreVariant.OVERWORLD, "%s Ore")
        add(HTOreVariant.DEEPSLATE, "Deepslate %s Ore")
        add(HTOreVariant.NETHER, "Nether %s Ore")
        add(HTOreVariant.END, "End %s Ore")
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
}
