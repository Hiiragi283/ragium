package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.getAroundPos
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineTypeKey
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.common.machine.*
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumMachineTypes : RagiumPlugin {
    override val priority: Int = -100

    override fun afterRagiumInit() {}

    override fun registerMachineType(register: RagiumPlugin.MachineRegister) {
        // generators
        register.registerGenerator(HEAT_GENERATOR)
        RagiumMachineTypes.Generator.entries
            .map(Generator::key)
            .forEach(register::registerGenerator)
        // processors
        register.registerProcessor(BLAST_FURNACE)
        register.registerProcessor(DISTILLATION_TOWER)
        register.registerProcessor(SAW_MILL)
        RagiumMachineTypes.Processor.entries
            .map(Processor::key)
            .forEach(register::registerProcessor)
    }

    override fun setupCommonMachineProperties(helper: RagiumPlugin.PropertyHelper) {
        // generators
        helper.modify(HEAT_GENERATOR) {
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
        }
        RagiumMachineTypes.Generator.entries.forEach {
            helper.modify(it.key, it::buildProperties)
        }
        // processors
        helper.modify(BLAST_FURNACE) {
            set(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntity.Factory.ofStatic(::HTBlastFurnaceMachineEntity),
            )
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        helper.modify(DISTILLATION_TOWER) {
            set(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntity.Factory.ofStatic(::HTDistillationTowerMachineEntity),
            )
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        helper.modify(SAW_MILL) {
            set(HTMachinePropertyKeys.FRONT_TEX) { Identifier.of("block/stonecutter_saw") }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.ofStatic(::HTSawMillMachineEntity))
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
            set(HTMachinePropertyKeys.RECIPE_SIZE, HTMachineType.Size.LARGE)
        }
        RagiumMachineTypes.Processor.entries.forEach {
            helper.modify(it.key, it::buildProperties)
        }
    }

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

        fun buildProperties(builder: HTPropertyHolder.Mutable) {
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
        ROCK_GENERATOR {
            override fun buildProperties(builder: HTPropertyHolder.Mutable) {
                super.buildProperties(builder)
                builder[HTMachinePropertyKeys.PROCESSOR_CONDITION] = RagiumMachineConditions.ROCK_SUCCEEDED
                builder.remove(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED)
            }
        },
        ;

        open fun buildProperties(builder: HTPropertyHolder.Mutable) {
            builder[HTMachinePropertyKeys.MACHINE_FACTORY] =
                HTMachineEntity.Factory.ofProcessor(::HTSimpleProcessorMachineEntity)
            builder[HTMachinePropertyKeys.PROCESSOR_CONDITION] = RagiumMachineConditions.ELECTRIC_CONDITION
            builder[HTMachinePropertyKeys.RECIPE_SIZE] = HTMachineType.Size.SIMPLE
            builder.setIfNonNull(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
        }

        override val key: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id(name.lowercase()))

        override fun asMachine(): HTMachineType = key.asMachine()
    }
}
