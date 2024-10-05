package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.init.RagiumMaterials

interface HTTranslationFormatter {
    val enPattern: String
    val jaPattern: String

    fun getTranslation(type: HTLangType, provider: HTTranslationProvider): String = when (type) {
        HTLangType.EN_US -> enPattern
        HTLangType.JA_JP -> jaPattern
    }.replace("%s", provider.getTranslation(type))

    interface Holder : HTTranslationFormatter {
        val provider: HTTranslationProvider
    }

    interface Tier : Holder {
        val tier: HTMachineTier

        override val provider: HTTranslationProvider
            get() = tier
    }

    interface Material : Holder {
        val material: RagiumMaterials

        override val provider: HTTranslationProvider
            get() = material

        fun getTranslation(type: HTLangType): String = getTranslation(type, material)
    }
}
