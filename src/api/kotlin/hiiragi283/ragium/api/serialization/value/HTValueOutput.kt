package hiiragi283.ragium.api.serialization.value

import hiiragi283.ragium.api.serialization.codec.BiCodec
import java.util.Optional

/**
 * NBTやJSONの書き込み専用のラッパー
 */
interface HTValueOutput {
    /**
     * 指定した[key]に値を書き込みます。
     * @param T 値のクラス
     * @param key 保存先のキー
     * @param codec [T]のコーデック
     * @param value 書き込む値
     */
    fun <T : Any> store(key: String, codec: BiCodec<*, T>, value: T?)

    fun <T : Any> storeOptional(key: String, codec: BiCodec<*, Optional<T>>, value: T?) {
        store(key, codec, Optional.ofNullable(value))
    }

    // Compound

    /**
     * 指定した[key]に[HTValueOutput]を作ります。
     * @param key 保存先のキー
     * @return [key]に紐づけられた[HTValueOutput]
     */
    fun child(key: String): HTValueOutput

    // List

    /**
     * 指定した[key]に[ValueOutputList]を作ります。
     * @param key 保存先のキー
     * @return [key]に紐づけられた[ValueOutputList]
     */
    fun childrenList(key: String): ValueOutputList

    /**
     * 指定した[key]に[TypedOutputList]を作ります。
     * @param T [TypedOutputList]の要素のクラス
     * @param key 保存先のキー
     * @param codec [T]のコーデック
     * @return [key]に紐づけられた[TypedOutputList]
     */
    fun <T : Any> list(key: String, codec: BiCodec<*, T>): TypedOutputList<T>

    // Primitives

    /**
     * 指定した[key]に[value]を書き込みます。
     * @param key 保存先のキー
     * @param value 書き込む値
     */
    fun putBoolean(key: String, value: Boolean)

    /**
     * 指定した[key]に[value]を書き込みます。
     * @param key 保存先のキー
     * @param value 書き込む値
     */
    fun putByte(key: String, value: Byte)

    /**
     * 指定した[key]に[value]を書き込みます。
     * @param key 保存先のキー
     * @param value 書き込む値
     */
    fun putShort(key: String, value: Short)

    /**
     * 指定した[key]に[value]を書き込みます。
     * @param key 保存先のキー
     * @param value 書き込む値
     */
    fun putInt(key: String, value: Int)

    /**
     * 指定した[key]に[value]を書き込みます。
     * @param key 保存先のキー
     * @param value 書き込む値
     */
    fun putLong(key: String, value: Long)

    /**
     * 指定した[key]に[value]を書き込みます。
     * @param key 保存先のキー
     * @param value 書き込む値
     */
    fun putFloat(key: String, value: Float)

    /**
     * 指定した[key]に[value]を書き込みます。
     * @param key 保存先のキー
     * @param value 書き込む値
     */
    fun putDouble(key: String, value: Double)

    /**
     * 指定した[key]に[value]を書き込みます。
     * @param key 保存先のキー
     * @param value 書き込む値
     */
    fun putString(key: String, value: String)

    /**
     * 要素の一覧を保持するインターフェース
     * @param T 要素のクラス
     */
    interface TypedOutputList<T : Any> {
        /**
         * この一覧が空かどうか判定します。
         */
        val isEmpty: Boolean

        /**
         * 指定した[element]を追加します。
         */
        fun add(element: T)
    }

    /**
     * 要素の一覧を保持するインターフェース
     */
    interface ValueOutputList {
        /**
         * この一覧が空かどうか判定します。
         */
        val isEmpty: Boolean

        /**
         * 新しく[HTValueOutput]を追加し，その値を返します。
         */
        fun addChild(): HTValueOutput

        /**
         * 最後の[HTValueOutput]の要素を削除します。
         */
        fun discardLast()
    }
}
