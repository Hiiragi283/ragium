package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.HTMachineTypeInitializer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getAroundPos
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.recipe.machine.HTRecipeComponentTypes
import hiiragi283.ragium.api.tags.RagiumFluidTags
import hiiragi283.ragium.common.machine.*
import net.minecraft.component.ComponentMap
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object RagiumMachineTypes : HTMachineTypeInitializer {
    override val priority: Int = -100

    override fun registerType(register: HTMachineTypeInitializer.Register) {
        // generators
        register.registerGenerator(HEAT_GENERATOR) {
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                world
                    .getMachineEntity(pos)
                    ?.let { it as? HTHeatGeneratorMachineEntity }
                    ?.isBurning
                    ?: false
            }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTHeatGeneratorMachineEntity))
        }
        RagiumMachineTypes.Generator.entries.forEach {
            register.registerGenerator(it.key, it::buildProperties)
        }
        // processors
        register.registerProcessor(BLAST_FURNACE) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTBlastFurnaceMachineEntity))
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
        }
        register.registerProcessor(DISTILLATION_TOWER) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTDistillationTowerMachineEntity))
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
        }
        register.registerProcessor(FLUID_DRILL) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTFluidDrillMachineEntity))
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
            set(HTMachinePropertyKeys.ADDITIONAL_RECIPE_MATCHER) { input: ComponentMap, recipe: ComponentMap ->
                input.get(HTRecipeComponentTypes.BIOME) == recipe.get(HTRecipeComponentTypes.BIOME)
            }
        }
        register.registerProcessor(MOB_EXTRACTOR) {
            set(HTMachinePropertyKeys.FRONT_MAPPER) { Direction.UP }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTMobExtractorMachineEntity))
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
            set(HTMachinePropertyKeys.ADDITIONAL_RECIPE_MATCHER) { input: ComponentMap, recipe: ComponentMap ->
                input.get(HTRecipeComponentTypes.ENTITY_TYPE) == recipe.get(HTRecipeComponentTypes.ENTITY_TYPE)
            }
        }
        register.registerProcessor(SAW_MILL) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory.of(::HTSawMillMachineEntity))
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, RagiumMachineConditions.ELECTRIC_CONDITION)
            set(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
        }

        RagiumMachineTypes.Processor.entries.forEach {
            register.registerProcessor(it.key, it::buildProperties)
        }
    }

    //    Generator    //

    @JvmField
    val HEAT_GENERATOR: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("heat_generator"))

    enum class Generator(private val fluidTag: TagKey<Fluid>? = null) : HTMachineConvertible {
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

        fun buildProperties(builder: HTPropertyHolder.Mutable) {
            builder[HTMachinePropertyKeys.FRONT_MAPPER] = { Direction.UP }
            builder[HTMachinePropertyKeys.MACHINE_FACTORY] = HTMachineEntity.Factory(::HTGeneratorMachineEntity)
            builder[HTMachinePropertyKeys.GENERATOR_PREDICATE] = ::canGenerate
            builder.setIfNonNull(HTMachinePropertyKeys.FUEL_TAG, fluidTag)
        }

        abstract fun canGenerate(world: World, pos: BlockPos): Boolean

        /*private val machineType: HTMachineType = HTMachineType.createGenerator(RagiumAPI.id(name.lowercase())) {
            set(HTMachinePropertyKeys.FRONT_MAPPER) { Direction.UP }
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(::HTGeneratorMachineEntity))
            set(HTMachinePropertyKeys.GENERATOR_PREDICATE, ::canGenerate)
            setIfNonNull(HTMachinePropertyKeys.FUEL_TAG, fluidTag)
        }*/

        val key: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id(name.lowercase()))

        override fun asMachine(): HTMachineType = key.asMachine()
    }

    //    Processor    //

    @JvmField
    val BLAST_FURNACE: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("blast_furnace"))

    @JvmField
    val DISTILLATION_TOWER: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("distillation_tower"))

    @JvmField
    val FLUID_DRILL: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("fluid_drill"))

    @JvmField
    val MOB_EXTRACTOR: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("mob_extractor"))

    @JvmField
    val SAW_MILL: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("saw_mill"))

    enum class Processor : HTMachineConvertible {
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
        ROCK_GENERATOR {
            override fun buildProperties(builder: HTPropertyHolder.Mutable) {
                super.buildProperties(builder)
                builder[HTMachinePropertyKeys.PROCESSOR_CONDITION] = RagiumMachineConditions.ROCK_SUCCEEDED
                builder.remove(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED)
            }
        },
        ;

        open fun buildProperties(builder: HTPropertyHolder.Mutable) {
            builder[HTMachinePropertyKeys.MACHINE_FACTORY] = HTMachineEntity.Factory(::HTProcessorMachineEntity)
            builder[HTMachinePropertyKeys.PROCESSOR_CONDITION] = RagiumMachineConditions.ELECTRIC_CONDITION
            builder.setIfNonNull(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, RagiumMachineConditions.ELECTRIC_SUCCEEDED)
        }

        /*private val machineType: HTMachineType = HTMachineType.createProcessor(RagiumAPI.id(name.lowercase())) {
            set(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntity.Factory(::HTProcessorMachineEntity))
            set(HTMachinePropertyKeys.PROCESSOR_CONDITION, condition)
            setIfNonNull(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED, succeeded)
        }*/

        val key: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id(name.lowercase()))

        override fun asMachine(): HTMachineType = key.asMachine()
    }
}
