package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.data.add
import hiiragi283.ragium.integration.RagiumMekIntegration
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumEnglishProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "en_us") {
    override fun addTranslations() {
        // Block
        add(RagiumBlocks.SOUL_MAGMA_BLOCK, "Soul Magma Block")

        add(RagiumBlocks.CHEMICAL_GLASS, "Chemical Glass")
        add(RagiumBlocks.SHAFT, "Shaft")

        add(RagiumBlocks.PLASTIC_BLOCK, "Plastic Block")

        add(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")
        add(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

        add(RagiumBlocks.MANUAL_GRINDER, "Manual Grinder")

        add(RagiumBlocks.CATALYST_ADDON, "Catalyst Addon")
        add(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")

        add(RagiumBlocks.Decorations.RAGI_ALLOY_BLOCK, "Block of Ragi-Alloy (Decoration)")
        add(RagiumBlocks.Decorations.RAGI_STEEL_BLOCK, "Block of Ragi-Steel (Decoration)")
        add(RagiumBlocks.Decorations.REFINED_RAGI_STEEL_BLOCK, "Block of Refined Ragi-Steel (Decoration)")

        add(RagiumBlocks.Decorations.BASIC_CASING, "Basic Casing (Decoration)")
        add(RagiumBlocks.Decorations.ADVANCED_CASING, "Advanced Casing (Decoration)")
        add(RagiumBlocks.Decorations.ELITE_CASING, "Elite Casing (Decoration)")
        add(RagiumBlocks.Decorations.ULTIMATE_CASING, "Ultimate Casing (Decoration)")

        add(RagiumBlocks.Decorations.BASIC_HULL, "Basic Hull (Decoration)")
        add(RagiumBlocks.Decorations.ADVANCED_HULL, "Advanced Hull (Decoration)")
        add(RagiumBlocks.Decorations.ELITE_HULL, "Elite Hull (Decoration)")
        add(RagiumBlocks.Decorations.ULTIMATE_HULL, "Ultimate Hull (Decoration)")

        add(RagiumBlocks.Decorations.BASIC_COIL, "Basic Coil (Decoration)")
        add(RagiumBlocks.Decorations.ADVANCED_COIL, "Advanced Coil (Decoration)")
        add(RagiumBlocks.Decorations.ELITE_COIL, "Elite Coil (Decoration)")
        add(RagiumBlocks.Decorations.ULTIMATE_COIL, "Ultimate Coil (Decoration)")

        add(RagiumBlocks.LEDBlocks.RED, "Red LED Block")
        add(RagiumBlocks.LEDBlocks.GREEN, "Green LED Block")
        add(RagiumBlocks.LEDBlocks.BLUE, "Blue LED Block")
        add(RagiumBlocks.LEDBlocks.CYAN, "Cyan LED Block")
        add(RagiumBlocks.LEDBlocks.MAGENTA, "Magenta LED Block")
        add(RagiumBlocks.LEDBlocks.YELLOW, "Yellow LED Block")
        add(RagiumBlocks.LEDBlocks.WHITE, "LED Block")
        // Content
        add(RagiumTranslationKeys.BURNER, "Burner")
        add(RagiumTranslationKeys.CASING, "Casing")
        add(RagiumTranslationKeys.CIRCUIT, "Circuit")
        add(RagiumTranslationKeys.COIL, "Coil")
        add(RagiumTranslationKeys.CRATE, "Crate")
        add(RagiumTranslationKeys.DRUM, "Drum")
        add(RagiumTranslationKeys.GRATE, "Grate")
        add(RagiumTranslationKeys.HULL, "hull")
        add(RagiumTranslationKeys.PLASTIC, "Plastic")

        add(HTOreVariant.OVERWORLD, "%s Ore")
        add(HTOreVariant.DEEP, "Deepslate %s Ore")
        add(HTOreVariant.NETHER, "Nether %s Ore")
        add(HTOreVariant.END, "End %s Ore")
        // Fluids
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            add(fluid.typeHolder.get().descriptionId, fluid.enName)
        }
        // Items
        add(RagiumItems.SWEET_BERRIES_CAKE_PIECE, "A piece of Sweet Berries Cake")
        add(RagiumItems.MELON_PIE, "Melon Pie")

        add(RagiumItems.BUTTER, "Butter")
        add(RagiumItems.CARAMEL, "Caramel")
        add(RagiumItems.DOUGH, "Dough")
        add(RagiumItems.FLOUR, "Flour")

        add(RagiumItems.CHOCOLATE, "Chocolate")
        add(RagiumItems.CHOCOLATE_APPLE, "Chocolate Apple")
        add(RagiumItems.CHOCOLATE_BREAD, "Chocolate Bread")
        add(RagiumItems.CHOCOLATE_COOKIE, "Chocolate Cookie")

        add(RagiumItems.CINNAMON_STICK, "Cinnamon Stick")
        add(RagiumItems.CINNAMON_POWDER, "Cinnamon Powder")
        add(RagiumItems.CINNAMON_ROLL, "Cinnamon Roll")

        add(RagiumItems.MINCED_MEAT, "Minced Meat")
        add(RagiumItems.MEAT_INGOT, "Meat Ingot")
        add(RagiumItems.COOKED_MEAT_INGOT, "Cooked Meat Ingot")
        add(RagiumItems.CANNED_COOKED_MEAT, "Canned Cooked Meat")

        add(RagiumItems.AMBROSIA, "Ambrosia")

        add(RagiumItems.FORGE_HAMMER, "Forge Hammer")
        add(RagiumItems.SILKY_PICKAXE, "Silky Pickaxe")

        add(RagiumItems.DYNAMITE, "Dynamite")
        add(RagiumItems.SLOT_LOCK, "Slot Lock")

        add(RagiumItems.GEAR_PRESS_MOLD, "Press Mold (Gear)")
        add(RagiumItems.PLATE_PRESS_MOLD, "Press Mold (Plate)")
        add(RagiumItems.ROD_PRESS_MOLD, "Press Mold (Rod)")

        // add(RagiumItems.HEATING_CATALYST, "Heating Catalyst")
        // add(RagiumItems.COOLING_CATALYST, "Cooling Catalyst")
        add(RagiumItems.OXIDIZATION_CATALYST, "Oxidization Catalyst")
        add(RagiumItems.REDUCTION_CATALYST, "Reduction Catalyst")
        add(RagiumItems.DEHYDRATION_CATALYST, "Dehydration Catalyst")

        add(RagiumItems.BEE_WAX, "Bee Wax")
        add(RagiumItems.CALCIUM_CARBIDE, "Calcium Carbide")
        add(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        add(RagiumItems.COAL_CHIP, "Coal Chip")
        add(RagiumItems.CRIMSON_CRYSTAL, "Crimson Crystal")
        add(RagiumItems.DEEPANT, "Deepant")
        add(RagiumItems.ENGINE, "V8 Engine")
        // add(RagiumItems.GLASS_SHARD, "Glass Shard")
        add(RagiumItems.LED, "L.E.D.")
        add(RagiumItems.LUMINESCENCE_DUST, "Luminescence Dust")
        add(RagiumItems.OBSIDIAN_TEAR, "Obsidian Tear")
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        add(RagiumItems.RESIDUAL_COKE, "Residual Coke")
        add(RagiumItems.SILKY_CRYSTAL, "Silky Crystal")
        add(RagiumItems.SLAG, "Slag")
        add(RagiumItems.SOAP, "Soap")
        add(RagiumItems.SOLAR_PANEL, "Solar Panel")
        // add(RagiumItems.STELLA_PLATE, "S.T.E.L.L.A. Plate")
        add(RagiumItems.TALLOW, "Tallow")
        add(RagiumItems.WARPED_CRYSTAL, "Warped Crystal")

        add(RagiumItems.Radioactives.URANIUM_FUEL, "Uranium Fuel")
        add(RagiumItems.Radioactives.PLUTONIUM_FUEL, "Plutonium Fuel")
        add(RagiumItems.Radioactives.YELLOW_CAKE, "Yellow Cake")
        add(RagiumItems.Radioactives.YELLOW_CAKE_PIECE, "A piece of Yellow Cake")
        add(RagiumItems.Radioactives.NUCLEAR_WASTE, "Nuclear Waste")

        add(RagiumItems.RAGI_TICKET, "Ragi-Ticket")
        // Machine
        add(HTMachineTier.BASIC, "Basic", "Basic %s")
        add(HTMachineTier.ADVANCED, "Advanced", "Advanced %s")
        add(HTMachineTier.ELITE, "Elite", "Elite %s")
        add(HTMachineTier.ULTIMATE, "Ultimate", "Ultimate %s")

        add(RagiumTranslationKeys.MACHINE_COST, "- Process Cost: %s FE/times")
        add(RagiumTranslationKeys.MACHINE_NAME, "- Machine Name: %s")
        add(RagiumTranslationKeys.MACHINE_TIER, "- Tier: %s")

        add(RagiumTranslationKeys.MACHINE_PREVIEW, "- Show Preview: %s")
        add(RagiumTranslationKeys.MACHINE_WORKING, "- Working: %s")

        add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %ss")
        add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The machine structure is valid!")

        add(RagiumTranslationKeys.COOLING_CONDITION, "Required Cooling Power: %s - %s")
        add(RagiumTranslationKeys.HEATING_CONDITION, "Required Heating Power: %s - %s")
        add(RagiumTranslationKeys.CATALYST_CONDITION, "Required Catalyst Item")
        // Machine Type
        add(
            RagiumMachineKeys.BEDROCK_MINER,
            "Bedrock Miner",
            "Collect minerals from Bedrock",
        )
        add(
            RagiumMachineKeys.BIOMASS_FERMENTER,
            "Biomass Fermenter",
            "Produce Biomass from Composter inputs",
        )
        add(
            RagiumMachineKeys.DRAIN,
            "Drain",
            "Drains fluids from front, experience from up, and fluid cube in slot",
        )
        add(
            RagiumMachineKeys.FLUID_DRILL,
            "Fluid Drill",
            "Pump up fluids from specified biomes",
        )
        add(
            RagiumMachineKeys.GAS_PLANT,
            "Gas Plant",
            "Collect gases from specified biomes",
        )
        add(
            RagiumMachineKeys.ROCK_GENERATOR,
            "Rock Generator",
            "Require water and lava source around",
        )

        add(
            RagiumMachineKeys.COMBUSTION_GENERATOR,
            "Combustion Generator",
            "Generate energy from liquid fuels",
        )
        add(
            RagiumMachineKeys.GAS_TURBINE,
            "Gas Turbine",
            "Generate energy from gaseous fuels",
        )
        add(
            RagiumMachineKeys.NUCLEAR_REACTOR,
            "Nuclear Reactor",
            "Generate energy from radioactive fuels",
        )
        add(
            RagiumMachineKeys.SOLAR_GENERATOR,
            "Solar Generator",
            "Generate energy in daytime",
        )
        add(
            RagiumMachineKeys.STEAM_GENERATOR,
            "Steam Generator",
            "Generate energy from water and coal like fuels",
        )
        add(
            RagiumMachineKeys.THERMAL_GENERATOR,
            "Thermal Generator",
            "Generate energy from hot fluids",
        )
        add(
            RagiumMachineKeys.VIBRATION_GENERATOR,
            "Vibration Generator",
            "Augh! Pervert! Death penalty!",
        )

        add(RagiumMachineKeys.ASSEMBLER, "Assembler", "I am Dr.Doom")
        add(RagiumMachineKeys.BLAST_FURNACE, "Large Blast Furnace", "Smelt multiple ingredients into one")
        add(RagiumMachineKeys.CHEMICAL_REACTOR, "Chemical Reactor", "Are You Ready?")
        // add(RagiumMachineKeys.CONDENSER, "Condenser")
        add(RagiumMachineKeys.COMPRESSOR, "Compressor", "saves.zip.zip")
        add(RagiumMachineKeys.CUTTING_MACHINE, "Cutting Machine", "Process Logs more efficiently")
        add(RagiumMachineKeys.DISTILLATION_TOWER, "Distillation Tower", "Process Crude Oil")
        // add(RagiumMachineKeys.ELECTROLYZER, "Electrolyzer", "Elek On")
        add(RagiumMachineKeys.EXTRACTOR, "Extractor", "Something like Centrifuge")
        add(RagiumMachineKeys.GRINDER, "Grinder", "Crush Up")
        add(RagiumMachineKeys.GROWTH_CHAMBER, "Growth Chamber")
        // add(RagiumMachineKeys.INFUSER, "Infuser", "Something not like Centrifuge")
        add(RagiumMachineKeys.LASER_TRANSFORMER, "Laser Transformer")
        // add(RagiumMachineKeys.LARGE_CHEMICAL_REACTOR, "large Chemical Reactor", "larger than you think")
        add(RagiumMachineKeys.MIXER, "Mixer", "Genomix...")
        add(RagiumMachineKeys.MULTI_SMELTER, "Multi Smelter", "Smelt multiple items at once")
        // Material
        add(RagiumMaterialKeys.ALKALI, "Alkali")
        add(RagiumMaterialKeys.ALUMINUM, "Aluminum")
        add(RagiumMaterialKeys.AMETHYST, "Amethyst")
        add(RagiumMaterialKeys.ASH, "Ash")
        add(RagiumMaterialKeys.BAUXITE, "Bauxite")
        add(RagiumMaterialKeys.BRASS, "Brass")
        add(RagiumMaterialKeys.BRONZE, "Bronze")
        add(RagiumMaterialKeys.CARBON, "Carbon")
        add(RagiumMaterialKeys.CINNABAR, "Cinnabar")
        add(RagiumMaterialKeys.COAL, "Coal")
        add(RagiumMaterialKeys.COPPER, "Copper")
        add(RagiumMaterialKeys.CRUDE_RAGINITE, "Crude Raginite")
        add(RagiumMaterialKeys.CRYOLITE, "Cryolite")
        add(RagiumMaterialKeys.DEEP_STEEL, "Deep Steel")
        add(RagiumMaterialKeys.DIAMOND, "Diamond")
        add(RagiumMaterialKeys.DRAGONIUM, "Dragonium")
        add(RagiumMaterialKeys.ECHORIUM, "Echorium")
        add(RagiumMaterialKeys.ELECTRUM, "Electrum")
        add(RagiumMaterialKeys.EMERALD, "Emerald")
        add(RagiumMaterialKeys.FIERIUM, "Fierium")
        add(RagiumMaterialKeys.FLUORITE, "Fluorite")
        add(RagiumMaterialKeys.GALENA, "Galena")
        add(RagiumMaterialKeys.GOLD, "Gold")
        add(RagiumMaterialKeys.INVAR, "Invar")
        add(RagiumMaterialKeys.IRIDIUM, "Iridium")
        add(RagiumMaterialKeys.IRON, "Iron")
        add(RagiumMaterialKeys.LAPIS, "Lapis")
        add(RagiumMaterialKeys.LEAD, "Lead")
        add(RagiumMaterialKeys.NETHERITE, "Netherite")
        add(RagiumMaterialKeys.NETHERITE_SCRAP, "Netherite Scrap")
        add(RagiumMaterialKeys.NICKEL, "Nickel")
        add(RagiumMaterialKeys.NITER, "Niter")
        add(RagiumMaterialKeys.PERIDOT, "Peridot")
        add(RagiumMaterialKeys.PLATINUM, "Platinum")
        add(RagiumMaterialKeys.PLUTONIUM, "Plutonium")
        add(RagiumMaterialKeys.PYRITE, "Pyrite")
        add(RagiumMaterialKeys.QUARTZ, "Quartz")
        add(RagiumMaterialKeys.RAGI_ALLOY, "Ragi-Alloy")
        add(RagiumMaterialKeys.RAGI_CRYSTAL, "Ragi-Crystal")
        add(RagiumMaterialKeys.RAGI_STEEL, "Ragi-Steel")
        add(RagiumMaterialKeys.RAGINITE, "Raginite")
        add(RagiumMaterialKeys.RAGIUM, "Ragium")
        add(RagiumMaterialKeys.REDSTONE, "Redstone")
        add(RagiumMaterialKeys.REFINED_RAGI_STEEL, "Refined Ragi-Steel")
        add(RagiumMaterialKeys.RUBY, "Ruby")
        add(RagiumMaterialKeys.SALT, "Salt")
        add(RagiumMaterialKeys.SAPPHIRE, "Sapphire")
        add(RagiumMaterialKeys.SILVER, "Silver")
        add(RagiumMaterialKeys.SPHALERITE, "Sphalerite")
        add(RagiumMaterialKeys.STEEL, "Steel")
        add(RagiumMaterialKeys.SULFUR, "Sulfur")
        add(RagiumMaterialKeys.TIN, "Tin")
        add(RagiumMaterialKeys.TITANIUM, "Titanium")
        add(RagiumMaterialKeys.TUNGSTEN, "Tungsten")
        add(RagiumMaterialKeys.URANIUM, "Uranium")
        add(RagiumMaterialKeys.WOOD, "Wood")
        add(RagiumMaterialKeys.ZINC, "Zinc")

        add(RagiumMekIntegration.OSMIUM, "Osmium")
        add(RagiumMekIntegration.REFINED_GLOWSTONE, "Refined Glowstone")
        add(RagiumMekIntegration.REFINED_OBSIDIAN, "Refined Obsidian")
        // Tag Prefix
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
        add(HTTagPrefix.STORAGE_BLOCK, "Block of %s")
        add(HTTagPrefix.WIRE, "%s Wire")
        // Misc
        add(RagiumTranslationKeys.FLUID_AMOUNT, "Amount: %s mb")
        add(RagiumTranslationKeys.FLUID_CAPACITY, "Capacity: %s mb")

        add("config.jade.plugin_ragium.energy_network", "Energy Network")
        add("config.jade.plugin_ragium.machine_info", "Machine Info")
    }
}
