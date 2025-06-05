package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addContent
import hiiragi283.ragium.api.extension.asFluidHolder
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumFluidTagProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    IntrinsicHolderTagsProvider<Fluid>(
        output,
        Registries.FLUID,
        provider,
        { fluid: Fluid -> fluid.asFluidHolder().key() },
        RagiumAPI.MOD_ID,
        helper,
    ) {
    private fun addFluid(tagKey: TagKey<Fluid>, content: HTFluidContent<*, *, *>) {
        tag(tagKey).addContent(content)
    }

    override fun addTags(provider: HolderLookup.Provider) {
        contents()
        category()

        pneumatic()
    }

    private fun contents() {
        // Common Tag
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            addFluid(content.commonTag, content)
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

        tag(RagiumFluidTags.CHOCOLATES).addContent(RagiumFluidContents.CHOCOLATE)

        tag(RagiumFluidTags.NITRO_FUEL)
            .addOptionalTag(commonId("boosted_diesel"))
            .addOptionalTag(commonId("high_power_biodiesel"))
        // addTag(RagiumFluidTags.NITRO_FUEL, RagiumFluidContents.NITRO_FUEL.commonTag)

        tag(RagiumFluidTags.NON_NITRO_FUEL)
            .addOptionalTag(commonId("biofuel"))
            .addOptionalTag(commonId("heavy_fuel"))
            .addOptionalTag(commonId("light_fuel"))
        // addTag(RagiumFluidTags.NON_NITRO_FUEL, RagiumFluidContents.FUEL.commonTag)

        tag(RagiumFluidTags.THERMAL_FUEL)
            .addOptionalTag(commonId("steam"))
            .addOptionalTag(commonId("superheated_sodium"))
            .addTag(Tags.Fluids.LAVA)
    }

    //    Integrations    //

    private fun pneumatic() {
        // addFluid(PneumaticCraftTags.Fluids.CRUDE_OIL, RagiumFluidContents.CRUDE_OIL)
        // addFluid(PneumaticCraftTags.Fluids.PLANT_OIL, RagiumFluidContents.PLANT_OIL)
    }
}
