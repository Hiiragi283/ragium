package hiiragi283.ragium.common.text

import hiiragi283.core.api.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import net.minecraft.Util

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

    // Blocks - Processors
    ALLOY_SMELTER("description", "alloy_smelter"),
    BLOCK_BREAKER("description", "block_breaker"),
    CUTTING_MACHINE("description", "cutting_machine"),
    COMPRESSOR("description", "compressor"),
    ELECTRIC_FURNACE("description", "electric_smelter"),
    EXTRACTOR("description", "extractor"),
    CRUSHER("description", "crusher"),

    DRYER("description", RagiumConst.DRYER),
    MELTER("description", RagiumConst.MELTER),
    MIXER("description", "mixer"),
    PYROLYZER("description", RagiumConst.PYROLYZER),
    REFINERY("description", "refinery"),

    BREWERY("description", "brewery"),
    MULTI_SMELTER("description", "multi_smelter"),
    PLANTER("description", "planter"),

    ENCHANTER("description", "enchanter"),
    MOB_CRUSHER("description", "mob_crusher"),
    SIMULATOR("description", "simulator"),

    // Blocks - Storages
    BATTERY("description", "battery"),
    CRATE("description", "crate"),
    TANK("description", "tank"),
    BUFFER("description", "buffer"),
    UNIVERSAL_CHEST("description", RagiumConst.UNIVERSAL_CHEST),

    // Items - Utilities
    SLOT_COVER("description", "slot_cover"),
    TRADER_CATALOG("description", "trader_catalog"),

    // Tooltips
    TOOLTIP_BLOCK_POS("tooltip", "block_pos"),
    TOOLTIP_CHARGE_POWER("tooltip", "blast_power"),
    TOOLTIP_DIMENSION("tooltip", "dimension"),
    TOOLTIP_LOOT_TABLE_ID("tooltip", "loot_table_id"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, RagiumAPI.id(path.joinToString(separator = ".")))
}
