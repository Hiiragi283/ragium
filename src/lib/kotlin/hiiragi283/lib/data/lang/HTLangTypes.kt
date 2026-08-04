package hiiragi283.lib.data.lang

/**
 * [HTLangType]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTLangTypes {
    @JvmStatic
    val EN_US: HTLangType = HTLangType.of("en_us")

    @JvmStatic
    val JA_JP: HTLangType = HTLangType.of("ja_jp")
}
