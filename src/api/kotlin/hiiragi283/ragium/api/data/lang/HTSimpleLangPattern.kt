package hiiragi283.ragium.api.data.lang

@JvmRecord
data class HTSimpleLangPattern(private val enPattern: String, private val jaPattern: String) : HTLangPatternProvider {
    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)
}
