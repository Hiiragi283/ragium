package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslatedNameProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import java.awt.Color

enum class RagiumEssenceType(val color: Color, private val enName: String, private val jpName: String) :
    HTMaterialLike,
    HTTranslatedNameProvider {
    RAGIUM(Color(0xff0033), "Ragium", "ラギウム"),
    AZURE(Color(0x656da1), "Azure Essence", "紺碧エッセンス"),
    DEEP(Color(0x404d5a), "Deep Essence", "深層エッセンス"),
    ;

    val basePrefix: HTMaterialPrefix get() = when (this) {
        RAGIUM -> CommonMaterialPrefixes.DUST
        AZURE -> CommonMaterialPrefixes.DUST
        DEEP -> CommonMaterialPrefixes.SCRAP
    }
    val parent: HTMaterialKey get() = when (this) {
        RAGIUM -> RagiumMaterialKeys.RAGINITE
        AZURE -> RagiumMaterialKeys.AZURE
        DEEP -> RagiumMaterialKeys.DEEP_STEEL
    }

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jpName
    }

    override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
}
