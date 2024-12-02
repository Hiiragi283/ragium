package hiiragi283.ragium.data

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.tags.RagiumBlockTags
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

object RagiumTagProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(::BlockProvider)
        // pack.addProvider(::EnchantmentProvider)
        pack.addProvider(::FluidProvider)
        pack.addProvider(::ItemProvider)
    }

    //    Block    //

    private class BlockProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.BlockTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            val blockCache: Multimap<TagKey<Block>, Block> = HashMultimap.create()

            fun add(tagKey: TagKey<Block>, block: Block) {
                blockCache.put(tagKey, block)
            }

            fun add(tagKey: TagKey<Block>, content: HTContent<Block>) {
                add(tagKey, content.value)
            }

            // vanilla
            add(BlockTags.SHOVEL_MINEABLE, RagiumBlocks.MUTATED_SOIL)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.POROUS_NETHERRACK)

            add(BlockTags.SLABS, RagiumBlocks.ASPHALT_SLAB)
            add(BlockTags.SLABS, RagiumBlocks.POLISHED_ASPHALT_SLAB)
            add(BlockTags.SLABS, RagiumBlocks.GYPSUM_SLAB)
            add(BlockTags.SLABS, RagiumBlocks.POLISHED_GYPSUM_SLAB)
            add(BlockTags.STAIRS, RagiumBlocks.ASPHALT_STAIRS)
            add(BlockTags.STAIRS, RagiumBlocks.POLISHED_ASPHALT_STAIRS)
            add(BlockTags.STAIRS, RagiumBlocks.GYPSUM_STAIRS)
            add(BlockTags.STAIRS, RagiumBlocks.POLISHED_GYPSUM_SLAB)

            RagiumBlocks.FOODS.forEach { add(BlockTags.HOE_MINEABLE, it) }

            buildList {
                addAll(RagiumBlocks.BUILDINGS)
                addAll(RagiumBlocks.MECHANICS)
                addAll(RagiumBlocks.MISC)

                remove(RagiumBlocks.WHITE_LINE)
                remove(RagiumBlocks.T_WHITE_LINE)
                remove(RagiumBlocks.CROSS_WHITE_LINE)
                remove(RagiumBlocks.ROPE)
            }.forEach { add(BlockTags.PICKAXE_MINEABLE, it) }
            add(BlockTags.AXE_MINEABLE, RagiumBlocks.ROPE)

            buildList {
                addAll(RagiumContents.Ores.entries)
                addAll(RagiumContents.StorageBlocks.entries)
                addAll(RagiumContents.Grates.entries)
                addAll(RagiumContents.Casings.entries)
                addAll(RagiumContents.Hulls.entries)
                addAll(RagiumContents.Coils.entries)
                addAll(RagiumContents.Pipes.entries)
                addAll(RagiumContents.Drums.entries)
            }.forEach { add(BlockTags.PICKAXE_MINEABLE, it) }

            add(BlockTags.CLIMBABLE, RagiumBlocks.ROPE)

            RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
                add(BlockTags.DRAGON_IMMUNE, ore)
            }

            buildList {
                addAll(RagiumContents.Exporters.entries)
                addAll(RagiumContents.Pipes.entries)
                addAll(RagiumContents.CrossPipes.entries)
            }.forEach { add(RagiumBlockTags.PIPE_CONNECTABLES, it) }

            blockCache.asMap().forEach { (tagKey: TagKey<Block>, blocks: Collection<Block>) ->
                blocks.sortedBy(Registries.BLOCK::getId).forEach { block: Block ->
                    getOrCreateTagBuilder(tagKey).add(block)
                }
            }
        }
    }

    //    Enchantment    //

    /*private class EnchantmentProvider(output: FabricDataOutput, completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
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
    }*/

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

            fun add(tagKey: TagKey<Fluid>, child: TagKey<Fluid>) {
                getOrCreateTagBuilder(tagKey).addTag(child)
            }

            add(ConventionalFluidTags.MILK, RagiumFluids.MILK)
            add(ConventionalFluidTags.HONEY, RagiumFluids.HONEY)

            add(ConventionalFluidTags.GASEOUS, RagiumFluids.AIR)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.HYDROGEN)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.NITROGEN)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.OXYGEN)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.CHLORINE)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.CARBON_MONOXIDE)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.CARBON_DIOXIDE)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.HYDROGEN_FLUORIDE)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.HYDROGEN_CHLORIDE)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.CHLOROSILANE)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.REFINED_GAS)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.NOBLE_GAS)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.URANIUM_HEXAFLUORIDE)
            add(ConventionalFluidTags.GASEOUS, RagiumFluids.ENRICHED_URANIUM_HEXAFLUORIDE)

            add(RagiumFluidTags.COOLANTS, Fluids.WATER)

            add(RagiumFluidTags.FUELS, RagiumFluidTags.NITRO_FUELS)
            add(RagiumFluidTags.FUELS, RagiumFluidTags.NON_NITRO_FUELS)

            add(RagiumFluidTags.NON_NITRO_FUELS, RagiumFluids.BIO_FUEL)
            add(RagiumFluidTags.NON_NITRO_FUELS, RagiumFluids.FUEL)
            add(RagiumFluidTags.NITRO_FUELS, RagiumFluids.NITRO_FUEL)

            add(RagiumFluidTags.ORGANIC_OILS, RagiumFluids.TALLOW)
            add(RagiumFluidTags.ORGANIC_OILS, RagiumFluids.SEED_OIL)

            add(RagiumFluidTags.THERMAL_FUELS, Fluids.LAVA)
        }
    }

    //    Item    //

    private class ItemProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.ItemTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            val itemCache: Multimap<TagKey<Item>, Item> = HashMultimap.create()

            fun add(tagKey: TagKey<Item>?, item: ItemConvertible?) {
                val item1: Item = item?.asItem() ?: return
                if (tagKey == null) return
                itemCache.put(tagKey, item1)
            }

            add(ItemTags.HEAD_ARMOR, RagiumItems.STEEL_HELMET)
            add(ItemTags.HEAD_ARMOR, RagiumItems.STELLA_GOGGLE)
            add(ItemTags.CHEST_ARMOR, RagiumItems.STEEL_CHESTPLATE)
            add(ItemTags.CHEST_ARMOR, RagiumItems.STELLA_JACKET)
            add(ItemTags.LEG_ARMOR, RagiumItems.STEEL_LEGGINGS)
            add(ItemTags.LEG_ARMOR, RagiumItems.STELLA_LEGGINGS)
            add(ItemTags.FOOT_ARMOR, RagiumItems.STEEL_BOOTS)
            add(ItemTags.FOOT_ARMOR, RagiumItems.STELLA_BOOTS)

            add(ItemTags.AXES, RagiumItems.GIGANT_HAMMER)
            add(ItemTags.AXES, RagiumItems.STEEL_AXE)
            add(ItemTags.HOES, RagiumItems.GIGANT_HAMMER)
            add(ItemTags.HOES, RagiumItems.STEEL_HOE)
            add(ItemTags.PICKAXES, RagiumItems.GIGANT_HAMMER)
            add(ItemTags.PICKAXES, RagiumItems.STEEL_PICKAXE)
            add(ItemTags.SHOVELS, RagiumItems.GIGANT_HAMMER)
            add(ItemTags.SHOVELS, RagiumItems.STEEL_SHOVEL)
            add(ItemTags.SWORDS, RagiumItems.STEEL_SWORD)
            add(ItemTags.SWORDS, RagiumItems.STELLA_SABER)
            add(ItemTags.SWORDS, RagiumItems.RAGIUM_SABER)

            add(ItemTags.PLANKS, RagiumContents.Plates.WOOD)
            add(ItemTags.COALS, RagiumItems.RESIDUAL_COKE)
            // ragium
            add(RagiumItemTags.ALKALI, RagiumContents.Dusts.ALKALI)
            add(RagiumItemTags.ALKALI, RagiumContents.Dusts.ASH)

            add(RagiumItemTags.FLUID_EXPORTER_FILTERS, RagiumItems.FLUID_FILTER)

            add(RagiumItemTags.ITEM_EXPORTER_FILTERS, RagiumItems.ITEM_FILTER)

            add(RagiumItemTags.SILICON, RagiumItems.CRUDE_SILICON)
            add(RagiumItemTags.SILICON_PLATES, RagiumItems.SILICON)
            add(RagiumItemTags.REFINED_SILICON_PLATES, RagiumItems.REFINED_SILICON)

            getOrCreateTagBuilder(RagiumItemTags.PROTEIN_FOODS)
                .add(Items.ROTTEN_FLESH)
                .addOptionalTag(ConventionalItemTags.RAW_MEAT_FOODS)
                .addOptionalTag(ConventionalItemTags.COOKED_MEAT_FOODS)
                .addOptionalTag(ConventionalItemTags.RAW_FISH_FOODS)
                .addOptionalTag(ConventionalItemTags.COOKED_FISH_FOODS)

            itemCache.asMap().forEach { (tagKey: TagKey<Item>, items: Collection<Item>) ->
                items.sortedBy(Registries.ITEM::getId).forEach { item: Item ->
                    getOrCreateTagBuilder(tagKey).add(item)
                }
            }
        }
    }
}
