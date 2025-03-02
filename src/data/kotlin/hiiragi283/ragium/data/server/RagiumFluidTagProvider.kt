package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
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
    lateinit var builder: HTTagBuilder<Fluid>

    override fun addTags(provider: HolderLookup.Provider) {
        builder = HTTagBuilder(provider.fluidLookup())

        RagiumFluids.REGISTER.forEach { holder: DeferredHolder<Fluid, out Fluid> ->
            if (!holder.get().isSource) return@forEach
            // Common Tag
            builder.add(holder.commonTag, holder)
        }

        for (fluid: RagiumVirtualFluids in RagiumVirtualFluids.entries) {
            // Gaseous Tag
            if (fluid.textureType == RagiumVirtualFluids.TextureType.GASEOUS) {
                builder.addTag(Tags.Fluids.GASEOUS, fluid.commonTag)
            }
        }

        builder.add(RagiumFluids.CRUDE_OIL.commonTag, RagiumFluids.FLOWING_CRUDE_OIL)

        builder.add(RagiumFluidTags.CHOCOLATES, RagiumVirtualFluids.CHOCOLATE.fluidHolder)

        builder.addTag(RagiumFluidTags.NITRO_FUEL, commonId("boosted_diesel"), HTTagBuilder.DependType.OPTIONAL)
        builder.addTag(RagiumFluidTags.NITRO_FUEL, commonId("high_power_biodiesel"), HTTagBuilder.DependType.OPTIONAL)
        builder.addTag(RagiumFluidTags.NITRO_FUEL, RagiumVirtualFluids.NITRO_FUEL.fluidHolder.commonTag)

        builder.addTag(RagiumFluidTags.NON_NITRO_FUEL, commonId("biofuel"), HTTagBuilder.DependType.OPTIONAL)
        builder.addTag(RagiumFluidTags.NON_NITRO_FUEL, commonId("heavy_fuel"), HTTagBuilder.DependType.OPTIONAL)
        builder.addTag(RagiumFluidTags.NON_NITRO_FUEL, commonId("light_fuel"), HTTagBuilder.DependType.OPTIONAL)
        builder.addTag(RagiumFluidTags.NON_NITRO_FUEL, RagiumVirtualFluids.BIODIESEL.fluidHolder.commonTag)
        builder.addTag(RagiumFluidTags.NON_NITRO_FUEL, RagiumVirtualFluids.FUEL.fluidHolder.commonTag)

        builder.addTag(RagiumFluidTags.THERMAL_FUEL, commonId("steam"), HTTagBuilder.DependType.OPTIONAL)
        builder.addTag(RagiumFluidTags.THERMAL_FUEL, commonId("superheated_sodium"), HTTagBuilder.DependType.OPTIONAL)
        builder.addTag(RagiumFluidTags.THERMAL_FUEL, Tags.Fluids.LAVA)

        builder.build { tagKey: TagKey<Fluid>, entry: TagEntry ->
            tag(tagKey).add(entry)
        }
    }
}
