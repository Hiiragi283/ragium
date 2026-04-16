package hiiragi283.ragium.data.client

import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLangTypes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.data.server.advancement.RagiumAdvancementKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput

class RagiumEnglishLangProvider(output: PackOutput) : HTLangProvider(output, RagiumAPI.MOD_ID, HTLangTypes.EN_US) {
    override fun addTranslations() {
        RagiumCommonTranslation.addTranslations(this)

        // Advancement
        add(RagiumAdvancementKeys.ROOT, "Ragium", "Welcome to Ragium!")

        add(RagiumAdvancementKeys.RAGI_ALLOY, "0xFF003f", "Get Ragi-Alloy Ingot")
        add(RagiumAdvancementKeys.ALLOY_SMELTER, "Al-Chemistry", "Get Alloy Smelter")

        add(RagiumAdvancementKeys.THERMOMETER, "Heat And Cool", "Get Thermometer")
        add(RagiumAdvancementKeys.ADVANCED_RAGI_ALLOY, "Maybe Red", "Get Advanced Ragi-Alloy Ingot")
        add(RagiumAdvancementKeys.REFINERY, "A BC is DEFinitely Good", "Get Refinery")
        add(RagiumAdvancementKeys.PLASTIC, "Plus-TiC", "Get Plastic Sheet")
        add(RagiumAdvancementKeys.REFINED_SILICON, "Refined Silicon", "Get Silicon Dust")
        add(RagiumAdvancementKeys.PYROLYZER, "Electric Coke Oven", "Get Pyrolyzer")
        add(RagiumAdvancementKeys.CRIMSON_CRYSTAL, "Chao!", "Get Crimson Crystal")
        add(RagiumAdvancementKeys.WARPED_CRYSTAL, "Stabilized Warp", "Get Warped Crystal")

        add(RagiumAdvancementKeys.RAGI_CRYSTAL, "Not a Energium", "Get Ragi-Crystal")
        add(RagiumAdvancementKeys.STAINLESS_STEEL, "Not a HV Age", "Get Stainless Steel Ingot")
        add(RagiumAdvancementKeys.ELECTRIC_CIRCUIT, "PCB: Pretty Cool Board", "Get Electric Circuit")
        add(RagiumAdvancementKeys.BREWERY, "Automatic Brewing", "Get Brewery")
        add(RagiumAdvancementKeys.MIXER, "Best Match!", "Get Mixer")
        // Block
        add(RagiumBlocks.MEAT_BLOCK, "Bone with Meat")
        add(RagiumBlocks.COOKED_MEAT_BLOCK, "Bone with Cooked Meat")

        add(RagiumBlocks.ALLOY_SMELTER, "Alloy Smelter")
        add(RagiumBlocks.ASSEMBLER, "Assembler")
        add(RagiumBlocks.AUTO_CHISEL, "Auto Chisel")
        add(RagiumBlocks.CRUSHER, "Crusher")
        add(RagiumBlocks.CUTTING_MACHINE, "Cutting Machine")
        add(RagiumBlocks.ELECTRIC_FURNACE, "Electric Furnace")
        add(RagiumBlocks.PLANTER, "Planter")

        add(RagiumBlocks.FREEZER, "Freezer")
        add(RagiumBlocks.MELTER, "Melter")
        add(RagiumBlocks.PYROLYZER, "Pyrolyzer")
        add(RagiumBlocks.REFINERY, "Refinery")

        add(RagiumBlocks.BREWERY, "Brewery")
        add(RagiumBlocks.MIXER, "Mixer")
        add(RagiumBlocks.WASHER, "Washer")

        add(RagiumBlocks.ENCHANTER, "Enchanter")

        add(RagiumBlocks.UNIVERSAL_CHEST, "Universal Chest")

        add(RagiumBlocks.BATTERY, "Variable Battery")
        add(RagiumBlocks.CRATE, "Variable Crate")
        add(RagiumBlocks.TANK, "Variable Tank")

        add(RagiumBlocks.VOID_TANK, "Void Tank")

        add(RagiumBlocks.IMITATION_SPAWNER, "Imitation Spawner")

        add(RagiumBlocks.CREATIVE_BATTERY, "Creative Battery")
        add(RagiumBlocks.CREATIVE_CRATE, "Creative Crate")
        add(RagiumBlocks.CREATIVE_TANK, "Creative Tank")
        // Fluid
        addFluid(RagiumFluids.HYDROGEN, "Hydrogen")
        addFluid(RagiumFluids.STEAM, "Steam")

        addFluid(RagiumFluids.OXYGEN, "Oxygen")

        addFluid(RagiumFluids.CREOSOTE, "Creosote")
        addFluid(RagiumFluids.SYNTHETIC_GAS, "Synthetic Gas")
        addFluid(RagiumFluids.SYNTHETIC_OIL, "Synthetic Oil")

        addFluid(RagiumFluids.METHANE, "Methane")
        addFluid(RagiumFluids.CRUDE_BIO, "Crude Bio")
        addFluid(RagiumFluids.ETHANOL, "Ethanol")
        addFluid(RagiumFluids.BIOFUEL, "Biofuel")

        addFluid(RagiumFluids.NITROGEN, "Nitrogen")
        addFluid(RagiumFluids.LIQUID_NITROGEN, "Liquid Nitrogen")

        addFluid(RagiumFluids.NAOH_SOLUTION, "Slime Solution")

        addFluid(RagiumFluids.MERCURY, "Mercury")

        addFluid(RagiumFluids.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluids.NAPHTHA, "Naphtha")
        addFluid(RagiumFluids.FUEL, "Fuel")

        addFluid(RagiumFluids.NITROGEN_DIOXIDE, "Nitrogen Dioxide")
        addFluid(RagiumFluids.AMMONIA, "Ammonia")
        addFluid(RagiumFluids.NITRIC_ACID, "Nitric Acid")

        addFluid(RagiumFluids.SULFUR_DIOXIDE, "Sulfur Dioxide")
        addFluid(RagiumFluids.SULFUR_TRIOXIDE, "Sulfur Trioxide")
        addFluid(RagiumFluids.SULFURIC_ACID, "Sulfuric Acid")

        addFluid(RagiumFluids.HELIUM, "Helium")
        // Item
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        add(RagiumItems.CARBON_COMPOUND, "Carbon Compound")
        add(RagiumItems.CRYO_CHARGE, "Cryo-Charge")

        add(RagiumItems.CRUDE_SILICON, "Crude Silicon")
        add(RagiumItems.GLYCEROL_DROP, "Glycerol")
        add(RagiumItems.NITROGLYCERIN, "Nitroglycerin")
        add(RagiumItems.NITROCELLULOSE, "Nitrocellulose")
        add(RagiumItems.SMOKELESS_POWDER, "Smokeless Powder")

        add(RagiumItems.MERCURY_BOTTLE, "Mercury Bottle")
        add(RagiumItems.THERMOMETER, "Thermometer")
        add(RagiumItems.SILICON_WAFER, "Silicon Wafer")
        add(RagiumItems.CIRCUIT_CHIP, "Circuit Chip")
        add(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        add(RagiumItems.ELECTRIC_CIRCUIT, "Electric Circuit")

        add(RagiumItems.ARTIFICIAL_ARTIFACT, "Artificial Artifact")

        add(RagiumItems.EMPTY_CAN, "Empty Can")

        add(RagiumItems.BLANK_DISC, "Blank Disc")
        add(RagiumItems.LOCATION_TICKET, "Location Ticket")
        add(RagiumItems.LOOT_TICKET, "Ragi-Ticket")

        add(RagiumItems.RAGI_MATTER, "Ragi-Matter")
        // Recipe
        add(RagiumRecipeLookups.ALLOYING, "Alloying")
        add(RagiumRecipeLookups.ASSEMBLING, "Assembling")
        add(RagiumRecipeLookups.CUTTING, "Cutting")
        add(RagiumRecipeLookups.PLANTING, "Planting")

        add(RagiumRecipeLookups.FREEZING, "Freezing")
        add(RagiumRecipeLookups.MELTING, "Melting")
        add(RagiumRecipeLookups.PYROLYZING, "Pyrolyzing")
        add(RagiumRecipeLookups.REFINING, "Refining")

        add(RagiumRecipeLookups.CHEMICAL_WASHING, "Chemical Washing")
        add(RagiumRecipeLookups.ELECTROLYZING, "Electrolyzing")
        add(RagiumRecipeLookups.MIXING, "Mixing")
        add(RagiumRecipeLookups.WASHING, "Washing")

        add(RagiumRecipeLookups.ENCHANTING, "Enchanting")

        // Tag
        add(RagiumTags.Items.FOODS_CAN, "Canned Foods")

        add(RagiumTags.Items.GENERATOR_UPGRADABLE, "Generators")
        add(RagiumTags.Items.PROCESSOR_UPGRADABLE, "Processors")
        add(RagiumTags.Items.MACHINE_UPGRADABLE, "Machines")
        add(RagiumTags.Items.DEVICE_UPGRADABLE, "Devices")
        add(RagiumTags.Items.EXTRA_VOIDING_UPGRADABLE, "Processors with Extra Output")
        add(RagiumTags.Items.EFFICIENT_CRUSHING_UPGRADABLE, "Pulverizer or Crusher")
        add(RagiumTags.Items.ENERGY_CAPACITY_UPGRADABLE, "Energy Storage")
        add(RagiumTags.Items.FLUID_CAPACITY_UPGRADABLE, "Fluid Storage")
        add(RagiumTags.Items.ITEM_CAPACITY_UPGRADABLE, "Item Storage")
        add(RagiumTags.Items.SMELTING_UPGRADABLE, "Electric / Multi Smelter")

        add(RagiumTags.Items.EXTRACTOR_EXCLUSIVE, "Upgrades for Extractor")
        add(RagiumTags.Items.SMELTER_EXCLUSIVE, "Upgrades for Smelters")
        // Text
        add(RagiumTranslation.RAGIUM, "Ragium")

        add(RagiumTranslation.CONFIG_ENERGY_CAPACITY, "Energy Capacity")
        add(RagiumTranslation.CONFIG_ENERGY_RATE, "Energy Rate")
        add(RagiumTranslation.CONFIG_FLUID_FIRST_INPUT, "First Input Tank Capacity")
        add(RagiumTranslation.CONFIG_FLUID_SECOND_INPUT, "Second Input Tank Capacity")
        add(RagiumTranslation.CONFIG_FLUID_THIRD_INPUT, "Third Input Tank Capacity")
        add(RagiumTranslation.CONFIG_FLUID_FIRST_OUTPUT, "First Output Tank Capacity")
        add(RagiumTranslation.CONFIG_FLUID_SECOND_OUTPUT, "Second Output Tank Capacity")
        add(RagiumTranslation.CONFIG_FLUID_THIRD_OUTPUT, "Third Output Tank Capacity")

        add(RagiumTranslation.GUI_SLOT_BOTH, "%s: Both")
        add(RagiumTranslation.GUI_SLOT_INPUT, "%s: Input")
        add(RagiumTranslation.GUI_SLOT_OUTPUT, "%s: Output")
        add(RagiumTranslation.GUI_SLOT_EXTRA_INPUT, "%s: Extra Input")
        add(RagiumTranslation.GUI_SLOT_EXTRA_OUTPUT, "%s: Extra Output")
        add(RagiumTranslation.GUI_SLOT_NONE, "%s: None")

        add(RagiumTranslation.ALLOY_SMELTER, "A machine which combines multiple items into one.")
        add(RagiumTranslation.CRUSHER, "A machine which crushes item into dust.")
        add(RagiumTranslation.CUTTING_MACHINE, "A machine which converts log or wooden items into planks.")
        add(RagiumTranslation.ELECTRIC_FURNACE, "A machine which smelts item by using energy.")

        add(RagiumTranslation.MELTER, "A machine which melts item or heats up fluid.")
        add(RagiumTranslation.PYROLYZER, "A machine which converts log or coal into charcoal or coal coke.")

        add(RagiumTranslation.FREEZER, "A machine which freeze item or cools down fluid.")

        add(RagiumTranslation.MIXER, "A machine which combines multiple items or fluids.")
        add(RagiumTranslation.WASHER, "A machine which producing item from crushed ore and fluid.")

        add(RagiumTranslation.BATTERY, "A energy storage which capacity is extendable by crafting with others.")
        add(RagiumTranslation.CRATE, "A item storage which capacity is extendable by crafting with others.")
        add(RagiumTranslation.TANK, "A fluid storage which capacity is extendable by crafting with others.")
        add(RagiumTranslation.BUFFER, "A combined storage with 9 slots, 3 tanks, and 1 battery.")
        add(RagiumTranslation.UNIVERSAL_CHEST, "A chest which shares its containment with the same color.")

        add(RagiumTranslation.TOOLTIP_BLOCK_POS, $$"Position: [%1$s, %2$s, %3$s]")
        add(RagiumTranslation.TOOLTIP_CHARGE_POWER, $$"Power: %1$s")
        add(RagiumTranslation.TOOLTIP_DIMENSION, $$"Dimension: %1$s")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, $$"Loot Table: %1$s")
    }
}
