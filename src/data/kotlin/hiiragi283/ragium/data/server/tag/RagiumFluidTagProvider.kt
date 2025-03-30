package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.data.HTTagProvider
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumFluidContents
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumFluidTagProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    HTTagProvider<Fluid>(Registries.FLUID, output, provider, helper) {
    private fun addFluid(tagKey: TagKey<Fluid>, content: HTFluidContent<*, *, *>) {
        add(tagKey, content.stillHolder)
        add(tagKey, content.flowHolder)
    }

    override fun addTagsInternal(provider: HolderLookup.Provider) {
        addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.HYDROGEN)
        addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.NITROGEN)
        addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.AMMONIA)
        addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.OXYGEN)
        addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.ROCKET_FUEL)
        addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.SULFUR_DIOXIDE)
        addFluid(Tags.Fluids.GASEOUS, RagiumFluidContents.SULFUR_TRIOXIDE)

        add(RagiumFluidTags.CHOCOLATES, RagiumFluidContents.CHOCOLATE.stillHolder)
        add(RagiumFluidTags.CHOCOLATES, RagiumFluidContents.CHOCOLATE.flowHolder)

        addTag(RagiumFluidTags.NITRO_FUEL, commonId("boosted_diesel"), HTTagBuilder.DependType.OPTIONAL)
        addTag(RagiumFluidTags.NITRO_FUEL, commonId("high_power_biodiesel"), HTTagBuilder.DependType.OPTIONAL)
        addTag(RagiumFluidTags.NITRO_FUEL, RagiumFluidContents.NITRO_FUEL.commonTag)

        addTag(RagiumFluidTags.NON_NITRO_FUEL, commonId("biofuel"), HTTagBuilder.DependType.OPTIONAL)
        addTag(RagiumFluidTags.NON_NITRO_FUEL, commonId("heavy_fuel"), HTTagBuilder.DependType.OPTIONAL)
        addTag(RagiumFluidTags.NON_NITRO_FUEL, commonId("light_fuel"), HTTagBuilder.DependType.OPTIONAL)
        addTag(RagiumFluidTags.NON_NITRO_FUEL, RagiumFluidContents.BIODIESEL.commonTag)
        addTag(RagiumFluidTags.NON_NITRO_FUEL, RagiumFluidContents.FUEL.commonTag)

        addTag(RagiumFluidTags.THERMAL_FUEL, commonId("steam"), HTTagBuilder.DependType.OPTIONAL)
        addTag(RagiumFluidTags.THERMAL_FUEL, commonId("superheated_sodium"), HTTagBuilder.DependType.OPTIONAL)
        addTag(RagiumFluidTags.THERMAL_FUEL, Tags.Fluids.LAVA)

        contents()
    }

    private fun contents() {
        // Common Tag
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            addFluid(content.commonTag, content)
        }
    }
}
