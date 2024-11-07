package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.getAroundPos
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.block.HTGeneratorBlockEntityBase
import hiiragi283.ragium.api.machine.block.HTMachineEntityFactory
import hiiragi283.ragium.api.machine.block.HTProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.property.HTMachineTooltipAppender
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.common.machine.*
import net.minecraft.block.Block
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.BiConsumer

object RagiumDefaultPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun afterRagiumInit(instance: RagiumAPI) {}

    override fun registerMachineType(consumer: BiConsumer<HTMachineKey, HTMachineType>) {
        // consumer
        RagiumMachineKeys.CONSUMERS.forEach { consumer.accept(it, HTMachineType.CONSUMER) }
        // generators
        RagiumMachineKeys.GENERATORS.forEach { consumer.accept(it, HTMachineType.GENERATOR) }
        // processors
        RagiumMachineKeys.PROCESSORS.forEach { consumer.accept(it, HTMachineType.PROCESSOR) }
    }

    override fun registerMaterial(consumer: BiConsumer<HTMaterialKey, HTMaterialKey.Type>) {
        // tier 1
        consumer.accept(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialKey.Type.MINERAL)
        consumer.accept(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialKey.Type.ALLOY)
        consumer.accept(RagiumMaterialKeys.ASH, HTMaterialKey.Type.DUST)
        consumer.accept(RagiumMaterialKeys.COPPER, HTMaterialKey.Type.METAL)
        consumer.accept(RagiumMaterialKeys.IRON, HTMaterialKey.Type.METAL)
        consumer.accept(RagiumMaterialKeys.NITER, HTMaterialKey.Type.MINERAL)
        consumer.accept(RagiumMaterialKeys.SULFUR, HTMaterialKey.Type.MINERAL)
        // tier 2
        consumer.accept(RagiumMaterialKeys.RAGINITE, HTMaterialKey.Type.MINERAL)
        consumer.accept(RagiumMaterialKeys.RAGI_STEEL, HTMaterialKey.Type.ALLOY)
        consumer.accept(RagiumMaterialKeys.FLUORITE, HTMaterialKey.Type.GEM)
        consumer.accept(RagiumMaterialKeys.GOLD, HTMaterialKey.Type.METAL)
        consumer.accept(RagiumMaterialKeys.PLASTIC, HTMaterialKey.Type.PLASTIC)
        consumer.accept(RagiumMaterialKeys.SILICON, HTMaterialKey.Type.METAL)
        consumer.accept(RagiumMaterialKeys.STEEL, HTMaterialKey.Type.ALLOY)
        // tier 3
        consumer.accept(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialKey.Type.GEM)
        consumer.accept(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialKey.Type.ALLOY)
        consumer.accept(RagiumMaterialKeys.ALUMINUM, HTMaterialKey.Type.METAL)
        consumer.accept(RagiumMaterialKeys.BAUXITE, HTMaterialKey.Type.MINERAL)
        consumer.accept(RagiumMaterialKeys.CRYOLITE, HTMaterialKey.Type.GEM)
        consumer.accept(RagiumMaterialKeys.ENGINEERING_PLASTIC, HTMaterialKey.Type.PLASTIC)
        consumer.accept(RagiumMaterialKeys.STELLA, HTMaterialKey.Type.PLASTIC)
        // tier 4
        consumer.accept(RagiumMaterialKeys.RAGIUM, HTMaterialKey.Type.GEM)
        // integration
    }

    override fun setupCommonMachineProperties(helper: RagiumPlugin.PropertyHelper<HTMachineKey>) {
        // consumers
        helper.modify(RagiumMachineKeys.DRAIN) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDrainBlockEntity))
        }
        // generators
        helper.modify(RagiumMachineKeys.COMBUSTION_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTCombustionGeneratorBlockEntity))
            set(HTMachinePropertyKeys.GENERATOR_COLOR, DyeColor.BLUE)
        }
        helper.modify(RagiumMachineKeys.SOLAR_PANEL) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTGeneratorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, _: BlockPos -> world.isDay }
            set(HTMachinePropertyKeys.VOXEL_SHAPE, Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0))
        }
        helper.modify(RagiumMachineKeys.STEAM_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTSteamGeneratorBlockEntity))
            set(HTMachinePropertyKeys.FRONT_MAPPER) { Direction.UP }
        }
        helper.modify(RagiumMachineKeys.THERMAL_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTGeneratorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.GENERATOR_COLOR, DyeColor.ORANGE)
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                when {
                    world.getBiome(pos).isIn(BiomeTags.IS_NETHER) -> true
                    else ->
                        pos
                            .getAroundPos {
                                val fluidState: FluidState = world.getFluidState(it)
                                fluidState.isIn(FluidTags.LAVA) && fluidState.isStill
                            }.size >= 4
                }
            }
        }
        helper.modify(RagiumMachineKeys.WATER_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTGeneratorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.GENERATOR_COLOR, DyeColor.CYAN)
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                pos.getAroundPos { world.getFluidState(it).isIn(FluidTags.WATER) }.size >= 2
            }
        }
        // processors
        RagiumMachineKeys.PROCESSORS.forEach {
            helper.modify(it) {
                set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTProcessorBlockEntityBase::Simple))
                set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            }
        }
        helper.modify(RagiumMachineKeys.BLAST_FURNACE) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTBlastFurnaceBlockEntity))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.DISTILLATION_TOWER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDistillationTowerBlockEntity))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.MULTI_SMELTER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTMultiSmelterBlockEntity))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
        }
        helper.modify(RagiumMachineKeys.SAW_MILL) {
            set(HTMachinePropertyKeys.FRONT_TEX) { Identifier.of("block/stonecutter_saw") }
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTSawmillBlockEntity))
        }
    }

    override fun setupCommonMaterialProperties(helper: RagiumPlugin.PropertyHelper<HTMaterialKey>) {
        // tier 1
        // tier 2
        helper.modify(RagiumMaterialKeys.TIER_TWO::contains) {
            set(HTMaterialPropertyKeys.RARITY, Rarity.UNCOMMON)
        }
        // tier 3
        helper.modify(RagiumMaterialKeys.TIER_THREE::contains) {
            set(HTMaterialPropertyKeys.RARITY, Rarity.RARE)
        }
        // tier 4
        helper.modify(RagiumMaterialKeys.RAGIUM) {
            set(HTMaterialPropertyKeys.RARITY, Rarity.EPIC)
        }
    }

    override fun bindMaterialToItem(consumer: (HTTagPrefix, HTMaterialKey, Item) -> Unit) {
        fun bindContents(contents: List<HTContent.Material<*>>) {
            contents.forEach { consumer(it.tagPrefix, it.material, it.asItem()) }
        }

        consumer(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.COPPER, Items.DEEPSLATE_COPPER_ORE)
        consumer(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.GOLD, Items.DEEPSLATE_GOLD_ORE)
        consumer(HTTagPrefix.DEEP_ORE, RagiumMaterialKeys.IRON, Items.DEEPSLATE_IRON_ORE)
        
        consumer(HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER, Items.COPPER_INGOT)
        consumer(HTTagPrefix.INGOT, RagiumMaterialKeys.GOLD, Items.GOLD_INGOT)
        consumer(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON, Items.IRON_INGOT)

        consumer(HTTagPrefix.NUGGET, RagiumMaterialKeys.GOLD, Items.GOLD_NUGGET)
        consumer(HTTagPrefix.NUGGET, RagiumMaterialKeys.IRON, Items.IRON_ORE)
        
        consumer(HTTagPrefix.ORE, RagiumMaterialKeys.COPPER, Items.COPPER_ORE)
        consumer(HTTagPrefix.ORE, RagiumMaterialKeys.GOLD, Items.GOLD_ORE)
        consumer(HTTagPrefix.ORE, RagiumMaterialKeys.IRON, Items.IRON_ORE)

        consumer(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.COPPER, Items.RAW_COPPER)
        consumer(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.GOLD, Items.RAW_GOLD)
        consumer(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.IRON, Items.RAW_IRON)

        consumer(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.COPPER, Items.COPPER_BLOCK)
        consumer(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.GOLD, Items.GOLD_BLOCK)
        consumer(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.IRON, Items.IRON_BLOCK)

        bindContents(RagiumContents.Ores.entries)
        bindContents(RagiumContents.StorageBlocks.entries)
        bindContents(RagiumContents.Dusts.entries)
        bindContents(RagiumContents.Gems.entries)
        bindContents(RagiumContents.Ingots.entries)
        bindContents(RagiumContents.Plates.entries)
        bindContents(RagiumContents.RawMaterials.entries)
    }
}
