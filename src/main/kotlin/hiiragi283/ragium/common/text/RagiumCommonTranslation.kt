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

    // Blocks - Generators
    THERMAL_GENERATOR("description", "thermal_generator"),
    COMBUSTION_GENERATOR("description", "combustion_generator"),
    SOLAR_PANEL_CONTROLLER("description", "solar_panel_controller"),
    ENCHANTMENT_GENERATOR("description", "enchantment_generator"),
    NUCLEAR_REACTOR("description", "nuclear_reactor"),

    // Blocks - Consumers

    // Blocks - Devices
    MOB_CAPTURER("description", "mob_capturer"),
    CEU("description", "creative_energy_unit"),

    // Blocks - Storages
    CRATE("description", "crate"),
    DRUM("description", "drum"),
    EXP_DRUM("description", "experience_drum"),

    // Creative Mode Tab
    CREATIVE_TAB_BLOCKS("itemGroup", "blocks"),
    CREATIVE_TAB_INGREDIENTS("itemGroup", "ingredients"),
    CREATIVE_TAB_ITEMS("itemGroup", "items"),

    // Items - Materials
    ELDER_HEART("description", "elder_heart"),

    // Items - Tools
    BLAST_CHARGE("description", "blast_charge"),
    DYNAMIC_LANTERN("description", "dynamic_lantern"),
    ELDRITCH_EGG("description", "eldritch_heart"),
    MAGNET("description", "magnet"),
    SLOT_COVER("description", "slot_cover"),
    TRADER_CATALOG("description", "trader_catalog"),

    // Items - Foods
    AMBROSIA("description", "ambrosia"),
    ICE_CREAM("description", "ice_cream"),
    RAGI_CHERRY("description", "ragi_cherry"),

    ;

    override val translationKey: String = Util.makeDescriptionId(type, RagiumAPI.id(path.joinToString(separator = ".")))
}
