package hiiragi283.ragium.data

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.tags.RagiumBlockTags
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.init.*
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.EntityTypeTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

object RagiumTagProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(::BlockProvider)
        pack.addProvider(::EntityProvider)
        pack.addProvider(::FluidProvider)
        pack.addProvider(::ItemProvider)
    }

    //    Block    //

    private class BlockProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.BlockTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            val blockCache: Multimap<TagKey<Block>, Block> = HashMultimap.create()

            fun add(tagKey: TagKey<Block>, content: HTBlockContent) {
                blockCache.put(tagKey, content.get())
            }

            // vanilla
            add(BlockTags.SHOVEL_MINEABLE, RagiumBlocks.MUTATED_SOIL)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.POROUS_NETHERRACK)

            add(BlockTags.SLABS, RagiumBlocks.Slabs.ASPHALT)
            add(BlockTags.SLABS, RagiumBlocks.Slabs.POLISHED_ASPHALT)
            add(BlockTags.SLABS, RagiumBlocks.Slabs.GYPSUM)
            add(BlockTags.SLABS, RagiumBlocks.Slabs.POLISHED_GYPSUM)
            add(BlockTags.SLABS, RagiumBlocks.Slabs.SLATE)
            add(BlockTags.SLABS, RagiumBlocks.Slabs.POLISHED_SLATE)
            add(BlockTags.STAIRS, RagiumBlocks.Stairs.ASPHALT)
            add(BlockTags.STAIRS, RagiumBlocks.Stairs.POLISHED_ASPHALT)
            add(BlockTags.STAIRS, RagiumBlocks.Stairs.GYPSUM)
            add(BlockTags.STAIRS, RagiumBlocks.Stairs.POLISHED_GYPSUM)
            add(BlockTags.STAIRS, RagiumBlocks.Stairs.SLATE)
            add(BlockTags.STAIRS, RagiumBlocks.Stairs.POLISHED_SLATE)

            add(BlockTags.AXE_MINEABLE, RagiumBlocks.ROPE)

            RagiumBlocks.FOODS.forEach { add(BlockTags.HOE_MINEABLE, it) }

            buildList {
                addAll(RagiumBlocks.Ores.entries)
                addAll(RagiumBlocks.StorageBlocks.entries)
                addAll(RagiumBlocks.Grates.entries)
                addAll(RagiumBlocks.Casings.entries)
                addAll(RagiumBlocks.Hulls.entries)
                addAll(RagiumBlocks.Coils.entries)

                addAll(RagiumBlocks.Exporters.entries)
                addAll(RagiumBlocks.Pipes.entries)
                addAll(RagiumBlocks.CrossPipes.entries)
                addAll(RagiumBlocks.PipeStations.entries)
                addAll(RagiumBlocks.FilteringPipes.entries)

                addAll(RagiumBlocks.Crates.entries)
                addAll(RagiumBlocks.Drums.entries)

                addAll(RagiumBlocks.Creatives.entries)
                addAll(RagiumBlocks.Stones.entries)
                addAll(RagiumBlocks.Slabs.entries)
                addAll(RagiumBlocks.Stairs.entries)
                addAll(RagiumBlocks.Glasses.entries)
                addAll(RagiumBlocks.MECHANICS)
                addAll(RagiumBlocks.MISC)
            }.forEach { add(BlockTags.PICKAXE_MINEABLE, it) }

            add(BlockTags.CLIMBABLE, RagiumBlocks.ROPE)

            RagiumBlocks.Ores.entries.forEach { ore: RagiumBlocks.Ores ->
                add(BlockTags.DRAGON_IMMUNE, ore)
            }

            buildList {
                addAll(RagiumBlocks.Exporters.entries)
                addAll(RagiumBlocks.Pipes.entries)
                addAll(RagiumBlocks.CrossPipes.entries)
                addAll(RagiumBlocks.PipeStations.entries)
                addAll(RagiumBlocks.FilteringPipes.entries)
            }.forEach { add(RagiumBlockTags.PIPE_CONNECTABLES, it) }
            add(RagiumBlockTags.PIPE_CONNECTABLES, RagiumBlocks.Creatives.CRATE)
            add(RagiumBlockTags.PIPE_CONNECTABLES, RagiumBlocks.Creatives.DRUM)
            add(RagiumBlockTags.PIPE_CONNECTABLES, RagiumBlocks.Creatives.EXPORTER)

            blockCache.asMap().forEach { (tagKey: TagKey<Block>, blocks: Collection<Block>) ->
                blocks.sortedBy(Registries.BLOCK::getId).forEach { block: Block ->
                    getOrCreateTagBuilder(tagKey).add(block)
                }
            }
        }
    }

    //    EntityType    //

    private class EntityProvider(output: FabricDataOutput, completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.EntityTypeTagProvider(output, completableFuture) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            RagiumEntityTypes.DYNAMITES.forEach { entityType: EntityType<out ThrownItemEntity> ->
                getOrCreateTagBuilder(EntityTypeTags.IMPACT_PROJECTILES).add(entityType)
                getOrCreateTagBuilder(ConventionalEntityTypeTags.CAPTURING_NOT_SUPPORTED).add(entityType)
                getOrCreateTagBuilder(ConventionalEntityTypeTags.TELEPORTING_NOT_SUPPORTED).add(entityType)
            }
        }
    }

    //    Fluid    //

    private class FluidProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.FluidTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            fun add(tagKey: TagKey<Fluid>, fluid: Fluid) {
                getOrCreateTagBuilder(tagKey).add(fluid)
            }

            fun add(tagKey: TagKey<Fluid>, fluid: HTFluidContent) {
                add(tagKey, fluid.get())
            }

            fun add(tagKey: TagKey<Fluid>, child: TagKey<Fluid>) {
                getOrCreateTagBuilder(tagKey).addTag(child)
            }

            add(ConventionalFluidTags.MILK, RagiumFluids.MILK)
            add(ConventionalFluidTags.HONEY, RagiumFluids.HONEY)
            add(ConventionalFluidTags.EXPERIENCE, RagiumFluids.EXPERIENCE)

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

            RagiumItemsNew.SteelArmors.entries.forEach {
                add(it.armorType.armorTag, it)
            }
            RagiumItemsNew.DeepSteelArmors.entries.forEach {
                add(it.armorType.armorTag, it)
            }
            RagiumItemsNew.StellaSuits.entries.forEach {
                add(it.armorType.armorTag, it)
            }
            RagiumItemsNew.SteelTools.entries.forEach {
                add(it.toolType.toolTag, it)
            }
            RagiumItemsNew.DeepSteelTools.entries.forEach {
                add(it.toolType.toolTag, it)
            }

            add(ItemTags.AXES, RagiumItemsNew.GIGANT_HAMMER)
            add(ItemTags.HOES, RagiumItemsNew.GIGANT_HAMMER)
            add(ItemTags.PICKAXES, RagiumItemsNew.GIGANT_HAMMER)
            add(ItemTags.SHOVELS, RagiumItemsNew.GIGANT_HAMMER)
            add(ItemTags.SWORDS, RagiumItemsNew.STELLA_SABER)
            add(ItemTags.SWORDS, RagiumItemsNew.RAGIUM_SABER)

            add(ItemTags.PLANKS, RagiumItemsNew.Plates.WOOD)
            add(ItemTags.COALS, RagiumItems.RESIDUAL_COKE)
            // ragium
            add(RagiumItemTags.ALKALI, RagiumItemsNew.Dusts.ALKALI)
            add(RagiumItemTags.ALKALI, RagiumItemsNew.Dusts.ASH)

            add(RagiumItemTags.FLUID_EXPORTER_FILTERS, RagiumItemsNew.FLUID_FILTER)

            add(RagiumItemTags.ITEM_EXPORTER_FILTERS, RagiumItemsNew.ITEM_FILTER)

            add(RagiumItemTags.SILICON, RagiumItems.CRUDE_SILICON)
            add(RagiumItemTags.SILICON_PLATES, RagiumItems.SILICON)
            add(RagiumItemTags.REFINED_SILICON_PLATES, RagiumItems.REFINED_SILICON)

            add(RagiumItemTags.ADVANCED_UPGRADES, RagiumBlocks.Hulls.ADVANCED)
            add(RagiumItemTags.BASIC_UPGRADES, RagiumBlocks.Hulls.BASIC)

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
