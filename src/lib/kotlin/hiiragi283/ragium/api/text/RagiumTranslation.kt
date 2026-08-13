package hiiragi283.ragium.api.text

import hiiragi283.lib.HTConstants
import hiiragi283.lib.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI

enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    RAGIUM(HTConstants.CONSTANTS, "name"),
    ;

    override val translationKey: String = RagiumAPI.id(path.joinToString(separator = ".")).toLanguageKey(type)
}
