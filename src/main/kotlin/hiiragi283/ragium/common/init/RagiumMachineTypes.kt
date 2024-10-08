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
import java.util.function.BiPredicate
import java.util.function.Consumer
import java.util.function.UnaryOperator

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
        // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineFactory.of(::HTHeatGeneratorBlockEntity))
        set(
            HTMachinePropertyKeys.GENERATOR_PREDICATE,
            BiPredicate { world: World, pos: BlockPos ->
                world
                    .getMachineEntity(pos)
                    ?.let { it as? HTHeatGeneratorMachineEntity }
                    ?.isBurning
                    ?: false
            },
        )
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
            set(HTMachinePropertyKeys.FRONT_MAPPER, UnaryOperator { Direction.UP })
            // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineFactory(HTGeneratorBlockEntity::Simple))
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(::HTGeneratorMachineEntity))
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE, BiPredicate(::canGenerate))
            fluidTag?.let { set(HTMachinePropertyKeys.FUEL_TAG, it) }
        }

        override fun asMachine(): HTMachineType = machineType
    }

    //    Processor    //

    @JvmField
    val BLAST_FURNACE: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id("blast_furnace")) {
        // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineFactory.of(::HTBlastFurnaceBlockEntity))
        set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTBlastFurnaceMachineEntity))
        set(HTMachinePropertyKeys.CONDITION, RagiumMachineConditions.ELECTRIC_CONSUMER)
    }

    @JvmField
    val DISTILLATION_TOWER: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id("distillation_tower")) {
        // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineFactory.of(::HTDistillationTowerBlockEntity))
        set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTDistillationTowerMachineEntity))
        set(HTMachinePropertyKeys.CONDITION, RagiumMachineConditions.ELECTRIC_CONSUMER)
    }

    @JvmField
    val FLUID_DRILL: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id("fluid_drill")) {
        // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineFactory.of(::HTFluidDrillBlockEntity))
        set(HTMachinePropertyKeys.CONDITION, RagiumMachineConditions.ELECTRIC_CONSUMER)
    }

    @JvmField
    val SAW_MILL: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id("saw_mill")) {
        // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineFactory.of(::HTSawMillBlockEntity))
        set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTSawMillMachineEntity))
        set(HTMachinePropertyKeys.CONDITION, RagiumMachineConditions.ELECTRIC_CONSUMER)
    }

    enum class Processor(condition: HTMachineCondition = RagiumMachineConditions.ELECTRIC_CONSUMER) : HTMachineConvertible {
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
        ROCK_GENERATOR(condition = RagiumMachineConditions.ROCK_GENERATOR),
        ;

        private val machineType: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id(name.lowercase())) {
            // set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineFactory(HTProcessorBlockEntityBase::Simple))
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(::HTProcessorMachineEntity))
            set(HTMachinePropertyKeys.CONDITION, condition)
        }

        override fun asMachine(): HTMachineType = machineType
    }
}
