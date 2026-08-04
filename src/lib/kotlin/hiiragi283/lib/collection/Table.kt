package hiiragi283.lib.collection

/**
 * 二つのキーに対して一つの値が対応するコレクションを表すインターフェースです。
 *
 * 参照 : [Google Guava - Table][com.google.common.collect.Table]
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface Table<R, C, out V> {
    /**
     * 組の個数
     */
    val size: Int

    /**
     * このテーブルが空かどうか判定します。
     */
    val isEmpty: Boolean

    /**
     * 指定した行と列が含まれているか判定します。
     */
    fun contains(row: R, column: C): Boolean

    /**
     * 指定した行が含まれているか判定します。
     */
    fun containsRow(row: R): Boolean

    /**
     * 指定した列が含まれているか判定します。
     */
    fun containsColumn(column: C): Boolean

    /**
     * 指定した値が含まれているか判定します。
     */
    fun containsValue(value: @UnsafeVariance V): Boolean

    /**
     * 指定した行と列に対応する値を返します。
     */
    operator fun get(row: R, column: C): V?

    /**
     * 指定した行に対応する列と値のマップを返します。
     */
    fun row(row: R): Map<C, V>

    /**
     * 指定した列に対応する行と値のマップを返します。
     */
    fun column(column: C): Map<R, V>

    /**
     * 行の一覧
     */
    val rowKeys: Set<R>

    /**
     * 列の一覧
     */
    val columnKeys: Set<C>

    /**
     * 値の一覧
     */
    val values: Collection<V>

    /**
     * 組の一覧
     */
    val entries: Set<Triple<R, C, V>>

    /**
     * [Table]のビルダーを表すインターフェースです。
     * @param R 行のクラス
     * @param C 列のクラス
     * @param V 値のクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.1
     */
    interface Builder<R, C, V> {
        /**
         * 値を追加します。
         */
        fun put(row: R, column: C, value: V): V?

        /**
         * 値を追加します。
         */
        fun put(triple: Triple<R, C, V>): V? = put(triple.first, triple.second, triple.third)

        operator fun set(row: R, column: C, value: V) {
            put(row, column, value)
        }

        /**
         * 値を追加します。
         */
        fun putAll(triples: Iterable<Triple<R, C, V>>) {
            triples.forEach(::put)
        }

        /**
         * 値を追加します。
         */
        fun putAll(triples: Sequence<Triple<R, C, V>>) {
            triples.forEach(::put)
        }

        /**
         * 値を追加します。
         */
        fun putAll(triples: Array<out Triple<R, C, V>>) {
            triples.forEach(::put)
        }

        /**
         * [Table]を生成します。
         */
        fun build(): Table<R, C, V>
    }
}
