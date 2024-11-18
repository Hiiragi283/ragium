package hiiragi283.ragium.api.content

interface HTTranslationProvider {
    val enName: String
    val jaName: String

    fun getTranslation(type: HTLangType): String = when (type) {
        HTLangType.EN_US -> enName
        HTLangType.JA_JP -> jaName
    }
}
