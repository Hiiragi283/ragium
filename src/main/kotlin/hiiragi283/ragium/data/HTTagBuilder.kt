package hiiragi283.ragium.data

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.multiMapOf
import hiiragi283.ragium.api.util.HTMultiMap
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey

@Suppress("UNCHECKED_CAST")
class HTTagBuilder<T : Any>(val lookup: HolderLookup.RegistryLookup<T>) {
    private val entryCache: HTMultiMap.Mutable<TagKey<T>, Entry> = multiMapOf()
    private val registryKey: ResourceKey<out Registry<T>> = lookup.key() as ResourceKey<out Registry<T>>

    fun add(tagKey: TagKey<T>, id: ResourceLocation, isOptional: Boolean = false) {
        add(tagKey, lookup.getOrThrow(ResourceKey.create(registryKey, id)), isOptional)
    }

    fun add(tagKey: TagKey<T>, holder: Holder<T>, isOptional: Boolean = false) {
        entryCache.put(tagKey, Entry(holder.idOrThrow, false, isOptional))
    }

    fun addTag(tagKey: TagKey<T>, child: ResourceLocation, isOptional: Boolean = false) {
        addTag(tagKey, TagKey.create(registryKey, child), isOptional)
    }

    fun addTag(tagKey: TagKey<T>, child: TagKey<T>, isOptional: Boolean = false) {
        entryCache.put(tagKey, Entry(child.location, true, isOptional))
    }

    fun build(action: (TagKey<T>, TagEntry) -> Unit) {
        entryCache.map.forEach { (tagKey: TagKey<T>, entries: Collection<Entry>) ->
            entries.sortedBy(Entry::id).toSet().forEach { entry: Entry ->
                action(tagKey, entry.toTagEntry())
            }
        }
    }

    private data class Entry(val id: ResourceLocation, val isTag: Boolean, val isOptional: Boolean) {
        fun toTagEntry(): TagEntry = if (isTag) {
            when (isOptional) {
                true -> TagEntry.optionalTag(id)
                false -> TagEntry.tag(id)
            }
        } else {
            when (isOptional) {
                true -> TagEntry.optionalElement(id)
                false -> TagEntry.element(id)
            }
        }
    }
}
