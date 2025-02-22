package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.extension.addEnchantment
import hiiragi283.ragium.api.extension.addFluid
import hiiragi283.ragium.api.machine.HTMachineType
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
import hiiragi283.ragium.integration.RagiumMekIntegration
import net.minecraft.data.PackOutput
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumEnglishProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "en_us") {
    override fun addTranslations() {
        // Block
        addBlock(RagiumBlocks.SOUL_MAGMA_BLOCK, "Soul Magma Block")
        addBlock(RagiumBlocks.CRUDE_OIL, "Crude Oil")

        addBlock(RagiumBlocks.SLAG_BLOCK, "Block of Slag")

        addBlock(RagiumBlocks.RAGI_BRICKS, "Ragi-Bricks")
        addBlock(RagiumBlocks.RAGI_BRICK_FAMILY.stairs, "Ragi-Brick Stairs")
        addBlock(RagiumBlocks.RAGI_BRICK_FAMILY.slab, "Ragi-Brick Slab")
        addBlock(RagiumBlocks.RAGI_BRICK_FAMILY.wall, "Ragi-Brick Wall")

        addBlock(RagiumBlocks.PLASTIC_BLOCK, "Plastic Block")
        addBlock(RagiumBlocks.PLASTIC_FAMILY.stairs, "Plastic Block Stairs")
        addBlock(RagiumBlocks.PLASTIC_FAMILY.slab, "Plastic Block Slab")
        addBlock(RagiumBlocks.PLASTIC_FAMILY.wall, "Plastic Block Wall")

        addBlock(RagiumBlocks.SHAFT, "Shaft")

        addBlock(RagiumBlocks.CHEMICAL_GLASS, "Chemical Glass")
        addBlock(RagiumBlocks.MOB_GLASS, "Mob Glass")
        addBlock(RagiumBlocks.OBSIDIAN_GLASS, "Obsidian Glass")
        addBlock(RagiumBlocks.SOUL_GLASS, "Soul Glass")

        addBlock(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")
        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

        addBlock(RagiumBlocks.MANUAL_GRINDER, "Manual Grinder")
        addBlock(RagiumBlocks.PRIMITIVE_BLAST_FURNACE, "Primitive Blast Furnace")
        addBlock(RagiumBlocks.DISENCHANTING_TABLE, "Disenchanting Table")

        addBlock(RagiumBlocks.COPPER_DRUM, "Copper Drum")

        addBlock(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")
        addBlock(RagiumBlocks.SLAG_COLLECTOR, "Slag Collector")

        addBlock(RagiumBlocks.MAGMA_BURNER, "Magma Burner")
        addBlock(RagiumBlocks.SOUL_BURNER, "Soul Burner")
        addBlock(RagiumBlocks.FIERY_BURNER, "Fiery Burner")

        addBlock(RagiumBlocks.getLedBlock(DyeColor.RED), "Red LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.GREEN), "Green LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.BLUE), "Blue LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.CYAN), "Cyan LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.MAGENTA), "Magenta LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.YELLOW), "Yellow LED Block")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.WHITE), "LED Block")

        add(RagiumTranslationKeys.MOB_GLASS, "Can be passed by only mobs")
        add(RagiumTranslationKeys.SOUL_GLASS, "Can be passed by only player")

        add(RagiumTranslationKeys.SPONGE_CAKE, "Decrease fall damage as same as Hay Bale")

        add(RagiumTranslationKeys.MANUAL_GRINDER, "Right-click to rotate")
        add(RagiumTranslationKeys.MANUAL_GRINDER_1, "Insert ingredients by hopper or pipes")

        add(RagiumTranslationKeys.PRIMITIVE_BLAST_FURNACE, "1x Iron Ingot + 4x Coal -> 1x Steel Ingot")
        add(RagiumTranslationKeys.PRIMITIVE_BLAST_FURNACE_1, "Required multiblock")

        add(RagiumTranslationKeys.ENERGY_NETWORK_INTERFACE, "Connect to Energy Network")
        add(RagiumTranslationKeys.SLAG_COLLECTOR, "Generate Slag when adjacent Large Blast Furnace processed")
        // Chemical
        add(RagiumMekIntegration.RAGINITE_SLURRY.cleanSlurry.translationKey, "Clean Raginite Slurry")
        add(RagiumMekIntegration.RAGINITE_SLURRY.dirtySlurry.translationKey, "Dirty Raginite Slurry")
        // Content
        add(HTOreVariant.OVERWORLD, "%s Ore")
        add(HTOreVariant.DEEPSLATE, "Deepslate %s Ore")
        add(HTOreVariant.NETHER, "Nether %s Ore")
        add(HTOreVariant.END, "End %s Ore")
        // Enchantment
        addEnchantment(RagiumEnchantments.CAPACITY, "Capacity", "Increase the capacity of item or fluid storages")
        // Fluids
        addFluid(RagiumFluids.HONEY, "Honey")
        addFluid(RagiumFluids.SNOW, "Powder Snow")
        addFluid(RagiumFluids.MOLTEN_METAL, "Molten Unknown Metal")

        addFluid(RagiumVirtualFluids.MUSHROOM_STEW, "Mushroom Stew")
        addFluid(RagiumVirtualFluids.AIR, "Air")

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
        // Items
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

        addItem(RagiumItems.AMBROSIA, "Ambrosia")

        addItem(RagiumItems.DIVING_GOGGLE, "Diving Goggles")
        addItem(RagiumItems.JETPACK, "Jetpack")
        addItem(RagiumItems.STEEL_ARMORS[ArmorItem.Type.HELMET], "Steel Helmet")
        addItem(RagiumItems.STEEL_ARMORS[ArmorItem.Type.CHESTPLATE], "Steel Chestplate")
        addItem(RagiumItems.STEEL_ARMORS[ArmorItem.Type.LEGGINGS], "Steel Leggings")
        addItem(RagiumItems.STEEL_ARMORS[ArmorItem.Type.BOOTS], "Steel Boots")

        addItem(RagiumItems.FEVER_PICKAXE, "Fever Pickaxe")
        addItem(RagiumItems.FORGE_HAMMER, "Forge Hammer")
        addItem(RagiumItems.RAGI_LANTERN, "Ragi-Lantern")
        addItem(RagiumItems.SILKY_PICKAXE, "Silky Pickaxe")
        addItem(RagiumItems.STEEL_SHEARS, "Steel Shears")

        addItem(RagiumItems.STEEL_TOOLS.axeItem, "Steel Axe")
        addItem(RagiumItems.STEEL_TOOLS.hoeItem, "Steel Hoe")
        addItem(RagiumItems.STEEL_TOOLS.pickaxeItem, "Steel Pickaxe")
        addItem(RagiumItems.STEEL_TOOLS.shovelItem, "Steel Shovel")
        addItem(RagiumItems.STEEL_TOOLS.swordItem, "Steel Sword")

        addItem(RagiumItems.DEFOLIANT, "Defoliant")
        addItem(RagiumItems.DYNAMITE, "Dynamite")
        addItem(RagiumItems.MAGNET, "Magnet")
        addItem(RagiumItems.POTION_BUNDLE, "Potion Bundle")
        addItem(RagiumItems.SOAP, "Soap")

        addItem(RagiumItems.BASIC_CIRCUIT, "Basic Circuit")
        addItem(RagiumItems.ADVANCED_CIRCUIT, "Advanced Circuit")
        addItem(RagiumItems.ELITE_CIRCUIT, "Elite Circuit")
        addItem(RagiumItems.ULTIMATE_CIRCUIT, "Ultimate Circuit")

        addItem(RagiumItems.BALL_PRESS_MOLD, "Press Mold (Ball)")
        addItem(RagiumItems.BLANK_PRESS_MOLD, "Press Mold (Blank)")
        addItem(RagiumItems.GEAR_PRESS_MOLD, "Press Mold (Gear)")
        addItem(RagiumItems.PLATE_PRESS_MOLD, "Press Mold (Plate)")
        addItem(RagiumItems.ROD_PRESS_MOLD, "Press Mold (Rod)")
        addItem(RagiumItems.WIRE_PRESS_MOLD, "Press Mold (Wire)")

        addItem(RagiumItems.REDSTONE_LENS, "Redstone Lens")
        addItem(RagiumItems.GLOW_LENS, "Glow Lens")
        addItem(RagiumItems.PRISMARINE_LENS, "Prismarine Lens")
        addItem(RagiumItems.MAGICAL_LENS, "Magical Lens")

        addItem(RagiumItems.MAGICAL_REAGENT, "Magical Reagent")
        addItem(RagiumItems.PRISMARINE_REAGENT, "Prismarine Reagent")
        addItem(RagiumItems.SCULK_REAGENT, "Sculk Reagent")
        addItem(RagiumItems.WITHER_REAGENT, "Wither Reagent")

        addItem(RagiumItems.EMPTY_FLUID_CUBE, "Empty Fluid Cube")
        addItem(RagiumItems.WATER_FLUID_CUBE, "Fluid Cube (Water)")
        addItem(RagiumItems.LAVA_FLUID_CUBE, "Fluid Cube (Lava)")

        addItem(RagiumItems.BEE_WAX, "Bee Wax")
        addItem(RagiumItems.CHEMICAL_MACHINE_CASING, "Chemical Machine Casing")
        addItem(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        addItem(RagiumItems.CRIMSON_CRYSTAL, "Crimson Crystal")
        addItem(RagiumItems.CRUDE_OIL_BUCKET, "Crude Oil Bucket")
        addItem(RagiumItems.ENGINE, "V8 Engine")
        addItem(RagiumItems.LED, "L.E.D.")
        addItem(RagiumItems.MACHINE_CASING, "Machine Casing")
        addItem(RagiumItems.PLASTIC_PLATE, "Plastic Plate")
        addItem(RagiumItems.POLYMER_RESIN, "Polymer Resin")
        addItem(RagiumItems.PRECISION_MACHINE_CASING, "Precision Machine Casing")
        addItem(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        addItem(RagiumItems.RAGI_TICKET, "Ragi-Ticket")
        addItem(RagiumItems.SILKY_CRYSTAL, "Silky Crystal")
        addItem(RagiumItems.SLAG, "Slag")
        addItem(RagiumItems.SOLAR_PANEL, "Solar Panel")
        addItem(RagiumItems.WARPED_CRYSTAL, "Warped Crystal")
        addItem(RagiumItems.YELLOW_CAKE, "Yellow Cake")
        addItem(RagiumItems.YELLOW_CAKE_PIECE, "A piece of Yellow Cake")

        add(RagiumTranslationKeys.BEE_WAX, "Can be used as same as Honeycomb")

        add(RagiumTranslationKeys.AMBROSIA, "Can be eaten for infinity times!")

        add(RagiumTranslationKeys.FEVER_PICKAXE, "Always applies Fortune V")
        add(RagiumTranslationKeys.SILKY_PICKAXE, "Always applies Silk Touch")

        add(RagiumTranslationKeys.DEFOLIANT, "Changes 9x9x9 area into waste land")
        add(RagiumTranslationKeys.DYNAMITE, "Explodes when hit")
        add(RagiumTranslationKeys.MAGNET, "Attracts around items")
        add(RagiumTranslationKeys.POTION_BUNDLE, "Combines potions up to 9")
        add(RagiumTranslationKeys.POTION_BUNDLE_1, "Left-click to open GUI")
        add(RagiumTranslationKeys.SOAP, "Right-click to wash targeted block")
        // Machine
        add(RagiumTranslationKeys.MACHINE_COST, "- Process Cost: %s FE/times")
        add(RagiumTranslationKeys.MACHINE_NAME, "- Machine Name: %s")
        add(RagiumTranslationKeys.MACHINE_TIER, "- Tier: %s")

        add(RagiumTranslationKeys.MACHINE_OWNER, "- Owner: %s")
        add(RagiumTranslationKeys.MACHINE_PREVIEW, "- Show Preview: %s")
        add(RagiumTranslationKeys.MACHINE_TICK_RATE, "- Tick Rate: %s ticks (%s sec)")
        add(RagiumTranslationKeys.MACHINE_WORKING, "- Working: %s")

        add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %ss")
        add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The machine structure is valid!")
        // Machine Type
        add(
            HTMachineType.BEDROCK_MINER,
            "Bedrock Miner",
            "Collect minerals from Bedrock",
        )
        add(HTMachineType.FISHER, "Fisher", "Fishing fishes from below water source")
        // add(HTMachineType.LOOT_SPAWNER, "Loot Spawner", "Generate mob drops from Broken Spawner")

        add(
            HTMachineType.COMBUSTION_GENERATOR,
            "Combustion Generator",
            "Generate energy from liquid fuels",
        )
        add(
            HTMachineType.SOLAR_GENERATOR,
            "Solar Generator",
            "Generate energy in daytime",
        )
        add(
            HTMachineType.STIRLING_GENERATOR,
            "Stirling Generator",
            "Generate energy from solid fuel and water",
        )
        add(
            HTMachineType.THERMAL_GENERATOR,
            "Thermal Generator",
            "Generate energy from hot fluids",
        )

        add(HTMachineType.ALCHEMICAL_BREWERY, "Alchemical Brewery", "Sequential Brewing")
        add(HTMachineType.ARCANE_ENCHANTER, "Arcane Enchanter", "Stable Enchanting")
        add(HTMachineType.ASSEMBLER, "Assembler", "You are the genius!")
        add(HTMachineType.BLAST_FURNACE, "Large Blast Furnace", "Smelt multiple ingredients into one")
        add(HTMachineType.COMPRESSOR, "Compressor", "saves.zip.zip")
        add(HTMachineType.EXTRACTOR, "Extractor", "Something like Centrifuge")
        add(HTMachineType.GRINDER, "Grinder", "Unbreakable Diamond")
        add(HTMachineType.GROWTH_CHAMBER, "Growth Chamber", "Growth Gran-ma")
        add(HTMachineType.INFUSER, "Infuser", "Something not like Centrifuge")
        add(HTMachineType.LASER_ASSEMBLY, "Laser Assembly", "Laser On...")
        add(HTMachineType.MIXER, "Mixer", "Best Match!")
        add(HTMachineType.MULTI_SMELTER, "Multi Smelter", "Smelt multiple items at once")
        add(HTMachineType.REFINERY, "Refinery", "Project Build")
        add(HTMachineType.SOLIDIFIER, "Solidifier", "Uncontrol Switch! Black Hazard!")
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
        add(CommonMaterials.CALCITE, "Calcite")
        add(CommonMaterials.CARBON, "Carbon")
        add(CommonMaterials.CHROMIUM, "Chromium")
        add(CommonMaterials.COAL_COKE, "Coal Coke")
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

        add(IntegrationMaterials.COPPER_ALLOY, "Copper Alloy")
        add(IntegrationMaterials.ENERGETIC_ALLOY, "Energetic Alloy")
        add(IntegrationMaterials.VIBRANT_ALLOY, "Vibrant Alloy")
        add(IntegrationMaterials.REDSTONE_ALLOY, "Redstone Alloy")
        add(IntegrationMaterials.CONDUCTIVE_ALLOY, "Conductive Alloy")
        add(IntegrationMaterials.PULSATING_ALLOY, "Pulsating Alloy")
        add(IntegrationMaterials.DARK_STEEL, "Dark Steel")
        add(IntegrationMaterials.SOULARIUM, "Soularium")
        add(IntegrationMaterials.END_STEEL, "End Steel")

        add(IntegrationMaterials.DARK_GEM, "Dark Gem")

        add(IntegrationMaterials.REFINED_GLOWSTONE, "Refined Glowstone")
        add(IntegrationMaterials.REFINED_OBSIDIAN, "Refined Obsidian")

        add(RagiumMaterials.DEEP_STEEL, "Deep Steel")
        add(RagiumMaterials.ECHORIUM, "Echorium")
        add(RagiumMaterials.FIERY_COAL, "Fiery Coal")
        add(RagiumMaterials.RAGI_ALLOY, "Ragi-Alloy")
        add(RagiumMaterials.RAGI_CRYSTAL, "Ragi-Crystal")
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
        add(HTTagPrefix.CLUMP, "%s Clump")
        add(HTTagPrefix.COIL, "%s Coil")
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
        add(RagiumItemTags.ADVANCED_CIRCUIT, "Advanced Circuit")
        add(RagiumItemTags.BASIC_CIRCUIT, "Basic Circuit")
        add(RagiumItemTags.COAL_COKE, "Coal Coke")
        add(RagiumItemTags.DOUGH, "Dough")
        add(RagiumItemTags.ELITE_CIRCUIT, "Elite Circuit")
        add(RagiumItemTags.PLASTICS, "Plastic")
        add(RagiumItemTags.SLAG, "Slag")
        add(RagiumItemTags.ULTIMATE_CIRCUIT, "Ultimate Circuit")

        add(RagiumItemTags.DIRT_SOILS, "Dirt Soil")
        add(RagiumItemTags.END_SOILS, "End Soil")
        add(RagiumItemTags.GEAR_MOLDS, "Press Mold (Gear)")
        add(RagiumItemTags.LED_BLOCKS, "LED Block")
        add(RagiumItemTags.MUSHROOM_SOILS, "Mushroom Soil")
        add(RagiumItemTags.NETHER_SOILS, "Nether Soil")
        add(RagiumItemTags.PLATE_MOLDS, "Press Mold (Plate)")
        add(RagiumItemTags.ROD_MOLDS, "Press Mold (Rod)")
        add(RagiumItemTags.SOLAR_PANELS, "Solar Panel")
        add(RagiumItemTags.WIRE_MOLDS, "Press Mold (Wire)")

        add(RagiumFluidTags.CREOSOTE, "Creosote")
        add(RagiumFluidTags.MEAT, "Meat")

        add(RagiumFluidTags.NITRO_FUEL, "Nitro Fuel")
        add(RagiumFluidTags.NON_NITRO_FUEL, "Non-Nitro Fuel")
        add(RagiumFluidTags.THERMAL_FUEL, "Thermal Fuel")
        // Misc
        add(RagiumTranslationKeys.FLUID_NAME, "- %s : %s mb")
        add(RagiumTranslationKeys.FLUID_AMOUNT, "Amount: %s mb")
        add(RagiumTranslationKeys.FLUID_CAPACITY, "Capacity: %s mb")

        add(RagiumTranslationKeys.FLUID_MOLTEN, "Molten %s")

        add("config.jade.plugin_ragium.energy_network", "Show Energy Network")
        add("config.jade.plugin_ragium.machine_info", "Show Machine Info")
        add("config.jade.plugin_ragium.error_message", "Show Error Message")
    }
}
