package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid

class RagiumFluidTagsProvider(context: HTDataGenContext) : HTTagsProvider<Fluid>(RagiumAPI.MOD_ID, Registries.FLUID, context) {
    override fun addTagsInternal(factory: BuilderFactory<Fluid>) {
        for (content: HTFluidContent<*, *, *> in RagiumFluids.REGISTER.entries) {
            factory
                .apply(content.fluidTag)
                .add(content.stillHolder)
                .add(content.flowingHolder)
        }
    }
}
