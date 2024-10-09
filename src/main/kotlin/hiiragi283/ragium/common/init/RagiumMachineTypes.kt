package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.HTMachineTypeInitializer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.common.machine.*
import hiiragi283.ragium.common.util.getAroundPos
import hiiragi283.ragium.common.util.getMachineEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.Consumer

object RagiumMachineTypes : HTMachineTypeInitializer {
    override fun registerType(register: Consumer<HTMachineConvertible>) {
        buildList {
            add(HEAT_GENERATOR)
            addAll(RagiumMachineTypes.Generator.entries)
            add(BLAST_FURNACE)
            add(DISTILLATION_TOWER)
            add(FLUID_DRILL)
            add(SAW_MILL)
            addAll(RagiumMachineTypes.Processor.entries)
        }.forEach(register::accept)
    }

    //    Generator    //

    @JvmField
    val HEAT_GENERATOR: HTMachineType = HTMachineType.createGenerator(RagiumAPI.id("heat_generator")) {
        set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
            world
                .getMachineEntity(pos)
                ?.let { it as? HTHeatGeneratorMachineEntity }
                ?.isBurning
                ?: false
        }
        set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTHeatGeneratorMachineEntity))
    }

    enum class Generator(fluidTag: TagKey<Fluid>? = null) : HTMachineConvertible {
        COMBUSTION(RagiumFluidTags.COMBUSTION_FUEL) {
            override fun canGenerate(world: World, pos: BlockPos): Boolean = false
        },
        SOLAR {
            override fun canGenerate(world: World, pos: BlockPos): Boolean = world.isDay
        },
        THERMAL(FluidTags.LAVA) {
            override fun canGenerate(world: World, pos: BlockPos): Boolean = when {
                world.getBiome(pos).isIn(BiomeTags.IS_NETHER) -> true
                else ->
                    pos
                        .getAroundPos {
                            val fluidState: FluidState = world.getFluidState(it)
                            fluidState.isIn(FluidTags.LAVA) && fluidState.isStill
                        }.size >= 4
            }
        },
        WATER(FluidTags.WATER) {
            override fun canGenerate(world: World, pos: BlockPos): Boolean =
                pos.getAroundPos { world.getFluidState(it).isIn(FluidTags.WATER) }.size >= 2
        },
        ;

        abstract fun canGenerate(world: World, pos: BlockPos): Boolean

        private val machineType: HTMachineType = HTMachineType.createGenerator(RagiumAPI.id(name.lowercase())) {
            set(HTMachinePropertyKeys.FRONT_MAPPER) { Direction.UP }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(::HTGeneratorMachineEntity))
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE, ::canGenerate)
            setIfNonNull(HTMachinePropertyKeys.FUEL_TAG, fluidTag)
        }

        override fun asMachine(): HTMachineType = machineType
    }

    //    Processor    //

    @JvmField
    val BLAST_FURNACE: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id("blast_furnace")) {
        set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTBlastFurnaceMachineEntity))
        set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
        set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
    }

    @JvmField
    val DISTILLATION_TOWER: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id("distillation_tower")) {
        set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTDistillationTowerMachineEntity))
        set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
        set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
    }

    @JvmField
    val FLUID_DRILL: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id("fluid_drill")) {
        set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
        set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
    }

    @JvmField
    val SAW_MILL: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id("saw_mill")) {
        set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTSawMillMachineEntity))
        set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
        set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
    }

    enum class Processor(
        condition: (World, BlockPos, HTMachineType, HTMachineTier) -> Boolean = RagiumMachineConditions.ELECTRIC_CONDITION,
        succeeded: ((World, BlockPos, HTMachineType, HTMachineTier) -> Unit)? = RagiumMachineConditions.ELECTRIC_SUCCEEDED,
    ) : HTMachineConvertible {
        ALLOY_FURNACE,
        ASSEMBLER,
        CHEMICAL_REACTOR,
        COMPRESSOR,
        DECOMPRESSOR,
        ELECTROLYZER,
        EXTRACTOR,
        GRINDER,
        METAL_FORMER,
        MIXER,
        ROCK_GENERATOR(condition = RagiumMachineConditions.ROCK_SUCCEEDED, succeeded = null),
        ;

        private val machineType: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id(name.lowercase())) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(::HTProcessorMachineEntity))
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, condition)
            setIfNonNull(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, succeeded)
        }

        override fun asMachine(): HTMachineType = machineType
    }
}
