package hiiragi283.ragium.data

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.data.recipe.HTMaterialItemRecipeRegistry
import hiiragi283.ragium.api.tags.RagiumBlockTags
import hiiragi283.ragium.api.tags.RagiumEnchantmentTags
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEnchantments
import hiiragi283.ragium.common.item.HTCrafterHammerItem
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.EnchantmentTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

object RagiumTagProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(RagiumTagProviders::BlockProvider)
        pack.addProvider(RagiumTagProviders::EnchantmentProvider)
        pack.addProvider(RagiumTagProviders::FluidProvider)
        pack.addProvider(RagiumTagProviders::ItemProvider)
    }

    //    Block    //

    private class BlockProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.BlockTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            fun add(tagKey: TagKey<Block>, block: Block) {
                getOrCreateTagBuilder(tagKey).add(block)
            }

            // vanilla
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.POROUS_NETHERRACK)

            add(BlockTags.HOE_MINEABLE, RagiumBlocks.SPONGE_CAKE)

            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.ADVANCED_CASING)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.BASIC_CASING)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.FIREBOX)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.MANUAL_FORGE)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.MANUAL_GRINDER)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.MANUAL_MIXER)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.META_CONSUMER)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.META_GENERATOR)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.META_PROCESSOR)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.NETWORK_INTERFACE)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.SHAFT)

            buildList {
                addAll(RagiumContents.Ores.entries)
                addAll(RagiumContents.StorageBlocks.entries)
                addAll(RagiumContents.Hulls.entries)
                addAll(RagiumContents.Coils.entries)
                addAll(RagiumContents.Pipes.entries)
            }.forEach { add(BlockTags.PICKAXE_MINEABLE, it.value) }

            RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
                add(BlockTags.DRAGON_IMMUNE, ore.value)
            }

            buildList {
                addAll(RagiumContents.Exporters.entries)
                addAll(RagiumContents.Pipes.entries)
            }.forEach { add(RagiumBlockTags.PIPE_CONNECTABLES, it.value) }
        }
    }

    //    Enchantment    //

    private class EnchantmentProvider(output: FabricDataOutput, completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.EnchantmentTagProvider(output, completableFuture) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            getOrCreateTagBuilder(EnchantmentTags.TRADEABLE)
                .add(RagiumEnchantments.SMELTING)
                .add(RagiumEnchantments.SLEDGE_HAMMER)
                .add(RagiumEnchantments.BUZZ_SAW)

            getOrCreateTagBuilder(EnchantmentTags.TREASURE)
                .add(RagiumEnchantments.SMELTING)
                .add(RagiumEnchantments.SLEDGE_HAMMER)
                .add(RagiumEnchantments.BUZZ_SAW)

            getOrCreateTagBuilder(RagiumEnchantmentTags.MODIFYING_EXCLUSIVE_SET)
                .add(RagiumEnchantments.SMELTING)
                .add(RagiumEnchantments.SLEDGE_HAMMER)
                .add(RagiumEnchantments.BUZZ_SAW)
        }
    }

    //    Fluid    //

    private class FluidProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.FluidTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            fun add(tagKey: TagKey<Fluid>, fluid: Fluid) {
                getOrCreateTagBuilder(tagKey).add(fluid)
            }

            fun add(tagKey: TagKey<Fluid>, fluid: RagiumContents.Fluids) {
                add(tagKey, fluid.value)
            }

            add(RagiumFluidTags.FUEL, RagiumContents.Fluids.BIO_FUEL)
            add(RagiumFluidTags.FUEL, RagiumContents.Fluids.FUEL)
            add(RagiumFluidTags.FUEL, RagiumContents.Fluids.AROMATIC_COMPOUNDS)

            add(RagiumFluidTags.ORGANIC_OILS, RagiumContents.Fluids.TALLOW)
            add(RagiumFluidTags.ORGANIC_OILS, RagiumContents.Fluids.SEED_OIL)
        }
    }

    //    Item    //

    private class ItemProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.ItemTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            val tagCache: Multimap<TagKey<Item>, Item> = HashMultimap.create()

            fun add(tagKey: TagKey<Item>?, item: ItemConvertible?) {
                val item1: Item = item?.asItem() ?: return
                if (tagKey == null) return
                tagCache.put(tagKey, item1)
            }

            buildList {
                addAll(RagiumContents.Ores.entries)
                addAll(RagiumContents.StorageBlocks.entries)

                addAll(RagiumContents.Dusts.entries)
                addAll(RagiumContents.Gems.entries)
                addAll(RagiumContents.Ingots.entries)
                addAll(RagiumContents.Plates.entries)
                addAll(RagiumContents.RawMaterials.entries)

                addAll(RagiumContents.Hulls.entries)
                addAll(RagiumContents.Coils.entries)
                addAll(RagiumContents.Exporters.entries)
                addAll(RagiumContents.Pipes.entries)
                addAll(RagiumContents.CircuitBoards.entries)
                addAll(RagiumContents.Circuits.entries)

                addAll(RagiumContents.Armors.entries)
                addAll(RagiumContents.Tools.entries)

                addAll(RagiumContents.Foods.entries)
                addAll(RagiumContents.Misc.entries)
            }.forEach { content: HTContent<out ItemConvertible> ->
                add(content.commonTagKey, content)
                if (content is HTContent.Material<*> && content.usePrefixedTag) {
                    add(content.prefixedTagKey, content)
                }
            }

            // ragium
            add(RagiumItemTags.ALKALI, RagiumContents.Dusts.ASH)

            getOrCreateTagBuilder(RagiumItemTags.PROTEIN_FOODS)
                .add(Items.ROTTEN_FLESH)
                .addOptionalTag(ConventionalItemTags.RAW_MEAT_FOODS)
                .addOptionalTag(ConventionalItemTags.COOKED_MEAT_FOODS)
                .addOptionalTag(ConventionalItemTags.RAW_FISH_FOODS)
                .addOptionalTag(ConventionalItemTags.COOKED_FISH_FOODS)

            buildList {
                addAll(HTCrafterHammerItem.Behavior.entries)
            }.forEach { add(RagiumItemTags.TOOL_MODULES, it) }

            HTMaterialItemRecipeRegistry.configureTags(::add)

            tagCache.asMap().forEach { (tagKey: TagKey<Item>, items: Collection<Item>) ->
                items.sortedBy(Registries.ITEM::getId).forEach { item: Item ->
                    getOrCreateTagBuilder(tagKey).add(item)
                }
            }
        }
    }
}
