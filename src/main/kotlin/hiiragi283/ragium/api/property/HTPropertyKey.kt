package hiiragi283.ragium.api.property

import net.minecraft.util.Identifier

sealed class HTPropertyKey<T : Any>(val id: Identifier) {
    @Suppress("UNCHECKED_CAST")
    fun cast(obj: Any?): T? = obj as? T

    override fun toString(): String = "HTPropertyKey[$id]"

    //    Simple    //

    class Simple<T : Any>(id: Identifier) : HTPropertyKey<T>(id)

    //    Defaulted    //

    class Defaulted<T : Any>(id: Identifier, val initializer: () -> T) : HTPropertyKey<T>(id) {
        constructor(id: Identifier, value: T) : this(id, { value })

        fun getDefaultValue(): T = initializer()
    }
}
