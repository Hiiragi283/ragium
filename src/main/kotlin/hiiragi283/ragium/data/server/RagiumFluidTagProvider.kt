package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.data.addElement
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagBuilder
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
        val gaseous: TagBuilder = getOrCreateRawBuilder(Tags.Fluids.GASEOUS)

        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            // Common Tag
            getOrCreateRawBuilder(fluid.commonTag)
                .addElement(fluid.stillHolder)
                .addElement(fluid.flowingHolder)
            // Gaseous Tag
            if (fluid.textureType == RagiumFluids.TextureType.GASEOUS) {
                gaseous
                    .addElement(fluid.stillHolder)
                    .addElement(fluid.flowingHolder)
            }
        }
    }
}
