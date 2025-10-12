package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslatedNameProvider
import hiiragi283.ragium.api.variant.HTVariantKey
import java.awt.Color

/**
 * 素材を表現するインターフェース
 */
interface HTMaterialType {
    fun materialName(): String

    /**
     * 翻訳可能な[HTMaterialType]の拡張インターフェース
     */
    interface Translatable :
        HTMaterialType,
        HTTranslatedNameProvider {
        fun translate(type: HTLanguageType, variant: HTVariantKey): String = variant.translate(type, getTranslatedName(type))
    }

    /**
     * 色を保持する[HTMaterialType]の拡張インターフェース
     */
    interface Colored : HTMaterialType {
        val color: Color
    }
}
