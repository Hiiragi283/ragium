package hiiragi283.ragium.api.data.lang

@JvmRecord
data class HTSimpleLangName(private val enName: String, private val jaName: String) : HTLangName {
    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jaName
    }
}
