package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.HTMachineTypeInitializer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineCondition
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTHeatGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.*
import hiiragi283.ragium.common.util.getAroundPos
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
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
    val HEAT_GENERATOR: HTMachineType.Generator = HTMachineType
        .Builder(RagiumAPI.id("heat_generator"))
        .frontType(HTMachineType.FrontType.SIDE)
        .factory(::HTHeatGeneratorBlockEntity)
        .buildGenerator(FluidTags.WATER) { world: World, pos: BlockPos ->
            (world.getBlockEntity(pos) as? HTHeatGeneratorBlockEntity)?.isBurning ?: false
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
                else -> pos.getAroundPos {
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

        private val machineType: HTMachineType.Generator = HTMachineType
            .Builder(RagiumAPI.id(name.lowercase()))
            .frontType(HTMachineType.FrontType.TOP)
            .factory(HTGeneratorBlockEntity::Simple)
            .buildGenerator(fluidTag, ::canGenerate)

        override fun asMachine(): HTMachineType = machineType
    }

    //    Processor    //

    @JvmField
    val BLAST_FURNACE: HTMachineType.Processor =
        HTMachineType
            .Builder(RagiumAPI.id("blast_furnace"))
            .frontType(HTMachineType.FrontType.SIDE)
            .factory(::HTBlastFurnaceBlockEntity)
            .buildProcessor(RagiumMachineConditions.ELECTRIC_CONSUMER)

    @JvmField
    val DISTILLATION_TOWER: HTMachineType.Processor =
        HTMachineType
            .Builder(RagiumAPI.id("distillation_tower"))
            .frontType(HTMachineType.FrontType.SIDE)
            .factory(::HTDistillationTowerBlockEntity)
            .buildProcessor(RagiumMachineConditions.ELECTRIC_CONSUMER)

    @JvmField
    val FLUID_DRILL: HTMachineType.Processor =
        HTMachineType
            .Builder(RagiumAPI.id("fluid_drill"))
            .frontType(HTMachineType.FrontType.SIDE)
            .factory(::HTFluidDrillBlockEntity)
            .buildProcessor(RagiumMachineConditions.ELECTRIC_CONSUMER)

    @JvmField
    val SAW_MILL: HTMachineType.Processor =
        HTMachineType
            .Builder(RagiumAPI.id("saw_mill"))
            .frontType(HTMachineType.FrontType.SIDE)
            .factory(::HTSawMillBlockEntity)
            .buildProcessor(RagiumMachineConditions.ELECTRIC_CONSUMER)

    enum class Processor(
        frontType: HTMachineType.FrontType = HTMachineType.FrontType.SIDE,
        condition: HTMachineCondition = RagiumMachineConditions.ELECTRIC_CONSUMER,
    ) : HTMachineConvertible {
        ALLOY_FURNACE,
        ASSEMBLER(frontType = HTMachineType.FrontType.TOP),
        CHEMICAL_REACTOR,
        COMPRESSOR,
        DECOMPRESSOR,
        ELECTROLYZER(frontType = HTMachineType.FrontType.TOP),
        EXTRACTOR,
        GRINDER,
        METAL_FORMER,
        MIXER(frontType = HTMachineType.FrontType.TOP),
        ROCK_GENERATOR(condition = RagiumMachineConditions.ROCK_GENERATOR),
        ;

        private val machineType: HTMachineType.Processor = HTMachineType
            .Builder(RagiumAPI.id(name.lowercase()))
            .frontType(frontType)
            .factory(HTProcessorBlockEntityBase::Simple)
            .buildProcessor(condition)

        override fun asMachine(): HTMachineType = machineType
    }
}
