package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialVariant

object RagiumMaterialVariants {
    @JvmStatic
    private fun createCustom(name: String, enPattern: String, jaPattern: String): HTMaterialVariant = object : HTMaterialVariant {
        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> enPattern
            HTLanguageType.JA_JP -> jaPattern
        }.replace("%s", value)

        override fun getSerializedName(): String = name
    }

    @JvmField
    val COIL: HTMaterialVariant = createCustom("coil", "%s Coil", "%sコイル")

    @JvmField
    val COMPONENT: HTMaterialVariant = createCustom("component", "%s Component", "%s構造体")

    @JvmField
    val COMPOUND: HTMaterialVariant = createCustom("compound", "%s Compound", "%s混合物")
}
