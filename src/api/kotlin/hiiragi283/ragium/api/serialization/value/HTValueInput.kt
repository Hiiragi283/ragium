package hiiragi283.ragium.api.serialization.value

import hiiragi283.ragium.api.serialization.codec.BiCodec

interface HTValueInput {
    fun <T : Any> read(key: String, codec: BiCodec<*, T>): T?

    // Compound
    fun child(key: String): HTValueInput?

    fun childOrEmpty(key: String): HTValueInput

    // List
    fun childrenList(key: String): ValueInputList?

    fun childrenListOrEmpty(key: String): ValueInputList

    fun <T : Any> list(key: String, codec: BiCodec<*, T>): TypedInputList<T>?

    fun <T : Any> listOrEmpty(key: String, codec: BiCodec<*, T>): TypedInputList<T>

    // primitives
    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun getByte(key: String, defaultValue: Byte): Byte

    fun getShort(key: String, defaultValue: Short): Short

    fun getInt(key: String): Int?

    fun getInt(key: String, defaultValue: Int): Int

    fun getLong(key: String): Long?

    fun getLong(key: String, defaultValue: Long): Long

    fun getFloat(key: String, defaultValue: Float): Float

    fun getDouble(key: String, defaultValue: Double): Double

    fun getString(key: String): String?

    fun getString(key: String, defaultValue: String): String

    interface TypedInputList<T : Any> : Iterable<T> {
        val isEmpty: Boolean
    }

    interface ValueInputList : Iterable<HTValueInput> {
        val isEmpty: Boolean
    }
}
