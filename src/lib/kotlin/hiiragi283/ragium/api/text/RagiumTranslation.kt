package hiiragi283.ragium.api.text

import hiiragi283.lib.HTConstants
import hiiragi283.lib.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI

/**
 * Ragiumで使用される[翻訳][HTTranslation]を集めたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    RAGIUM(HTConstants.CONSTANTS, "name"),

    // Config
    CONFIG_ENERGY_CAPACITY("config", "energy_capacity"),
    CONFIG_ENERGY_RATE("config", "energy_rate"),

    // Tooltips
    TOOLTIPS_MEMORY_DISC_DATA(HTConstants.TOOLTIP, "memory_disc_data")
    ;

    override val translationKey: String = RagiumAPI.id(path.joinToString(separator = ".")).toLanguageKey(type)
}
