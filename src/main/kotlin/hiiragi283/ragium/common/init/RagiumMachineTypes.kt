package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.HTMachineTypeInitializer
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.machine.HTMachineCondition
import hiiragi283.ragium.common.machine.HTMachineConvertible
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.Consumer

object RagiumMachineTypes : HTMachineTypeInitializer {
    override fun registerType(register: Consumer<HTMachineConvertible>) {
        buildList {
            add(BLAST_FURNACE)
            add(DISTILLATION_TOWER)
            addAll(RagiumMachineTypes.Generator.entries)
            addAll(RagiumMachineTypes.Processor.entries)
        }.forEach(register::accept)
    }

    //    Multi    //

    @JvmField
    val BLAST_FURNACE =
        HTMachineType.Processor(Ragium.id("blast_furnace"), RagiumMachineConditions.ELECTRIC_CONSUMER)

    @JvmField
    val DISTILLATION_TOWER =
        HTMachineType.Processor(Ragium.id("distillation_tower"), RagiumMachineConditions.ELECTRIC_CONSUMER)

    //    Generator    //

    enum class Generator : HTMachineConvertible {
        WATER {
            override fun canGenerate(world: World, pos: BlockPos): Boolean = Direction.entries
                .map(pos::offset)
                .map(world::getFluidState)
                .filter { it.isIn(FluidTags.WATER) }
                .size >= 2
        },
        SOLAR {
            override fun canGenerate(world: World, pos: BlockPos): Boolean = world.isDay
        },
        ;

        abstract fun canGenerate(world: World, pos: BlockPos): Boolean

        private val machineType = HTMachineType.Generator(Ragium.id(name.lowercase()), ::canGenerate)

        override fun asMachine(): HTMachineType = machineType
    }

    //    Processor    //

    enum class Processor(condition: HTMachineCondition = RagiumMachineConditions.ELECTRIC_CONSUMER) : HTMachineConvertible {
        ALLOY_FURNACE,
        ASSEMBLER,
        CENTRIFUGE,
        CHEMICAL_REACTOR,
        COMPRESSOR,
        ELECTROLYZER,
        EXTRACTOR,
        GRINDER,
        METAL_FORMER,
        MIXER,
        ROCK_GENERATOR(RagiumMachineConditions.ROCK_GENERATOR),
        SOLIDIFIER,
        ;

        private val machineType = HTMachineType.Processor(Ragium.id(name.lowercase()), condition)

        override fun asMachine(): HTMachineType = machineType
    }
}
