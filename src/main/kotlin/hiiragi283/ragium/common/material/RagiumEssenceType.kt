package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.variant.HTMaterialVariant
import hiiragi283.ragium.common.variant.HTItemMaterialVariant
import java.awt.Color

enum class RagiumEssenceType(override val color: Color, private val enName: String, private val jpName: String) :
    HTMaterialType.Colored,
    HTMaterialType.Translatable {
    RAGIUM(Color(0xff0033), "Ragium", "ラギウム"),
    AZURE(Color(0x656da1), "Azure Essence", "紺碧エッセンス"),
    DEEP(Color(0x404d5a), "Deep Essence", "深層エッセンス"),
    ;

    val baseVariant: HTMaterialVariant.ItemTag get() = when (this) {
        RAGIUM -> HTItemMaterialVariant.DUST
        AZURE -> HTItemMaterialVariant.DUST
        DEEP -> HTItemMaterialVariant.SCRAP
    }
    val parent: RagiumMaterialType get() = when (this) {
        RAGIUM -> RagiumMaterialType.RAGINITE
        AZURE -> RagiumMaterialType.AZURE
        DEEP -> RagiumMaterialType.DEEP_STEEL
    }

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jpName
    }

    override fun materialName(): String = name.lowercase()
}
