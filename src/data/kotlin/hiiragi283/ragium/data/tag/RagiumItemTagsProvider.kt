package hiiragi283.ragium.data.tag

import hiiragi283.lib.data.tag.HTItemTagsProvider
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.init.RagiumFluids
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags

class RagiumItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, contentsGetter: CompletableFuture<TagLookup<Block>>) : HTItemTagsProvider(output, lookupProvider, contentsGetter, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        for (content: HTFluidContent in RagiumFluids.REGISTER.asSequence()) {
            tags(Tags.Items.BUCKETS, content.bucketTag).add(content.bucketHolder)
        }
    }
}
