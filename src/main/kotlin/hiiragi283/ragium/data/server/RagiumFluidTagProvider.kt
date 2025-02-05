package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.extension.addTag
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumFluids
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumFluidTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper,
) : TagsProvider<Fluid>(output, Registries.FLUID, provider, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        val gasBuilder: TagAppender<Fluid> = tag(Tags.Fluids.GASEOUS)

        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            // Common Tag
            tag(fluid.commonTag).add(fluid.fluidHolder)
            // Gaseous Tag
            if (fluid.get().fluidType.isLighterThanAir) {
                gasBuilder.add(fluid.fluidHolder)
            }
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
