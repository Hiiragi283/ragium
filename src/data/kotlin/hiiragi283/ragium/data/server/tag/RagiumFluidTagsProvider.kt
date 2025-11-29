package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

class RagiumFluidTagsProvider(context: HTDataGenContext) : HTTagsProvider<Fluid>(Registries.FLUID, context) {
    override fun addTagsInternal(factory: BuilderFactory<Fluid>) {
        contents(factory)
        category(factory)
    }

    private fun contents(factory: BuilderFactory<Fluid>) {
        // Common Tag
        for (content: HTFluidContent<*, *, *, *, *> in RagiumFluidContents.REGISTER.contents) {
            factory
                .apply(content.commonTag)
                .add(content.still)
        }
    }

    private fun category(factory: BuilderFactory<Fluid>) {
        factory
            .apply(Tags.Fluids.GASEOUS)
            .add(RagiumFluidContents.NATURAL_GAS)
    }
}
