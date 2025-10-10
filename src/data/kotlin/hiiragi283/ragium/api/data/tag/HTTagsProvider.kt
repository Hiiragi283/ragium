package hiiragi283.ragium.api.data.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.collection.buildMultiMap
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagBuilder.Entry
import hiiragi283.ragium.api.registry.RegistryKey
import net.minecraft.core.HolderLookup
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagKey
import kotlin.collections.forEach

abstract class HTTagsProvider<T : Any>(registryKey: RegistryKey<T>, context: HTDataGenContext) :
    TagsProvider<T>(
        context.output,
        registryKey,
        context.registries,
        RagiumAPI.MOD_ID,
        context.fileHelper,
    ) {
    @Suppress("DEPRECATION")
    final override fun addTags(provider: HolderLookup.Provider) {
        buildMultiMap { HTTagBuilder(this).apply(::addTags) }
            .map
            .forEach { (tagKey: TagKey<T>, entries: Collection<Entry>) ->
                entries
                    .sortedWith(Entry.COMPARATOR)
                    .toSet()
                    .forEach { entry: Entry ->
                        tag(tagKey).add(entry.toTagEntry())
                    }
            }
    }

    protected abstract fun addTags(builder: HTTagBuilder<T>)

    @Deprecated("Use `HTTagBuilder` instead")
    override fun tag(tag: TagKey<T>): TagAppender<T> = super.tag(tag)
}
