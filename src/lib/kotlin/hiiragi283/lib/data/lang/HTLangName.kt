package hiiragi283.lib.data.lang

/**
 * 翻訳名を返す処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTLangName {
    /**
     * 指定された[言語の種類][type]から翻訳名を返します。
     */
    fun getTranslatedName(type: HTLangType): String
}

fun HTLangName(enName: String, jaName: String): HTLangName = EnJaLangName(enName, jaName)

@JvmRecord
private data class EnJaLangName(private val enName: String, private val jaName: String) : HTLangName {
    override fun getTranslatedName(type: HTLangType): String = when (type) {
        HTLangTypes.JA_JP -> jaName
        else -> enName
    }
}
