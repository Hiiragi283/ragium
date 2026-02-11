package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTFluidTagsProvider
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.level.material.Fluid

class RagiumFluidTagsProvider(context: HTDataGenContext) : HTFluidTagsProvider(RagiumAPI.MOD_ID, context) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Fluid>) {
        addContents(factory, RagiumFluids.REGISTER.asSequence())

        factory
            .apply(RagiumTags.Fluids.ALCOHOL)
            .addContent(RagiumFluids.METHANOL)
            .addContent(RagiumFluids.ETHANOL)

        factory
            .apply(RagiumTags.Fluids.BIODIESEL)
            .addContent(RagiumFluids.BIOFUEL)
        factory
            .apply(RagiumTags.Fluids.DIESEL)
            .addContent(RagiumFluids.FUEL)
    }
}
