package hiiragi283.ragium.common.text

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.text.HTTranslation
import net.minecraft.Util

/**
 * @see mekanism.common.MekanismLang
 * @see hiiragi283.ragium.setup.RagiumBlockTypes
 */
enum class RagiumCommonTranslation(type: String, vararg path: String) : HTTranslation {
    // Blocks
    WARPED_WART("description", "warped_wart"),
    EXP_BERRIES("description", "exp_berries"),

    QUARTZ_GLASS("description", "quartz_glass"),
    OBSIDIAN_GLASS("description", "obsidian_glass"),
    CRIMSON_GLASS("description", "crimson_crystal_glass"),
    WARPED_GLASS("description", "warped_crystal_glass"),

    // Blocks - Generators
    THERMAL_GENERATOR("description", "thermal_generator"),
    COMBUSTION_GENERATOR("description", "combustion_generator"),
    SOLAR_PANEL_UNIT("description", "solar_panel_unit"),
    SOLAR_PANEL_CONTROLLER("description", "solar_panel_controller"),
    ENCHANTMENT_GENERATOR("description", "enchantment_generator"),
    NUCLEAR_REACTOR("description", "nuclear_reactor"),

    // Blocks - Processors
    ALLOY_SMELTER("description", "alloy_smelter"),
    BLOCK_BREAKER("description", "block_breaker"),
    CUTTING_MACHINE("description", "cutting_machine"),
    COMPRESSOR("description", "compressor"),
    EXTRACTOR("description", "extractor"),
    PULVERIZER("description", "pulverizer"),

    CRUSHER("description", "crusher"),
    MELTER("description", "melter"),
    MIXER("description", "mixer"),
    REFINERY("description", "refinery"),
    WASHER("description", "washer"),

    BREWERY("description", "brewery"),
    MULTI_SMELTER("description", "multi_smelter"),
    PLANTER("description", "planter"),

    ENCHANT_COPIER("description", "enchantment_copier"),
    ENCHANTER("description", "enchanter"),
    SIMULATOR("description", "simulator"),

    // Blocks - Devices
    FLUID_COLLECTOR("description", "water_collector"),
    ITEM_COLLECTOR("description", "item_collector"),

    DIM_ANCHOR("description", "dimensional_anchor"),
    ENI("description", "energy_network_interface"),

    CEU("description", "creative_energy_unit"),

    // Blocks - Storages
    CRATE("description", "crate"),
    OPEN_CRATE("description", "open_crate"),

    DRUM("description", "drum"),
    EXP_DRUM("description", "experience_drum"),

    // Command
    COMMAND_ENERGY_ADD("command", "energy_network.add"),
    COMMAND_ENERGY_GET("command", "energy_network.get"),
    COMMAND_ENERGY_SET("command", "energy_network.set"),

    // Error
    NO_DESTINATION("error", "no_destination"),
    UNKNOWN_DIMENSION("error", "unknown_dimension"),
    FUEL_SHORTAGE("error", "fuel_shortage"),

    // Items - Materials
    ELDER_HEART("description", "elder_heart"),

    // Items - Tools
    BLAST_CHARGE("description", "blast_charge"),
    STRIKE_CHARGE("description", "strike_charge"),
    NEUTRAL_CHARGE("description", "neutral_charge"),
    FISHING_CHARGE("description", "fishing_charge"),
    TELEPORT_CHARGE("description", "teleport_charge"),
    CONFUSING_CHARGE("description", "confusion_charge"),

    DYNAMIC_LANTERN("description", "dynamic_lantern"),
    ELDRITCH_EGG("description", "eldritch_heart"),
    MAGNET("description", "magnet"),
    SLOT_COVER("description", "slot_cover"),
    TRADER_CATALOG("description", "trader_catalog"),

    // Items - Foods
    AMBROSIA("description", "ambrosia"),
    ICE_CREAM("description", "ice_cream"),
    RAGI_CHERRY("description", "ragi_cherry"),

    // Items - Upgrades
    EXP_COLLECTOR_UPGRADE("description", "upgrade", "exp_collector"),
    FISHING_UPGRADE("description", "upgrade", "fishing"),
    MOB_CAPTURE_UPGRADE("description", "upgrade", "mob_capture"),

    // Key Mapping
    KEY_CATEGORY("key", "category"),
    KEY_OPEN_UNIVERSAL_BUNDLE("key", "open_universal_bundle"),

    // Jade
    JADE_MACHINE_TIER("jade", "machine_tier"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, RagiumAPI.id(path.joinToString(separator = ".")))
}
