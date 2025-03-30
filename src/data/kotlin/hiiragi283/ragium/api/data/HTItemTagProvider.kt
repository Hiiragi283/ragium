package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.multiMapOf
import hiiragi283.ragium.api.util.HTMultiMap
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

abstract class HTItemTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    blockTags: HTTagProvider<Block>,
    helper: ExistingFileHelper,
) : ItemTagsProvider(output, provider, blockTags.contentsGetter(), RagiumAPI.MOD_ID, helper),
    HTTagBuilder.ItemTag {
    final override fun addTags(provider: HolderLookup.Provider) {
        addTagsInternal(provider)

        entryCache.map.forEach { (tagKey: TagKey<Item>, entries: Collection<HTTagBuilder.Entry>) ->
            entries.sortedBy(HTTagBuilder.Entry::id).toSet().forEach { entry: HTTagBuilder.Entry ->
                tag(tagKey).add(entry.toTagEntry())
            }
        }
    }

    protected abstract fun addTagsInternal(provider: HolderLookup.Provider)

    //    HTTagBuilder    //

    private val entryCache: HTMultiMap.Mutable<TagKey<Item>, HTTagBuilder.Entry> = multiMapOf()

    final override val registryKey: ResourceKey<out Registry<Item>> = Registries.ITEM

    final override fun add(tagKey: TagKey<Item>, entry: HTTagBuilder.Entry) {
        entryCache.put(tagKey, entry)
    }

    override fun copyFromBlock(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        copy(blockTag, itemTag)
    }
}
