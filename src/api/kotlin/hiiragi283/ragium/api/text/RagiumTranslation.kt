package hiiragi283.ragium.api.text

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.resource.toDescriptionKey
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst

enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    RAGIUM(HTConst.CONSTANTS, "mod_name"),

    // Config
    CONFIG_ENERGY_CAPACITY("config", "energy_capacity"),
    CONFIG_ENERGY_RATE("config", "energy_rate"),
    CONFIG_FLUID_FIRST_INPUT("config", "fluid", "first_input"),
    CONFIG_FLUID_SECOND_INPUT("config", "fluid", "second_input"),
    CONFIG_FLUID_THIRD_INPUT("config", "fluid", "third_input"),
    CONFIG_FLUID_FIRST_OUTPUT("config", "fluid", "first_output"),
    CONFIG_FLUID_SECOND_OUTPUT("config", "fluid", "second_output"),
    CONFIG_FLUID_THIRD_OUTPUT("config", "fluid", "third_output"),

    // GUI
    GUI_SLOT_BOTH(HTConst.GUI, "slot", "both"),
    GUI_SLOT_INPUT(HTConst.GUI, "slot", "input"),
    GUI_SLOT_OUTPUT(HTConst.GUI, "slot", "output"),
    GUI_SLOT_EXTRA_INPUT(HTConst.GUI, "slot", "extra_input"),
    GUI_SLOT_EXTRA_OUTPUT(HTConst.GUI, "slot", "extra_output"),
    GUI_SLOT_NONE(HTConst.GUI, "slot", "none"),

    // Blocks - Machine
    ALLOY_SMELTER(HTConst.DESCRIPTION, RagiumConst.ALLOY_SMELTER),
    ASSEMBLER(HTConst.DESCRIPTION, RagiumConst.ASSEMBLER),
    AUTO_CHISEL(HTConst.DESCRIPTION, RagiumConst.AUTO_CHISEL),
    CRUSHER(HTConst.DESCRIPTION, RagiumConst.CRUSHER),
    CUTTING_MACHINE(HTConst.DESCRIPTION, RagiumConst.CUTTING_MACHINE),
    ELECTRIC_FURNACE(HTConst.DESCRIPTION, RagiumConst.ELECTRIC_FURNACE),

    MELTER(HTConst.DESCRIPTION, RagiumConst.MELTER),
    PYROLYZER(HTConst.DESCRIPTION, RagiumConst.PYROLYZER),
    REFINERY(HTConst.DESCRIPTION, RagiumConst.REFINERY),

    FREEZER(HTConst.DESCRIPTION, RagiumConst.FREEZER),

    BREWERY(HTConst.DESCRIPTION, RagiumConst.BREWERY),
    MIXER(HTConst.DESCRIPTION, RagiumConst.MIXER),
    WASHER(HTConst.DESCRIPTION, RagiumConst.WASHER),

    // Blocks - Device
    PLANTER(HTConst.DESCRIPTION, "planter"),
    ENCHANTER(HTConst.DESCRIPTION, "enchanter"),

    // Blocks - Storages
    BATTERY(HTConst.DESCRIPTION, "battery"),
    CRATE(HTConst.DESCRIPTION, "crate"),
    TANK(HTConst.DESCRIPTION, "tank"),
    BUFFER(HTConst.DESCRIPTION, "buffer"),
    UNIVERSAL_CHEST(HTConst.DESCRIPTION, RagiumConst.UNIVERSAL_CHEST),

    // Items
    BLUEPRINT(HTConst.DESCRIPTION, "blueprint"),
    BLUEPRINT_NUMBER(HTConst.DESCRIPTION, "blueprint", "number"),

    // Tooltips
    TOOLTIP_BLOCK_POS(HTConst.TOOLTIP, "block_pos"),
    TOOLTIP_CHARGE_POWER(HTConst.TOOLTIP, "blast_power"),
    TOOLTIP_DIMENSION(HTConst.TOOLTIP, "dimension"),
    TOOLTIP_LOOT_TABLE_ID("tooltip", "loot_table_id"),
    ;

    override val translationKey: String = RagiumAPI.id(path.joinToString(separator = ".")).toDescriptionKey(type)
}
