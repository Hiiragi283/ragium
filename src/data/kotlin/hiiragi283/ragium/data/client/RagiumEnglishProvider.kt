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
import hiiragi283.ragium.api.util.HTCrateVariant
import hiiragi283.ragium.api.util.HTDrumVariant
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.integration.RagiumMekAddon
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
        addBlock(RagiumBlocks.RAGI_BRICK_SETS.stairs, "Ragi-Brick Stairs")
        addBlock(RagiumBlocks.RAGI_BRICK_SETS.slab, "Ragi-Brick Slab")
        addBlock(RagiumBlocks.RAGI_BRICK_SETS.wall, "Ragi-Brick Wall")

        addBlock(RagiumBlocks.PLASTIC_BLOCK, "Plastic Block")
        addBlock(RagiumBlocks.PLASTIC_SETS.stairs, "Plastic Block Stairs")
        addBlock(RagiumBlocks.PLASTIC_SETS.slab, "Plastic Block Slab")
        addBlock(RagiumBlocks.PLASTIC_SETS.wall, "Plastic Block Wall")

        addBlock(RagiumBlocks.BLUE_NETHER_BRICKS, "Blue Nether Bricks")
        addBlock(RagiumBlocks.BLUE_NETHER_BRICK_SETS.stairs, "Blue Nether Brick Stairs")
        addBlock(RagiumBlocks.BLUE_NETHER_BRICK_SETS.slab, "Blue Nether Brick Slab")
        addBlock(RagiumBlocks.BLUE_NETHER_BRICK_SETS.wall, "Blue Nether Brick Wall")

        addBlock(RagiumBlocks.SHAFT, "Shaft")

        addBlock(RagiumBlocks.MOB_GLASS, "Mob Glass")
        addBlock(RagiumBlocks.OBSIDIAN_GLASS, "Obsidian Glass")
        addBlock(RagiumBlocks.QUARTZ_GLASS, "Quartz Glass")
        addBlock(RagiumBlocks.SOUL_GLASS, "Soul Glass")

        addBlock(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")
        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

        addBlock(RagiumBlocks.CHEMICAL_MACHINE_FRAME, "Chemical Machine Frame")
        addBlock(RagiumBlocks.COBBLESTONE_CASING, "Cobblestone Casing")
        addBlock(RagiumBlocks.MACHINE_FRAME, "Machine Frame")
        addBlock(RagiumBlocks.PRECISION_MACHINE_FRAME, "Precision Machine Frame")
        addBlock(RagiumBlocks.WOODEN_CASING, "Wooden Casing")

        addBlock(RagiumBlocks.MANUAL_GRINDER, "Manual Grinder")
        addBlock(RagiumBlocks.DISENCHANTING_TABLE, "Disenchanting Table")

        addBlock(RagiumBlocks.getCrate(HTCrateVariant.WOODEN), "Wooden Crate")
        addBlock(RagiumBlocks.getCrate(HTCrateVariant.IRON), "Iron Crate")
        addBlock(RagiumBlocks.getCrate(HTCrateVariant.STEEL), "Steel Crate")
        addBlock(RagiumBlocks.getCrate(HTCrateVariant.DEEP_STEEL), "Deep Steel Crate")
        addBlock(RagiumBlocks.getCrate(HTCrateVariant.DIAMOND), "Diamond Crate")
        addBlock(RagiumBlocks.getCrate(HTCrateVariant.NETHERITE), "Netherite Crate")

        addBlock(RagiumBlocks.getDrum(HTDrumVariant.COPPER), "Copper Drum")
        addBlock(RagiumBlocks.getDrum(HTDrumVariant.GOLD), "Gold Drum")
        addBlock(RagiumBlocks.getDrum(HTDrumVariant.ALUMINUM), "Aluminum Drum")
        addBlock(RagiumBlocks.getDrum(HTDrumVariant.EMERALD), "Emerald Drum")
        addBlock(RagiumBlocks.getDrum(HTDrumVariant.RAGIUM), "Ragium Drum")

        addBlock(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")

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

        add(RagiumTranslationKeys.ENERGY_NETWORK_INTERFACE, "Connect to Energy Network")
        add(RagiumTranslationKeys.SLAG_COLLECTOR, "Generate Slag when adjacent Large Blast Furnace processed")
        // Chemical
        add(RagiumMekAddon.RAGINITE_SLURRY.cleanSlurry.translationKey, "Clean Raginite Slurry")
        add(RagiumMekAddon.RAGINITE_SLURRY.dirtySlurry.translationKey, "Dirty Raginite Slurry")
        // Component
        add(RagiumTranslationKeys.ENTITY_DATA, "Holding Entity: %s")
        // Content
        add(HTOreVariant.OVERWORLD, "%s Ore")
        add(HTOreVariant.DEEPSLATE, "Deepslate %s Ore")
        add(HTOreVariant.NETHER, "Nether %s Ore")
        add(HTOreVariant.END, "End %s Ore")
        // Enchantment
        addEnchantment(RagiumEnchantments.CAPACITY, "Capacity", "Increase the capacity of item or fluid storages")
        // Entity
        addEntityType(RagiumEntityTypes.DYNAMITE, "Dynamite")
        addEntityType(RagiumEntityTypes.FLATTEN_DYNAMITE, "Flatten Dynamite")
        addEntityType(RagiumEntityTypes.POISON_DYNAMITE, "Poison Dynamite")
        // Fluids
        addFluid(RagiumFluids.GLASS, "Molten Glass")
        addFluid(RagiumFluids.HONEY, "Honey")
        addFluid(RagiumFluids.SNOW, "Powder Snow")

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
        addItem(RagiumItems.MEAT_SANDWICH, "Meat Sandwich")

        addItem(RagiumItems.WARPED_WART, "Warped Wart")

        addItem(RagiumItems.AMBROSIA, "Ambrosia")

        addItem(RagiumItems.DIVING_GOGGLE, "Diving Goggles")
        addItem(RagiumItems.JETPACK, "Jetpack")

        addItem(RagiumItems.EMBER_ALLOY_ARMORS[ArmorItem.Type.HELMET], "Ember Alloy Helmet")
        addItem(RagiumItems.EMBER_ALLOY_ARMORS[ArmorItem.Type.CHESTPLATE], "Ember Alloy Chestplate")
        addItem(RagiumItems.EMBER_ALLOY_ARMORS[ArmorItem.Type.LEGGINGS], "Ember Alloy Leggings")
        addItem(RagiumItems.EMBER_ALLOY_ARMORS[ArmorItem.Type.BOOTS], "Ember Alloy Boots")

        addItem(RagiumItems.STEEL_ARMORS[ArmorItem.Type.HELMET], "Steel Helmet")
        addItem(RagiumItems.STEEL_ARMORS[ArmorItem.Type.CHESTPLATE], "Steel Chestplate")
        addItem(RagiumItems.STEEL_ARMORS[ArmorItem.Type.LEGGINGS], "Steel Leggings")
        addItem(RagiumItems.STEEL_ARMORS[ArmorItem.Type.BOOTS], "Steel Boots")

        addItem(RagiumItems.FEVER_PICKAXE, "Fever Pickaxe")
        addItem(RagiumItems.FORGE_HAMMER, "Forge Hammer")
        addItem(RagiumItems.RAGI_LANTERN, "Ragi-Lantern")
        addItem(RagiumItems.RAGI_SHEARS, "Ragi-Shears")
        addItem(RagiumItems.SILKY_PICKAXE, "Silky Pickaxe")

        addItem(RagiumItems.EMBER_ALLOY_TOOLS.axeItem, "Ember Alloy Axe")
        addItem(RagiumItems.EMBER_ALLOY_TOOLS.hoeItem, "Ember Alloy Hoe")
        addItem(RagiumItems.EMBER_ALLOY_TOOLS.pickaxeItem, "Ember Alloy Pickaxe")
        addItem(RagiumItems.EMBER_ALLOY_TOOLS.shovelItem, "Ember Alloy Shovel")
        addItem(RagiumItems.EMBER_ALLOY_TOOLS.swordItem, "Ember Alloy Sword")

        addItem(RagiumItems.STEEL_TOOLS.axeItem, "Steel Axe")
        addItem(RagiumItems.STEEL_TOOLS.hoeItem, "Steel Hoe")
        addItem(RagiumItems.STEEL_TOOLS.pickaxeItem, "Steel Pickaxe")
        addItem(RagiumItems.STEEL_TOOLS.shovelItem, "Steel Shovel")
        addItem(RagiumItems.STEEL_TOOLS.swordItem, "Steel Sword")

        addItem(RagiumItems.POTION_BUNDLE, "Potion Bundle")
        addItem(RagiumItems.DEFOLIANT, "Defoliant")
        addItem(RagiumItems.DURALUMIN_CASE, "Duralumin Case")

        addItem(RagiumItems.ITEM_MAGNET, "Item Magnet")
        addItem(RagiumItems.EXP_MAGNET, "Exp Magnet")

        addItem(RagiumItems.DYNAMITE, "Dynamite")
        addItem(RagiumItems.FLATTEN_DYNAMITE, "Flatten Dynamite")
        addItem(RagiumItems.POISON_DYNAMITE, "Poison Dynamite")

        addItem(RagiumItems.BALL_PRESS_MOLD, "Press Mold (Ball)")
        addItem(RagiumItems.BLOCK_PRESS_MOLD, "Press Mold (Block)")
        addItem(RagiumItems.BLANK_PRESS_MOLD, "Press Mold (Blank)")
        addItem(RagiumItems.GEAR_PRESS_MOLD, "Press Mold (Gear)")
        addItem(RagiumItems.INGOT_PRESS_MOLD, "Press Mold (Ingot)")
        addItem(RagiumItems.PLATE_PRESS_MOLD, "Press Mold (Plate)")
        addItem(RagiumItems.ROD_PRESS_MOLD, "Press Mold (Rod)")
        addItem(RagiumItems.WIRE_PRESS_MOLD, "Press Mold (Wire)")

        addItem(RagiumItems.BASIC_CIRCUIT, "Basic Circuit")
        addItem(RagiumItems.ADVANCED_CIRCUIT, "Advanced Circuit")
        addItem(RagiumItems.ELITE_CIRCUIT, "Elite Circuit")

        addItem(RagiumItems.REDSTONE_LENS, "Redstone Lens")
        addItem(RagiumItems.GLOWSTONE_LENS, "Glowstone Lens")
        addItem(RagiumItems.DIAMOND_LENS, "Diamond Lens")
        addItem(RagiumItems.EMERALD_LENS, "Emerald Lens")
        addItem(RagiumItems.AMETHYST_LENS, "Amethyst Lens")

        addItem(RagiumItems.BEE_WAX, "Bee Wax")
        addItem(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        addItem(RagiumItems.CRUDE_OIL_BUCKET, "Crude Oil Bucket")
        addItem(RagiumItems.ENGINE, "V8 Engine")
        addItem(RagiumItems.HONEY_BUCKET, "Honey Bucket")
        addItem(RagiumItems.LED, "L.E.D.")
        addItem(RagiumItems.PLASTIC_PLATE, "Plastic Plate")
        addItem(RagiumItems.POLYMER_RESIN, "Polymer Resin")
        addItem(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        addItem(RagiumItems.SLAG, "Slag")
        addItem(RagiumItems.SOAP, "Soap")
        addItem(RagiumItems.SOLAR_PANEL, "Solar Panel")
        addItem(RagiumItems.STEEL_COMPOUND, "Steel Compound")
        addItem(RagiumItems.STONE_BOARD, "Stone Board")
        addItem(RagiumItems.TAR, "Tar")
        addItem(RagiumItems.YELLOW_CAKE, "Yellow Cake")
        addItem(RagiumItems.YELLOW_CAKE_PIECE, "A piece of Yellow Cake")

        addItem(RagiumItems.BLANK_TICKET, "Blank Ticket")
        addItem(RagiumItems.RAGI_TICKET, "Ragi-Ticket")

        add(RagiumTranslationKeys.AMBROSIA, "Can be eaten for infinity times!")
        add(RagiumTranslationKeys.BEE_WAX, "Can be used as same as Honeycomb")
        add(RagiumTranslationKeys.DEFOLIANT, "Changes 9x9x9 area into waste land")
        add(RagiumTranslationKeys.DURALUMIN_CASE, "Right-click to collect or place Spawner")
        add(RagiumTranslationKeys.DYNAMITE, "Explodes when hit")
        add(RagiumTranslationKeys.EFFECT_RANGE, "Effect Range: %s blocks")
        add(RagiumTranslationKeys.EXP_MAGNET, "Attracts around exp orbs")
        add(RagiumTranslationKeys.EXPLOSION_POWER, "Explosion Power: %s")
        add(RagiumTranslationKeys.FEVER_PICKAXE, "Always applies Fortune V")
        add(RagiumTranslationKeys.FLATTEN_DYNAMITE, "Flatten ALL blocks above hit position")
        add(RagiumTranslationKeys.ITEM_MAGNET, "Attracts around items")
        add(RagiumTranslationKeys.POISON_DYNAMITE, "Give Poison effect for entities in range")
        add(RagiumTranslationKeys.POTION_BUNDLE, "Combines potions up to 9")
        add(RagiumTranslationKeys.POTION_BUNDLE_1, "Left-click to open GUI")
        add(RagiumTranslationKeys.SILKY_PICKAXE, "Always applies Silk Touch")
        add(RagiumTranslationKeys.SOAP, "Right-click to wash targeted block")
        add(RagiumTranslationKeys.WARPED_WART, "Removes one random effect when eaten")
        // Machine
        add(RagiumTranslationKeys.MACHINE_COST, "- Process Cost: %s FE/times")
        add(RagiumTranslationKeys.MACHINE_COST_MODIFIER, "- Energy Cost Modifier: %sx")
        add(RagiumTranslationKeys.MACHINE_NAME, "- Machine Name: %s")
        add(RagiumTranslationKeys.MACHINE_NETWORK_ENERGY, "%s FE")
        add(RagiumTranslationKeys.MACHINE_TICK_RATE, "- Tick Rate: %s ticks (%s sec)")
        add(RagiumTranslationKeys.MACHINE_WORKING_SUCCESS, "Working Successfully!")

        add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %ss")
        add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The machine structure is valid!")
        // Machine Type
        add(HTMachineType.BEDROCK_MINER, "Bedrock Miner", "Collect minerals from Bedrock")
        add(HTMachineType.FISHER, "Fisher", "Fishing fishes from below water source")

        add(HTMachineType.COMBUSTION_GENERATOR, "Combustion Generator", "Generate energy from liquid fuels")
        add(HTMachineType.ENCH_GENERATOR, "Enchantment Generator", "Generate energy from around enchantment sources")
        add(HTMachineType.SOLAR_GENERATOR, "Solar Generator", "Generate energy in daytime")
        add(HTMachineType.STIRLING_GENERATOR, "Stirling Generator", "Generate energy from solid fuel and water")
        add(HTMachineType.THERMAL_GENERATOR, "Thermal Generator", "Generate energy from hot fluids")

        add(HTMachineType.ALLOY_FURNACE, "Alloy Furnace", "A = A")
        add(HTMachineType.ASSEMBLER, "Assembler", "You are the genius!")
        add(HTMachineType.AUTO_CHISEL, "Auto Chisel", "Automated Stonecutter")
        add(HTMachineType.BREWERY, "Alchemical Brewery", "Sequential Brewing")
        add(HTMachineType.COMPRESSOR, "Compressor", "saves.zip.zip")
        add(HTMachineType.CRUSHER, "Crusher", "Crush Up!")
        add(HTMachineType.ELECTRIC_FURNACE, "Electric Furnace", "Smelting with energy or below heat")
        add(HTMachineType.ENCHANTER, "Arcane Enchanter", "Stable Enchanting")
        add(HTMachineType.EXTRACTOR, "Extractor", "Something like Centrifuge")
        add(HTMachineType.GRINDER, "Grinder", "Unbreakable Diamond")
        add(HTMachineType.GROWTH_CHAMBER, "Growth Chamber", "Growth Gran-ma")
        add(HTMachineType.INFUSER, "Infuser", "Something not like Centrifuge")
        add(HTMachineType.LASER_ASSEMBLY, "Laser Assembly", "Laser On...")
        add(HTMachineType.MIXER, "Mixer", "Best Match!")
        add(HTMachineType.MULTI_SMELTER, "Multi Smelter", "Allow bulk smelting")
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
        add(CommonMaterials.WOOD, "Wood")
        add(CommonMaterials.ZINC, "Zinc")

        add(IntegrationMaterials.BLACK_QUARTZ, "Black Quartz")

        add(IntegrationMaterials.ANDESITE_ALLOY, "Andesite Alloy")
        add(IntegrationMaterials.CARDBOARD, "Cardboard")
        add(IntegrationMaterials.ROSE_QUARTZ, "Rose Quartz")

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

        add(IntegrationMaterials.HOP_GRAPHITE, "HOP Graphite")

        add(IntegrationMaterials.REFINED_GLOWSTONE, "Refined Glowstone")
        add(IntegrationMaterials.REFINED_OBSIDIAN, "Refined Obsidian")

        add(IntegrationMaterials.CARMINITE, "Carminite")
        add(IntegrationMaterials.FIERY_METAL, "Fiery")
        add(IntegrationMaterials.IRONWOOD, "Ironwood")
        add(IntegrationMaterials.KNIGHTMETAL, "Knightmetal")
        add(IntegrationMaterials.STEELEAF, "Steeleaf")

        add(RagiumMaterials.CRIMSON_CRYSTAL, "Crimson Crystal")
        add(RagiumMaterials.DEEP_STEEL, "Deep Steel")
        add(RagiumMaterials.DURALUMIN, "Duralumin")
        add(RagiumMaterials.EMBER_ALLOY, "Ember Alloy")
        add(RagiumMaterials.FIERY_COAL, "Fiery Coal")
        add(RagiumMaterials.RAGI_ALLOY, "Ragi-Alloy")
        add(RagiumMaterials.RAGI_CRYSTAL, "Ragi-Crystal")
        add(RagiumMaterials.RAGINITE, "Raginite")
        add(RagiumMaterials.RAGIUM, "Ragium")
        add(RagiumMaterials.WARPED_CRYSTAL, "Warped Crystal")
        add(VanillaMaterials.AMETHYST, "Amethyst")
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
        // Tag Prefix
        add(HTTagPrefix.BLOCK, "Block of %s")
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
        add(HTTagPrefix.TINY_DUST, "Tiny %s Dust")
        add(HTTagPrefix.WIRE, "%s Wire")
        // Tags
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

        add(RagiumFluidTags.CHOCOLATES, "Chocolate")
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

        add("config.jade.plugin_ragium.enchantable_block", "Enchantable Block")
        add("config.jade.plugin_ragium.energy_network", "Show Energy Network")
        add("config.jade.plugin_ragium.error_message", "Show Error Message")
        add("config.jade.plugin_ragium.machine_info", "Show Machine Info")
    }
}
