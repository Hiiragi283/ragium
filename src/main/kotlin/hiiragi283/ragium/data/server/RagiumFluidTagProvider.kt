package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumFluidsNew
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

        tag(Tags.Fluids.HONEY)
            .add(RagiumFluidsNew.HONEY)

        tag(RagiumFluidsNew.SNOW.commonTag)
            .add(RagiumFluidsNew.SNOW)

        tag(RagiumFluidsNew.CRUDE_OIL.commonTag)
            .add(RagiumFluidsNew.CRUDE_OIL)
            .add(RagiumFluidsNew.FLOWING_CRUDE_OIL)

        RagiumFluids.REGISTER.entries.forEach { holder: DeferredHolder<Fluid, out Fluid> ->
            // Common Tag
            tag(holder.commonTag).add(holder.keyOrThrow)
        }

        tag(RagiumFluidTags.NITRO_FUEL)
            .addTag(fluidTagKey(commonId("boosted_diesel")), true)
            .addTag(fluidTagKey(commonId("high_power_biodiesel")), true)
            .addTag(RagiumFluids.NITRO_FUEL.commonTag)

        tag(RagiumFluidTags.NON_NITRO_FUEL)
            .addTag(fluidTagKey(commonId("biofuel")), true)
            .addTag(fluidTagKey(commonId("heavy_fuel")), true)
            .addTag(fluidTagKey(commonId("light_fuel")), true)
            .addTag(RagiumFluids.BIODIESEL.commonTag)
            .addTag(RagiumFluids.FUEL.commonTag)

        tag(RagiumFluidTags.THERMAL_FUEL)
            .addTag(fluidTagKey(commonId("steam")), true)
            .addTag(fluidTagKey(commonId("superheated_sodium")), true)
            .addTag(Tags.Fluids.LAVA)
    }
}
