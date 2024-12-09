package hiiragi283.ragium.api.util

import net.minecraft.component.ComponentChanges
import net.minecraft.component.ComponentMap
import net.minecraft.component.ComponentMapImpl
import net.minecraft.component.ComponentType

interface MutableComponentMap : ComponentMap {
    fun <T : Any> set(type: ComponentType<T>, value: T?): T?

    fun <T : Any> remove(type: ComponentType<T>): T?

    fun getChanges(): ComponentChanges

    companion object {
        @JvmField
        val EMPTY: MutableComponentMap = object : MutableComponentMap {
            override fun <T : Any> set(type: ComponentType<T>, value: T?): T? = null

            override fun <T : Any> remove(type: ComponentType<T>): T? = null

            override fun getChanges(): ComponentChanges = ComponentChanges.EMPTY

            override fun <T : Any> get(type: ComponentType<out T>): T? = null

            override fun getTypes(): Set<ComponentType<*>> = setOf()
        }

        @JvmStatic
        fun orNull(map: ComponentMap): MutableComponentMap? = (map as? ComponentMapImpl)?.let(::of)

        @JvmStatic
        fun orEmpty(map: ComponentMap): MutableComponentMap = orNull(map) ?: EMPTY

        @JvmStatic
        fun of(mapImpl: ComponentMapImpl): MutableComponentMap = object : MutableComponentMap {
            override fun <T : Any> set(type: ComponentType<T>, value: T?): T? = mapImpl.set(type, value)

            override fun <T : Any> remove(type: ComponentType<T>): T? = mapImpl.remove(type)

            override fun getChanges(): ComponentChanges = mapImpl.changes

            override fun <T : Any> get(type: ComponentType<out T>): T? = mapImpl.get(type)

            override fun getTypes(): Set<ComponentType<*>> = mapImpl.types
        }
    }
}
