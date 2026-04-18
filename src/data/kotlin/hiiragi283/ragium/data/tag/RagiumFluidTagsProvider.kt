package hiiragi283.ragium.data.tag

import hiiragi283.core.api.data.tag.HTFluidTagsProvider
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumFluidTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : HTFluidTagsProvider(fileHelper, output, lookupProvider, RagiumAPI.MOD_ID) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Fluid>) {
        addContents(factory, RagiumFluids.REGISTER.asSequence())

        // Chemical
        factory
            .apply(RagiumTags.Fluids.ALCOHOL)
            .addContentTag(RagiumFluids.ETHANOL)
            .addTag(commonTag("alcohol"), HTTagDependType.OPTIONAL)
            .addTag(commonTag("bioethanol"), HTTagDependType.OPTIONAL)
        // Other
        factory
            .apply(RagiumTags.Fluids.BIODIESEL)
            .addContentTag(RagiumFluids.BIOFUEL)
            .addTag(commonTag("biodiesel"), HTTagDependType.OPTIONAL)

        factory
            .apply(RagiumTags.Fluids.DIESEL)
            .addContentTag(RagiumFluids.FUEL)
            .addTag(commonTag("diesel"), HTTagDependType.OPTIONAL)

        factory.apply(RagiumTags.Fluids.IS_MATTER).addContentTag(RagiumFluids.RAGI_MATTER)
    }
}
