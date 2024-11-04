package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.getAroundPos
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.block.HTGeneratorBlockEntityBase
import hiiragi283.ragium.api.machine.block.HTMachineEntityFactory
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.property.HTMachineTooltipAppender
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.machine.HTDrainBlockEntity
import hiiragi283.ragium.common.machine.HTSteamGeneratorBlockEntity
import net.minecraft.block.Block
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

    override fun afterRagiumInit(instance: RagiumAPI) {}

    override fun registerMachineType(register: RagiumPlugin.MachineRegister) {
        // consumer
        RagiumMachineKeys.CONSUMERS.forEach(register::registerConsumer)
        // generators
        RagiumMachineKeys.GENERATORS.forEach(register::registerGenerator)
        // processors
        RagiumMachineKeys.PROCESSORS.forEach(register::registerProcessor)
    }

    override fun setupCommonMachineProperties(helper: RagiumPlugin.PropertyHelper) {
        // consumers
        helper.modify(RagiumMachineKeys.DRAIN) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDrainBlockEntity))
        }
        // generators
        /*helper.modify(RagiumMachineKeys.HEAT_GENERATOR) {
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
        helper.modify(RagiumMachineKeys.COMBUSTION_GENERATOR) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(HTGeneratorBlockEntityBase::Simple))
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
                // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(::HTSimpleProcessorMachineEntity))
                set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
                set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.SIMPLE)
            }
        }
        helper.modify(RagiumMachineKeys.BLAST_FURNACE) {
            // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.ofStatic(::HTBlastFurnaceMachineEntity),)
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        helper.modify(RagiumMachineKeys.DISTILLATION_TOWER) {
            // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.ofStatic(::HTDistillationTowerMachineEntity))
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        helper.modify(RagiumMachineKeys.SAW_MILL) {
            set(HTMachinePropertyKeys.FRONT_TEX) { Identifier.of("block/stonecutter_saw") }
            set(HTMachinePropertyKeys.TOOLTIP_BUILDER, HTMachineTooltipAppender.DEFAULT_PROCESSOR)
            // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.ofStatic(::HTSawMillMachineEntity))
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
    }
}
