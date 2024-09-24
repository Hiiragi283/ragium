package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockTags
import hiiragi283.ragium.common.init.RagiumItemTags
import hiiragi283.ragium.data.util.HTMetalItemRecipeGroup
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
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

            getOrCreateTagBuilder(RagiumBlockTags.COILS)
                .add(RagiumContents.Coils.COPPER.block)
                .add(RagiumContents.Coils.GOLD.block)
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
            fun add(tagKey: TagKey<Item>, item: ItemConvertible) {
                getOrCreateTagBuilder(tagKey).add(item.asItem())
            }

            // vanilla
            add(ItemTags.SWORDS, RagiumContents.STEEL_SWORD)
            add(ItemTags.SHOVELS, RagiumContents.STEEL_SHOVEL)
            add(ItemTags.PICKAXES, RagiumContents.STEEL_PICKAXE)
            add(ItemTags.AXES, RagiumContents.STEEL_AXE)
            add(ItemTags.HOES, RagiumContents.STEEL_HOE)
            // conventional
            add(ConventionalItemTags.GEMS, RagiumContents.RAGI_CRYSTAL)
            add(RagiumItemTags.COPPER_PLATES, RagiumContents.Plates.COPPER)
            add(RagiumItemTags.GOLD_PLATES, RagiumContents.Plates.GOLD)
            add(RagiumItemTags.IRON_PLATES, RagiumContents.Plates.IRON)
            add(RagiumItemTags.RAGINITE_ORES, RagiumContents.DEEPSLATE_RAGINITE_ORE)
            add(RagiumItemTags.RAGINITE_ORES, RagiumContents.RAGINITE_ORE)
            add(RagiumItemTags.STEEL_INGOTS, RagiumContents.Ingots.STEEL)
            add(RagiumItemTags.STEEL_PLATES, RagiumContents.Plates.STEEL)
            // ragium
            add(RagiumItemTags.ORGANIC_OILS, RagiumContents.Fluids.TALLOW)
            add(RagiumItemTags.ORGANIC_OILS, RagiumContents.Fluids.SEED_OIL)

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
