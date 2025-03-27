package hiiragi283.ragium.api.property

import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

sealed class HTPropertyKey<T : Any>(val id: ResourceLocation) {
    @Suppress("UNCHECKED_CAST")
    fun castAs(obj: Any?): T? = obj as? T

    override fun equals(other: Any?): Boolean = (other as? HTPropertyKey<*>)?.id == id

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "HTPropertyKey[$id]"

    class Simple<T : Any>(id: ResourceLocation) : HTPropertyKey<T>(id)

    class Defaulted<T : Any>(id: ResourceLocation, private val defaultValue: Supplier<T>) : HTPropertyKey<T>(id) {
        fun getDefaultValue(): T = defaultValue.get()
    }
}
