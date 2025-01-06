package hiiragi283.ragium.api.util

import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey

/**
 * [TagKey]の値を取得するインターフェース
 * @see [net.minecraft.registry.Registry.iterateEntries]
 */
fun interface HTTagValueGetter<T : Any> {
    /**
     * 指定した[tagKey]から[RegistryEntry]の一覧を返します。
     */
    fun getEntries(tagKey: TagKey<T>): Iterable<RegistryEntry<T>>

    /**
     * 指定した[tagKey]から[T]の一覧を返します。
     */
    fun getValues(tagKey: TagKey<T>): List<T> = getEntries(tagKey).map(RegistryEntry<T>::value)

    companion object {
        /**
         * 指定した[lookup]から[HTTagValueGetter]を返します。
         */
        @JvmStatic
        fun <T : Any> ofLookup(lookup: RegistryEntryLookup<T>): HTTagValueGetter<T> =
            HTTagValueGetter<T> { lookup.getOptional(it).map { it as Iterable<RegistryEntry<T>> }.orElse(listOf()) }
    }
}
