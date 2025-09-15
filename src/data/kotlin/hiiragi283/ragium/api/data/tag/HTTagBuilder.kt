package hiiragi283.ragium.api.data.tag

import hiiragi283.ragium.api.collection.HTMultiMap
import hiiragi283.ragium.api.extension.multiMapOf
import hiiragi283.ragium.api.extension.toId
import hiiragi283.ragium.api.registry.HTHolderLike
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import java.util.function.BiConsumer

/**
 * 登録した[TagKey]をソートして生成するビルダー
 */
@Suppress("UNCHECKED_CAST")
class HTTagBuilder<T : Any>(private val registryKey: ResourceKey<out Registry<T>>) {
    private val entryCache: HTMultiMap.Mutable<TagKey<T>, Entry> = multiMapOf()

    fun addOptional(tagKey: TagKey<T>, modId: String, path: String): HTTagBuilder<T> = add(tagKey, modId.toId(path), DependType.OPTIONAL)

    fun add(tagKey: TagKey<T>, key: ResourceKey<T>, type: DependType = DependType.REQUIRED): HTTagBuilder<T> =
        add(tagKey, key.location(), type)

    fun add(tagKey: TagKey<T>, holder: HTHolderLike, type: DependType = DependType.REQUIRED): HTTagBuilder<T> =
        add(tagKey, holder.getId(), type)

    fun add(tagKey: TagKey<T>, id: ResourceLocation, type: DependType = DependType.REQUIRED): HTTagBuilder<T> = apply {
        entryCache.put(tagKey, Entry(id, false, type))
    }

    fun addTag(tagKey: TagKey<T>, child: ResourceLocation, type: DependType = DependType.REQUIRED): HTTagBuilder<T> =
        addTag(tagKey, TagKey.create(registryKey, child), type)

    fun addTag(tagKey: TagKey<T>, child: TagKey<T>, type: DependType = DependType.REQUIRED): HTTagBuilder<T> = apply {
        entryCache.put(tagKey, Entry(child.location, true, type))
    }

    fun build(action: BiConsumer<TagKey<T>, TagEntry>) {
        entryCache.map.forEach { (tagKey: TagKey<T>, entries: Collection<Entry>) ->
            entries
                .sortedWith(
                    Comparator
                        .comparing(Entry::isTag, Comparator.reverseOrder())
                        .thenComparing(Entry::type)
                        .thenComparing(Entry::id),
                ).toSet()
                .forEach { entry: Entry ->
                    action.accept(tagKey, entry.toTagEntry())
                }
        }
    }

    enum class DependType {
        REQUIRED,
        OPTIONAL,
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
