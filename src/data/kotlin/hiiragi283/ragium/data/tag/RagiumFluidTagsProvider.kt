package hiiragi283.ragium.data.tag

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.tag.HTFluidTagsProvider
import hiiragi283.lib.data.tag.HTTagDependType
import hiiragi283.lib.resource.toId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class RagiumFluidTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    HTFluidTagsProvider(output, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        addContents(RagiumFluids.REGISTER.asSequence())

        builder(RagiumTags.Fluids.RESINS)
            .addContentTag(RagiumFluids.RESIN)
            .addContentTag(RagiumFluids.SYNTHETIC_RESIN)
        builder(RagiumTags.Fluids.ALCOHOLS)
            .addContentTag(RagiumFluids.ALCOHOL)
            .addTag(HTConstants.COMMON.toId("methanol"), type = HTTagDependType.OPTIONAL)
            .addTag(HTConstants.COMMON.toId("ethanol"), type = HTTagDependType.OPTIONAL)
            .addTag(HTConstants.COMMON.toId("bioethanol"), type = HTTagDependType.OPTIONAL)
            .addTag(HTConstants.COMMON.toId("bio_ethanol"), type = HTTagDependType.OPTIONAL)
        builder(RagiumTags.Fluids.ALDEHYDES)
            .addContentTag(RagiumFluids.ALDEHYDE)
            .addTag(HTConstants.COMMON.toId("formaldehyde"), type = HTTagDependType.OPTIONAL)
            .addTag(HTConstants.COMMON.toId("acetaldehyde"), type = HTTagDependType.OPTIONAL)
    }
}
