package hiiragi283.ragium.api.storage.value

import hiiragi283.ragium.api.codec.BiCodec

interface HTValueOutput {
    fun <T : Any> store(key: String, codec: BiCodec<*, T>, value: T?)

    fun putBoolean(key: String, value: Boolean)

    fun putByte(key: String, value: Byte)

    fun putShort(key: String, value: Short)

    fun putInt(key: String, value: Int)

    fun putLong(key: String, value: Long)

    fun putFloat(key: String, value: Float)

    fun putDouble(key: String, value: Double)

    fun putString(key: String, value: String)

    // Compound
    fun child(key: String): HTValueOutput

    // List
    fun childrenList(key: String): ValueOutputList

    fun <T : Any> list(key: String, codec: BiCodec<*, T>): TypedOutputList<T>

    interface TypedOutputList<T : Any> {
        val isEmpty: Boolean

        fun add(element: T)
    }

    interface ValueOutputList {
        val isEmpty: Boolean

        fun addChild(): HTValueOutput

        fun discardLast()
    }
}
