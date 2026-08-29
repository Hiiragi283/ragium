package hiiragi283.ragium.api.text

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst

enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    RAGIUM(HTConst.CONSTANTS, "mod_name"),

    // Config
    CONFIG_ENERGY_CAPACITY("config", "energy_capacity"),
    CONFIG_ENERGY_RATE("config", "energy_rate"),

    // GUI
    GUI_SLOT_BOTH(HTConst.GUI, "slot", "both"),
    GUI_SLOT_INPUT(HTConst.GUI, "slot", "input"),
    GUI_SLOT_OUTPUT(HTConst.GUI, "slot", "output"),
    GUI_SLOT_EXTRA_INPUT(HTConst.GUI, "slot", "extra_input"),
    GUI_SLOT_EXTRA_OUTPUT(HTConst.GUI, "slot", "extra_output"),
    GUI_SLOT_NONE(HTConst.GUI, "slot", "none"),

    // Blocks - Generator
    BOILER(HTConst.DESCRIPTION, RagiumConst.BOILER),

    // Blocks - Machine
    ALLOY_SMELTER(HTConst.DESCRIPTION, RagiumConst.ALLOY_SMELTER),
    ASSEMBLER(HTConst.DESCRIPTION, RagiumConst.ASSEMBLER),
    AUTO_CHISEL(HTConst.DESCRIPTION, RagiumConst.AUTO_CHISEL),
    COMPRESSOR(HTConst.DESCRIPTION, RagiumConst.COMPRESSOR),
    CRUSHER(HTConst.DESCRIPTION, RagiumConst.CRUSHER),
    CUTTING_MACHINE(HTConst.DESCRIPTION, RagiumConst.CUTTING_MACHINE),
    ELECTRIC_FURNACE(HTConst.DESCRIPTION, RagiumConst.ELECTRIC_FURNACE),

    FREEZER(HTConst.DESCRIPTION, RagiumConst.FREEZER),
    MELTER(HTConst.DESCRIPTION, RagiumConst.MELTER),
    PYROLYZER(HTConst.DESCRIPTION, RagiumConst.PYROLYZER),
    REFINERY(HTConst.DESCRIPTION, RagiumConst.REFINERY),

    BREWERY(HTConst.DESCRIPTION, RagiumConst.BREWERY),
    PLANTER(HTConst.DESCRIPTION, RagiumConst.PLANTER),

    SCANNER(HTConst.DESCRIPTION, RagiumConst.SCANNER),
    PRINTER(HTConst.DESCRIPTION, RagiumConst.PRINTER),

    CHEMICAL_BATH(HTConst.DESCRIPTION, RagiumConst.CHEMICAL_BATH),
    MIXER(HTConst.DESCRIPTION, RagiumConst.MIXER),
    WASHER(HTConst.DESCRIPTION, RagiumConst.WASHER),

    FLUID_DUPLICATOR(HTConst.DESCRIPTION, RagiumConst.FLUID_DUPLICATOR),

    // Blocks - Device
    ENCHANTER(HTConst.DESCRIPTION, RagiumConst.ENCHANTER),
    MASS_FABRICATOR(HTConst.DESCRIPTION, RagiumConst.MASS_FABRICATOR),

    // Blocks - Storages
    BATTERY(HTConst.DESCRIPTION, "battery"),
    CRATE(HTConst.DESCRIPTION, "crate"),
    TANK(HTConst.DESCRIPTION, "tank"),
    BUFFER(HTConst.DESCRIPTION, "buffer"),
    UNIVERSAL_CHEST(HTConst.DESCRIPTION, RagiumConst.UNIVERSAL_CHEST),

    // Items

    // Tooltips
    TOOLTIP_BLOCK_POS(HTConst.TOOLTIP, "block_pos"),
    TOOLTIP_CHARGE_POWER(HTConst.TOOLTIP, "blast_power"),
    TOOLTIP_DIMENSION(HTConst.TOOLTIP, "dimension"),
    TOOLTIP_LOOT_TABLE_ID(HTConst.TOOLTIP, "loot_table_id"),
    TOOLTIPS_MEMORY_DISC_DATA(HTConst.TOOLTIP, "memory_disc_data"),
    ;

    override val translationKey: String = RagiumAPI.id(path.joinToString(separator = ".")).toLanguageKey(type)
}
