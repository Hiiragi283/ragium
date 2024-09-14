package hiiragi283.ragium.datagen

import hiiragi283.ragium.common.data.HTMetalItemFamily
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.item.Item
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.FluidTags
import java.util.concurrent.CompletableFuture

object RagiumTagProviders {

    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(::BlockProvider)
        pack.addProvider(::FluidProvider)
        pack.addProvider(::ItemProvider)
    }

    private class BlockProvider(
        output: FabricDataOutput,
        registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
    ) : FabricTagProvider.BlockTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            // pickaxe
            getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(RagiumBlocks.RAGINITE_ORE)
                .add(RagiumBlocks.DEEPSLATE_RAGINITE_ORE)
            getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
        }
    }

    private class FluidProvider(
        output: FabricDataOutput,
        registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
    ) : FabricTagProvider.FluidTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            // fluids
            getOrCreateTagBuilder(FluidTags.WATER).add(RagiumFluids.OIL.still)
        }
    }


    private class ItemProvider(
        output: FabricDataOutput,
        registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
    ) : FabricTagProvider.ItemTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {

            HTMetalItemFamily.registry.forEach { (name: String, family: HTMetalItemFamily) ->
                HTMetalItemFamily.Variant.entries.forEach variant@{ variant: HTMetalItemFamily.Variant ->
                    val item: Item = family.get(variant) ?: return@variant
                    getOrCreateTagBuilder(variant.allTagKey).add(item)

                    // val tagKey: TagKey<Item> = family.getTagKey(variant)
                    // getOrCreateTagBuilder(tagKey).add(item)
                }
            }
        }
    }

}