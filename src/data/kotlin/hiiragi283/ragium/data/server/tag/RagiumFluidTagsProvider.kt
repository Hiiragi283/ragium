package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid

class RagiumFluidTagsProvider(context: HTDataGenContext) : HTTagsProvider<Fluid>(RagiumAPI.MOD_ID, Registries.FLUID, context) {
    override fun addTagsInternal(factory: BuilderFactory<Fluid>) {
        for (content: HTFluidContent<*, *, *> in RagiumFluids.REGISTER.asSequence()) {
            factory.apply(content.fluidTag).addContent(content)
        }

        factory
            .apply(RagiumTags.Fluids.ALCOHOL)
            .addContent(RagiumFluids.METHANOL)
            .addContent(RagiumFluids.ETHANOL)
    }

    fun HTTagBuilder<Fluid>.addContent(content: HTFluidContent<*, *, *>): HTTagBuilder<Fluid> {
        this.add(content)
        if (content is HTFluidContent.Flowing<*, *, *, *>) {
            this.add(content.flowingHolder)
        }
        return this
    }
}
