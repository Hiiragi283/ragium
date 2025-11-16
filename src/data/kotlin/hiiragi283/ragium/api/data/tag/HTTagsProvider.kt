package hiiragi283.ragium.api.data.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.collection.buildMultiMap
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.registry.RegistryKey
import net.minecraft.core.HolderLookup
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import kotlin.collections.forEach

/**
 * [HTTagBuilder]に基づいた[TagsProvider]の拡張クラス
 */
abstract class HTTagsProvider<T : Any>(registryKey: RegistryKey<T>, context: HTDataGenContext) :
    TagsProvider<T>(
        context.output,
        registryKey,
        context.registries,
        RagiumAPI.MOD_ID,
        context.fileHelper,
    ) {
    companion object {
        @JvmField
        val COMPARATOR: Comparator<TagEntry> = Comparator
            .comparing(TagEntry::isTag, Comparator.reverseOrder())
            .thenComparing(TagEntry::isRequired)
            .thenComparing(TagEntry::getId)
    }

    @Suppress("DEPRECATION")
    final override fun addTags(provider: HolderLookup.Provider) {
        buildMultiMap { HTTagBuilder(this@HTTagsProvider.registryKey, this).apply(::addTags) }
            .map
            .forEach { (tagKey: TagKey<T>, entries: Collection<TagEntry>) ->
                entries
                    .sortedWith(COMPARATOR)
                    .toSet()
                    .forEach { entry: TagEntry -> tag(tagKey).add(entry) }
            }
    }

    /**
     * 指定された[builder]にタグを登録します。
     */
    protected abstract fun addTags(builder: HTTagBuilder<T>)

    @Deprecated("Use `HTTagBuilder` instead")
    override fun tag(tag: TagKey<T>): TagAppender<T> = super.tag(tag)
}
