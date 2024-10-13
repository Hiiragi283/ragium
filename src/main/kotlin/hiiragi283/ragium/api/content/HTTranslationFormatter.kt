package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.data.HTLangType

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

    interface Material : Holder {
        val material: RagiumMaterials

        override val provider: HTTranslationProvider
            get() = material

        fun getTranslation(type: HTLangType): String = getTranslation(type, material)
    }
}
