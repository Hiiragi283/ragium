package hiiragi283.ragium.data.tag

import hiiragi283.lib.data.tag.HTFluidTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.fluid.RagiumFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class RagiumFluidTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    HTFluidTagsProvider(output, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        addContents(RagiumFluids.REGISTER.asSequence())
    }
}
