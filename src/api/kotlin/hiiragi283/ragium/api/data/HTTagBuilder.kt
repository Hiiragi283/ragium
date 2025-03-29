package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.multiMapOf
import hiiragi283.ragium.api.util.HTMultiMap
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey

/**
 * 登録した[TagKey]をソートして生成するビルダー
 */
@Suppress("UNCHECKED_CAST")
class HTTagBuilder<T : Any>(val registryKey: ResourceKey<out Registry<T>>) {
    private val entryCache: HTMultiMap.Mutable<TagKey<T>, Entry> = multiMapOf()

    fun add(
        tagKey: TagKey<T>,
        mod: IntegrationMods,
        path: String,
        type: DependType = DependType.REQUIRED,
    ) {
        add(tagKey, mod.id(path), type)
    }

    fun add(tagKey: TagKey<T>, id: ResourceLocation, type: DependType = DependType.REQUIRED) {
        entryCache.put(tagKey, Entry(id, false, type))
    }

    fun add(tagKey: TagKey<T>, holder: Holder<T>, type: DependType = DependType.REQUIRED) {
        entryCache.put(tagKey, Entry(holder.idOrThrow, false, type))
    }

    fun addTag(tagKey: TagKey<T>, child: ResourceLocation, type: DependType = DependType.REQUIRED) {
        addTag(tagKey, TagKey.create(registryKey, child), type)
    }

    fun addTag(tagKey: TagKey<T>, child: TagKey<T>, type: DependType = DependType.REQUIRED) {
        entryCache.put(tagKey, Entry(child.location, true, type))
    }

    fun build(action: (TagKey<T>, TagEntry) -> Unit) {
        entryCache.map.forEach { (tagKey: TagKey<T>, entries: Collection<Entry>) ->
            entries.sortedBy(Entry::id).toSet().forEach { entry: Entry ->
                action(tagKey, entry.toTagEntry())
            }
        }
    }

    enum class DependType {
        OPTIONAL,
        REQUIRED,
    }

    private data class Entry(val id: ResourceLocation, val isTag: Boolean, val type: DependType) {
        fun toTagEntry(): TagEntry = if (isTag) {
            when (type) {
                DependType.OPTIONAL -> TagEntry.optionalTag(id)
                DependType.REQUIRED -> TagEntry.tag(id)
            }
        } else {
            when (type) {
                DependType.OPTIONAL -> TagEntry.optionalElement(id)
                DependType.REQUIRED -> TagEntry.element(id)
            }
        }
    }
}
