package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.data.lang.HTMaterialTranslationHelper
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.common.text.RagiumTranslation
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.data.PackOutput
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

class RagiumEnglishLangProvider(output: PackOutput) : HTLangProvider.English(output, RagiumAPI.MOD_ID) {
    override fun addTranslations() {
        for (material: RagiumMaterial in RagiumMaterial.entries) {
            // Block
            for ((prefix: HTMaterialPrefix, block: HTHasTranslationKey) in RagiumBlocks.MATERIALS.column(material)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, material) ?: continue
                add(block, name)
            }
            // Item
            for ((prefix: HTMaterialPrefix, item: HTHasTranslationKey) in RagiumItems.MATERIALS.column(material)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, material) ?: continue
                add(item, name)
            }
        }

        // Block
        add(RagiumBlocks.DRYER, "Dryer")
        add(RagiumBlocks.MELTER, "Melter")
        add(RagiumBlocks.PYROLYZER, "Pyrolyzer")

        add(RagiumBlocks.BATTERY, "Variable Battery")
        add(RagiumBlocks.CRATE, "Variable Crate")
        add(RagiumBlocks.TANK, "Variable Tank")

        // Fluid
        addFluid(RagiumFluids.SALT_WATER, "Salt Water")

        addFluid(RagiumFluids.SLIME, "Slime")
        addFluid(RagiumFluids.GELLED_EXPLOSIVE, "Gelled Explosive")
        addFluid(RagiumFluids.CRUDE_BIO, "Crude Bio")
        addFluid(RagiumFluids.BIOFUEL, "Biofuel")

        addFluid(RagiumFluids.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluids.NAPHTHA, "Naphtha")
        addFluid(RagiumFluids.FUEL, "Fuel")
        addFluid(RagiumFluids.LUBRICANT, "Lubricant")

        addFluid(RagiumFluids.DESTABILIZED_RAGINITE, "Destabilized Raginite")
        addFluid(RagiumFluids.COOLANT, "Coolant")
        addFluid(RagiumFluids.CREOSOTE, "Creosote")

        // Item
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        add(RagiumItems.RAGIUM_POWDER, "Ragium Powder")

        add(RagiumItems.LOOT_TICKET, "Ragi-Ticket")
        add(RagiumItems.POTION_DROP, "Potion Drop")
        add(RagiumItems.SLOT_COVER, "Slot Cover")
        add(RagiumItems.TRADER_CATALOG, "Trader's Catalog")

        // Recipe
        add(RagiumRecipeTypes.ALLOYING, "Alloying")
        add(RagiumRecipeTypes.CRUSHING, "Crushing")
        add(RagiumRecipeTypes.CUTTING, "Cutting")
        add(RagiumRecipeTypes.DRYING, "Drying")
        add(RagiumRecipeTypes.MELTING, "Melting")
        add(RagiumRecipeTypes.PYROLYZING, "Pyrolyzing")
        add(RagiumRecipeTypes.REFINING, "Refining")

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

        add(RagiumTranslation.SLOT_COVER, "Ignored by recipes when placed in machine slot.")
        add(RagiumTranslation.TRADER_CATALOG, "Dropped from Wandering Trader. Right-click to trade with merchant.")

        add(RagiumTranslation.TOOLTIP_BLOCK_POS, $$"Position: [%1$s, %2$s, %3$s]")
        add(RagiumTranslation.TOOLTIP_CHARGE_POWER, $$"Power: %1$s")
        add(RagiumTranslation.TOOLTIP_DIMENSION, $$"Dimension: %1$s")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, $$"Loot Table: %1$s")
    }
}
