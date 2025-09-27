package hiiragi283.ragium.api.data.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.RegistryKey
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

abstract class HTTagsProvider<T : Any>(
    output: PackOutput,
    private val registryKey: RegistryKey<T>,
    provider: CompletableFuture<HolderLookup.Provider>,
    helper: ExistingFileHelper,
) : TagsProvider<T>(
        output,
        registryKey,
        provider,
        RagiumAPI.MOD_ID,
        helper,
    ) {
    @Suppress("DEPRECATION")
    final override fun addTags(provider: HolderLookup.Provider) {
        HTTagBuilder(registryKey).apply(::addTags).build { tagKey: TagKey<T>, entry: TagEntry ->
            tag(tagKey).add(entry)
        }
    }

    protected abstract fun addTags(builder: HTTagBuilder<T>)

    @Deprecated("Use `HTTagBuilder` instead")
    override fun tag(tag: TagKey<T>): TagAppender<T> = super.tag(tag)
}
