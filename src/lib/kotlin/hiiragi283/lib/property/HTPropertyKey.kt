package hiiragi283.lib.property

import hiiragi283.lib.resource.HTIdLike
import net.minecraft.resources.Identifier

sealed class HTPropertyKey<T : Any>(private val id: Identifier) : HTIdLike {
    abstract fun getDefaultOrNull(): T?

    final override fun getId(): Identifier = id

    class Simple<T : Any>(id: Identifier) : HTPropertyKey<T>(id) {
        override fun getDefaultOrNull(): T? = null
    }

    class Defaulted<T : Any>(id: Identifier, private val defaultValue: () -> T) : HTPropertyKey<T>(id) {
        constructor(id: Identifier, defaultValue: T) : this(id, { defaultValue })

        override fun getDefaultOrNull(): T = defaultValue()
    }
}
