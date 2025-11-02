package hiiragi283.ragium.api.collection

import com.google.common.collect.ImmutableListMultimap

fun <K : Any, V : Any> immutableMultiMapOf(): ImmutableMultiMap<K, V> = ImmutableMultiMap(ImmutableListMultimap.of())

inline fun <K : Any, V : Any> buildMultiMap(
    initialCapacity: Int = 10,
    perKey: Int = 2,
    builderAction: ImmutableMultiMap.Builder<K, V>.() -> Unit,
): ImmutableMultiMap<K, V> = ImmutableMultiMap.Builder<K, V>(initialCapacity, perKey).apply(builderAction).build()
