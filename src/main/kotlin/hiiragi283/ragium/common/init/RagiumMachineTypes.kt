package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getAroundPos
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineTypeKey
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.common.machine.HTGeneratorMachineEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumMachineTypes {
    //    Generator    //

    @JvmField
    val HEAT_GENERATOR: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("heat_generator"))

    enum class Generator(val color: DyeColor, private val fluidTag: TagKey<Fluid>? = null) : HTMachineConvertible {
        COMBUSTION(DyeColor.BLUE, RagiumFluidTags.FUEL) {
            override fun canGenerate(world: World, pos: BlockPos): Boolean = false
        },
        SOLAR(DyeColor.LIGHT_BLUE) {
            override fun canGenerate(world: World, pos: BlockPos): Boolean = world.isDay
        },
        THERMAL(DyeColor.ORANGE, FluidTags.LAVA) {
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
        WATER(DyeColor.CYAN, FluidTags.WATER) {
            override fun canGenerate(world: World, pos: BlockPos): Boolean =
                pos.getAroundPos { world.getFluidState(it).isIn(FluidTags.WATER) }.size >= 2
        },
        ;

        fun buildProperties(builder: HTMutablePropertyHolder) {
            builder[HTMachinePropertyKeys.MACHINE_FACTORY] =
                HTMachineEntity.Factory.ofGenerator(::HTGeneratorMachineEntity)
            builder[HTMachinePropertyKeys.GENERATOR_PREDICATE] = ::canGenerate
            builder[HTMachinePropertyKeys.GENERATOR_COLOR] = color
            builder.setIfNonNull(HTMachinePropertyKeys.FUEL_TAG, fluidTag)
        }

        abstract fun canGenerate(world: World, pos: BlockPos): Boolean

        override val key: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id(name.lowercase()))

        override fun asMachine(): HTMachineType = key.asMachine()
    }

    //    Processor    //

    @JvmField
    val BLAST_FURNACE: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("blast_furnace"))

    @JvmField
    val DISTILLATION_TOWER: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("distillation_tower"))

    // val FLUID_DRILL: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("fluid_drill"))

    // val MOB_EXTRACTOR: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("mob_extractor"))

    @JvmField
    val SAW_MILL: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("saw_mill"))

    enum class Processor : HTMachineConvertible {
        ALLOY_FURNACE,
        ASSEMBLER,
        CHEMICAL_REACTOR,
        ELECTROLYZER,
        EXTRACTOR,
        GRINDER,
        METAL_FORMER,
        MIXER,
        ROCK_GENERATOR,
        ;

        override val key: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id(name.lowercase()))

        override fun asMachine(): HTMachineType = key.asMachine()
    }
}
