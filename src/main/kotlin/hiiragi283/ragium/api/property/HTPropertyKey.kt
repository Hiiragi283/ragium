package hiiragi283.ragium.api.property

import net.minecraft.util.Identifier

sealed class HTPropertyKey<T : Any>(val id: Identifier) {
    companion object {
        @JvmStatic
        fun <T : Any> ofSimple(id: Identifier): Simple<T> = Simple(id)

        @JvmStatic
        fun <T : Any> ofDefaulted(id: Identifier, value: T): Defaulted<T> = ofDefaulted(id) { value }

        @JvmStatic
        fun <T : Any> ofDefaulted(id: Identifier, initializer: () -> T): Defaulted<T> = Defaulted(id, initializer)
    }

    @Suppress("UNCHECKED_CAST")
    fun cast(obj: Any?): T? = obj as? T

    override fun toString(): String = "HTPropertyKey[$id]"

    //    Simple    //

    class Simple<T : Any>(id: Identifier) : HTPropertyKey<T>(id)

    //    Defaulted    //

    class Defaulted<T : Any>(id: Identifier, val initializer: () -> T) : HTPropertyKey<T>(id) {
        fun getDefaultValue(): T = initializer()
    }
}
