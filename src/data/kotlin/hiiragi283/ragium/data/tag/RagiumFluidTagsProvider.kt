package hiiragi283.ragium.data.tag

import hiiragi283.lib.data.tag.HTFluidTagsProvider
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.fluid.RagiumFluids
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput

class RagiumFluidTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTFluidTagsProvider(output, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        addContents(RagiumFluids.REGISTER.asSequence())

        builder(HTCommonTags.Fluids.ALKALI_SOLUTION)
            .addContentTag(RagiumFluids.NAOH_SOLUTION)
            .addContentTag(RagiumFluids.CAOH_SOLUTION)

        /*builder(HiiragiCoreTags.Fluids.ELDRITCH)
            .add(createKey("oritech", "still_strange_matter"), HTTagDependType.OPTIONAL)
            .addContentTag(HCFluids.OMINOUS_FLUX)*/
    }
}
