package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.machine.HTMachineBlockRegistry
import hiiragi283.ragium.common.tags.RagiumBlockTags
import hiiragi283.ragium.common.tags.RagiumItemTags
import hiiragi283.ragium.common.util.HTBlockContent
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

object RagiumTagProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(RagiumTagProviders::BlockProvider)
        // pack.addProvider(RagiumTagProviders::FluidProvider)
        pack.addProvider(RagiumTagProviders::ItemProvider)
    }

    //    Block    //

    private class BlockProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.BlockTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            fun add(tagKey: TagKey<Block>, block: Block) {
                getOrCreateTagBuilder(tagKey).add(block)
            }

            fun add(tagKey: TagKey<Block>, block: HTBlockContent) {
                add(tagKey, block.block)
            }

            // vanilla
            add(BlockTags.PICKAXE_MINEABLE, RagiumContents.RAGINITE_ORE)
            add(BlockTags.PICKAXE_MINEABLE, RagiumContents.DEEPSLATE_RAGINITE_ORE)
            add(BlockTags.PICKAXE_MINEABLE, RagiumContents.OBLIVION_CLUSTER)

            add(BlockTags.PICKAXE_MINEABLE, RagiumContents.MANUAL_GRINDER)
            add(BlockTags.PICKAXE_MINEABLE, RagiumContents.SHAFT)
            add(BlockTags.PICKAXE_MINEABLE, RagiumContents.ALCHEMICAL_INFUSER)

            buildList<HTBlockContent> {
                addAll(RagiumContents.StorageBlocks.entries)
                addAll(RagiumContents.Hulls.entries)
                addAll(RagiumContents.Coils.entries)
            }.forEach { add(BlockTags.PICKAXE_MINEABLE, it) }
            // ragium
            RagiumContents.Coils.entries.forEach { add(RagiumBlockTags.COILS, it) }
            HTMachineBlockRegistry.forEachBlock { block: Block ->
                add(BlockTags.PICKAXE_MINEABLE, block)
            }
        }
    }

    //    Fluid    //

    /*private class FluidProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.FluidTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
        }
    }*/

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

            add(ItemTags.HEAD_ARMOR, RagiumContents.STEEL_HELMET)
            add(ItemTags.CHEST_ARMOR, RagiumContents.STEEL_CHESTPLATE)
            add(ItemTags.LEG_ARMOR, RagiumContents.STEEL_LEGGINGS)
            add(ItemTags.FOOT_ARMOR, RagiumContents.STEEL_BOOTS)
            // conventional
            add(ConventionalItemTags.GEMS, RagiumContents.RAGI_CRYSTAL)
            add(ConventionalItemTags.GEMS, RagiumContents.OBLIVION_CRYSTAL)
            add(ConventionalItemTags.ORES, RagiumContents.RAGINITE_ORE)
            add(ConventionalItemTags.ORES, RagiumContents.DEEPSLATE_RAGINITE_ORE)
            add(ConventionalItemTags.RAW_MATERIALS, RagiumContents.RAW_RAGINITE)
            add(RagiumItemTags.BASALTS, Items.BASALT)
            add(RagiumItemTags.BASALTS, Items.POLISHED_BASALT)
            add(RagiumItemTags.BASALTS, Items.SMOOTH_BASALT)
            add(RagiumItemTags.CARBON_PLATES, RagiumContents.Plates.CARBON)
            add(RagiumItemTags.COPPER_PLATES, RagiumContents.Plates.COPPER)
            add(RagiumItemTags.GOLD_PLATES, RagiumContents.Plates.GOLD)
            add(RagiumItemTags.IRON_PLATES, RagiumContents.Plates.IRON)
            add(RagiumItemTags.RAGINITE_ORES, RagiumContents.RAGINITE_ORE)
            add(RagiumItemTags.RAGINITE_ORES, RagiumContents.DEEPSLATE_RAGINITE_ORE)
            add(RagiumItemTags.SILICON_PLATES, RagiumContents.Plates.SILICON)
            add(RagiumItemTags.STEEL_BLOCKS, RagiumContents.StorageBlocks.STEEL)
            add(RagiumItemTags.STEEL_INGOTS, RagiumContents.Ingots.STEEL)
            add(RagiumItemTags.STEEL_PLATES, RagiumContents.Plates.STEEL)
            add(RagiumItemTags.SULFUR_DUSTS, RagiumContents.Dusts.SULFUR)

            RagiumContents.StorageBlocks.entries.forEach { add(ConventionalItemTags.STORAGE_BLOCKS, it) }
            RagiumContents.Dusts.entries.forEach { add(ConventionalItemTags.DUSTS, it) }
            RagiElement.entries.forEach { add(ConventionalItemTags.DUSTS, it.dustItem) }
            RagiumContents.Ingots.entries.forEach { add(ConventionalItemTags.INGOTS, it) }
            RagiumContents.Plates.entries.forEach { add(RagiumItemTags.PLATES, it) }
            // ragium
            add(RagiumItemTags.ALKALI, RagiumContents.Dusts.ASH)
            add(RagiumItemTags.ALKALI, RagiumContents.Fluids.SODIUM_HYDROXIDE)

            add(RagiumItemTags.ORGANIC_OILS, RagiumContents.Fluids.TALLOW)
            add(RagiumItemTags.ORGANIC_OILS, RagiumContents.Fluids.SEED_OIL)
        }
    }
}
