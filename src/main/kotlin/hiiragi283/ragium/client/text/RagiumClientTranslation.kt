package hiiragi283.ragium.client.text

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.text.HTTranslation
import net.minecraft.Util

enum class RagiumClientTranslation(type: String, vararg path: String) : HTTranslation {
    // Key Mapping
    KEY_CATEGORY("key", "category"),
    KEY_OPEN_UNIVERSAL_BUNDLE("key", "open_universal_bundle"),

    // Jade
    JADE_EXP_STORAGE("tooltip", "experience_storage"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, RagiumAPI.id(path.joinToString(separator = ".")))
}
