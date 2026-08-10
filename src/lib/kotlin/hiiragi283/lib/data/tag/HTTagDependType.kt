package hiiragi283.lib.data.tag

/**
 * タグの依存関係を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTTagDependType {
    /**
     * 必須
     */
    REQUIRED,

    /**
     * 選択的
     */
    OPTIONAL,
}
