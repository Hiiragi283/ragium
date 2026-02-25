package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTFluidTagsProvider
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.level.material.Fluid

class RagiumFluidTagsProvider(context: HTDataGenContext) : HTFluidTagsProvider(RagiumAPI.MOD_ID, context) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Fluid>) {
        addContents(factory, RagiumFluids.REGISTER.asSequence())

        // Chemical
        factory
            .apply(RagiumTags.Fluids.ALCOHOL)
            .addContentTag(RagiumFluids.METHANOL)
            .addContentTag(RagiumFluids.ETHANOL)
            .addTag(commonTag("alcohol"), HTTagDependType.OPTIONAL)
            .addTag(commonTag("bioethanol"), HTTagDependType.OPTIONAL)

        factory
            .apply(RagiumTags.Fluids.ALDEHYDE)
            .addContentTag(RagiumFluids.METHANAL)
        factory
            .apply(RagiumTags.Fluids.CARBOXYLIC_ACID)
            .addContentTag(RagiumFluids.METHANOIC_ACID)
        // Other
        factory
            .apply(RagiumTags.Fluids.BIODIESEL)
            .addContentTag(RagiumFluids.BIOFUEL)
            .addTag(commonTag("biodiesel"), HTTagDependType.OPTIONAL)

        factory
            .apply(RagiumTags.Fluids.DIESEL)
            .addContentTag(RagiumFluids.FUEL)
            .addTag(commonTag("diesel"), HTTagDependType.OPTIONAL)
    }
}
