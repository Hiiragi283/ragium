package hiiragi283.ragium.datagen

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import java.util.concurrent.CompletableFuture

object RagiumTagProviders {

    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
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

    private class ItemProvider(
        output: FabricDataOutput,
        registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
    ) : FabricTagProvider.ItemTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            // ingots
            getOrCreateTagBuilder(ConventionalItemTags.INGOTS)
                .add(RagiumItems.RAGI_ALLOY_INGOT)
                .add(RagiumItems.RAGI_STEEL_INGOT)
                .add(RagiumItems.REFINED_RAGI_STEEL_INGOT)
        }
    }

}