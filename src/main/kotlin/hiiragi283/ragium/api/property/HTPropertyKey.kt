package hiiragi283.ragium.api.property

import net.minecraft.util.Identifier

/**
 * Typed Key for [HTPropertyHolder]
 */
sealed class HTPropertyKey<T : Any>(val id: Identifier) {
    companion object {
        @JvmStatic
        fun <T : Any> ofSimple(id: Identifier): Simple<T> = Simple(id)

        @JvmStatic
        fun <T : Any> ofDefaulted(id: Identifier, value: T): Defaulted<T> = ofDefaulted(id) { value }

        @JvmStatic
        fun <T : Any> ofDefaulted(id: Identifier, initializer: () -> T): Defaulted<T> = Defaulted(id, initializer)

        @JvmStatic
        fun ofFlag(id: Identifier): Defaulted<Unit> = ofDefaulted(id, Unit)
    }

    /**
     * Try to cast [obj] into [T], or null if failed
     */
    @Suppress("UNCHECKED_CAST")
    fun cast(obj: Any?): T? = obj as? T

    override fun toString(): String = "HTPropertyKey[$id]"

    //    Simple    //

    /**
     * [hiiragi283.ragium.api.property.HTPropertyKey] without default value
     */
    class Simple<T : Any>(id: Identifier) : HTPropertyKey<T>(id)

    //    Defaulted    //

    /**
     * [hiiragi283.ragium.api.property.HTPropertyKey] with default value
     */
    class Defaulted<T : Any>(id: Identifier, val initializer: () -> T) : HTPropertyKey<T>(id) {
        fun getDefaultValue(): T = initializer()
    }
}
