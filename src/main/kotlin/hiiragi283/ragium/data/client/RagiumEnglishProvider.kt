package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.data.add
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumEnglishProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "en_us") {
    override fun addTranslations() {
        // Content
        add(RagiumTranslationKeys.CASING, "Casing")
        add(RagiumTranslationKeys.CIRCUIT, "Circuit")
        add(RagiumTranslationKeys.COIL, "Coil")
        add(RagiumTranslationKeys.CRATE, "Crate")
        add(RagiumTranslationKeys.DRUM, "Drum")
        add(RagiumTranslationKeys.GRATE, "Grate")
        add(RagiumTranslationKeys.HULL, "hull")
        add(RagiumTranslationKeys.PLASTIC, "Plastic")
        // Fluids
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            add(fluid.get().fluidType.descriptionId, fluid.enName)
        }
        // Machine
        add(HTMachineTier.PRIMITIVE, "Primitive", "Primitive %s")
        add(HTMachineTier.SIMPLE, "Simple", "Simple %s")
        add(HTMachineTier.BASIC, "Basic", "Basic %s")
        add(HTMachineTier.ADVANCED, "Advanced", "Advanced %s")
        add(HTMachineTier.ELITE, "Elite", "Elite %s")
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
        // add(RagiumMaterialKeys.NETHER_STAR, "Nether Star")
        add(RagiumMaterialKeys.NETHERITE, "Netherite")
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
        // add(RagiumMaterialKeys.STONE, "Stone")
        add(RagiumMaterialKeys.SULFUR, "Sulfur")
        add(RagiumMaterialKeys.TIN, "Tin")
        add(RagiumMaterialKeys.TITANIUM, "Titanium")
        add(RagiumMaterialKeys.TUNGSTEN, "Tungsten")
        add(RagiumMaterialKeys.URANIUM, "Uranium")
        // add(RagiumMaterialKeys.WOOD, "Wood")
        add(RagiumMaterialKeys.ZINC, "Zinc")
        // Tag Prefix
        add(HTTagPrefix.DUST, "%s Dust")
        add(HTTagPrefix.GEAR, "%s Gear")
        add(HTTagPrefix.GEM, "%s")
        add(HTTagPrefix.INGOT, "%s Ingot")
        add(HTTagPrefix.NUGGET, "%s Nugget")
        add(HTTagPrefix.ORE, "%s Ore")
        add(HTTagPrefix.PLATE, "%s Plate")
        add(HTTagPrefix.RAW_MATERIAL, "Raw %s")
        add(HTTagPrefix.ROD, "%s Rod")
        add(HTTagPrefix.STORAGE_BLOCK, "Block of %s")
        add(HTTagPrefix.WIRE, "%s Wire")
    }
}
