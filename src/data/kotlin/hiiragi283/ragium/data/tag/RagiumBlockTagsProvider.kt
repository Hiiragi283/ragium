package hiiragi283.ragium.data.tag

import hiiragi283.lib.data.tag.HTTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block

class RagiumBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTTagsProvider<Block>(output, Registries.BLOCK, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
    }
}
