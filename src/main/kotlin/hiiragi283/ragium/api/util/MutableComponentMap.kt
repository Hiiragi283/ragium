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
