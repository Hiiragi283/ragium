package hiiragi283.ragium.api.property

import net.minecraft.util.Identifier
import kotlin.jvm.Throws

sealed class HTPropertyKey<T : Any>(val id: Identifier) {
    @Throws(AssertionError::class)
    abstract fun getDefaultValue(): T

    @Suppress("UNCHECKED_CAST")
    fun cast(obj: Any?): T? = obj as? T

    //    Simple    //

    class Simple<T : Any>(id: Identifier) : HTPropertyKey<T>(id) {
        override fun getDefaultValue(): T = throw AssertionError("Simple property key cannot provide default value!")
    }

    //    Defaulted    //

    class Defaulted<T : Any>(id: Identifier, val initializer: () -> T) : HTPropertyKey<T>(id) {
        constructor(id: Identifier, value: T) : this(id, { value })

        override fun getDefaultValue(): T = initializer()
    }
}
