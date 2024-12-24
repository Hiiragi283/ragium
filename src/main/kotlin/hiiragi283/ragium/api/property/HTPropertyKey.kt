package hiiragi283.ragium.api.property

import net.minecraft.util.Identifier

/**
 * [HTPropertyHolder]のキー
 * @param T 値のクラス
 * @param id ユニークな値
 */
sealed class HTPropertyKey<T : Any>(val id: Identifier) {
    companion object {
        @JvmStatic
        fun <T : Any> ofSimple(id: Identifier): Simple<T> = Simple(id)

        @JvmStatic
        fun <T : Any> ofDefaulted(id: Identifier, value: T): Defaulted<T> = Defaulted(id, value)

        @JvmStatic
        fun <T : Any> ofDefaulted(id: Identifier, initializer: () -> T): Defaulted<T> = Defaulted(id, initializer)

        @JvmStatic
        fun ofFlag(id: Identifier): Defaulted<Unit> = ofDefaulted(id, Unit)
    }

    /**
     * 指定された[obj]を[T]にキャストします。
     * @return キャストできなかった場合はnull
     */
    @Suppress("UNCHECKED_CAST")
    fun cast(obj: Any?): T? = obj as? T

    override fun toString(): String = "HTPropertyKey[$id]"

    //    Simple    //

    /**
     * デフォルト値を持たない[hiiragi283.ragium.api.property.HTPropertyKey]
     */
    class Simple<T : Any>(id: Identifier) : HTPropertyKey<T>(id)

    //    Defaulted    //

    /**
     * デフォルト値を持つ[hiiragi283.ragium.api.property.HTPropertyKey]
     * @param initializer デフォルト値を渡すブロック
     */
    class Defaulted<T : Any>(id: Identifier, private val initializer: () -> T) : HTPropertyKey<T>(id) {
        constructor(id: Identifier, defaultValue: T) : this(id, { defaultValue })

        fun getDefaultValue(): T = initializer()
    }
}
