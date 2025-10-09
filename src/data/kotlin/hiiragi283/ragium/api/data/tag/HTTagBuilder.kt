package hiiragi283.ragium.api.data.tag

import hiiragi283.ragium.api.collection.ImmutableMultiMap
import hiiragi283.ragium.api.registry.HTHolderLike
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey

/**
 * 登録した[TagKey]をソートして生成するビルダー
 */
@JvmInline
value class HTTagBuilder<T : Any>(private val entryCache: ImmutableMultiMap.Builder<TagKey<T>, Entry>) {
    fun add(tagKey: TagKey<T>, key: ResourceKey<T>, type: DependType = DependType.REQUIRED): HTTagBuilder<T> =
        add(tagKey, key.location(), type)

    fun add(tagKey: TagKey<T>, holder: HTHolderLike, type: DependType = DependType.REQUIRED): HTTagBuilder<T> =
        add(tagKey, holder.getId(), type)

    fun add(tagKey: TagKey<T>, id: ResourceLocation, type: DependType = DependType.REQUIRED): HTTagBuilder<T> = apply {
        entryCache[tagKey] = Entry(id, false, type)
    }

    fun addTag(tagKey: TagKey<T>, child: TagKey<T>, type: DependType = DependType.REQUIRED): HTTagBuilder<T> = apply {
        entryCache[tagKey] = Entry(child.location, true, type)
    }

    enum class DependType {
        REQUIRED,
        OPTIONAL,
    }

    @JvmRecord
    data class Entry(val id: ResourceLocation, val isTag: Boolean, val type: DependType) {
        companion object {
            @JvmField
            val COMPARATOR: Comparator<Entry> = Comparator
                .comparing(Entry::isTag, Comparator.reverseOrder())
                .thenComparing(Entry::type)
                .thenComparing(Entry::id)
        }

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
