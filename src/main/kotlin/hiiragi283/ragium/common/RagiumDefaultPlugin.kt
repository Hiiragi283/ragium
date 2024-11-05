package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.getAroundPos
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTypeNew
import hiiragi283.ragium.api.machine.block.HTGeneratorBlockEntityBase
import hiiragi283.ragium.api.machine.block.HTMachineEntityFactory
import hiiragi283.ragium.api.machine.block.HTProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.property.HTMachineTooltipAppender
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.common.machine.*
import net.minecraft.block.Block
import net.minecraft.fluid.FluidState
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.BiConsumer

object RagiumDefaultPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun afterRagiumInit(instance: RagiumAPI) {}

    override fun registerMachineType(register: BiConsumer<HTMachineKey, HTMachineTypeNew>) {
        // consumer
        RagiumMachineKeys.CONSUMERS.forEach { register.accept(it, HTMachineTypeNew.CONSUMER) }
        // generators
        RagiumMachineKeys.GENERATORS.forEach { register.accept(it, HTMachineTypeNew.GENERATOR) }
        // processors
        RagiumMachineKeys.PROCESSORS.forEach { register.accept(it, HTMachineTypeNew.PROCESSOR) }
    }

    override fun registerMaterial(register: BiConsumer<HTMaterialKey, HTMaterialKey.Type>) {
        // tier 1
        register.accept(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialKey.Type.MINERAL)
        register.accept(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialKey.Type.ALLOY)
        register.accept(RagiumMaterialKeys.COPPER, HTMaterialKey.Type.METAL)
        register.accept(RagiumMaterialKeys.IRON, HTMaterialKey.Type.METAL)
        register.accept(RagiumMaterialKeys.ASH, HTMaterialKey.Type.DUST)
        register.accept(RagiumMaterialKeys.NITER, HTMaterialKey.Type.MINERAL)
        register.accept(RagiumMaterialKeys.SULFUR, HTMaterialKey.Type.MINERAL)
        // tier 2
        register.accept(RagiumMaterialKeys.RAGINITE, HTMaterialKey.Type.MINERAL)
        register.accept(RagiumMaterialKeys.RAGI_STEEL, HTMaterialKey.Type.ALLOY)
        register.accept(RagiumMaterialKeys.FLUORITE, HTMaterialKey.Type.GEM)
        register.accept(RagiumMaterialKeys.GOLD, HTMaterialKey.Type.METAL)
        register.accept(RagiumMaterialKeys.PLASTIC, HTMaterialKey.Type.PLASTIC)
        register.accept(RagiumMaterialKeys.SILICON, HTMaterialKey.Type.METAL)
        register.accept(RagiumMaterialKeys.STEEL, HTMaterialKey.Type.ALLOY)
        // tier 3
        register.accept(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialKey.Type.GEM)
        register.accept(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialKey.Type.ALLOY)
        register.accept(RagiumMaterialKeys.ALUMINUM, HTMaterialKey.Type.METAL)
        register.accept(RagiumMaterialKeys.BAUXITE, HTMaterialKey.Type.MINERAL)
        register.accept(RagiumMaterialKeys.ENGINEERING_PLASTIC, HTMaterialKey.Type.PLASTIC)
        register.accept(RagiumMaterialKeys.STELLA, HTMaterialKey.Type.PLASTIC)
        // tier 4
        register.accept(RagiumMaterialKeys.RAGIUM, HTMaterialKey.Type.GEM)
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
}
