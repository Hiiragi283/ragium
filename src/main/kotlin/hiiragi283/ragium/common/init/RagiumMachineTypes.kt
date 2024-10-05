package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.HTMachineTypeInitializer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineCondition
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineFactory
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.block.entity.machine.*
import hiiragi283.ragium.common.util.getAroundPos
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
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
            addAll(RagiumMachineTypes.Processor.entries)
        }.forEach(register::accept)
    }

    //    Generator    //

    @JvmField
    val HEAT_GENERATOR: HTMachineType.Generator = HTMachineType
        .Builder(RagiumAPI.id("heat_generator"))
        .frontType(HTMachineType.FrontType.SIDE)
        .factory(::HTHeatGeneratorBlockEntity)
        .buildGenerator { world: World, pos: BlockPos ->
            (world.getBlockEntity(pos) as? HTHeatGeneratorBlockEntity)?.isBurning ?: false
        }

    enum class Generator(factory: HTMachineFactory = HTMachineFactory(::HTGeneratorBlockEntity)) : HTMachineConvertible {
        SOLAR {
            override fun canGenerate(world: World, pos: BlockPos): Boolean = world.isDay
        },
        THERMAL {
            override fun canGenerate(world: World, pos: BlockPos): Boolean = when {
                world.getBiome(pos).isIn(BiomeTags.IS_NETHER) -> true
                else -> pos.getAroundPos { world.getFluidState(it).isIn(FluidTags.LAVA) }.size >= 2
            }
        },
        WATER {
            override fun canGenerate(world: World, pos: BlockPos): Boolean =
                pos.getAroundPos { world.getFluidState(it).isIn(FluidTags.WATER) }.size >= 2
        },
        ;

        abstract fun canGenerate(world: World, pos: BlockPos): Boolean

        private val machineType: HTMachineType.Generator = HTMachineType
            .Builder(RagiumAPI.id(name.lowercase()))
            .frontType(HTMachineType.FrontType.TOP)
            .factory(factory)
            .buildGenerator(::canGenerate)

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

    enum class Processor(
        frontType: HTMachineType.FrontType = HTMachineType.FrontType.SIDE,
        condition: HTMachineCondition = RagiumMachineConditions.ELECTRIC_CONSUMER,
    ) : HTMachineConvertible {
        ALLOY_FURNACE,
        ASSEMBLER(frontType = HTMachineType.FrontType.TOP),
        CENTRIFUGE(frontType = HTMachineType.FrontType.TOP),
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
