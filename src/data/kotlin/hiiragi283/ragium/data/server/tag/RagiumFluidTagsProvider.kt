package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.setup.RagiumFluidContents
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumFluidTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    HTTagsProvider<Fluid>(
        output,
        Registries.FLUID,
        provider,
        helper,
    ) {
    override fun addTags(builder: HTTagBuilder<Fluid>) {
        contents(builder)
        category(builder)

        pneumatic(builder)
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

    private fun pneumatic(builder: HTTagBuilder<Fluid>) {
        fun addTag(tagKey: TagKey<Fluid>, content: HTFluidContent<*, *, *>) {
            builder.addContent(tagKey, content)
            builder.addTag(content.commonTag, tagKey, HTTagBuilder.DependType.OPTIONAL)
        }

        addTag(PneumaticCraftTags.Fluids.CRUDE_OIL, RagiumFluidContents.CRUDE_OIL)
        addTag(PneumaticCraftTags.Fluids.DIESEL, RagiumFluidContents.DIESEL)
        addTag(PneumaticCraftTags.Fluids.LPG, RagiumFluidContents.LPG)
        addTag(PneumaticCraftTags.Fluids.LUBRICANT, RagiumFluidContents.LUBRICANT)
    }

    //    Extensions    //

    private fun HTTagBuilder<Fluid>.addContent(tagKey: TagKey<Fluid>, content: HTFluidContent<*, *, *>) {
        add(tagKey, content.getStill(), BuiltInRegistries.FLUID::getKey)
        add(tagKey, content.getFlow(), BuiltInRegistries.FLUID::getKey)
    }
}
