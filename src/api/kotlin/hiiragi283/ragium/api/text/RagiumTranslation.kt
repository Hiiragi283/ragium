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

    // Block Type
    EMPTY_ENTRY("description", "empty"),

    // Gui
    INFINITE("gui", "infinite"),
    NONE("gui", "none"),
    EMPTY("gui", "empty"),

    CAPACITY("gui", "capacity"),
    CAPACITY_MB("gui", "capacity.mb"),
    CAPACITY_FE("gui", "capacity.fe"),

    STORED("gui", "stored"),
    STORED_MB("gui", "stored.mb"),
    STORED_FE("gui", "stored.fe"),

    // Item Name
    ITEM_POTION("item", "potion", "effect"),

    // Item Description
    TOOLTIP_ENERGY_PERCENTAGE("tooltip", "energy_percentage"),
    TOOLTIP_EXP_PERCENTAGE("tooltip", "experience_percentage"),
    TOOLTIP_INTRINSIC_ENCHANTMENT("tooltip", "intrinsic_enchantment"),
    TOOLTIP_LOOT_TABLE_ID("tooltip", "loot_table_id"),
    TOOLTIP_SHOW_DESCRIPTION("tooltip", "show_description"),
    TOOLTIP_SHOW_DETAILS("tooltip", "show_details"),
    TOOLTIP_WIP("tooltip", "work_in_progress"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, RagiumAPI.id(path.joinToString(separator = ".")))
}
