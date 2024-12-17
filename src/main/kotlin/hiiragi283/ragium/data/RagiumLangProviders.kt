package hiiragi283.ragium.data

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.splitWith
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Util
import net.minecraft.world.World
import java.util.concurrent.CompletableFuture

object RagiumLangProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(RagiumLangProviders::EnglishLang)
        pack.addProvider(RagiumLangProviders::JapaneseLang)
    }

    @JvmName("addBlock")
    fun TranslationBuilder.add(entry: HTContent<Block>, value: String) {
        val block: Block = entry.value
        add(block, value)
    }

    @JvmName("addItem")
    fun TranslationBuilder.add(entry: HTContent<Item>, value: String) {
        add(entry.value, value)
    }

    fun TranslationBuilder.add(enchantment: RegistryKey<Enchantment>, value: String) {
        add("enchantment.${enchantment.value.splitWith('.')}", value)
    }

    fun TranslationBuilder.add(tier: HTMachineTier, name: String, prefix: String) {
        add(tier.translationKey, name)
        add(tier.prefixKey, prefix)
    }

    fun TranslationBuilder.add(key: HTMachineKey, value: String, desc: String? = null) {
        add(key.translationKey, value)
        desc?.let { add(key.descriptionKey, it) }
    }

    fun TranslationBuilder.add(key: HTMaterialKey, value: String) {
        add(key.translationKey, value)
    }

    fun TranslationBuilder.add(prefix: HTTagPrefix, value: String) {
        add(prefix.translationKey, value)
    }

    fun TranslationBuilder.addWorld(key: RegistryKey<World>, value: String) {
        add(Util.createTranslationKey("world", key.value), value)
    }

    //    English    //

    private class EnglishLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            RagiumFluids.entries.forEach { fluid: RagiumFluids ->
                builder.add(
                    fluid.translationKey,
                    fluid.enName,
                )
            }

            builder.add(RagiumTranslationKeys.PRESS_CTRL, "Press Ctrl to show descriptions")

            // Advancements
            builder.add(RagiumTranslationKeys.ADVANCEMENT_BUJIN, "Tycoon the Racoon")
            builder.add(RagiumTranslationKeys.ADVANCEMENT_STELLA_SUIT, "Synthetically Treated External Lightweight-Layered Augment")
            builder.add(RagiumTranslationKeys.ADVANCEMENT_THIS_CAKE_IS_DIE, "This cake is DIE.")
            // Blocks
            builder.add(RagiumBlocks.CREATIVE_CRATE, "Creative Crate")
            builder.add(RagiumBlocks.CREATIVE_DRUM, "Creative Drum")
            builder.add(RagiumBlocks.CREATIVE_EXPORTER, "Creative Exporter")
            builder.add(RagiumBlocks.CREATIVE_SOURCE, "Creative Power Source")

            builder.add(RagiumBlocks.MUTATED_SOIL, "Mutated Soil")
            builder.add(RagiumBlocks.POROUS_NETHERRACK, "Porous Netherrack")

            builder.add(RagiumBlocks.ASPHALT, "Asphalt")
            builder.add(RagiumBlocks.ASPHALT_SLAB, "Asphalt Slab")
            builder.add(RagiumBlocks.ASPHALT_STAIRS, "Asphalt Stairs")
            builder.add(RagiumBlocks.POLISHED_ASPHALT, "Polished Asphalt")
            builder.add(RagiumBlocks.POLISHED_ASPHALT_SLAB, "Polished Asphalt Slab")
            builder.add(RagiumBlocks.POLISHED_ASPHALT_STAIRS, "Polished Asphalt Stairs")
            builder.add(RagiumBlocks.GYPSUM, "Gypsum")
            builder.add(RagiumBlocks.GYPSUM_SLAB, "Gypsum Slab")
            builder.add(RagiumBlocks.GYPSUM_STAIRS, "Gypsum Stairs")
            builder.add(RagiumBlocks.POLISHED_GYPSUM, "Polished Gypsum")
            builder.add(RagiumBlocks.POLISHED_GYPSUM_SLAB, "Polished Gypsum Slab")
            builder.add(RagiumBlocks.POLISHED_GYPSUM_STAIRS, "Polished Gypsum Stairs")
            builder.add(RagiumBlocks.SLATE, "Slate")
            builder.add(RagiumBlocks.SLATE_SLAB, "Slate Slab")
            builder.add(RagiumBlocks.SLATE_STAIRS, "Slate Stairs")
            builder.add(RagiumBlocks.POLISHED_SLATE, "Polished Slate")
            builder.add(RagiumBlocks.POLISHED_SLATE_SLAB, "Polished Slate Slab")
            builder.add(RagiumBlocks.POLISHED_SLATE_STAIRS, "Polished Slate Stairs")
            builder.add(RagiumBlocks.WHITE_LINE, "White Line")
            builder.add(RagiumBlocks.T_WHITE_LINE, "White Line (T)")
            builder.add(RagiumBlocks.CROSS_WHITE_LINE, "White Line (Cross)")
            builder.add(RagiumBlocks.STEEL_GLASS, "Steel Glass")
            builder.add(RagiumBlocks.RAGIUM_GLASS, "Ragium Glass")

            builder.add(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")
            builder.add(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

            builder.add(RagiumBlocks.AUTO_ILLUMINATOR, "Auto Illuminator")
            builder.add(RagiumBlocks.BACKPACK_INTERFACE, "Backpack Interface")
            builder.add(RagiumBlocks.ENCHANTMENT_BOOKSHELF, "Enchantment Bookshelf")
            builder.add(RagiumBlocks.ITEM_DISPLAY, "Item Display")
            builder.add(RagiumBlocks.LARGE_PROCESSOR, "Large Processor")
            builder.add(RagiumBlocks.MANUAL_FORGE, "Ragi-Anvil")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "Ragi-Grinder")
            builder.add(RagiumBlocks.MANUAL_MIXER, "Ragi-Basin")
            builder.add(RagiumBlocks.NETWORK_INTERFACE, "E.N.I.")
            builder.add(RagiumBlocks.OPEN_CRATE, "Open Crate")
            builder.add(RagiumBlocks.ROPE, "Rope")
            builder.add(RagiumBlocks.SHAFT, "Shaft")
            builder.add(RagiumBlocks.TELEPORT_ANCHOR, "Teleport Anchor")
            builder.add(RagiumBlocks.TRASH_BOX, "Trash Box")

            builder.add(RagiumContents.Pipes.STONE, "Stone Pipe")
            builder.add(RagiumContents.Pipes.WOODEN, "Wooden Pipe")
            builder.add(RagiumContents.Pipes.IRON, "Iron Pipe")
            builder.add(RagiumContents.Pipes.COPPER, "Copper Pipe")
            builder.add(RagiumContents.Pipes.UNIVERSAL, "Universal Pipe")

            builder.add(RagiumContents.CrossPipes.STEEL, "Steel Pipe")
            builder.add(RagiumContents.CrossPipes.GOLD, "Gold Pipe")

            builder.add(RagiumContents.PipeStations.ITEM, "Item Pipe Station")
            builder.add(RagiumContents.PipeStations.FLUID, "Fluid Pipe Station")

            builder.add(RagiumContents.FilteringPipe.ITEM, "Item Filtering Pipe")
            builder.add(RagiumContents.FilteringPipe.FLUID, "Fluid Filtering Pipe")

            builder.add(RagiumTranslationKeys.AUTO_ILLUMINATOR, "Place lights in area of %s block radius")
            builder.add(RagiumTranslationKeys.LARGE_PROCESSOR, "Extend processor machine inside the multiblock")
            builder.add(RagiumTranslationKeys.MANUAL_GRINDER, "Insert items by hopper or something else")
            builder.add(RagiumTranslationKeys.MUTATED_SOIL, "Used for Growth Chamber")
            builder.add(
                RagiumTranslationKeys.NETWORK_INTERFACE,
                "Connect Wireless Network and energy cables from other mod",
            )
            builder.add(RagiumTranslationKeys.OPEN_CRATE, "Drop inserted items below")
            builder.add(RagiumTranslationKeys.PIPE_STATION, "Priority transport to adjacent storage")
            builder.add(RagiumTranslationKeys.POROUS_NETHERRACK, "Absorb around lava like sponge but not reusable")
            builder.add(RagiumTranslationKeys.SPONGE_CAKE, "Decrease falling damage when land on")
            builder.add(RagiumTranslationKeys.TRASH_BOX, "Remove ALL inserted items or fluids")

            builder.add(RagiumTranslationKeys.CRATE_CAPACITY, "Capacity: %s Items")

            builder.add(RagiumTranslationKeys.DRUM_AMOUNT, "Amount: %s Unit")
            builder.add(RagiumTranslationKeys.DRUM_CAPACITY, "Capacity: %s Unit")
            builder.add(RagiumTranslationKeys.DRUM_FLUID, "Fluid: %s")

            builder.add(RagiumTranslationKeys.TRANSPORTER_FLUID_SPEED, "Fluid Speed: %s Units/s")
            builder.add(RagiumTranslationKeys.TRANSPORTER_ITEM_SPEED, "Item Speed: %s /s")

            builder.add(RagiumTranslationKeys.EXPORTER_FLUID_FILTER, "Current Fluid Filter: %s")
            builder.add(RagiumTranslationKeys.EXPORTER_ITEM_FILTER, "Current Item Filter: %s")
            // Contents
            builder.add(RagiumTranslationKeys.BATTERY, "Battery")
            builder.add(RagiumTranslationKeys.CASING, "Casing")
            builder.add(RagiumTranslationKeys.CIRCUIT, "Circuit")
            builder.add(RagiumTranslationKeys.CIRCUIT_BOARD, "Circuit Board")
            builder.add(RagiumTranslationKeys.COIL, "Coil")
            builder.add(RagiumTranslationKeys.CRATE, "Crate")
            builder.add(RagiumTranslationKeys.DRILL_HEAD, "Drill Head")
            builder.add(RagiumTranslationKeys.DRUM, "Drum")
            builder.add(RagiumTranslationKeys.EXPORTER, "Exporter")
            builder.add(RagiumTranslationKeys.GRATE, "Grate")
            builder.add(RagiumTranslationKeys.HULL, "Hull")
            // Enchantments
            // builder.add(RagiumEnchantments.SMELTING, "Smelting")
            // builder.add(RagiumEnchantments.SLEDGE_HAMMER, "Sledge Hammer")
            // builder.add(RagiumEnchantments.BUZZ_SAW, "Buzz Saw")
            // Items
            builder.add(RagiumItems.STEEL_HELMET, "Steel Helmet")
            builder.add(RagiumItems.STEEL_CHESTPLATE, "Steel Chestplate")
            builder.add(RagiumItems.STEEL_LEGGINGS, "Steel Leggings")
            builder.add(RagiumItems.STEEL_BOOTS, "Steel Boots")
            builder.add(RagiumItems.STELLA_GOGGLE, "S.T.E.L.L.A. Goggles")
            builder.add(RagiumItems.STELLA_JACKET, "S.T.E.L.L.A. Jacket")
            builder.add(RagiumItems.STELLA_LEGGINGS, "S.T.E.L.L.A. Leggings")
            builder.add(RagiumItems.STELLA_BOOTS, "S.T.E.L.L.A. Boots")

            builder.add(RagiumItems.STEEL_AXE, "Steel Axe")
            builder.add(RagiumItems.STEEL_HOE, "Steel Hoe")
            builder.add(RagiumItems.STEEL_PICKAXE, "Steel Pickaxe")
            builder.add(RagiumItems.STEEL_SHOVEL, "Steel Shovel")
            builder.add(RagiumItems.STEEL_SWORD, "Steel Sword")
            builder.add(RagiumItems.STELLA_SABER, "S.T.E.L.L.A. Saber")
            builder.add(RagiumItems.RAGIUM_SABER, "Ragium Saber")

            builder.add(RagiumItems.BEE_WAX, "Bee Wax")
            builder.add(RagiumItems.BUTTER, "Butter")
            builder.add(RagiumItems.CARAMEL, "Caramel")
            builder.add(RagiumItems.CHOCOLATE, "Chocolate")
            builder.add(RagiumItems.CHOCOLATE_APPLE, "Chocolate Apple")
            builder.add(RagiumItems.CHOCOLATE_BREAD, "Chocolate Bread")
            builder.add(RagiumItems.CHOCOLATE_COOKIE, "Chocolate Cookie")
            builder.add(RagiumItems.COOKED_MEAT_INGOT, "Cooked Meat Ingot")
            builder.add(RagiumItems.FLOUR, "Flour")
            builder.add(RagiumItems.DOUGH, "Dough")
            builder.add(RagiumItems.MEAT_INGOT, "Meat Ingot")
            builder.add(RagiumItems.MELON_PIE, "Melon Pie")
            builder.add(RagiumItems.MINCED_MEAT, "Minced Meat")
            builder.add(RagiumItems.PULP, "Pulp")
            builder.add(RagiumItems.SWEET_BERRIES_CAKE_PIECE, "A piece of Sweet Berries Cake")

            builder.add(RagiumItems.ANVIL_DYNAMITE, "Anvil Dynamite")
            builder.add(RagiumItems.BACKPACK, "Backpack")
            builder.add(RagiumItems.BASALT_MESH, "Basalt Mesh")
            builder.add(RagiumItems.BEDROCK_DYNAMITE, "Bedrock Dynamite")
            builder.add(RagiumItems.BLAZING_CARBON_ELECTRODE, "Blazing Carbon Electrode")
            builder.add(RagiumItems.CARBON_ELECTRODE, "Carbon Electrode")
            builder.add(RagiumItems.CHARGED_CARBON_ELECTRODE, "Charged Carbon Electrode")
            builder.add(RagiumItems.CRIMSON_CRYSTAL, "Crimson Crystal")
            builder.add(RagiumItems.CRUDE_SILICON, "Crude Silicon")
            builder.add(RagiumItems.DEEPANT, "Deepant")
            builder.add(RagiumItems.DYNAMITE, "Dynamite")
            builder.add(RagiumItems.EMPTY_FLUID_CUBE, "Fluid Cube (Empty)")
            builder.add(RagiumItems.ENGINE, "V8 Engine")
            builder.add(RagiumItems.ENGINEERING_PLASTIC_PLATE, "Engineering Plastic Plate")
            builder.add(RagiumItems.FILLED_FLUID_CUBE, "Fluid Cube (%s)")
            builder.add(RagiumItems.FLATTENING_DYNAMITE, "Flattening Dynamite")
            builder.add(RagiumItems.FLUID_FILTER, "Fluid Filter")
            builder.add(RagiumItems.FORGE_HAMMER, "Forge Hammer")
            builder.add(RagiumItems.GIGANT_HAMMER, "Gigant Hammer")
            builder.add(RagiumItems.GUIDE_BOOK, "Guide Book")
            builder.add(RagiumItems.ITEM_FILTER, "Item Filter")
            builder.add(RagiumItems.LASER_EMITTER, "Laser Emitter")
            builder.add(RagiumItems.LED, "L.E.D.")
            builder.add(RagiumItems.LUMINESCENCE_DUST, "Luminescence Dust")
            builder.add(RagiumItems.NUCLEAR_WASTE, "Nuclear Waste")
            builder.add(RagiumItems.OBSIDIAN_TEAR, "Obsidian Tear")
            builder.add(RagiumItems.PLASTIC_PLATE, "Plastic Plate")
            builder.add(RagiumItems.POLYMER_RESIN, "Polymer Resin")
            builder.add(RagiumItems.PROCESSOR_SOCKET, "Processor Socket")
            builder.add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
            builder.add(RagiumItems.RAGI_CRYSTAL_PROCESSOR, "Ragi-Crystal Processor")
            builder.add(RagiumItems.RAGI_TICKET, "Ragi-Ticket")
            builder.add(RagiumItems.RAGI_WRENCH, "Ragi-Wrench")
            builder.add(RagiumItems.REFINED_SILICON, "Refined Silicon")
            builder.add(RagiumItems.RESIDUAL_COKE, "Residual Coke")
            builder.add(RagiumItems.SILICON, "Silicon")
            builder.add(RagiumItems.SLAG, "Slag")
            builder.add(RagiumItems.SOAP_INGOT, "Soap Ingot")
            builder.add(RagiumItems.SOLAR_PANEL, "Solar Panel")
            builder.add(RagiumItems.STELLA_PLATE, "S.T.E.L.L.A. Plate")
            builder.add(RagiumItems.TRADER_CATALOG, "Trader Catalog")
            builder.add(RagiumItems.URANIUM_FUEL, "Uranium Fuel")
            builder.add(RagiumItems.PLUTONIUM_FUEL, "Plutonium Fuel")
            builder.add(RagiumItems.WARPED_CRYSTAL, "Warped Crystal")
            builder.add(RagiumItems.YELLOW_CAKE, "Yellow Cake")
            builder.add(RagiumItems.YELLOW_CAKE_PIECE, "A piece of Yellow Cake")

            builder.add(RagiumContents.PressMold.GEAR, "Press Mold (Gear)")
            builder.add(RagiumContents.PressMold.PIPE, "Press Mold (Pipe)")
            builder.add(RagiumContents.PressMold.PLATE, "Press Mold (Plate)")
            builder.add(RagiumContents.PressMold.ROD, "Press Mold (Rod)")

            builder.add(RagiumTranslationKeys.ANVIL_DYNAMITE, "Place Anvil when land on")
            builder.add(RagiumTranslationKeys.BACKPACK, "Shares inventory between the same color")
            builder.add(RagiumTranslationKeys.BEDROCK_DYNAMITE, "Flatten Bedrocks inside hit chunk")
            builder.add(RagiumTranslationKeys.FLATTENING_DYNAMITE, "Remove ALL blocks above when hit")
            builder.add(
                RagiumTranslationKeys.RAGI_WRENCH,
                "Right-click to rotate horizontally, change front when sneaking",
            )
            builder.add(RagiumTranslationKeys.ROPE, "Place down Ropes when land on")
            builder.add(RagiumTranslationKeys.TRADER_CATALOG, "Right-click to open Wandering Trader's Screen")
            builder.add(
                RagiumTranslationKeys.WARPED_CRYSTAL,
                "Right-click to teleport on linked Teleport Anchor, or bind it with sneaking",
            )

            builder.add(RagiumTranslationKeys.DYNAMITE_DESTROY, "Destroy: %s")
            builder.add(RagiumTranslationKeys.DYNAMITE_POWER, "Power: %s")
            builder.add(RagiumTranslationKeys.FILTER, "Right-click on Exporters to apply, or open setting menu")
            builder.add(RagiumTranslationKeys.FILTER_FORMAT, "Example: \"minecraft:iron_ingot\", [\"minecraft:water\"], \"#c:ores\"")
            builder.add(RagiumTranslationKeys.WARPED_CRYSTAL_DESTINATION, "Destination: %s")
            // Item Group
            builder.add(RagiumItemGroup.ITEM_KEY, "Ragium - Items")
            builder.add(RagiumItemGroup.FLUID_KEY, "Ragium - Fluids")
            builder.add(RagiumItemGroup.MACHINE_KEY, "Ragium - Machines")
            // Machine
            builder.add(RagiumTranslationKeys.MACHINE_NAME, "Name: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TIER, "Tier: %s")
            builder.add(RagiumTranslationKeys.MACHINE_FLUID_AMOUNT, "Amount: %s B, %s Units")
            builder.add(RagiumTranslationKeys.MACHINE_NETWORK_ENERGY, "Network Energy: %s Units")
            builder.add(RagiumTranslationKeys.MACHINE_RECIPE_COST, "Recipe cost: %s E")
            builder.add(RagiumTranslationKeys.MACHINE_SHOW_PREVIEW, "Show preview: %s")
            builder.add(RagiumTranslationKeys.MACHINE_SLOT_COUNTS, "Input/Output Slots: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TANK_CAPACITY, "Each Tank Capacity: %s Buckets")

            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %ss")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The structure is valid!")
            // Machine Tier
            builder.add(HTMachineTier.PRIMITIVE, "Primitive", "Primitive %s")
            builder.add(HTMachineTier.BASIC, "Basic", "Basic %s")
            builder.add(HTMachineTier.ADVANCED, "Advanced", "Advanced %s")
            // Machine Type
            builder.add(
                RagiumMachineKeys.BEDROCK_MINER,
                "Bedrock Miner",
                "Collect minerals from Bedrock",
            )
            builder.add(
                RagiumMachineKeys.BIOMASS_FERMENTER,
                "Biomass Fermenter",
                "Produce Biomass from Composter inputs",
            )
            builder.add(
                RagiumMachineKeys.CANNING_MACHINE,
                "Canning Machine",
                "Insert or Extract fluids from Fluid Cube",
            )
            builder.add(
                RagiumMachineKeys.DRAIN,
                "Drain",
                "Drains fluids from each side",
            )
            builder.add(
                RagiumMachineKeys.FLUID_DRILL,
                "Fluid Drill",
                "Pump up fluids from specified biomes",
            )
            builder.add(
                RagiumMachineKeys.ROCK_GENERATOR,
                "Rock Generator",
                "Require water and lava source around",
            )

            builder.add(
                RagiumMachineKeys.COMBUSTION_GENERATOR,
                "Combustion Generator",
                "Generate energy from liquid fuels",
            )
            builder.add(
                RagiumMachineKeys.NUCLEAR_REACTOR,
                "Nuclear Reactor",
                "Generate energy from radioactive fuels",
            )
            builder.add(
                RagiumMachineKeys.SOLAR_GENERATOR,
                "Solar Generator",
                "Generate energy in daytime",
            )
            builder.add(
                RagiumMachineKeys.STEAM_GENERATOR,
                "Steam Generator",
                "Generate energy from water and coal like fuels",
            )
            builder.add(
                RagiumMachineKeys.THERMAL_GENERATOR,
                "Thermal Generator",
                "Generate energy from hot fluids",
            )

            // builder.add(RagiumMachineKeys.ALLOY_FURNACE, "Alloy Furnace", "Smelt two ingredients into one")
            builder.add(RagiumMachineKeys.ASSEMBLER, "Assembler", "Dr.Doom, Assemble!")
            builder.add(RagiumMachineKeys.BLAST_FURNACE, "Large Blast Furnace", "Smelt multiple ingredients into one")
            builder.add(RagiumMachineKeys.CHEMICAL_REACTOR, "Chemical Reactor", "Are You Ready?")
            builder.add(RagiumMachineKeys.COMPRESSOR, "Compressor", "saves.zip.zip")
            builder.add(RagiumMachineKeys.CUTTING_MACHINE, "Cutting Machine", "Process Logs more efficiently")
            builder.add(RagiumMachineKeys.DISTILLATION_TOWER, "Distillation Tower", "Process Crude Oil")
            builder.add(RagiumMachineKeys.ELECTROLYZER, "Electrolyzer", "Elek On")
            builder.add(RagiumMachineKeys.EXTRACTOR, "Extractor", "Something like Centrifuge")
            builder.add(RagiumMachineKeys.GRINDER, "Grinder", "Crush Up")
            builder.add(RagiumMachineKeys.GROWTH_CHAMBER, "Growth Chamber")
            builder.add(RagiumMachineKeys.LASER_TRANSFORMER, "Laser Transformer")
            // builder.add(RagiumMachineKeys.METAL_FORMER, "Metal Former", "It's High Quality.")
            builder.add(RagiumMachineKeys.MIXER, "Mixer", "Genomix...")
            builder.add(RagiumMachineKeys.MULTI_SMELTER, "Multi Smelter", "Smelt multiple items at once")
            // Material
            builder.add(RagiumMaterialKeys.CRUDE_RAGINITE, "Crude Raginite")
            builder.add(RagiumMaterialKeys.RAGI_ALLOY, "Ragi-Alloy")
            builder.add(RagiumMaterialKeys.ALKALI, "Alkali")
            builder.add(RagiumMaterialKeys.ASH, "Ash")
            builder.add(RagiumMaterialKeys.COPPER, "Copper")
            builder.add(RagiumMaterialKeys.IRON, "Iron")
            builder.add(RagiumMaterialKeys.NITER, "Niter")
            builder.add(RagiumMaterialKeys.SALT, "Salt")
            builder.add(RagiumMaterialKeys.SULFUR, "Sulfur")
            builder.add(RagiumMaterialKeys.WOOD, "Wood")

            builder.add(RagiumMaterialKeys.RAGINITE, "Raginite")
            builder.add(RagiumMaterialKeys.RAGI_STEEL, "Ragi-Steel")
            builder.add(RagiumMaterialKeys.FLUORITE, "Fluorite")
            builder.add(RagiumMaterialKeys.GOLD, "Gold")
            builder.add(RagiumMaterialKeys.REDSTONE, "Redstone")
            builder.add(RagiumMaterialKeys.STEEL, "Steel")

            builder.add(RagiumMaterialKeys.RAGI_CRYSTAL, "Ragi-Crystal")
            builder.add(RagiumMaterialKeys.REFINED_RAGI_STEEL, "Refined Ragi-Steel")
            builder.add(RagiumMaterialKeys.ALUMINUM, "Aluminum")
            builder.add(RagiumMaterialKeys.BAUXITE, "Bauxite")
            builder.add(RagiumMaterialKeys.CRYOLITE, "Cryolite")
            builder.add(RagiumMaterialKeys.DEEP_STEEL, "Deep Steel")

            builder.add(RagiumMaterialKeys.RAGIUM, "Ragium")
            builder.add(RagiumMaterialKeys.NETHERITE, "Netherite")

            builder.add(RagiumMaterialKeys.COAL, "Coal")
            builder.add(RagiumMaterialKeys.DIAMOND, "Diamond")
            builder.add(RagiumMaterialKeys.EMERALD, "Emerald")
            builder.add(RagiumMaterialKeys.LAPIS, "Lapis")
            builder.add(RagiumMaterialKeys.PERIDOT, "Peridot")
            builder.add(RagiumMaterialKeys.QUARTZ, "Quartz")
            builder.add(RagiumMaterialKeys.SAPPHIRE, "Sapphire")
            builder.add(RagiumMaterialKeys.RUBY, "Ruby")

            builder.add(RagiumMaterialKeys.IRIDIUM, "Iridium")
            builder.add(RagiumMaterialKeys.LEAD, "Lead")
            builder.add(RagiumMaterialKeys.NICKEL, "Nickel")
            builder.add(RagiumMaterialKeys.PLATINUM, "Platinum")
            builder.add(RagiumMaterialKeys.SILVER, "Silver")
            builder.add(RagiumMaterialKeys.TIN, "Tin")
            builder.add(RagiumMaterialKeys.TUNGSTEN, "Tungsten")
            builder.add(RagiumMaterialKeys.ZINC, "Zinc")
            // Tag Prefix
            builder.add(HTTagPrefix.DEEP_ORE, "Deepslate %s Ore")
            builder.add(HTTagPrefix.END_ORE, "End %s Ore")
            builder.add(HTTagPrefix.DUST, "%s Dust")
            builder.add(HTTagPrefix.GEAR, "%s Gear")
            builder.add(HTTagPrefix.GEM, "%s")
            builder.add(HTTagPrefix.INGOT, "%s Ingot")
            builder.add(HTTagPrefix.NETHER_ORE, "Nether %s Ore")
            builder.add(HTTagPrefix.NUGGET, "%s Nugget")
            builder.add(HTTagPrefix.ORE, "%s Ore")
            builder.add(HTTagPrefix.PLATE, "%s Plate")
            builder.add(HTTagPrefix.RAW_MATERIAL, "Raw %s")
            builder.add(HTTagPrefix.ROD, "%s Rod")
            builder.add(HTTagPrefix.STORAGE_BLOCK, "Block of %s")
            // World
            builder.addWorld(World.OVERWORLD, "Overworld")
            builder.addWorld(World.NETHER, "Nether")
            builder.addWorld(World.END, "The End")
        }
    }

    //    Japanese    //

    private class JapaneseLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, "ja_jp", registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            RagiumFluids.entries.forEach { fluid: RagiumFluids ->
                builder.add(
                    fluid.translationKey,
                    fluid.jaName,
                )
            }

            builder.add(RagiumTranslationKeys.PRESS_CTRL, "Ctrlキーを押して説明を表示")
            // Advancements
            builder.add(RagiumTranslationKeys.ADVANCEMENT_BUJIN, "タイクーン将軍")
            builder.add(RagiumTranslationKeys.ADVANCEMENT_STELLA_SUIT, "ｽｺﾞｲ ﾂﾖｸﾃ ｴｹﾞﾂﾅｲｸﾗｲ Love-Loveﾅ ｱｰﾏｰ")
            builder.add(RagiumTranslationKeys.ADVANCEMENT_THIS_CAKE_IS_DIE, "つばさレストラン名物「デスケーキ」")
            // Blocks
            builder.add(RagiumBlocks.CREATIVE_CRATE, "クリエイティブ用クレート")
            builder.add(RagiumBlocks.CREATIVE_DRUM, "クリエイティブ用ドラム")
            builder.add(RagiumBlocks.CREATIVE_EXPORTER, "クリエイティブ用搬出機")
            builder.add(RagiumBlocks.CREATIVE_SOURCE, "クリエイティブ用エネルギー源")

            builder.add(RagiumBlocks.MUTATED_SOIL, "変異した土壌")
            builder.add(RagiumBlocks.POROUS_NETHERRACK, "多孔質ネザーラック")

            builder.add(RagiumBlocks.ASPHALT, "アスファルト")
            builder.add(RagiumBlocks.ASPHALT_SLAB, "アスファルトのハーフブロック")
            builder.add(RagiumBlocks.ASPHALT_STAIRS, "アスファルトの階段")
            builder.add(RagiumBlocks.POLISHED_ASPHALT, "磨かれたアスファルト")
            builder.add(RagiumBlocks.POLISHED_ASPHALT_SLAB, "磨かれたアスファルトのハーフブロック")
            builder.add(RagiumBlocks.POLISHED_ASPHALT_STAIRS, "磨かれたアスファルトの階段")
            builder.add(RagiumBlocks.GYPSUM, "石膏")
            builder.add(RagiumBlocks.GYPSUM_SLAB, "石膏のハーフブロック")
            builder.add(RagiumBlocks.GYPSUM_STAIRS, "石膏の階段")
            builder.add(RagiumBlocks.POLISHED_GYPSUM, "磨かれた石膏")
            builder.add(RagiumBlocks.POLISHED_GYPSUM_SLAB, "磨かれた石膏のハーフブロック")
            builder.add(RagiumBlocks.POLISHED_GYPSUM_STAIRS, "磨かれた石膏の階段")
            builder.add(RagiumBlocks.SLATE, "スレート")
            builder.add(RagiumBlocks.SLATE_SLAB, "スレートのハーフブロック")
            builder.add(RagiumBlocks.SLATE_STAIRS, "スレートの階段")
            builder.add(RagiumBlocks.POLISHED_SLATE, "磨かれたスレート")
            builder.add(RagiumBlocks.POLISHED_SLATE_SLAB, "磨かれたスレートのハーフブロック")
            builder.add(RagiumBlocks.POLISHED_SLATE_STAIRS, "磨かれたスレートの階段")
            builder.add(RagiumBlocks.WHITE_LINE, "白線")
            builder.add(RagiumBlocks.T_WHITE_LINE, "白線（T字）")
            builder.add(RagiumBlocks.CROSS_WHITE_LINE, "白線（交差）")
            builder.add(RagiumBlocks.STEEL_GLASS, "鋼鉄ガラス")
            builder.add(RagiumBlocks.RAGIUM_GLASS, "ラギウムガラス")

            builder.add(RagiumBlocks.SPONGE_CAKE, "スポンジケーキ")
            builder.add(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")

            builder.add(RagiumBlocks.AUTO_ILLUMINATOR, "光源置き太郎")
            builder.add(RagiumBlocks.BACKPACK_INTERFACE, "バックパックインターフェース")
            builder.add(RagiumBlocks.ENCHANTMENT_BOOKSHELF, "エンチャント本棚")
            builder.add(RagiumBlocks.ITEM_DISPLAY, "アイテムティスプレイ")
            builder.add(RagiumBlocks.LARGE_PROCESSOR, "大型処理装置")
            builder.add(RagiumBlocks.MANUAL_FORGE, "らぎ金床")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "らぎ臼")
            builder.add(RagiumBlocks.MANUAL_MIXER, "らぎ釜")
            builder.add(RagiumBlocks.NETWORK_INTERFACE, "E.N.I.")
            builder.add(RagiumBlocks.OPEN_CRATE, "オープンクレート")
            builder.add(RagiumBlocks.ROPE, "ロープ")
            builder.add(RagiumBlocks.SHAFT, "シャフト")
            builder.add(RagiumBlocks.TELEPORT_ANCHOR, "テレポートアンカー")
            builder.add(RagiumBlocks.TRASH_BOX, "ゴミ箱")

            builder.add(RagiumContents.Pipes.STONE, "石パイプ")
            builder.add(RagiumContents.Pipes.WOODEN, "木製パイプ")
            builder.add(RagiumContents.Pipes.IRON, "鉄パイプ")
            builder.add(RagiumContents.Pipes.COPPER, "銅パイプ")
            builder.add(RagiumContents.Pipes.UNIVERSAL, "万能パイプ")

            builder.add(RagiumContents.CrossPipes.STEEL, "鋼鉄パイプ")
            builder.add(RagiumContents.CrossPipes.GOLD, "金パイプ")

            builder.add(RagiumContents.PipeStations.ITEM, "アイテムパイプステーション")
            builder.add(RagiumContents.PipeStations.FLUID, "液体パイプステーション")

            builder.add(RagiumContents.FilteringPipe.ITEM, "アイテムフィルタリングパイプ")
            builder.add(RagiumContents.FilteringPipe.FLUID, "液体フィルタリングパイプ")

            builder.add(RagiumTranslationKeys.AUTO_ILLUMINATOR, "半径%sブロックの範囲に光源を自動で設置する")
            builder.add(RagiumTranslationKeys.LARGE_PROCESSOR, "マルチブロック内の加工機械を拡張する")
            builder.add(RagiumTranslationKeys.MANUAL_GRINDER, "ホッパーなどでアイテムを搬入できる")
            builder.add(RagiumTranslationKeys.MUTATED_SOIL, "成長チャンバーで使用")
            builder.add(
                RagiumTranslationKeys.NETWORK_INTERFACE,
                "無線ネットワークと他modのエネルギーケーブルをつなげる",
            )
            builder.add(RagiumTranslationKeys.OPEN_CRATE, "搬入されたアイテムを下にドロップする")
            builder.add(RagiumTranslationKeys.PIPE_STATION, "優先して隣接したストレージに輸送する")
            builder.add(RagiumTranslationKeys.POROUS_NETHERRACK, "スポンジのように溶岩を吸い取る（使い切り）")
            builder.add(RagiumTranslationKeys.SPONGE_CAKE, "着地時のダメージを軽減する")
            builder.add(RagiumTranslationKeys.TRASH_BOX, "搬入された「すべて」のアイテムや液体を消滅させる")

            builder.add(RagiumTranslationKeys.CRATE_CAPACITY, "容量: %s 個")

            builder.add(RagiumTranslationKeys.DRUM_AMOUNT, "液体量: %s ユニット")
            builder.add(RagiumTranslationKeys.DRUM_CAPACITY, "容量: %s ユニット")
            builder.add(RagiumTranslationKeys.DRUM_FLUID, "液体: %s")

            builder.add(RagiumTranslationKeys.TRANSPORTER_FLUID_SPEED, "液体速度: %s ユニット/秒")
            builder.add(RagiumTranslationKeys.TRANSPORTER_ITEM_SPEED, "アイテム速度: %s個/秒")

            builder.add(RagiumTranslationKeys.EXPORTER_FLUID_FILTER, "現在の液体フィルタ: %s")
            builder.add(RagiumTranslationKeys.EXPORTER_ITEM_FILTER, "現在のアイテムフィルタ: %s")
            // Contents
            builder.add(RagiumTranslationKeys.BATTERY, "バッテリー")
            builder.add(RagiumTranslationKeys.CASING, "外装")
            builder.add(RagiumTranslationKeys.CIRCUIT, "回路")
            builder.add(RagiumTranslationKeys.CIRCUIT_BOARD, "回路基板")
            builder.add(RagiumTranslationKeys.COIL, "コイル")
            builder.add(RagiumTranslationKeys.CRATE, "クレート")
            builder.add(RagiumTranslationKeys.DRILL_HEAD, "ドリルヘッド")
            builder.add(RagiumTranslationKeys.DRUM, "ドラム")
            builder.add(RagiumTranslationKeys.EXPORTER, "搬出機")
            builder.add(RagiumTranslationKeys.GRATE, "格子")
            builder.add(RagiumTranslationKeys.HULL, "筐体")
            // Enchantment
            // builder.add(RagiumEnchantments.SMELTING, "精錬")
            // builder.add(RagiumEnchantments.SLEDGE_HAMMER, "粉砕")
            // builder.add(RagiumEnchantments.BUZZ_SAW, "製材")
            // Items
            builder.add(RagiumItems.STEEL_HELMET, "スチールのヘルメット")
            builder.add(RagiumItems.STEEL_CHESTPLATE, "スチールのチェストプレート")
            builder.add(RagiumItems.STEEL_LEGGINGS, "スチールのレギンス")
            builder.add(RagiumItems.STEEL_BOOTS, "スチールのブーツ")
            builder.add(RagiumItems.STELLA_GOGGLE, "S.T.E.L.L.A.ゴーグル")
            builder.add(RagiumItems.STELLA_JACKET, "S.T.E.L.L.A.ジャケット")
            builder.add(RagiumItems.STELLA_LEGGINGS, "S.T.E.L.L.A.レギンス")
            builder.add(RagiumItems.STELLA_BOOTS, "S.T.E.L.L.A.ブーツ")

            builder.add(RagiumItems.STEEL_AXE, "スチールの斧")
            builder.add(RagiumItems.STEEL_HOE, "スチールのクワ")
            builder.add(RagiumItems.STEEL_PICKAXE, "スチールのツルハシ")
            builder.add(RagiumItems.STEEL_SHOVEL, "スチールのショベル")
            builder.add(RagiumItems.STEEL_SWORD, "スチールの剣")
            builder.add(RagiumItems.STELLA_SABER, "S.T.E.L.L.A.セイバー")
            builder.add(RagiumItems.RAGIUM_SABER, "ラギウムセイバー")

            builder.add(RagiumItems.BEE_WAX, "蜜蠟")
            builder.add(RagiumItems.BUTTER, "バター")
            builder.add(RagiumItems.CARAMEL, "キャラメル")
            builder.add(RagiumItems.CHOCOLATE, "チョコレート")
            builder.add(RagiumItems.CHOCOLATE_APPLE, "チョコリンゴ")
            builder.add(RagiumItems.CHOCOLATE_BREAD, "チョコパン")
            builder.add(RagiumItems.CHOCOLATE_COOKIE, "チョコレートクッキー")
            builder.add(RagiumItems.COOKED_MEAT_INGOT, "焼肉インゴット")
            builder.add(RagiumItems.FLOUR, "小麦粉")
            builder.add(RagiumItems.DOUGH, "生地")
            builder.add(RagiumItems.MEAT_INGOT, "生肉インゴット")
            builder.add(RagiumItems.MELON_PIE, "メロンパイ")
            builder.add(RagiumItems.MINCED_MEAT, "ひき肉")
            builder.add(RagiumItems.PULP, "パルプ")
            builder.add(RagiumItems.SWEET_BERRIES_CAKE_PIECE, "一切れのスイートベリーケーキ")

            builder.add(RagiumItems.ANVIL_DYNAMITE, "金床ダイナマイト")
            builder.add(RagiumItems.BACKPACK, "バックパック")
            builder.add(RagiumItems.BASALT_MESH, "玄武岩メッシュ")
            builder.add(RagiumItems.BEDROCK_DYNAMITE, "岩盤ダイナマイト")
            builder.add(RagiumItems.BLAZING_CARBON_ELECTRODE, "燃え盛る炭素電極")
            builder.add(RagiumItems.CARBON_ELECTRODE, "炭素電極")
            builder.add(RagiumItems.CHARGED_CARBON_ELECTRODE, "チャージされた炭素電極")
            builder.add(RagiumItems.CRIMSON_CRYSTAL, "深紅の結晶")
            builder.add(RagiumItems.CRUDE_SILICON, "粗製シリコン")
            builder.add(RagiumItems.DEEPANT, "ディーパント")
            builder.add(RagiumItems.DYNAMITE, "ダイナマイト")
            builder.add(RagiumItems.EMPTY_FLUID_CUBE, "液体キューブ（なし）")
            builder.add(RagiumItems.ENGINE, "V8エンジン")
            builder.add(RagiumItems.ENGINEERING_PLASTIC_PLATE, "エンジニアリングプラスチック板")
            builder.add(RagiumItems.FILLED_FLUID_CUBE, "液体キューブ（%s）")
            builder.add(RagiumItems.FLATTENING_DYNAMITE, "整地用ダイナマイト")
            builder.add(RagiumItems.FLUID_FILTER, "液体フィルタ")
            builder.add(RagiumItems.FORGE_HAMMER, "鍛造ハンマー")
            builder.add(RagiumItems.GIGANT_HAMMER, "ギガントハンマー")
            builder.add(RagiumItems.GUIDE_BOOK, "ガイドブック")
            builder.add(RagiumItems.ITEM_FILTER, "アイテムフィルタ")
            builder.add(RagiumItems.LASER_EMITTER, "レーザーエミッタ")
            builder.add(RagiumItems.LED, "L.E.D.")
            builder.add(RagiumItems.LUMINESCENCE_DUST, "ルミネッセンスダスト")
            builder.add(RagiumItems.NUCLEAR_WASTE, "核廃棄物")
            builder.add(RagiumItems.OBSIDIAN_TEAR, "黒曜石の涙")
            builder.add(RagiumItems.PLASTIC_PLATE, "プラスチック板")
            builder.add(RagiumItems.POLYMER_RESIN, "高分子樹脂")
            builder.add(RagiumItems.PROCESSOR_SOCKET, "プロセッサソケット")
            builder.add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
            builder.add(RagiumItems.RAGI_CRYSTAL_PROCESSOR, "ラギクリスタリルプロセッサ")
            builder.add(RagiumItems.RAGI_TICKET, "らぎチケット")
            builder.add(RagiumItems.RAGI_WRENCH, "らぎレンチ")
            builder.add(RagiumItems.REFINED_SILICON, "精製シリコン")
            builder.add(RagiumItems.RESIDUAL_COKE, "残渣油コークス")
            builder.add(RagiumItems.SILICON, "シリコン")
            builder.add(RagiumItems.SLAG, "スラグ")
            builder.add(RagiumItems.SOAP_INGOT, "石鹸インゴット")
            builder.add(RagiumItems.SOLAR_PANEL, "太陽光パネル")
            builder.add(RagiumItems.STELLA_PLATE, "S.T.E.L.L.A.板")
            builder.add(RagiumItems.TRADER_CATALOG, "行商人カタログ")
            builder.add(RagiumItems.URANIUM_FUEL, "ウラン燃料")
            builder.add(RagiumItems.PLUTONIUM_FUEL, "プルトニウム燃料")
            builder.add(RagiumItems.WARPED_CRYSTAL, "歪んだ結晶")
            builder.add(RagiumItems.YELLOW_CAKE, "イエローケーキ")
            builder.add(RagiumItems.YELLOW_CAKE_PIECE, "一切れのイエローケーキ")

            builder.add(RagiumContents.PressMold.GEAR, "プレス型（歯車）")
            builder.add(RagiumContents.PressMold.PIPE, "プレス型（パイプ）")
            builder.add(RagiumContents.PressMold.PLATE, "プレス型（板材）")
            builder.add(RagiumContents.PressMold.ROD, "プレス型（棒材）")

            builder.add(RagiumTranslationKeys.ANVIL_DYNAMITE, "着弾点に金床を設置する")
            builder.add(RagiumTranslationKeys.BACKPACK, "同じ色同士でインベントリを共有する")
            builder.add(RagiumTranslationKeys.BEDROCK_DYNAMITE, "着弾したチャンク内の岩盤を整地する")
            builder.add(RagiumTranslationKeys.FLATTENING_DYNAMITE, "着弾点より上のブロックを「すべて」消滅させる")
            builder.add(RagiumTranslationKeys.RAGI_WRENCH, "右クリックで水平方向の回転，シフト右クリックで正面を変更")
            builder.add(RagiumTranslationKeys.ROPE, "着弾点からロープを下す")
            builder.add(RagiumTranslationKeys.TRADER_CATALOG, "右クリックで行商人との取引を行う")
            builder.add(
                RagiumTranslationKeys.WARPED_CRYSTAL,
                "右クリックでテレポートアンカーの上にテレポート，シフト右クリックで紐づけ",
            )

            builder.add(RagiumTranslationKeys.DYNAMITE_DESTROY, "地形破壊: %s")
            builder.add(RagiumTranslationKeys.DYNAMITE_POWER, "威力: %s")
            builder.add(RagiumTranslationKeys.FILTER, "搬出機に右クリックで適用，または設定画面を開く")
            builder.add(RagiumTranslationKeys.FILTER_FORMAT, "例: \"minecraft:iron_ingot\", [\"minecraft:water\"], \"#c:ores\"")
            builder.add(RagiumTranslationKeys.WARPED_CRYSTAL_DESTINATION, "座標: %s")
            // Item Group
            builder.add(RagiumItemGroup.ITEM_KEY, "Ragium - アイテム")
            builder.add(RagiumItemGroup.FLUID_KEY, "Ragium - 液体")
            builder.add(RagiumItemGroup.MACHINE_KEY, "Ragium - 機械")
            // Machine
            builder.add(RagiumTranslationKeys.MACHINE_NAME, "名称: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TIER, "ティア: %s")
            builder.add(RagiumTranslationKeys.MACHINE_FLUID_AMOUNT, "液体量: %s B, %s ユニット")
            builder.add(RagiumTranslationKeys.MACHINE_NETWORK_ENERGY, "ネットワーク上のエネルギー量: %s ユニット")
            builder.add(RagiumTranslationKeys.MACHINE_RECIPE_COST, "処理コスト: %s E")
            builder.add(RagiumTranslationKeys.MACHINE_SHOW_PREVIEW, "プレビューの表示: %s")
            builder.add(RagiumTranslationKeys.MACHINE_SLOT_COUNTS, "入力/出力スロット数: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TANK_CAPACITY, "各液体タンクの容量: %s バケツ")

            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "次の条件を満たしていません: %s (座標: %s)")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "構造物は有効です！")
            // Machine Tier
            builder.add(HTMachineTier.PRIMITIVE, "簡易", "簡易型%s")
            builder.add(HTMachineTier.BASIC, "基本", "基本型%s")
            builder.add(HTMachineTier.ADVANCED, "発展", "発展型%s")
            // Machine Type
            builder.add(RagiumMachineKeys.BEDROCK_MINER, "岩盤採掘機", "岩盤から鉱物を採掘する")
            builder.add(RagiumMachineKeys.BIOMASS_FERMENTER, "バイオマス発酵槽", "植物からバイオマスを生産する")
            builder.add(RagiumMachineKeys.CANNING_MACHINE, "缶詰機", "液体キューブに液体を出し入れできる")
            builder.add(RagiumMachineKeys.DRAIN, "排水溝", "各面から液体を吸い取る")
            builder.add(RagiumMachineKeys.FLUID_DRILL, "液体採掘機", "特定のバイオームから液体を汲み上げる")
            builder.add(RagiumMachineKeys.ROCK_GENERATOR, "岩石生成機", "水と溶岩を少なくとも一つずつ隣接させる")

            builder.add(RagiumMachineKeys.COMBUSTION_GENERATOR, "燃焼発電機", "液体燃料から発電する")
            builder.add(RagiumMachineKeys.NUCLEAR_REACTOR, "原子炉", "放射性燃料から発電する")
            builder.add(RagiumMachineKeys.SOLAR_GENERATOR, "太陽光発電機", "日中に発電する")
            builder.add(RagiumMachineKeys.STEAM_GENERATOR, "蒸気発電機", "水と石炭類から発電する")
            builder.add(RagiumMachineKeys.THERMAL_GENERATOR, "地熱発電機", "高温の液体から発電する")

            // builder.add(RagiumMachineKeys.ALLOY_FURNACE, "合金かまど", "二つの素材を一つに焼き上げる")
            builder.add(RagiumMachineKeys.ASSEMBLER, "組立機", "悪魔博士，アッセンブル！")
            builder.add(RagiumMachineKeys.BLAST_FURNACE, "大型高炉", "複数の素材を一つに焼き上げる")
            builder.add(RagiumMachineKeys.CHEMICAL_REACTOR, "化学反応槽", "Are You Ready?")
            builder.add(RagiumMachineKeys.COMPRESSOR, "圧縮機", "saves.zip.zip")
            builder.add(RagiumMachineKeys.CUTTING_MACHINE, "裁断機", "")
            builder.add(RagiumMachineKeys.DISTILLATION_TOWER, "蒸留塔", "原油を処理する")
            builder.add(RagiumMachineKeys.ELECTROLYZER, "電解槽", "エレキ オン")
            builder.add(RagiumMachineKeys.EXTRACTOR, "抽出器", "遠心分離機みたいなやつ")
            builder.add(RagiumMachineKeys.GRINDER, "粉砕機", "クラッシュ・アップ")
            builder.add(RagiumMachineKeys.GROWTH_CHAMBER, "成長チャンバー")
            builder.add(RagiumMachineKeys.LASER_TRANSFORMER, "レーザー変換機")
            // builder.add(RagiumMachineKeys.METAL_FORMER, "金属加工機", "It's High Quality.")
            builder.add(RagiumMachineKeys.MULTI_SMELTER, "並列精錬機", "複数のアイテムを一度に製錬する")
            builder.add(RagiumMachineKeys.MIXER, "ミキサー", "ゲノミクス...")
            // Material
            builder.add(RagiumMaterialKeys.CRUDE_RAGINITE, "粗製ラギナイト")
            builder.add(RagiumMaterialKeys.RAGI_ALLOY, "ラギ合金")
            builder.add(RagiumMaterialKeys.ALKALI, "アルカリ")
            builder.add(RagiumMaterialKeys.ASH, "灰")
            builder.add(RagiumMaterialKeys.COPPER, "銅")
            builder.add(RagiumMaterialKeys.IRON, "鉄")
            builder.add(RagiumMaterialKeys.NITER, "硝石")
            builder.add(RagiumMaterialKeys.SALT, "塩")
            builder.add(RagiumMaterialKeys.SULFUR, "硫黄")
            builder.add(RagiumMaterialKeys.WOOD, "木材")

            builder.add(RagiumMaterialKeys.RAGINITE, "ラギナイト")
            builder.add(RagiumMaterialKeys.RAGI_STEEL, "ラギスチール")
            builder.add(RagiumMaterialKeys.FLUORITE, "蛍石")
            builder.add(RagiumMaterialKeys.GOLD, "金")
            builder.add(RagiumMaterialKeys.REDSTONE, "レッドストーン")
            builder.add(RagiumMaterialKeys.STEEL, "スチール")

            builder.add(RagiumMaterialKeys.RAGI_CRYSTAL, "ラギクリスタリル")
            builder.add(RagiumMaterialKeys.REFINED_RAGI_STEEL, "精製ラギスチール")
            builder.add(RagiumMaterialKeys.ALUMINUM, "アルミニウム")
            builder.add(RagiumMaterialKeys.BAUXITE, "ボーキサイト")
            builder.add(RagiumMaterialKeys.CRYOLITE, "氷晶石")
            builder.add(RagiumMaterialKeys.DEEP_STEEL, "深層鋼")

            builder.add(RagiumMaterialKeys.RAGIUM, "ラギウム")
            builder.add(RagiumMaterialKeys.NETHERITE, "ネザライト")

            builder.add(RagiumMaterialKeys.COAL, "石炭")
            builder.add(RagiumMaterialKeys.DIAMOND, "ダイアモンド")
            builder.add(RagiumMaterialKeys.EMERALD, "エメラルド")
            builder.add(RagiumMaterialKeys.LAPIS, "ラピス")
            builder.add(RagiumMaterialKeys.PERIDOT, "ペリドット")
            builder.add(RagiumMaterialKeys.QUARTZ, "水晶")
            builder.add(RagiumMaterialKeys.SAPPHIRE, "サファイア")
            builder.add(RagiumMaterialKeys.RUBY, "ルビー")

            builder.add(RagiumMaterialKeys.IRIDIUM, "イリジウム")
            builder.add(RagiumMaterialKeys.LEAD, "鉛")
            builder.add(RagiumMaterialKeys.NICKEL, "ニッケル")
            builder.add(RagiumMaterialKeys.PLATINUM, "白金")
            builder.add(RagiumMaterialKeys.SILVER, "銀")
            builder.add(RagiumMaterialKeys.TIN, "スズ")
            builder.add(RagiumMaterialKeys.TUNGSTEN, "タングステン")
            builder.add(RagiumMaterialKeys.ZINC, "亜鉛")
            // Tag Prefix
            builder.add(HTTagPrefix.DEEP_ORE, "深層%s鉱石")
            builder.add(HTTagPrefix.END_ORE, "エンド%s鉱石")
            builder.add(HTTagPrefix.DUST, "%sの粉")
            builder.add(HTTagPrefix.GEAR, "%sの歯車")
            builder.add(HTTagPrefix.GEM, "%s")
            builder.add(HTTagPrefix.INGOT, "%sインゴット")
            builder.add(HTTagPrefix.NUGGET, "%sのナゲット")
            builder.add(HTTagPrefix.NETHER_ORE, "ネザー%s鉱石")
            builder.add(HTTagPrefix.ORE, "%s鉱石")
            builder.add(HTTagPrefix.PLATE, "%s板")
            builder.add(HTTagPrefix.RAW_MATERIAL, "%sの原石")
            builder.add(HTTagPrefix.ROD, "%s棒")
            builder.add(HTTagPrefix.STORAGE_BLOCK, "%sブロック")
            // World
            builder.addWorld(World.OVERWORLD, "オーバーワールド")
            builder.addWorld(World.NETHER, "ネザー")
            builder.addWorld(World.END, "ジ・エンド")
        }
    }
}
