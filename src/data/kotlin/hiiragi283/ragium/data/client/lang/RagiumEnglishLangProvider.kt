package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.common.upgrade.RagiumUpgradeKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.data.PackOutput

class RagiumEnglishLangProvider(output: PackOutput) : HTLangProvider.English(output, RagiumAPI.MOD_ID) {
    override fun addTranslations() {
        addMaterials()
        RagiumCommonTranslation.addTranslations(this)

        // Block
        add(RagiumBlocks.MEAT_BLOCK, "Bone with Meat")
        add(RagiumBlocks.COOKED_MEAT_BLOCK, "Bone with Cooked Meat")

        add(RagiumBlocks.ALLOY_SMELTER, "Alloy Smelter")
        add(RagiumBlocks.ASSEMBLER, "Assembler")
        add(RagiumBlocks.CRUSHER, "Crusher")
        add(RagiumBlocks.CUTTING_MACHINE, "Cutting Machine")
        add(RagiumBlocks.ELECTRIC_FURNACE, "Electric Furnace")
        add(RagiumBlocks.FORMING_PRESS, "Forming Press")

        add(RagiumBlocks.MELTER, "Melter")
        add(RagiumBlocks.PYROLYZER, "Pyrolyzer")
        add(RagiumBlocks.REFINERY, "Refinery")
        add(RagiumBlocks.SOLIDIFIER, "Solidifier")

        add(RagiumBlocks.MIXER, "Mixer")
        add(RagiumBlocks.WASHER, "Washer")

        add(RagiumBlocks.FERMENTER, "Fermenter")
        add(RagiumBlocks.PLANTER, "Planter")

        add(RagiumBlocks.ENCHANTER, "Enchanter")

        add(RagiumBlocks.BATTERY, "Variable Battery")
        add(RagiumBlocks.CRATE, "Variable Crate")
        add(RagiumBlocks.TANK, "Variable Tank")
        add(RagiumBlocks.RESONANT_INTERFACE, "Resonant Interface")
        add(RagiumBlocks.UNIVERSAL_CHEST, "Universal Chest")

        add(RagiumBlocks.IMITATION_SPAWNER, "Imitation Spawner")

        add(RagiumBlocks.CREATIVE_BATTERY, "Creative Battery")
        add(RagiumBlocks.CREATIVE_CRATE, "Creative Crate")
        add(RagiumBlocks.CREATIVE_TANK, "Creative Tank")
        // Fluid
        addFluid(RagiumFluids.AIR, "Air")
        addFluid(RagiumFluids.HYDROGEN, "Hydrogen")
        addFluid(RagiumFluids.CARBON_DIOXIDE, "Carbon Dioxide")
        addFluid(RagiumFluids.NITROGEN, "Nitrogen")
        addFluid(RagiumFluids.AMMONIA, "Ammonia")
        addFluid(RagiumFluids.OXYGEN, "Oxygen")

        addFluid(RagiumFluids.NITRIC_ACID, "Nitric Acid")
        addFluid(RagiumFluids.MIXTURE_ACID, "Mixture Acid")
        addFluid(RagiumFluids.SULFURIC_ACID, "Sulfuric Acid")

        addFluid(RagiumFluids.CREOSOTE, "Creosote")
        addFluid(RagiumFluids.SYNTHETIC_GAS, "Synthetic Gas")
        addFluid(RagiumFluids.SYNTHETIC_OIL, "Synthetic Oil")

        addFluid(RagiumFluids.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluids.NAPHTHA, "Naphtha")
        addFluid(RagiumFluids.RESIDUE_OIL, "Residue Oil")

        addFluid(RagiumFluids.METHANE, "Methane")
        addFluid(RagiumFluids.ETHYLENE, "Ethylene")
        addFluid(RagiumFluids.BUTADIENE, "Butadiene")

        addFluid(RagiumFluids.METHANOL, "Methanol")
        addFluid(RagiumFluids.ETHANOL, "Ethanol")

        addFluid(RagiumFluids.FUEL, "Fuel")
        addFluid(RagiumFluids.LUBRICANT, "Lubricant")

        addFluid(RagiumFluids.SUNFLOWER_OIL, "Sunflower Oil")
        addFluid(RagiumFluids.BIOFUEL, "Biofuel")

        addFluid(RagiumFluids.COOLANT, "Coolant")
        addFluid(RagiumFluids.MOLTEN_RAGINITE, "Destabilized Raginite")
        addFluid(RagiumFluids.RAGI_MATTER, "Ragi-Matter")

        // Item
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        add(RagiumItems.CIRCUIT_BOARD, "Circuit Board")
        add(RagiumItems.PLATED_CIRCUIT_BOARD, "Plated Circuit Board")
        add(RagiumItems.PRINTED_CIRCUIT_BOARD, "Printed Circuit Board")
        add(RagiumItems.ELECTRIC_CIRCUIT, "Electric Circuit")

        add(RagiumItems.MOLASSES, "Molasses")
        add(RagiumItems.EMPTY_CAN, "Empty Can")

        add(RagiumItems.BLANK_DISC, "Blank Disc")
        add(RagiumItems.LOCATION_TICKET, "Location Ticket")
        add(RagiumItems.LOOT_TICKET, "Ragi-Ticket")
        add(RagiumItems.POTION_DROP, "Potion Drop")

        // Recipe
        add(RagiumRecipeTypes.ALLOYING, "Alloying")
        add(RagiumRecipeTypes.ASSEMBLING, "Assembling")
        add(RagiumRecipeTypes.CRUSHING, "Crushing")
        add(RagiumRecipeTypes.CUTTING, "Cutting")
        add(RagiumRecipeTypes.PRESSING, "Pressing")

        add(RagiumRecipeTypes.MELTING, "Melting")
        add(RagiumRecipeTypes.PYROLYZING, "Pyrolyzing")
        add(RagiumRecipeTypes.REFINING, "Refining")
        add(RagiumRecipeTypes.SOLIDIFYING, "Solidifying")

        add(RagiumRecipeTypes.BATHING, "Bathing")
        add(RagiumRecipeTypes.MIXING, "Mixing")
        add(RagiumRecipeTypes.REACTING, "Chemical Reacting")
        add(RagiumRecipeTypes.WASHING, "Washing")

        add(RagiumRecipeTypes.COMPRESSING, "Implosion Compressing")
        add(RagiumRecipeTypes.ENCHANTING, "Enchanting")
        add(RagiumRecipeTypes.PLANTING, "Planting")

        // Tag
        add(RagiumTags.Items.FOODS_CAN, "Canned Foods")
        add(RagiumTags.Items.MOLDS, "Molds")

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
        add(RagiumTranslation.CONFIG_FLUID_FIRST_OUTPUT, "First Output Tank Capacity")
        add(RagiumTranslation.CONFIG_FLUID_SECOND_OUTPUT, "Second Output Tank Capacity")
        add(RagiumTranslation.CONFIG_FLUID_THIRD_OUTPUT, "Third Output Tank Capacity")

        add(RagiumTranslation.GUI_SLOT_BOTH, "%s: Both")
        add(RagiumTranslation.GUI_SLOT_INPUT, "%s: Input")
        add(RagiumTranslation.GUI_SLOT_OUTPUT, "%s: Output")
        add(RagiumTranslation.GUI_SLOT_EXTRA_INPUT, "%s: Extra Input")
        add(RagiumTranslation.GUI_SLOT_EXTRA_OUTPUT, "%s: Extra Output")
        add(RagiumTranslation.GUI_SLOT_NONE, "%s: None")

        add(RagiumTranslation.MELTER, "A machine which melts item into fluid.")
        add(RagiumTranslation.PYROLYZER, "A machine which converts ingredients into others by heating.")

        add(RagiumTranslation.BATTERY, "A energy storage which capacity is extendable by upgrade.")
        add(RagiumTranslation.CRATE, "A item storage which capacity is extendable by upgrade.")
        add(RagiumTranslation.TANK, "A fluid storage which capacity is extendable by upgrade.")
        add(RagiumTranslation.BUFFER, "A combined storage with 9 slots, 3 tanks, and 1 battery.")
        add(RagiumTranslation.UNIVERSAL_CHEST, "A chest which shares its containment with the same color.")

        add(RagiumTranslation.TOOLTIP_BLOCK_POS, $$"Position: [%1$s, %2$s, %3$s]")
        add(RagiumTranslation.TOOLTIP_CHARGE_POWER, $$"Power: %1$s")
        add(RagiumTranslation.TOOLTIP_DIMENSION, $$"Dimension: %1$s")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, $$"Loot Table: %1$s")
        add(RagiumTranslation.TOOLTIP_UPGRADE_EXCLUSIVE, $$"Conflicting Upgrades: %1$s")
        add(RagiumTranslation.TOOLTIP_UPGRADE_TARGET, $$"Upgrade Targets: %1$s")
        // Upgrade
        add(HTUpgradeKeys.BASE_MULTIPLIER, $$"- Base Multiplier: %1$s")
        add(HTUpgradeKeys.IS_CREATIVE, "- Creative")

        add(HTUpgradeKeys.ENERGY_EFFICIENCY, $$"- Energy Efficiency: %1$s")
        add(HTUpgradeKeys.ENERGY_GENERATION, $$"- Energy Generation: %1$s")
        add(HTUpgradeKeys.SPEED, $$"- Speed: %1$s")

        add(HTUpgradeKeys.ENERGY_CAPACITY, $$"- Energy Capacity: %1$s")
        add(HTUpgradeKeys.FLUID_CAPACITY, $$"- Fluid Capacity: %1$s")
        add(HTUpgradeKeys.ITEM_CAPACITY, $$"- Item Capacity: %1$s")

        add(RagiumUpgradeKeys.BLASTING, "- Only process Blasting Recipes")
        add(RagiumUpgradeKeys.SMOKING, "- Only process Smoking Recipes")
        add(RagiumUpgradeKeys.VOID_EXTRA, "- Extra output disabled")
        add(RagiumUpgradeKeys.USE_LUBRICANT, "- Use lubricant per operation")
    }
}
