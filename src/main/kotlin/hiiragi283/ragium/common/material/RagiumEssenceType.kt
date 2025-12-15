package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import java.awt.Color

enum class RagiumEssenceType(val color: Color, private val enName: String, private val jpName: String) : HTMaterialLike.Translatable {
    RAGIUM(Color(0xff0033), "Ragium", "ラギウム"),
    AZURE(Color(0x6666cc), "Azure Essence", "紺碧エッセンス"),
    DEEP(Color(0x699699), "Deep Essence", "深層エッセンス"),
    ;

    fun getBaseEntry(): Pair<HTPrefixLike, HTMaterialKey> = when (this) {
        RAGIUM -> CommonMaterialPrefixes.DUST to RagiumMaterialKeys.RAGINITE
        AZURE -> CommonMaterialPrefixes.DUST to RagiumMaterialKeys.AZURE
        DEEP -> CommonMaterialPrefixes.SCRAP to RagiumMaterialKeys.DEEP_STEEL
    }

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jpName
    }

    override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
}
