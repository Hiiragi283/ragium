package hiiragi283.ragium.api.util.material

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.data.HTTranslatedNameProvider
import hiiragi283.ragium.api.registry.HTVariantKey
import net.minecraft.util.StringRepresentable

interface HTMaterialType : StringRepresentable {
    interface Translatable :
        HTMaterialType,
        HTTranslatedNameProvider {
        fun translate(type: HTLanguageType, variant: HTVariantKey): String = variant.translate(type, getTranslatedName(type))
    }
}
