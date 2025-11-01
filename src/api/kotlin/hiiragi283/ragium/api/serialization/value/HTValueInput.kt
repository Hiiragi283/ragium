package hiiragi283.ragium.api.serialization.value

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.serialization.codec.BiCodec
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * NBTやJSONの読み取り専用のラッパー
 */
interface HTValueInput {
    /**
     * 指定した[key]に紐づいた値を返します。
     * @param T 戻り値のクラス
     * @param key 保存先のキー
     * @param codec [T]のコーデック
     * @return 指定した[key]に値がない，[codec]での変換に失敗した場合は`null`
     */
    fun <T : Any> read(key: String, codec: Codec<T>): T?

    /**
     * 指定した[key]に紐づいた値を返します。
     * @param T 戻り値のクラス
     * @param key 保存先のキー
     * @param codec [T]のコーデック
     * @return 指定した[key]に値がない，[codec]での変換に失敗した場合は`null`
     */
    fun <T : Any> read(key: String, codec: BiCodec<*, T>): T? = read(key, codec.codec)

    fun <T : Any> readOptional(key: String, codec: Codec<Optional<T>>): T? = read(key, codec)?.getOrNull()

    fun <T : Any> readOptional(key: String, codec: BiCodec<*, Optional<T>>): T? = read(key, codec)?.getOrNull()

    // Compound

    /**
     * 指定した[key]から[HTValueInput]を返します。
     * @param key 保存先のキー
     * @return 指定した[key]に値がない場合は`null`
     */
    fun child(key: String): HTValueInput?

    /**
     * 指定した[key]から[HTValueInput]を返します。
     * @param key 保存先のキー
     */
    fun childOrEmpty(key: String): HTValueInput

    // List

    /**
     * 指定した[key]から[ValueInputList]を返します。
     * @param key 保存先のキー
     * @return 指定した[key]に値がない場合は`null`
     */
    fun childrenList(key: String): ValueInputList?

    /**
     * 指定した[key]から[ValueInputList]を返します。
     * @param key 保存先のキー
     */
    fun childrenListOrEmpty(key: String): ValueInputList

    /**
     * 指定した[key]から[ValueInputList]を返します。
     * @param T [TypedInputList]の要素のクラス
     * @param key 保存先のキー
     * @param codec [T]のコーデック
     * @return 指定した[key]に値がない，[codec]での変換に失敗した場合は`null`
     */
    fun <T : Any> list(key: String, codec: Codec<T>): TypedInputList<T>?

    /**
     * 指定した[key]から[ValueInputList]を返します。
     * @param T [TypedInputList]の要素のクラス
     * @param key 保存先のキー
     * @param codec [T]のコーデック
     * @return 指定した[key]に値がない，[codec]での変換に失敗した場合は`null`
     */
    fun <T : Any> list(key: String, codec: BiCodec<*, T>): TypedInputList<T>? = list(key, codec.codec)

    /**
     * 指定した[key]から[ValueInputList]を返します。
     * @param T [TypedInputList]の要素のクラス
     * @param key 保存先のキー
     * @param codec [T]のコーデック
     */
    fun <T : Any> listOrEmpty(key: String, codec: Codec<T>): TypedInputList<T>

    /**
     * 指定した[key]から[ValueInputList]を返します。
     * @param T [TypedInputList]の要素のクラス
     * @param key 保存先のキー
     * @param codec [T]のコーデック
     */
    fun <T : Any> listOrEmpty(key: String, codec: BiCodec<*, T>): TypedInputList<T> = listOrEmpty(key, codec.codec)

    // Primitives

    /**
     * 指定した[key]から[Boolean]を返します。
     * @param key 保存先のキー
     * @param defaultValue 戻り値のデフォルト値
     * @return 指定した[key]に値がない場合は[defaultValue]
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    /**
     * 指定した[key]から[Byte]を返します。
     * @param key 保存先のキー
     * @param defaultValue 戻り値のデフォルト値
     * @return 指定した[key]に値がない場合は[defaultValue]
     */
    fun getByte(key: String, defaultValue: Byte): Byte

    /**
     * 指定した[key]から[Short]を返します。
     * @param key 保存先のキー
     * @param defaultValue 戻り値のデフォルト値
     * @return 指定した[key]に値がない場合は[defaultValue]
     */
    fun getShort(key: String, defaultValue: Short): Short

    /**
     * 指定した[key]から[Int]を返します。
     * @param key 保存先のキー
     * @return 指定した[key]に値がない場合は`null`
     */
    fun getInt(key: String): Int?

    /**
     * 指定した[key]から[Int]を返します。
     * @param key 保存先のキー
     * @param defaultValue 戻り値のデフォルト値
     * @return 指定した[key]に値がない場合は[defaultValue]
     */
    fun getInt(key: String, defaultValue: Int): Int

    /**
     * 指定した[key]から[Long]を返します。
     * @param key 保存先のキー
     * @return 指定した[key]に値がない場合は`null`
     */
    fun getLong(key: String): Long?

    /**
     * 指定した[key]から[Long]を返します。
     * @param key 保存先のキー
     * @param defaultValue 戻り値のデフォルト値
     * @return 指定した[key]に値がない場合は[defaultValue]
     */
    fun getLong(key: String, defaultValue: Long): Long

    /**
     * 指定した[key]から[Float]を返します。
     * @param key 保存先のキー
     * @param defaultValue 戻り値のデフォルト値
     * @return 指定した[key]に値がない場合は[defaultValue]
     */
    fun getFloat(key: String, defaultValue: Float): Float

    /**
     * 指定した[key]から[Double]を返します。
     * @param key 保存先のキー
     * @param defaultValue 戻り値のデフォルト値
     * @return 指定した[key]に値がない場合は[defaultValue]
     */
    fun getDouble(key: String, defaultValue: Double): Double

    /**
     * 指定した[key]から[String]を返します。
     * @param key 保存先のキー
     * @return 指定した[key]に値がない場合は`null`
     */
    fun getString(key: String): String?

    /**
     * 指定した[key]から[String]を返します。
     * @param key 保存先のキー
     * @param defaultValue 戻り値のデフォルト値
     * @return 指定した[key]に値がない場合は[defaultValue]
     */
    fun getString(key: String, defaultValue: String): String

    /**
     * 要素の一覧を保持するインターフェース
     * @param T 要素のクラス
     */
    interface TypedInputList<T : Any> : Iterable<T> {
        val isEmpty: Boolean
    }

    /**
     * [HTValueInput]の一覧を保持するインターフェース
     */
    interface ValueInputList : Iterable<HTValueInput> {
        val isEmpty: Boolean
    }
}
