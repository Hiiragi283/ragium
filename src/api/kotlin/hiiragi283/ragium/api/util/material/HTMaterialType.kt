package hiiragi283.ragium.api.util.material

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.util.translate.HTTranslatable
import net.minecraft.util.StringRepresentable

interface HTMaterialType :
    HTTranslatable,
    StringRepresentable {
    fun translate(type: HTLanguageType, variant: HTMaterialVariant): String = variant.translate(type, getTranslatedName(type))
}
