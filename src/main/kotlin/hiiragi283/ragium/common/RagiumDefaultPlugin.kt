package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.getAroundPos
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.entity.HTGeneratorMachineEntityBase
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.property.HTMachineTooltipAppender
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.machine.*
import net.minecraft.fluid.FluidState
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object RagiumDefaultPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun afterRagiumInit() {}

    override fun registerMachineType(register: RagiumPlugin.MachineRegister) {
        // consumer
        RagiumMachineTypes.Consumer.entries
            .map(RagiumMachineTypes.Consumer::key)
            .forEach(register::registerConsumer)
        // generators
        // register.registerGenerator(RagiumMachineTypes.HEAT_GENERATOR)
        RagiumMachineTypes.Generator.entries
            .map(RagiumMachineTypes.Generator::key)
            .forEach(register::registerGenerator)
        // processors
        register.registerProcessor(RagiumMachineTypes.BLAST_FURNACE)
        register.registerProcessor(RagiumMachineTypes.DISTILLATION_TOWER)
        register.registerProcessor(RagiumMachineTypes.SAW_MILL)
        RagiumMachineTypes.Processor.entries
            .map(RagiumMachineTypes.Processor::key)
            .forEach(register::registerProcessor)
    }

    override fun setupCommonMachineProperties(helper: RagiumPlugin.PropertyHelper) {
        // consumers
        helper.modify(RagiumMachineTypes.Consumer.DRAIN) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.ofStatic(::HTDrainMachineEntity))
        }
        // generators
        /*helper.modify(RagiumMachineTypes.HEAT_GENERATOR) {
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                world
                    .getMachineEntity(pos)
                    ?.let { it as? HTHeatGeneratorMachineEntity }
                    ?.isBurning
                    ?: false
            }
            set(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntity.Factory.ofStatic(::HTHeatGeneratorMachineEntity),
            )
            set(HTMachinePropertyKeys.GENERATOR_COLOR, DyeColor.RED)
        }*/
        helper.modify(RagiumMachineTypes.Generator.COMBUSTION) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(HTGeneratorMachineEntityBase::Simple))
            set(HTMachinePropertyKeys.GENERATOR_COLOR, DyeColor.BLUE)
        }
        helper.modify(RagiumMachineTypes.Generator.SOLAR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(HTGeneratorMachineEntityBase::Simple))
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, _: BlockPos -> world.isDay }
        }
        helper.modify(RagiumMachineTypes.Generator.STEAM) {
            set(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntity.Factory.ofStatic(::HTSteamGeneratorMachineEntity),
            )
            set(HTMachinePropertyKeys.FRONT_MAPPER) { Direction.UP }
        }
        helper.modify(RagiumMachineTypes.Generator.THERMAL) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(HTGeneratorMachineEntityBase::Simple))
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
        helper.modify(RagiumMachineTypes.Generator.WATER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(HTGeneratorMachineEntityBase::Simple))
            set(HTMachinePropertyKeys.GENERATOR_COLOR, DyeColor.CYAN)
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                pos.getAroundPos { world.getFluidState(it).isIn(FluidTags.WATER) }.size >= 2
            }
        }
        // processors
        helper.modify(RagiumMachineTypes.BLAST_FURNACE) {
            set(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntity.Factory.ofStatic(::HTBlastFurnaceMachineEntity),
            )
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        helper.modify(RagiumMachineTypes.DISTILLATION_TOWER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.ofStatic(::HTDistillationTowerMachineEntity))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        helper.modify(RagiumMachineTypes.SAW_MILL) {
            set(HTMachinePropertyKeys.FRONT_TEX) { Identifier.of("block/stonecutter_saw") }
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.ofStatic(::HTSawMillMachineEntity))
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        RagiumMachineTypes.Processor.entries.forEach {
            helper.modify(it.key) {
                set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(::HTSimpleProcessorMachineEntity))
                set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
                set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.SIMPLE)
            }
        }
    }
}
