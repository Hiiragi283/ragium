package hiiragi283.ragium.api.text

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.Util

/**
 * @see mekanism.api.text.APILang
 * @see mekanism.common.MekanismLang
 */
enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    RAGIUM("constants", "mod_name"),
    ERROR("constants", "error"),
    INFINITE("constants", "infinite"),
    NONE("constants", "none"),
    EMPTY("constants", "empty"),

    TRUE("constants", "true"),
    FALSE("constants", "false"),

    // Block Type
    EMPTY_ENTRY("description", "empty"),

    // Error
    EMPTY_TAG_KEY("error", "empty.tag_key"),
    INVALID_PACKET_S2C("error", "invalid_packet", "s2c"),
    INVALID_PACKET_C2S("error", "invalid_packet", "c2s"),

    MISSING_SERVER("error", "missing", "server"),
    MISSING_REGISTRY("error", "missing", "registry"),
    MISSING_KEY("error", "missing", "key"),

    // GUI
    CAPACITY("gui", "capacity"),
    CAPACITY_MB("gui", "capacity", "mb"),
    CAPACITY_FE("gui", "capacity", "fe"),

    STORED("gui", "stored"),
    STORED_MB("gui", "stored", "mb"),
    STORED_FE("gui", "stored", "fe"),

    FRACTION("gui", "fraction"),
    PERCENTAGE("gui", "percentage"),
    TICK("gui", "tick"),

    PER_MB("gui", "per", "mb"),
    PER_TICK("gui", "per", "tick"),

    BURN_TIME("gui", "burn_tick"),

    // Item Description
    TOOLTIP_BLOCK_POS("tooltip", "block_pos"),
    TOOLTIP_CHARGE_POWER("tooltip", "blast_power"),
    TOOLTIP_DIMENSION("tooltip", "dimension"),
    TOOLTIP_INTRINSIC_ENCHANTMENT("tooltip", "intrinsic_enchantment"),
    TOOLTIP_LOOT_TABLE_ID("tooltip", "loot_table_id"),
    TOOLTIP_UPGRADE_GROUP("tooltip", "upgrade_group"),

    TOOLTIP_SHOW_DESCRIPTION("tooltip", "show_description"),
    TOOLTIP_SHOW_DETAILS("tooltip", "show_details"),

    DATAPACK_WIP("datapack", "work_in_progress"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, RagiumAPI.id(path.joinToString(separator = ".")))
}
