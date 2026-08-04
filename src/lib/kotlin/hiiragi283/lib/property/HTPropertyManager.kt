package hiiragi283.lib.property

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.network.codec.StreamCodec

/**
 * キーに対応する[HTPropertyGetter]を管理するクラスです。
 * @param K キーのクラス
 * @param E [HTPropertyManager.Entry]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
class HTPropertyManager<K : Any, E : HTPropertyManager.Entry<K>>(private val map: Map<K, E>) : Iterable<E> {
    companion object {
        @JvmStatic
        fun <K : Any, E : Entry<K>> codec(keyCodec: Codec<K>, instance: () -> HTPropertyManager<K, E>, errorMessage: (K) -> String): Codec<E> = keyCodec.comapFlatMap(
            { key: K ->
                instance()
                    .firstOrNull { it.key == key }
                    ?.let { DataResult.success(it) }
                    ?: DataResult.error { errorMessage(key) }
            },
            { it.key },
        )

        @JvmStatic
        fun <B : Any, K : Any, E : Entry<K>> streamCodec(keyCodec: StreamCodec<B, K>, instance: () -> HTPropertyManager<K, E>, errorMessage: (K) -> String): StreamCodec<B, E> = keyCodec.map(
            { key: K -> instance().firstOrNull { it.key == key } ?: errorMessage(key).let(::error) },
            { it.key },
        )
    }

    operator fun contains(key: K): Boolean = key in map

    operator fun get(key: K): E? = map[key]

    fun getOrThrow(key: K): E = get(key) ?: error("Missing entry: $key")

    val keys: Set<K> get() = map.keys

    val entries: Collection<E> = map.values

    override fun iterator(): Iterator<E> = entries.iterator()

    /**
     * [HTPropertyManager]の値に実装するインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 21.1.1.0
     */
    interface Entry<K : Any> : HTPropertyGetter {
        /**
         * 対応するキー
         */
        val key: K
    }
}
