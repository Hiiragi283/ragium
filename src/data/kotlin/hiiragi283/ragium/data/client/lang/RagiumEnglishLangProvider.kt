package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumTags
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.common.text.RagiumTranslation
import hiiragi283.ragium.common.upgrade.RagiumUpgradeKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.data.PackOutput

class RagiumEnglishLangProvider(output: PackOutput) : HTLangProvider.English(output, RagiumAPI.MOD_ID) {
    override fun addTranslations() {
        RagiumCommonTranslation.addTranslations(this)

        // Block
        add(RagiumBlocks.MEAT_BLOCK, "Bone with Meat")
        add(RagiumBlocks.COOKED_MEAT_BLOCK, "Bone with Cooked Meat")

        add(RagiumBlocks.ALLOY_SMELTER, "Alloy Smelter")
        add(RagiumBlocks.CRUSHER, "Crusher")
        add(RagiumBlocks.CUTTING_MACHINE, "Cutting Machine")

        add(RagiumBlocks.DRYER, "Dryer")
        add(RagiumBlocks.MELTER, "Melter")
        add(RagiumBlocks.MIXER, "Mixer")
        add(RagiumBlocks.PYROLYZER, "Pyrolyzer")

        add(RagiumBlocks.PLANTER, "Planter")

        add(RagiumBlocks.BATTERY, "Variable Battery")
        add(RagiumBlocks.CRATE, "Variable Crate")
        add(RagiumBlocks.TANK, "Variable Tank")
        add(RagiumBlocks.RESONANT_INTERFACE, "Resonant Interface")
        add(RagiumBlocks.UNIVERSAL_CHEST, "Universal Chest")

        add(RagiumBlocks.IMITATION_SPAWNER, "Imitation Spawner")
        // Fluid
        addFluid(RagiumFluids.SLIME, "Slime")
        addFluid(RagiumFluids.GELLED_EXPLOSIVE, "Gelled Explosive")
        addFluid(RagiumFluids.CRUDE_BIO, "Crude Bio")
        addFluid(RagiumFluids.ETHANOL, "Ethanol")
        addFluid(RagiumFluids.BIOFUEL, "Biofuel")
        addFluid(RagiumFluids.FERTILIZER, "Liquid Fertilizer")

        addFluid(RagiumFluids.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluids.NAPHTHA, "Naphtha")
        addFluid(RagiumFluids.FUEL, "Fuel")
        addFluid(RagiumFluids.LUBRICANT, "Lubricant")

        addFluid(RagiumFluids.COOLANT, "Coolant")
        addFluid(RagiumFluids.CREOSOTE, "Creosote")

        // Item
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        add(RagiumItems.TAR, "Tar")

        add(RagiumItems.FISH_CAN, "Fish Can")
        add(RagiumItems.FRUIT_CAN, "Fruit Can")
        add(RagiumItems.MEAT_CAN, "Meat Can")
        add(RagiumItems.SOUP_CAN, "Soup Can")

        add(RagiumItems.BLANK_DISC, "Blank Disc")
        add(RagiumItems.LOCATION_TICKET, "Location Ticket")
        add(RagiumItems.LOOT_TICKET, "Ragi-Ticket")
        add(RagiumItems.POTION_DROP, "Potion Drop")

        // Recipe
        add(RagiumRecipeTypes.ALLOYING, "Alloying")
        add(RagiumRecipeTypes.CRUSHING, "Crushing")
        add(RagiumRecipeTypes.CUTTING, "Cutting")
        add(RagiumRecipeTypes.DRYING, "Drying")
        add(RagiumRecipeTypes.MELTING, "Melting")
        add(RagiumRecipeTypes.MIXING, "Mixing")
        add(RagiumRecipeTypes.PLANTING, "Planting")
        add(RagiumRecipeTypes.PRESSING, "Pressing")
        add(RagiumRecipeTypes.PYROLYZING, "Pyrolyzing")
        add(RagiumRecipeTypes.REFINING, "Refining")
        add(RagiumRecipeTypes.SIMULATING, "Simulating")
        add(RagiumRecipeTypes.SOLIDIFYING, "Solidifying")

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

        add(RagiumTranslation.GUI_SLOT_BOTH, "Both")
        add(RagiumTranslation.GUI_SLOT_INPUT, "Input")
        add(RagiumTranslation.GUI_SLOT_OUTPUT, "Output")
        add(RagiumTranslation.GUI_SLOT_EXTRA_INPUT, "Extra Input")
        add(RagiumTranslation.GUI_SLOT_EXTRA_OUTPUT, "Extra Output")
        add(RagiumTranslation.GUI_SLOT_NONE, "None")

        add(RagiumTranslation.DRYER, "A machine which converts ingredients into others by drying.")
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
