package hiiragi283.ragium.api.data.tag

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

abstract class HTTagsProvider<T : Any>(
    output: PackOutput,
    private val registryKey: ResourceKey<out Registry<T>>,
    provider: CompletableFuture<HolderLookup.Provider>,
    helper: ExistingFileHelper,
) : TagsProvider<T>(
        output,
        registryKey,
        provider,
        RagiumAPI.MOD_ID,
        helper,
    ) {
    final override fun addTags(provider: HolderLookup.Provider) {
        HTTagBuilder(registryKey).apply(::addTags).build { tagKey: TagKey<T>, entry: TagEntry ->
            tag(tagKey).add(entry)
        }
    }

    protected abstract fun addTags(builder: HTTagBuilder<T>)
}
