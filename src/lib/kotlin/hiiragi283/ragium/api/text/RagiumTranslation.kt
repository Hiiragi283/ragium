package hiiragi283.ragium.api.text

import hiiragi283.lib.HTConstants
import hiiragi283.lib.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI

enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    RAGIUM(HTConstants.CONSTANTS, "name"),

    // Config
    CONFIG_ENERGY_CAPACITY("config", "energy_capacity"),
    CONFIG_ENERGY_RATE("config", "energy_rate"),
    ;

    override val translationKey: String = RagiumAPI.id(path.joinToString(separator = ".")).toLanguageKey(type)
}
