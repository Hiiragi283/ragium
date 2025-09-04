package hiiragi283.ragium.common.storage.nbt

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.storage.value.HTValueInput

internal object HTEmptyValueInput : HTValueInput {
    override fun <T : Any> read(key: String, codec: BiCodec<*, T>): T? = null

    override fun child(key: String): HTValueInput? = null

    override fun childOrEmpty(key: String): HTValueInput = this

    override fun childrenList(key: String): HTValueInput.ValueInputList? = null

    override fun childrenListOrEmpty(key: String): HTValueInput.ValueInputList = EmptyInputList

    override fun <T : Any> list(key: String, codec: BiCodec<*, T>): HTValueInput.TypedInputList<T>? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> listOrEmpty(key: String, codec: BiCodec<*, T>): HTValueInput.TypedInputList<T> = emptyTypedList()

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = defaultValue

    override fun getByte(key: String, defaultValue: Byte): Byte = defaultValue

    override fun getShort(key: String, defaultValue: Short): Short = defaultValue

    override fun getInt(key: String): Int? = null

    override fun getInt(key: String, defaultValue: Int): Int = defaultValue

    override fun getLong(key: String): Long? = null

    override fun getLong(key: String, defaultValue: Long): Long = defaultValue

    override fun getFloat(key: String, defaultValue: Float): Float = defaultValue

    override fun getDouble(key: String, defaultValue: Double): Double = defaultValue

    override fun getString(key: String): String? = null

    override fun getString(key: String, defaultValue: String): String = defaultValue

    object EmptyInputList : HTValueInput.ValueInputList {
        override val isEmpty: Boolean = true

        override fun iterator(): Iterator<HTValueInput> = listOf<HTValueInput>().iterator()
    }

    object EmptyTypedList : HTValueInput.TypedInputList<Any> {
        override val isEmpty: Boolean = true

        override fun iterator(): Iterator<Any> = listOf<Any>().iterator()
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : Any> emptyTypedList(): HTValueInput.TypedInputList<T> = EmptyTypedList as HTValueInput.TypedInputList<T>
}
