package hiiragi283.ragium.api.collection

inline fun <K : Any, V : Any> buildMultiMap(
    initialCapacity: Int = 10,
    perKey: Int = 2,
    builderAction: ImmutableMultiMap.Builder<K, V>.() -> Unit,
): ImmutableMultiMap<K, V> = ImmutableMultiMap.Builder<K, V>(initialCapacity, perKey).apply(builderAction).build()
