package hiiragi283.ragium.data

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.tags.*
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEnchantments
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
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
import net.minecraft.registry.tag.ItemTags
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

            // ragium
            RagiumAPI.getInstance().machineRegistry.blocks.values.forEach {
                add(RagiumBlockTags.MACHINES, it)
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

            fun add(tagKey: TagKey<Fluid>, fluid: RagiumFluids) {
                add(tagKey, fluid.value)
            }

            add(RagiumFluidTags.FUEL, RagiumFluids.BIO_FUEL)
            add(RagiumFluidTags.FUEL, RagiumFluids.FUEL)
            add(RagiumFluidTags.FUEL, RagiumFluids.AROMATIC_COMPOUNDS)

            add(RagiumFluidTags.ORGANIC_OILS, RagiumFluids.TALLOW)
            add(RagiumFluidTags.ORGANIC_OILS, RagiumFluids.SEED_OIL)
        }
    }

    //    Item    //

    private class ItemProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.ItemTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            val itemCache: Multimap<TagKey<Item>, Item> = HashMultimap.create()
            val tagCache: Multimap<TagKey<Item>, TagKey<Item>> = HashMultimap.create()

            fun add(tagKey: TagKey<Item>?, item: ItemConvertible?) {
                val item1: Item = item?.asItem() ?: return
                if (tagKey == null) return
                itemCache.put(tagKey, item1)
            }

            fun add(tagKey: TagKey<Item>?, child: TagKey<Item>?) {
                if (child == null) return
                if (tagKey == null) return
                tagCache.put(tagKey, child)
            }

            buildList {
                addAll(RagiumContents.Ores.entries)
                addAll(RagiumContents.StorageBlocks.entries)

                addAll(RagiumContents.Dusts.entries)
                addAll(RagiumContents.Gems.entries)
                addAll(RagiumContents.Ingots.entries)
                addAll(RagiumContents.Plates.entries)
                addAll(RagiumContents.RawMaterials.entries)
            }.forEach { content: HTContent<out ItemConvertible> ->
                if (content is HTContent.Material<*>) {
                    add(content.prefixedTagKey, content)
                    add(content.commonTagKey, content.prefixedTagKey)
                } else {
                    add(content.commonTagKey, content)
                }
            }

            getOrCreateTagBuilder(ItemTags.HEAD_ARMOR).add(
                RagiumItems.STEEL_HELMET,
                RagiumItems.STELLA_GOGGLE,
                RagiumItems.RAGIUM_HELMET,
            )
            getOrCreateTagBuilder(ItemTags.CHEST_ARMOR).add(
                RagiumItems.STEEL_CHESTPLATE,
                RagiumItems.STELLA_JACKET,
                RagiumItems.RAGIUM_CHESTPLATE,
            )
            getOrCreateTagBuilder(ItemTags.LEG_ARMOR).add(
                RagiumItems.STEEL_LEGGINGS,
                RagiumItems.STELLA_LEGGINGS,
                RagiumItems.RAGIUM_LEGGINGS,
            )
            getOrCreateTagBuilder(ItemTags.FOOT_ARMOR).add(
                RagiumItems.STEEL_BOOTS,
                RagiumItems.STELLA_BOOTS,
                RagiumItems.RAGIUM_BOOTS,
            )

            add(ItemTags.AXES, RagiumItems.STEEL_AXE)
            add(ItemTags.HOES, RagiumItems.STEEL_HOE)
            add(ItemTags.PICKAXES, RagiumItems.STEEL_PICKAXE)
            add(ItemTags.SHOVELS, RagiumItems.STEEL_SHOVEL)
            add(ItemTags.SWORDS, RagiumItems.STEEL_SWORD)

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

            RagiumAPI.getInstance().materialRegistry.types.forEach { (key: HTMaterialKey, type: HTMaterialKey.Type) ->
                type.validPrefixes.forEach { prefix: HTTagPrefix ->
                    add(prefix.commonTagKey, prefix.createTag(key))
                }
            }

            itemCache.asMap().forEach { (tagKey: TagKey<Item>, items: Collection<Item>) ->
                items.sortedBy(Registries.ITEM::getId).forEach { item: Item ->
                    getOrCreateTagBuilder(tagKey).add(item)
                }
            }
            tagCache.asMap().forEach { (tagKey: TagKey<Item>, children: Collection<TagKey<Item>>) ->
                children.sortedBy(TagKey<Item>::id).forEach { child: TagKey<Item> ->
                    getOrCreateTagBuilder(tagKey).addOptionalTag(child)
                }
            }
        }
    }
}
