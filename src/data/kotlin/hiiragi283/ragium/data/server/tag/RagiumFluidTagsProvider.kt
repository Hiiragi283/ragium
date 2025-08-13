package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.asFluidHolder
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.setup.RagiumFluidContents
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumFluidTagsProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    IntrinsicHolderTagsProvider<Fluid>(
        output,
        Registries.FLUID,
        provider,
        { fluid: Fluid -> fluid.asFluidHolder().key() },
        RagiumAPI.MOD_ID,
        helper,
    ) {
    override fun addTags(provider: HolderLookup.Provider) {
        contents()
        category()

        pneumatic()
    }

    private fun contents() {
        // Common Tag
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            tag(content.commonTag).addContent(content)
        }
    }

    private fun category() {
    }

    //    Integrations    //

    private fun pneumatic() {
        fun addTag(tagKey: TagKey<Fluid>, content: HTFluidContent<*, *, *>) {
            tag(tagKey).addContent(content)
            tag(content).addOptionalTag(tagKey)
        }

        addTag(PneumaticCraftTags.Fluids.CRUDE_OIL, RagiumFluidContents.CRUDE_OIL)
        addTag(PneumaticCraftTags.Fluids.DIESEL, RagiumFluidContents.DIESEL)
        addTag(PneumaticCraftTags.Fluids.LPG, RagiumFluidContents.LPG)
        addTag(PneumaticCraftTags.Fluids.LUBRICANT, RagiumFluidContents.LUBRICANT)
    }

    //    Extensions    //

    private fun IntrinsicTagAppender<Fluid>.addContent(content: HTFluidContent<*, *, *>) {
        add(content.getStill())
        add(content.getFlow())
    }

    private fun tag(content: HTFluidContent<*, *, *>): IntrinsicTagAppender<Fluid> = tag(content.commonTag)
}
