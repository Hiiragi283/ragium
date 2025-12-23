package hiiragi283.ragium.common.text

import hiiragi283.core.api.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.Util

enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    RAGIUM("constants", "mod_name"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, RagiumAPI.id(path.joinToString(separator = ".")))
}
