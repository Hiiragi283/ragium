package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid

class RagiumFluidTagsProvider(context: HTDataGenContext) : HTTagsProvider<Fluid>(Registries.FLUID, context) {
    override fun addTags(builder: HTTagBuilder<Fluid>) {
        contents(builder)
        category(builder)
    }

    private fun contents(builder: HTTagBuilder<Fluid>) {
        // Common Tag
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            builder.addContent(content.commonTag, content)
        }
    }

    private fun category(builder: HTTagBuilder<Fluid>) {
    }

    //    Integrations    //

    //    Extensions    //

    private fun HTTagBuilder<Fluid>.addContent(tagKey: TagKey<Fluid>, content: HTFluidContent<*, *, *>) {
        add(tagKey, HTHolderLike.fromFluid(content.getStill()))
        add(tagKey, HTHolderLike.fromFluid(content.getFlow()))
    }
}
