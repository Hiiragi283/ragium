package hiiragi283.ragium.api.property

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.toDataResult

/**
 * [HTMutablePropertyHolder]向けのビルダー
 */
class HTPropertyHolderBuilder(private val map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf()) {
    fun <T : Any> put(key: HTPropertyKey<T>, value: T?): HTPropertyHolderBuilder = apply {
        if (value != null) map[key] = value
    }

    fun add(vararg keys: HTPropertyKey<Unit>): HTPropertyHolderBuilder = apply {
        keys.forEach { put(it, Unit) }
    }

    fun remove(key: HTPropertyKey<*>): HTPropertyHolderBuilder = apply {
        map.remove(key)
    }

    fun build(): HTPropertyHolder = object : HTPropertyHolder {
        override fun <T : Any> getResult(key: HTPropertyKey<T>): DataResult<T> = map[key]
            .let(key::cast)
            .toDataResult { "Unknown key: $key" }
    }
}
