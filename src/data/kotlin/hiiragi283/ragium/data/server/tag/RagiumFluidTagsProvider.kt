package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addHolder
import hiiragi283.ragium.api.extension.asFluidHolder
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumFluidContents
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
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
        // addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.HYDROGEN)
        // addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.NITROGEN)
        // addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.AMMONIA)
        // addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.OXYGEN)
        // addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.ROCKET_FUEL)
        // addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.SULFUR_DIOXIDE)
        // addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.SULFUR_TRIOXIDE)

        tag(RagiumModTags.Fluids.FUELS_NITRO)
            .addOptionalTag(commonId("boosted_diesel"))
            .addOptionalTag(commonId("high_power_biodiesel"))
        // addTag(RagiumFluidTags.NITRO_FUEL, RagiumFluidContents.NITRO_FUEL.commonTag)

        tag(RagiumModTags.Fluids.FUELS_NON_NITRO)
            .addOptionalTag(commonId("biofuel"))
            .addOptionalTag(commonId("heavy_fuel"))
            .addOptionalTag(commonId("light_fuel"))
        // addTag(RagiumFluidTags.NON_NITRO_FUEL, RagiumFluidContents.FUEL.commonTag)

        tag(RagiumModTags.Fluids.FUELS_THERMAL)
            .addTag(Tags.Fluids.LAVA)
            .addOptionalTag(commonId("steam"))
            .addOptionalTag(commonId("superheated_sodium"))
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
        addHolder(content.stillHolder).addHolder(content.flowHolder)
    }

    private fun tag(content: HTFluidContent<*, *, *>): IntrinsicTagAppender<Fluid> = tag(content.commonTag)
}
