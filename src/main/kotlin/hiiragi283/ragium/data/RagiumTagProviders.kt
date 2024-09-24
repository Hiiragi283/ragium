package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumItemTags
import hiiragi283.ragium.data.util.HTMetalItemRecipeGroup
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
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
            val pickaxeMinable: FabricTagBuilder = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            pickaxeMinable
                .add(
                    RagiumContents.RAGINITE_ORE,
                    RagiumContents.DEEPSLATE_RAGINITE_ORE,
                    RagiumContents.MANUAL_GRINDER,
                    RagiumContents.BURNING_BOX,
                    RagiumContents.BLAZING_BOX,
                    RagiumContents.WATER_GENERATOR,
                    RagiumContents.WIND_GENERATOR,
                )
            RagiumContents.StorageBlocks.entries
                .map(RagiumContents.StorageBlocks::block)
                .forEach(pickaxeMinable::add)
            RagiumContents.Hulls.entries
                .map(RagiumContents.Hulls::block)
                .forEach(pickaxeMinable::add)
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
            getOrCreateTagBuilder(ItemTags.SWORDS).add(RagiumContents.STEEL_SWORD)
            getOrCreateTagBuilder(ItemTags.SHOVELS).add(RagiumContents.STEEL_SHOVEL)
            getOrCreateTagBuilder(ItemTags.PICKAXES).add(RagiumContents.STEEL_PICKAXE)
            getOrCreateTagBuilder(ItemTags.AXES).add(RagiumContents.STEEL_AXE)
            getOrCreateTagBuilder(ItemTags.HOES).add(RagiumContents.STEEL_HOE)

            getOrCreateTagBuilder(RagiumItemTags.ORGANIC_OILS)
                .add(
                    RagiumContents.Fluids.TALLOW.asItem(),
                    RagiumContents.Fluids.SEED_OIL.asItem(),
                )

            getOrCreateTagBuilder(RagiumItemTags.RAGINITE_ORES)
                .add(
                    RagiumContents.RAGINITE_ORE.asItem(),
                    RagiumContents.DEEPSLATE_RAGINITE_ORE.asItem(),
                )

            getOrCreateTagBuilder(RagiumItemTags.STEEL_INGOTS)
                .add(RagiumContents.Ingots.STEEL.asItem())

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
