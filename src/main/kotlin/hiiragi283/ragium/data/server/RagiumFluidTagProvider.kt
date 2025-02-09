package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.concurrent.CompletableFuture

class RagiumFluidTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper,
) : TagsProvider<Fluid>(output, Registries.FLUID, provider, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        val gasBuilder: TagAppender<Fluid> = tag(Tags.Fluids.GASEOUS)

        RagiumFluids.REGISTER.forEach { holder: DeferredHolder<Fluid, out Fluid> ->
            if (!holder.get().isSource) return@forEach
            // Common Tag
            tag(holder.commonTag).add(holder.keyOrThrow)
        }

        tag(RagiumFluids.CRUDE_OIL.commonTag)
            .add(RagiumFluids.FLOWING_CRUDE_OIL)

        tag(RagiumFluidTags.NITRO_FUEL)
            .addOptionalTag(commonId("boosted_diesel"))
            .addOptionalTag(commonId("high_power_biodiesel"))
            .addTag(RagiumVirtualFluids.NITRO_FUEL.fluidHolder.commonTag)

        tag(RagiumFluidTags.NON_NITRO_FUEL)
            .addOptionalTag(commonId("biofuel"))
            .addOptionalTag(commonId("heavy_fuel"))
            .addOptionalTag(commonId("light_fuel"))
            .addTag(RagiumVirtualFluids.BIODIESEL.fluidHolder.commonTag)
            .addTag(RagiumVirtualFluids.FUEL.fluidHolder.commonTag)

        tag(RagiumFluidTags.THERMAL_FUEL)
            .addOptionalTag(commonId("steam"))
            .addOptionalTag(commonId("superheated_sodium"))
            .addTag(Tags.Fluids.LAVA)
    }
}
