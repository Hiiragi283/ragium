package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.data.addElement
import hiiragi283.ragium.data.addTag
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagKey
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
        fun add(tagKey: TagKey<Fluid>, fluid: RagiumFluids) {
            getOrCreateRawBuilder(tagKey).addElement(fluid.fluidHolder)
        }

        fun add(tagKey: TagKey<Fluid>, fluid: TagKey<Fluid>) {
            getOrCreateRawBuilder(tagKey).addTag(fluid)
        }

        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            // Common Tag
            add(fluid.commonTag, fluid)
            // Gaseous Tag
            if (fluid.textureType == RagiumFluids.TextureType.GASEOUS) {
                add(Tags.Fluids.GASEOUS, fluid)
            }
        }

        add(RagiumFluidTags.NITRO_FUEL, RagiumFluids.NITRO_FUEL.commonTag)

        add(RagiumFluidTags.NON_NITRO_FUEL, RagiumFluids.FUEL.commonTag)
        add(RagiumFluidTags.NON_NITRO_FUEL, RagiumFluids.BIO_FUEL.commonTag)

        add(RagiumFluidTags.NUCLEAR_FUEL, RagiumFluids.ENRICHED_URANIUM_HEXAFLUORIDE.commonTag)

        add(RagiumFluidTags.THERMAL_FUEL, Tags.Fluids.LAVA)
    }
}
