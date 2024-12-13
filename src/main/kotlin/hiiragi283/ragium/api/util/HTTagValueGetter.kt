package hiiragi283.ragium.api.util

import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey

fun interface HTTagValueGetter<T : Any> {
    fun getEntries(tagKey: TagKey<T>): Iterable<RegistryEntry<T>>

    fun getValues(tagKey: TagKey<T>): List<T> = getEntries(tagKey).map(RegistryEntry<T>::value)

    companion object {
        @JvmStatic
        fun <T : Any> ofLookup(lookup: RegistryEntryLookup<T>): HTTagValueGetter<T> =
            HTTagValueGetter<T> { lookup.getOptional(it).map { it as Iterable<RegistryEntry<T>> }.orElse(listOf()) }
    }
}
