package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.*
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumEnglishProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "en_us") {
    override fun addTranslations() {
        // Block
        add(RagiumBlocks.SOUL_MAGMA_BLOCK, "Soul Magma Block")

        add(RagiumBlocks.SLAG_BLOCK, "Block of Slag")

        add(RagiumBlocks.CHEMICAL_GLASS, "Chemical Glass")
        add(RagiumBlocks.SHAFT, "Shaft")

        add(RagiumBlocks.PLASTIC_BLOCK, "Plastic Block")

        add(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")
        add(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

        add(RagiumBlocks.MANUAL_GRINDER, "Manual Grinder")
        add(RagiumBlocks.PRIMITIVE_BLAST_FURNACE, "Primitive Blast Furnace")

        add(RagiumBlocks.CATALYST_ADDON, "Catalyst Holder")
        add(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")
        add(RagiumBlocks.SLAG_COLLECTOR, "Slag Collector")

        add(RagiumBlocks.Decorations.RAGI_ALLOY_BLOCK, "Block of Ragi-Alloy (Decoration)")

        add(RagiumBlocks.Decorations.BASIC_CASING, "Basic Casing (Decoration)")
        add(RagiumBlocks.Decorations.ADVANCED_CASING, "Advanced Casing (Decoration)")
        add(RagiumBlocks.Decorations.ELITE_CASING, "Elite Casing (Decoration)")
        add(RagiumBlocks.Decorations.ULTIMATE_CASING, "Ultimate Casing (Decoration)")

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
        add(RagiumTranslationKeys.CASING_WALL, "Casing Wall")
        add(RagiumTranslationKeys.CIRCUIT, "Circuit")
        add(RagiumTranslationKeys.COIL, "Coil")
        add(RagiumTranslationKeys.CRATE, "Crate")
        add(RagiumTranslationKeys.DRUM, "Drum")
        add(RagiumTranslationKeys.GRATE, "Grate")
        add(RagiumTranslationKeys.HULL, "hull")
        add(RagiumTranslationKeys.PLASTIC, "Plastic")

        add(HTOreVariant.OVERWORLD, "%s Ore")
        add(HTOreVariant.DEEPSLATE, "Deepslate %s Ore")
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
        add(RagiumItems.WIRE_PRESS_MOLD, "Press Mold (Wire)")

        add(RagiumItems.ALKALI_REAGENT, "Alkali Reagent")
        add(RagiumItems.BLAZE_REAGENT, "Blaze Reagent")
        add(RagiumItems.CREEPER_REAGENT, "Creeper Reagent")
        add(RagiumItems.DEEPANT_REAGENT, "Deepant Reagent")
        add(RagiumItems.ENDER_REAGENT, "Ender Reagent")
        add(RagiumItems.NETHER_REAGENT, "Nether Reagent")
        add(RagiumItems.PRISMARINE_REAGENT, "Prismarine Reagent")
        add(RagiumItems.RAGIUM_REAGENT, "Ragium Reagent")
        add(RagiumItems.SCULK_REAGENT, "Sculk Reagent")
        add(RagiumItems.SOUL_REAGENT, "Soul Reagent")
        add(RagiumItems.WITHER_REAGENT, "Wither Reagent")

        add(RagiumItems.BEE_WAX, "Bee Wax")
        add(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        add(RagiumItems.CRIMSON_CRYSTAL, "Crimson Crystal")
        add(RagiumItems.ENGINE, "V8 Engine")
        add(RagiumItems.LED, "L.E.D.")
        add(RagiumItems.LUMINESCENCE_DUST, "Luminescence Dust")
        add(RagiumItems.OBSIDIAN_TEAR, "Obsidian Tear")
        add(RagiumItems.PLASTIC_PLATE, "Plastic Plate")
        add(RagiumItems.POLYMER_RESIN, "Polymer Resin")
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        add(RagiumItems.SILKY_CRYSTAL, "Silky Crystal")
        add(RagiumItems.SOAP, "Soap")
        add(RagiumItems.SOLAR_PANEL, "Solar Panel")
        add(RagiumItems.WARPED_CRYSTAL, "Warped Crystal")

        add(RagiumItems.NUCLEAR_WASTE, "Nuclear Waste")
        add(RagiumItems.PLUTONIUM_FUEL, "Plutonium Fuel")
        add(RagiumItems.URANIUM_FUEL, "Uranium Fuel")
        add(RagiumItems.YELLOW_CAKE, "Yellow Cake")
        add(RagiumItems.YELLOW_CAKE_PIECE, "A piece of Yellow Cake")

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
        add(RagiumTranslationKeys.MACHINE_TICK_RATE, "- Tick Rate: %s ticks (%s sec)")
        add(RagiumTranslationKeys.MACHINE_WORKING, "- Working: %s")

        add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %ss")
        add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The machine structure is valid!")

        add(RagiumTranslationKeys.BIOME_CONDITION, "Required Biomes: %s")
        add(RagiumTranslationKeys.CATALYST_CONDITION, "Required Catalyst Item")
        add(RagiumTranslationKeys.ENCHANTMENT_CONDITION, "Required Enchantments: %s")
        add(
            RagiumTranslationKeys.ROCK_GENERATOR_CONDITION,
            "Require Water and Lava Source around the machine, and Catalyst Item",
        )
        add(RagiumTranslationKeys.SOURCE_CONDITION, "Required Source %s from %s side")
        // Machine Type
        add(
            RagiumMachineKeys.BEDROCK_MINER,
            "Bedrock Miner",
            "Collect minerals from Bedrock",
        )
        add(
            RagiumMachineKeys.DRAIN,
            "Drain",
            "Drains fluids from front, experience from up, and fluid cube in slot",
        )

        add(
            RagiumMachineKeys.COMBUSTION_GENERATOR,
            "Combustion Generator",
            "Generate energy from liquid fuels",
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
            RagiumMachineKeys.STEAM_TURBINE,
            "Steam Turbine",
            "Generate energy from steam",
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

        add(RagiumMachineKeys.ASSEMBLER, "Assembler", "You are the genius!")
        add(RagiumMachineKeys.BLAST_FURNACE, "Large Blast Furnace", "Smelt multiple ingredients into one")
        add(RagiumMachineKeys.COMPRESSOR, "Compressor", "saves.zip.zip")
        add(RagiumMachineKeys.EXTRACTOR, "Extractor", "Something like Centrifuge")
        add(RagiumMachineKeys.GRINDER, "Grinder", "Unbreakable Diamond")
        add(RagiumMachineKeys.GROWTH_CHAMBER, "Growth Chamber", "Growth Gran-ma")
        add(RagiumMachineKeys.INFUSER, "Infuser", "Something not like Centrifuge")
        add(RagiumMachineKeys.LASER_ASSEMBLY, "Laser Assembly", "Laser On...")
        add(RagiumMachineKeys.MIXER, "Mixer", "Best Match!")
        add(RagiumMachineKeys.MULTI_SMELTER, "Multi Smelter", "Smelt multiple items at once")
        add(RagiumMachineKeys.REFINERY, "Refinery", "Project Build")
        // Material
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
        add(CommonMaterials.CRYOLITE, "Cryolite")
        add(CommonMaterials.ELECTRUM, "Electrum")
        add(CommonMaterials.FLUORITE, "Fluorite")
        add(CommonMaterials.INVAR, "Invar")
        add(CommonMaterials.IRIDIUM, "Iridium")
        add(CommonMaterials.LEAD, "Lead")
        add(CommonMaterials.NICKEL, "Nickel")
        add(CommonMaterials.OSMIUM, "Osmium")
        add(CommonMaterials.PERIDOT, "Peridot")
        add(CommonMaterials.PLATINUM, "Platinum")
        add(CommonMaterials.PLUTONIUM, "Plutonium")
        add(CommonMaterials.RUBY, "Ruby")
        add(CommonMaterials.SALT, "Salt")
        add(CommonMaterials.SALTPETER, "Saltpeter")
        add(CommonMaterials.SAPPHIRE, "Sapphire")
        add(CommonMaterials.SILICON, "Silicon")
        add(CommonMaterials.SILVER, "Silver")
        add(CommonMaterials.STAINLESS_STEEL, "Stainless Steel")
        add(CommonMaterials.STEEL, "Steel")
        add(CommonMaterials.SULFUR, "Sulfur")
        add(CommonMaterials.TIN, "Tin")
        add(CommonMaterials.TITANIUM, "Titanium")
        add(CommonMaterials.TUNGSTEN, "Tungsten")
        add(CommonMaterials.URANIUM, "Uranium")
        add(CommonMaterials.WOOD, "Wood")
        add(CommonMaterials.ZINC, "Zinc")
        add(IntegrationMaterials.BLACK_QUARTZ, "Black Quartz")
        add(IntegrationMaterials.DARK_GEM, "Dark Gem")
        add(IntegrationMaterials.REFINED_GLOWSTONE, "Refined Glowstone")
        add(IntegrationMaterials.REFINED_OBSIDIAN, "Refined Obsidian")
        add(RagiumMaterials.DEEP_STEEL, "Deep Steel")
        add(RagiumMaterials.ECHORIUM, "Echorium")
        add(RagiumMaterials.FIERY_COAL, "Fiery Coal")
        add(RagiumMaterials.RAGI_ALLOY, "Ragi-Alloy")
        add(RagiumMaterials.RAGI_CRYSTAL, "Ragi-Crystal")
        add(RagiumMaterials.RAGI_STEEL, "Ragi-Steel")
        add(RagiumMaterials.RAGINITE, "Raginite")
        add(RagiumMaterials.RAGIUM, "Ragium")
        add(VanillaMaterials.AMETHYST, "Amethyst")
        add(VanillaMaterials.COAL, "Coal")
        add(VanillaMaterials.COPPER, "Copper")
        add(VanillaMaterials.DIAMOND, "Diamond")
        add(VanillaMaterials.EMERALD, "Emerald")
        add(VanillaMaterials.GOLD, "Gold")
        add(VanillaMaterials.IRON, "Iron")
        add(VanillaMaterials.LAPIS, "Lapis")
        add(VanillaMaterials.NETHERITE, "Netherite")
        add(VanillaMaterials.NETHERITE_SCRAP, "Netherite Scrap")
        add(VanillaMaterials.QUARTZ, "Quartz")
        add(VanillaMaterials.REDSTONE, "Redstone")
        // Tag Prefix
        add(HTTagPrefix.CASING, "%s Casing")
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
        // Tags
        add(RagiumItemTags.ALKALI_REAGENTS, "Alkali Reagents")
        add(RagiumItemTags.DOUGH, "Doughs")
        add(RagiumItemTags.PLASTICS, "Plastics")

        add(RagiumItemTags.SOLAR_PANELS, "Solar Panels")
        // Misc
        add(RagiumTranslationKeys.FLUID_AMOUNT, "Amount: %s mb")
        add(RagiumTranslationKeys.FLUID_CAPACITY, "Capacity: %s mb")

        add("config.jade.plugin_ragium.energy_network", "Energy Network")
        add("config.jade.plugin_ragium.machine_info", "Machine Info")
    }
}
