package hiiragi283.ragium.common.datagen.tag

import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid

data object RagiumFluidTagsProvider : HTTagsProvider<Fluid>(Registries.FLUID) {
    override fun addTagsInternal(factory: BuilderFactory<Fluid>) {
        for (content: HTFluidContent in RagiumFluids.REGISTER.asSequence()) {
            factory.apply(content.fluidTag).addContent(content)
        }

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

    fun HTTagBuilder<Fluid>.addContent(content: HTFluidContent): HTTagBuilder<Fluid> {
        this.add(content)
        content.flowingHolder?.let(this::add)
        return this
    }
}
