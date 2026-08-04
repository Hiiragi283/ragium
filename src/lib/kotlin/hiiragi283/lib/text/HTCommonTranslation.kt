package hiiragi283.lib.text

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.toId

/**
 * Ragiumで使用される[翻訳][HTTranslation]を集めたクラスです。
 *
 * 参照 : [Mekanism - APILang](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/text/APILang.java)
 *       [Mekanism - MekanismLang](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/MekanismLang.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTCommonTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    ERROR(HTConstants.CONSTANTS, "error"),
    INFINITE(HTConstants.CONSTANTS, "infinite"),
    NONE(HTConstants.CONSTANTS, "none"),
    EMPTY(HTConstants.CONSTANTS, "empty"),

    TRUE(HTConstants.CONSTANTS, "true"),
    FALSE(HTConstants.CONSTANTS, "false"),

    DOWN("direction", "down"),
    UP("direction", "up"),
    NORTH("direction", "north"),
    SOUTH("direction", "south"),
    WEST("direction", "west"),
    EAST("direction", "east"),

    // Error
    INVALID_PACKET_S2C(HTConstants.ERROR, "invalid_packet", "s2c"),
    INVALID_PACKET_C2S(HTConstants.ERROR, "invalid_packet", "c2s"),

    // GUI
    CAPACITY(HTConstants.GUI, "capacity"),
    CAPACITY_MB(HTConstants.GUI, "capacity", "mb"),
    CAPACITY_FE(HTConstants.GUI, "capacity", "fe"),

    STORED(HTConstants.GUI, "stored"),
    STORED_MB(HTConstants.GUI, "stored", "mb"),
    STORED_FE(HTConstants.GUI, "stored", "fe"),
    STORED_EXP(HTConstants.GUI, "stored", "exp"),

    FRACTION(HTConstants.GUI, "fraction"),
    PERCENTAGE(HTConstants.GUI, "percentage"),
    PROGRESS(HTConstants.GUI, "progress"),

    TICK(HTConstants.GUI, "tick"),
    SECONDS(HTConstants.GUI, "seconds"),

    CHANCE_PRODUCE(HTConstants.GUI, "chance", "produce"),

    // Item Description
    TOOLTIP_INTRINSIC_ENCHANTMENT(HTConstants.TOOLTIP, "intrinsic_enchantment"),
    TOOLTIP_SHOW_DESCRIPTION(HTConstants.TOOLTIP, "show_description"),
    TOOLTIP_SHOW_DETAILS(HTConstants.TOOLTIP, "show_details"),

    DATAPACK_WIP("datapack", "work_in_progress"),
    ;

    override val translationKey: String = HTConstants.MOD_ID.toId(path.joinToString(separator = ".")).toLanguageKey(type)
}
