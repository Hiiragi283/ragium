package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslatedNameProvider
import hiiragi283.ragium.api.variant.HTVariantKey
import java.awt.Color

interface HTMaterialType {
    fun materialName(): String

    interface Translatable :
        HTMaterialType,
        HTTranslatedNameProvider {
        fun translate(type: HTLanguageType, variant: HTVariantKey): String = variant.translate(type, getTranslatedName(type))
    }

    interface Colored : HTMaterialType {
        val color: Color
    }
}
