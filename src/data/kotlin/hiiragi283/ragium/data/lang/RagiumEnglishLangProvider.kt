package hiiragi283.ragium.data.lang

import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.advancment.RagiumAdvancementKeys
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.effect.RagiumMobEffects
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.item.alchemy.RagiumPotions
import hiiragi283.ragium.integration.jade.HTMachineOwnerProvider
import hiiragi283.ragium.integration.jade.HTMachineUpgradeProvider
import net.minecraft.data.PackOutput

class RagiumEnglishLangProvider(output: PackOutput) :
    HTLangProvider(output, RagiumAPI.MOD_ID, HTLangTypes.EN_US),
    RagiumLangProvider {
    override fun addTranslations() {
        addPatternTranslations(this)

        // Advancement
        addAdvancement(RagiumAdvancementKeys.ROOT, "Ragium", "Welcome to Ragium!")
        addAdvancement(RagiumAdvancementKeys.SOOTY_IRON, "Acquire Blackware", "Craft a Sooty Iron Ingot")

        addAdvancement(
            RagiumAdvancementKeys.MECHANICAL_MACHINE_PARTS,
            "Mechanical Machines",
            "Craft a Machine Casing (Mechanical)",
        )
        addAdvancement(RagiumAdvancementKeys.ASSEMBLER, "Rava(n)gers, assemble!", "Acquire Assembler")
        addAdvancement(RagiumAdvancementKeys.CRUSHER, "Macerator, Pulverizer, or Crusher?", "Acquire Crusher")
        addAdvancement(RagiumAdvancementKeys.UPGRADE_STATION, "Build Up", "Acquire Upgrade Station")

        addAdvancement(RagiumAdvancementKeys.HEAT_MACHINE_PARTS, "Heat And Cool", "Craft a Machine Casing (Heat)")
        addAdvancement(RagiumAdvancementKeys.BLACK_STEEL, "Black Roaring", "Craft a Black Steel Ingot")
        addAdvancement(RagiumAdvancementKeys.FREEZER, "My power is 530,000.", "Acquire Freezer")
        addAdvancement(RagiumAdvancementKeys.MELTER, "(S)melter(y)", "Acquire Melter")
        // Block
        add(RagiumBlocks.QUARTZ_GLASS, "Quartz Glass")
        add(RagiumBlocks.QUARTZ_GLASS_PANE, "Quartz Glass Pane")

        add(RagiumBlocks.FUEL_DYNAMO, "Fuel Dynamo")
        add(RagiumBlocks.MAGMATIC_DYNAMO, "Magmatic Dynamo")
        add(RagiumBlocks.COMBUSTION_DYNAMO, "Combustion Dynamo")

        add(RagiumBlocks.ASSEMBLER, "Assembler")
        add(RagiumBlocks.CENTRIFUGE, "Centrifuge")
        add(RagiumBlocks.CRUSHER, "Crusher")
        add(RagiumBlocks.COMPRESSOR, "Compressor")
        add(RagiumBlocks.CUTTING_MACHINE, "Cutting Machine")
        add(RagiumBlocks.PRESS_MACHINE, "Press Machine")

        add(RagiumBlocks.ALLOY_SMELTER, "Alloy Smelter")
        add(RagiumBlocks.FREEZER, "Freezer")
        add(RagiumBlocks.MELTER, "Melter")
        add(RagiumBlocks.PYROLYZER, "Pyrolyzer")
        add(RagiumBlocks.REFINERY, "Refinery")
        add(RagiumBlocks.SMELTER, "Smelter")

        add(RagiumBlocks.CHEMICAL_BATH, "Chemical Bath")
        add(RagiumBlocks.CHEMICAL_REACTOR, "Chemical Reactor")
        add(RagiumBlocks.MIXER, "Mixer")

        add(RagiumBlocks.BREWERY, "Brewery")
        add(RagiumBlocks.PLANTER, "Planter")
        add(RagiumBlocks.RESOURCE_PUMP, "Resource Pump")

        add(RagiumBlocks.BLENDER, "Blender")
        add(RagiumBlocks.ENGRAVER, "Laser Engraver")
        add(RagiumBlocks.SCANNER, "Laser Scanner")

        add(RagiumBlocks.ARCANE_TRANSFORMER, "Arcane Transformer")
        add(RagiumBlocks.ENCHANTER, "Enchanter")

        add(RagiumBlocks.TANK, "Variable Tank")
        add(RagiumBlocks.POTION_TANK, "Potion Tank")
        add(RagiumBlocks.VOID_TANK, "Void Tank")
        add(RagiumBlocks.CREATIVE_TANK, "Creative Tank")

        add(RagiumBlocks.CREATIVE_BATTERY, "Creative Battery")

        add(RagiumBlocks.UPGRADE_STATION, "Upgrade Station")

        add(RagiumBlocks.MACHINE_CASING, "Machine Casing")
        // Damage Type
        add("death.attack.chemicalBurn", $$"%1$s was injured chemical burn")
        add("death.attack.chemicalBurn.player", $$"%1$s was injured chemical burn while fighting %2$s")
        // Fluid
        addFluid(RagiumFluids.HONEY, "Honey")
        addFluid(RagiumFluids.MOLTEN_BEESWAX, "Molten Beeswax")
        addFluid(RagiumFluids.RESIN, "Resin")
        // add(RagiumFluids.POTION.getFluidType().descriptionId, "Invalid Potion")
        add(RagiumFluids.POTION.bucketHolder, $$"%1$s Bucket")
        addFluid(RagiumFluids.OMINOUS_FLUX, "Ominous Flux")
        addFluid(RagiumFluids.MOLTEN_GLASS, "Molten Glass")
        addFluid(RagiumFluids.MOLTEN_REDSTONE, "Destabilized Redstone")
        addFluid(RagiumFluids.MOLTEN_GLOWSTONE, "Energized Glowstone")
        addFluid(RagiumFluids.MOLTEN_ENDER, "Resonant Ender")

        // H
        addFluid(RagiumFluids.HYDROGEN, "Hydrogen")
        // C
        addFluid(RagiumFluids.CARBON_DIOXIDE, "Carbon Dioxide")
        addFluid(RagiumFluids.CREOSOTE, "Creosote")
        addFluid(RagiumFluids.BLACK_LIQUOR, "Black Liquor")
        addFluid(RagiumFluids.PLANT_OIL, "Plant Oil")
        addFluid(RagiumFluids.ALCOHOL, "Alcohol")
        addFluid(RagiumFluids.ALDEHYDE, "Aldehyde")
        addFluid(RagiumFluids.AROMATIC_COMPOUND, "Aromatic Compound")
        addFluid(RagiumFluids.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluids.NATURAL_GAS, "Natural Gas")
        addFluid(RagiumFluids.NAPHTHA, "Naphtha")
        addFluid(RagiumFluids.FUEL, "Fuel")
        addFluid(RagiumFluids.ANTI_RUST_OIL, "Anti-rust Oil")
        addFluid(RagiumFluids.MOLTEN_PLASTIC, "Molten Plastic")
        addFluid(RagiumFluids.SYNTHETIC_RESIN, "Synthetic Resin")
        // N
        addFluid(RagiumFluids.NITRIC_ACID, "Nitric Acid")
        addFluid(RagiumFluids.LIQUID_EXPLOSIVE, "Liquid Explosive")
        // O
        addFluid(RagiumFluids.OXYGEN, "Oxygen")
        // F
        addFluid(RagiumFluids.HYDROGEN_FLUORIDE, "Hydrogen Fluoride")
        addFluid(RagiumFluids.HYDROFLUORIC_ACID, "Hydrofluoric Acid")
        // Na
        addFluid(RagiumFluids.SALT_WATER, "Salt Water")
        addFluid(RagiumFluids.HYDROXIDE_SOLUTION, "Alkali Hydroxide Solution")
        addFluid(RagiumFluids.SULFATE_SOLUTION, "Alkali Sulfate Solution")
        // Al
        addFluid(RagiumFluids.ALUMINA_SOLUTION, "Alumina Solution")
        // P
        addFluid(RagiumFluids.PHOSPHORIC_ACID, "Phosphoric Acid")
        // S
        addFluid(RagiumFluids.SULFUR_DIOXIDE, "Sulfur Dioxide")
        addFluid(RagiumFluids.SULFUR_TRIOXIDE, "Sulfur Trioxide")
        addFluid(RagiumFluids.SULFURIC_ACID, "Sulfuric Acid")
        // Cl
        addFluid(RagiumFluids.CHLORINE, "Chlorine")
        addFluid(RagiumFluids.HYDROGEN_CHLORIDE, "Hydrogen Chloride")
        addFluid(RagiumFluids.HYDROCHLORIC_ACID, "Hydrochloric Acid")
        // Ca
        addFluid(RagiumFluids.LIMEWATER, "Limewater")

        // Item
        add(RagiumItems.BAMBOO_CHARCOAL, "Bamboo Charcoal")
        add(RagiumItems.PARTICLE_BOARD, "Particle Board")
        add(RagiumItems.MORTAR, "Mortar")
        add(RagiumItems.TAR, "Tar")
        add(RagiumItems.STICKY_BALL, "Sticky Ball")
        add(RagiumItems.ALKALI_HYDROXIDE, "Alkali Hydroxide")
        add(RagiumItems.ALKALI_CARBONATE, "Alkali Carbonate")
        add(RagiumItems.ALKALI_SULFATE, "Alkali Sulfate")
        add(RagiumItems.PLASTIC_PLATE, "Plastic Plate")
        add(RagiumItems.SYNTHETIC_FEATHER, "Synthetic Feather")
        add(RagiumItems.SYNTHETIC_LEATHER, "Synthetic Leather")
        add(RagiumItems.SYNTHETIC_FIBER, "Synthetic Fiber")
        // add(RagiumItems.ALCLAD_PLATE, "Alclad Plate")
        add(RagiumItems.BEESWAX, "Beeswax")
        add(RagiumItems.BIO_CHAFF, "Bio Chaff")
        add(RagiumItems.SPLASH_BOTTLE, "Splash Bottle")
        add(RagiumItems.LINGERING_BOTTLE, "Lingering Bottle")
        add(RagiumItems.CRUDE_SILICON, "Crude Silicon")
        add(RagiumItems.SILICON_WAFER, "Silicon Wafer")
        add(RagiumItems.SILICON_CHIP, "Silicon Chip")
        add(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        add(RagiumItems.ELECTRIC_CIRCUIT, "Electric Circuit")
        add(RagiumItems.DIAMOND_WAFER, "Diamond Wafer")
        add(RagiumItems.DIAMOND_CHIP, "Diamond Chip")
        add(RagiumItems.ELDER_HEART, "Elder Heart")
        add(RagiumItems.WITHER_DOLL, "Wither Doll")
        add(RagiumItems.WITHER_STAR, "Wither Star")

        add(RagiumItems.BLANK_DIE, "Blank Press Die")
        add(RagiumItems.GEAR_DIE, "Gear Press Die")
        add(RagiumItems.PACKING_DIE, "Packing Press Die")
        add(RagiumItems.UNPACKING_DIE, "Unpacking Press Die")
        add(RagiumItems.HEATING_COIL, "Heating Coil")
        add(RagiumItems.BLANK_MOLD, "Blank Mold")
        add(RagiumItems.BALL_MOLD, "Ball Mold")
        add(RagiumItems.BLOCK_MOLD, "Block Mold")
        add(RagiumItems.PLATE_MOLD, "Plate Mold")
        add(RagiumItems.SHADOW_LENS, "Shadow Lens")
        add(RagiumItems.NETHER_STAR_LENS, "Nether Star Lens")
        add(RagiumItems.MEMORY_DISC, "Memory Disc")

        add(RagiumItems.BASIC_UPGRADE, "Basic Upgrade")
        add(RagiumItems.ADVANCED_UPGRADE, "Advanced Upgrade")
        add(RagiumItems.ELITE_UPGRADE, "Elite Upgrade")
        add(RagiumItems.ULTIMATE_UPGRADE, "Ultimate Upgrade")

        // Mob Effect
        add(RagiumMobEffects.FROSTBITE, "Frostbite")
        add(RagiumMobEffects.CHEMICAL_BURN, "Chemical Burn")

        // Potion
        addPotion(RagiumPotions.FROSTBITE, "Frostbite")
        addPotion(RagiumPotions.CHEMICAL_BURN, "Chemical Burn")

        addCustomPotion("nausea", "Nausea")
        addCustomPotion("blindness", "Blindness")
        addCustomPotion("hunger", "Hunger")
        addCustomPotion("wither", "Wither")
        addCustomPotion("darkness", "Darkness")
        addCustomPotion("golden_apple", "Golden Apple")
        addCustomPotion("enchanted_golden_apple", "Enchanted Golden Apple")

        // Recipe Type
        addRecipeType(RagiumRecipeTypes.ASSEMBLING, "Assembling")
        addRecipeType(RagiumRecipeTypes.CENTRIFUGING, "Centrifuging")
        addRecipeType(RagiumRecipeTypes.COMPRESSING, "Compressing")
        addRecipeType(RagiumRecipeTypes.CRUSHING, "Crushing")
        addRecipeType(RagiumRecipeTypes.CUTTING, "Cutting")
        addRecipeType(RagiumRecipeTypes.FILLING, "Filling")
        addRecipeType(RagiumRecipeTypes.PRESSING, "Pressing")

        addRecipeType(RagiumRecipeTypes.ALLOYING, "Alloying")
        addRecipeType(RagiumRecipeTypes.DRYING, "Drying")
        addRecipeType(RagiumRecipeTypes.FREEZING, "Freezing")
        addRecipeType(RagiumRecipeTypes.MELTING, "Melting")
        addRecipeType(RagiumRecipeTypes.PYROLYZING, "Pyrolyzing")
        addRecipeType(RagiumRecipeTypes.REFINING, "Refining")

        addRecipeType(RagiumRecipeTypes.BATHING, "Chemical Bathing")
        addRecipeType(RagiumRecipeTypes.MIXING, "Mixing")
        addRecipeType(RagiumRecipeTypes.REACTING, "Chemical Reacting")

        addRecipeType(RagiumRecipeTypes.BREWING, "Brewing")
        addRecipeType(RagiumRecipeTypes.PLANTING, "Planting")
        addRecipeType(RagiumRecipeTypes.RESOURCE_PUMPING, "Resource Pumping")

        addRecipeType(RagiumRecipeTypes.ENGRAVING, "Laser Engraving")

        addRecipeType(RagiumRecipeTypes.ENCHANTING, "Enchanting")
        addRecipeType(RagiumRecipeTypes.TRANSFORMING, "Transforming")

        // Text - Lib
        add(HTCommonTranslation.ERROR, "Error")
        add(HTCommonTranslation.INFINITE, "Infinite")
        add(HTCommonTranslation.NONE, "None")
        add(HTCommonTranslation.EMPTY, "Empty")

        add(HTCommonTranslation.DOWN, "Down")
        add(HTCommonTranslation.UP, "Up")
        add(HTCommonTranslation.NORTH, "North")
        add(HTCommonTranslation.SOUTH, "South")
        add(HTCommonTranslation.WEST, "West")
        add(HTCommonTranslation.EAST, "East")

        add(HTCommonTranslation.INVALID_PACKET_S2C, $$"Invalid packet received from server side: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_C2S, $$"Invalid packet received from client side: %1$s")

        add(HTCommonTranslation.PROGRESS, $$"Progress: %1$s %%")
        add(HTCommonTranslation.SECONDS, $$"%1$s sec (%2$s ticks)")

        add(HTCommonTranslation.BIOME, $$"Biome: %1$s")
        add(HTCommonTranslation.ENCHANTMENT_POWER, $$"Enchantment Power: %1$s")

        add(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"Always has at least %1$s")
        add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION, "Press Shift to show description")
        add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS, "Press Ctrl to show details")

        add(HTCommonTranslation.DATAPACK_WIP, "Enables work in progress contents")
        // Text - Ragium
        add(RagiumTranslation.RAGIUM, "Ragium")

        add(RagiumTranslation.CONFIG_ENERGY_CAPACITY, "Energy Capacity")
        add(RagiumTranslation.CONFIG_ENERGY_RATE, "Energy Rate")

        add(RagiumTranslation.TOOLTIPS_SULFUR, "Dropped from Coal Ores")
        add(RagiumTranslation.TOOLTIPS_CINNABAR, "Dropped from Redstone Ores")
        add(RagiumTranslation.TOOLTIPS_APATITE, "Dropped from Lapis Lazuri Ores")
        add(RagiumTranslation.TOOLTIPS_CARBON, "Dropped from Diamond Ores")
        add(RagiumTranslation.TOOLTIPS_FLUORITE, "Dropped from Quartz Ores")

        add(RagiumTranslation.TOOLTIPS_MEMORY_DISC_DATA, $$"Scanned Item: %1$s")
        add(RagiumTranslation.TOOLTIPS_UPGRADE_SPEED, $$"Speed: %1$s")
        add(RagiumTranslation.TOOLTIPS_UPGRADE_EFFICIENCY, $$"Energy Efficiency: %1$s")
        add(RagiumTranslation.TOOLTIPS_UPGRADE_CAPACITY, $$"Energy Capacity: %1$s")

        // Jade Config
        addJadeConfig(HTMachineOwnerProvider.ID, "機械の所有者")
        addJadeConfig(HTMachineUpgradeProvider.ID, "機械のアップグレード")
    }
}
