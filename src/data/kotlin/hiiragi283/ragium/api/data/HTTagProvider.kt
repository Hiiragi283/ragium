package hiiragi283.ragium.api.data

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

abstract class HTTagProvider<T : Any>(
    private val registry: ResourceKey<out Registry<T>>,
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    helper: ExistingFileHelper,
) : TagsProvider<T>(output, registry, provider, RagiumAPI.MOD_ID, helper) {
    final override fun addTags(provider: HolderLookup.Provider) {
        val builder: HTTagBuilder<T> = HTTagBuilder(registry)

        addTagsInternal(builder, provider)

        builder.build { tagKey: TagKey<T>, entry: TagEntry ->
            tag(tagKey).add(entry)
        }
    }

    protected abstract fun addTagsInternal(builder: HTTagBuilder<T>, provider: HolderLookup.Provider)
}
