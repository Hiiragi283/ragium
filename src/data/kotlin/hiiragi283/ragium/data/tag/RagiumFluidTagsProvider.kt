package hiiragi283.ragium.data.tag

import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.tag.RawTagKey
import hiiragi283.core.support.data.tag.HTFluidTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumFluidTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : HTFluidTagsProvider(fileHelper, output, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        addContents(RagiumFluids.REGISTER.asSequence())

        // Chemical
        builder(RagiumTags.Fluids.ALCOHOL)
            .addContentTag(RagiumFluids.ETHANOL)
            .addTag(RawTagKey.common("alcohol"), HTTagDependType.OPTIONAL)
            .addTag(RawTagKey.common("bioethanol"), HTTagDependType.OPTIONAL)
        // Other
        builder(RagiumTags.Fluids.BIODIESEL)
            .addContentTag(RagiumFluids.BIOFUEL)
            .addTag(RawTagKey.common("biodiesel"), HTTagDependType.OPTIONAL)

        builder(RagiumTags.Fluids.DIESEL)
            .addContentTag(RagiumFluids.FUEL)
            .addTag(RawTagKey.common("diesel"), HTTagDependType.OPTIONAL)

        builder(RagiumTags.Fluids.LIQUID_MATTER)
            .addContentTag(RagiumFluids.RAGI_MATTER)
            .addTag(RawTagKey.common("uu_matter"), HTTagDependType.OPTIONAL)
    }
}
