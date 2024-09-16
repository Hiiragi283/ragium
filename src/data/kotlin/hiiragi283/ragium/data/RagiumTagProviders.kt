package hiiragi283.ragium.data

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.group.HTMetalItemRecipeGroup
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

object RagiumTagProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(RagiumTagProviders::BlockProvider)
        pack.addProvider(RagiumTagProviders::FluidProvider)
        pack.addProvider(RagiumTagProviders::ItemProvider)
    }

    @JvmStatic
    fun blockTag(id: Identifier): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id)

    @JvmStatic
    fun fluidTag(id: Identifier): TagKey<Fluid> = TagKey.of(RegistryKeys.FLUID, id)

    //    Block    //

    private class BlockProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.BlockTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            RagiumBlocks.REGISTER.generateTag(::getOrCreateTagBuilder)
        }
    }

    //    Fluid    //

    private class FluidProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.FluidTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
        }
    }

    //    Item    //

    private class ItemProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.ItemTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            getOrCreateTagBuilder(RagiumItemTags.STEEL_INGOTS).add(RagiumItems.STEEL_INGOT)

            RagiumItems.REGISTER.generateTag(::getOrCreateTagBuilder)

            HTMetalItemRecipeGroup.registry.forEach { (name: String, family: HTMetalItemRecipeGroup) ->
                HTMetalItemRecipeGroup.Variant.entries.forEach variant@{ variant: HTMetalItemRecipeGroup.Variant ->
                    val item: Item = family.get(variant) ?: return@variant
                    getOrCreateTagBuilder(variant.allTagKey).add(item)

                    // val tagKey: TagKey<Item> = family.getTagKey(variant)
                    // getOrCreateTagBuilder(tagKey).add(item)
                }
            }
        }
    }
}
