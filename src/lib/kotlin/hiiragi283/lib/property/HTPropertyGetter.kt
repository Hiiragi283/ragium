package hiiragi283.lib.property

/**
 * [HTPropertyKey]に基づいてデータを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
interface HTPropertyGetter {
    /**
     * 指定した[key]が含まれるか判定します。
     */
    operator fun contains(key: HTPropertyKey<*>): Boolean = get(key) != null

    /**
     * 指定した[key]に紐づいた値を返します。
     * @return 値がない場合は`null`
     */
    operator fun <T : Any> get(key: HTPropertyKey<T>): T?
}
