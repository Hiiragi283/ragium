package hiiragi283.ragium.api.text

import hiiragi283.core.api.resource.toDescriptionKey
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst

enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    RAGIUM("constants", "mod_name"),

    // Config
    CONFIG_ENERGY_CAPACITY("config", "energy_capacity"),
    CONFIG_ENERGY_RATE("config", "energy_rate"),
    CONFIG_FLUID_FIRST_INPUT("config", "fluid", "first_input"),
    CONFIG_FLUID_SECOND_INPUT("config", "fluid", "second_input"),
    CONFIG_FLUID_FIRST_OUTPUT("config", "fluid", "first_output"),
    CONFIG_FLUID_SECOND_OUTPUT("config", "fluid", "second_output"),
    CONFIG_FLUID_THIRD_OUTPUT("config", "fluid", "third_output"),

    // GUI
    GUI_SLOT_BOTH("gui", "slot", "both"),
    GUI_SLOT_INPUT("gui", "slot", "input"),
    GUI_SLOT_OUTPUT("gui", "slot", "output"),
    GUI_SLOT_EXTRA_INPUT("gui", "slot", "extra_input"),
    GUI_SLOT_EXTRA_OUTPUT("gui", "slot", "extra_output"),
    GUI_SLOT_NONE("gui", "slot", "none"),

    // Blocks - Machine
    ALLOY_SMELTER("description", RagiumConst.ALLOY_SMELTER),
    ASSEMBLER("description", RagiumConst.ASSEMBLER),
    CRUSHER("description", RagiumConst.CRUSHER),
    CUTTING_MACHINE("description", RagiumConst.CUTTING_MACHINE),
    ELECTRIC_FURNACE("description", RagiumConst.ELECTRIC_FURNACE),
    FORMING_PRESS("description", RagiumConst.FORMING_PRESS),

    MELTER("description", RagiumConst.MELTER),
    PYROLYZER("description", RagiumConst.PYROLYZER),
    REFINERY("description", RagiumConst.REFINERY),
    SOLIDIFIER("description", RagiumConst.SOLIDIFIER),

    BREWERY("description", RagiumConst.BREWERY),
    MIXER("description", RagiumConst.MIXER),
    WASHER("description", RagiumConst.WASHER),

    // Blocks - Device
    FERMENTER("description", RagiumConst.FERMENTER),
    PLANTER("description", "planter"),
    ENCHANTER("description", "enchanter"),

    // Blocks - Storages
    BATTERY("description", "battery"),
    CRATE("description", "crate"),
    TANK("description", "tank"),
    BUFFER("description", "buffer"),
    UNIVERSAL_CHEST("description", RagiumConst.UNIVERSAL_CHEST),

    // Items

    // Tooltips
    TOOLTIP_BLOCK_POS("tooltip", "block_pos"),
    TOOLTIP_CHARGE_POWER("tooltip", "blast_power"),
    TOOLTIP_DIMENSION("tooltip", "dimension"),
    TOOLTIP_LOOT_TABLE_ID("tooltip", "loot_table_id"),
    TOOLTIP_UPGRADE_EXCLUSIVE("tooltip", "upgrade", "exclusive"),
    TOOLTIP_UPGRADE_TARGET("tooltip", "upgrade", "target"),
    ;

    override val translationKey: String = RagiumAPI.id(path.joinToString(separator = ".")).toDescriptionKey(type)
}
