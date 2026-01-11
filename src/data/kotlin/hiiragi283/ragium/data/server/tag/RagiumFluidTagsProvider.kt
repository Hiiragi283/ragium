package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid

class RagiumFluidTagsProvider(context: HTDataGenContext) : HTTagsProvider<Fluid>(RagiumAPI.MOD_ID, Registries.FLUID, context) {
    override fun addTagsInternal(factory: BuilderFactory<Fluid>) {
        RagiumFluids.REGISTER
            .asSequence()
            .forEach(::addContent.partially1(factory))
    }

    fun addContent(factory: BuilderFactory<Fluid>, content: HTFluidContent<*, *, *>) {
        val builder: HTTagBuilder<Fluid> = factory.apply(content.fluidTag).add(content)
        if (content is HTFluidContent.Flowing<*, *, *, *>) {
            builder.add(content.flowingHolder)
        }
    }
}
