package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.aroundPos
import hiiragi283.ragium.api.machine.HTMachineEntityFactory
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.screen.HTMachineScreenHandler
import hiiragi283.ragium.common.block.machine.consume.*
import hiiragi283.ragium.common.block.machine.generator.*
import hiiragi283.ragium.common.block.machine.process.*
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.screen.*
import net.minecraft.fluid.FluidState
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.BiConsumer
import java.util.function.Function

class DefaultMachinePlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun registerMachine(consumer: BiConsumer<HTMachineKey, HTMachineType>) {
        // consumer
        RagiumMachineKeys.CONSUMERS.forEach { consumer.accept(it, HTMachineType.CONSUMER) }
        // generators
        RagiumMachineKeys.GENERATORS.forEach { consumer.accept(it, HTMachineType.GENERATOR) }
        // processors
        RagiumMachineKeys.PROCESSORS.forEach { consumer.accept(it, HTMachineType.PROCESSOR) }
    }

    override fun setupMachineProperties(helper: Function<HTMachineKey, HTPropertyHolderBuilder>) {
        // consumers
        helper
            .apply(RagiumMachineKeys.BEDROCK_MINER)
            .put(HTMachinePropertyKeys.FRONT_MAPPER) { Direction.DOWN }
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTBedrockMinerBlockEntity))
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.BEDROCK_MINER)
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FIREWORK)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTSmallMachineScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_STONE_BREAK)

        helper
            .apply(RagiumMachineKeys.BIOMASS_FERMENTER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTBiomassFermenterBlockEntity))
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.COMPOSTER)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTSmallMachineScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_COMPOSTER_FILL_SUCCESS)

        helper
            .apply(RagiumMachineKeys.DRAIN)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDrainBlockEntity))
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTSmallMachineScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_BUCKET_FILL)

        helper
            .apply(RagiumMachineKeys.FLUID_DRILL)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTFluidDrillBlockEntity))
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.FLUID_DRILL)
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FIREWORK)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTSmallMachineScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_BUCKET_FILL_FISH)

        helper
            .apply(RagiumMachineKeys.GAS_PLANT)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTGasPlantBlockEntity))
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.GAS_PLANT)
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FIREWORK)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTSmallMachineScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_BUCKET_FILL)

        helper
            .apply(RagiumMachineKeys.ROCK_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTRockGeneratorBlockEntity))
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.ASH)
            .put(HTMachinePropertyKeys.RECIPE_TYPE, RagiumRecipeTypes.ROCK_GENERATOR)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTRockGeneratorScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_STONE_BREAK)

        // generators
        RagiumMachineKeys.GENERATORS
            .map(helper::apply)
            .forEach { it.put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.SMOKE) }

        helper
            .apply(RagiumMachineKeys.COMBUSTION_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTCombustionGeneratorBlockEntity))
            .put(HTMachinePropertyKeys.MODEL_ID, RagiumAPI.id("block/generator"))
            .put(HTMachinePropertyKeys.ACTIVE_MODEL_ID, RagiumAPI.id("block/generator"))
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTSmallMachineScreenHandler))

        helper
            .apply(RagiumMachineKeys.NUCLEAR_REACTOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTNuclearReactorBlockEntity))
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTSmallMachineScreenHandler))

        helper
            .apply(RagiumMachineKeys.SOLAR_GENERATOR)
            .put(HTMachinePropertyKeys.FRONT_MAPPER) { Direction.UP }
            .put(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, _: BlockPos -> world.isDay }
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(::HTSimpleGeneratorBlockEntity))

        helper
            .apply(RagiumMachineKeys.STEAM_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTSteamGeneratorBlockEntity))
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.POOF)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTSmallMachineScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_FIRE_EXTINGUISH)

        helper
            .apply(RagiumMachineKeys.THERMAL_GENERATOR)
            .put(HTMachinePropertyKeys.GENERATOR_PREDICATE) { world: World, pos: BlockPos ->
                when {
                    world.getBiome(pos).isIn(BiomeTags.IS_NETHER) -> true
                    else ->
                        pos
                            .aroundPos
                            .filter {
                                val fluidState: FluidState = world.getFluidState(it)
                                fluidState.isIn(FluidTags.LAVA) && fluidState.isStill
                            }.size >= 4
                }
            }.put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTThermalGeneratorBlockEntity))
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FLAME)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTSmallMachineScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_BUCKET_EMPTY_LAVA)

        helper
            .apply(RagiumMachineKeys.VIBRATION_GENERATOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTVibrationGeneratorBlockEntity))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK)
        // processors
        RagiumMachineKeys.PROCESSORS
            .map(helper::apply)
            .forEach {
                it
                    .put(
                        HTMachinePropertyKeys.MACHINE_FACTORY,
                        HTMachineEntityFactory(::HTSimpleRecipeProcessorBlockEntity),
                    ).put(
                        HTMachinePropertyKeys.SCREEN_FACTORY,
                        HTMachineScreenHandler.Factory(::HTSimpleMachineScreenHandler),
                    )
            }

        helper
            .apply(RagiumMachineKeys.ASSEMBLER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTAssemblerBlockEntity))
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTAssemblerScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_CRAFTER_CRAFT)

        helper
            .apply(RagiumMachineKeys.BLAST_FURNACE)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(::HTLargeRecipeProcessorBlockEntity))
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.BLAST_FURNACE)
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FLAME)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTLargeMachineScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE)

        helper
            .apply(RagiumMachineKeys.CHEMICAL_REACTOR)
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.ELECTRIC_SPARK)

        helper
            .apply(RagiumMachineKeys.COMPRESSOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTCompressorBlockEntity))
            .put(HTMachinePropertyKeys.RECIPE_TYPE, RagiumRecipeTypes.COMPRESSOR)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_PISTON_EXTEND)

        helper
            .apply(RagiumMachineKeys.CUTTING_MACHINE)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTCuttingMachineBlockEntity))
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.CUTTING_MACHINE)
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.CRIT)
            .put(HTMachinePropertyKeys.RECIPE_TYPE, RagiumRecipeTypes.CUTTING_MACHINE)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTGrinderScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.ITEM_AXE_STRIP)

        helper
            .apply(RagiumMachineKeys.DISTILLATION_TOWER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTDistillationTowerBlockEntity))
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.DISTILLATION_TOWER)
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.FALLING_DRIPSTONE_LAVA)
            .put(HTMachinePropertyKeys.RECIPE_TYPE, RagiumRecipeTypes.DISTILLATION)
            .put(
                HTMachinePropertyKeys.SCREEN_FACTORY,
                HTMachineScreenHandler.Factory(::HTDistillationTowerScreenHandler),
            ).put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_LAVA_POP)

        helper
            .apply(RagiumMachineKeys.ELECTROLYZER)
            .put(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntityFactory(::HTChemicalRecipeProcessorBlockEntity),
            ).put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTChemicalMachineScreenHandler))
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.ELECTRIC_SPARK)

        helper
            .apply(RagiumMachineKeys.EXTRACTOR)
            .put(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntityFactory(::HTChemicalRecipeProcessorBlockEntity),
            ).put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTChemicalMachineScreenHandler))
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.ELECTRIC_SPARK)

        helper
            .apply(RagiumMachineKeys.GRINDER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTGrinderBlockEntity))
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.CRIT)
            .put(HTMachinePropertyKeys.RECIPE_TYPE, RagiumRecipeTypes.GRINDER)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTGrinderScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_GRINDSTONE_USE)

        helper
            .apply(RagiumMachineKeys.GROWTH_CHAMBER)
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.HAPPY_VILLAGER)
            .put(HTMachinePropertyKeys.RECIPE_TYPE, RagiumRecipeTypes.GROWTH_CHAMBER)

        helper
            .apply(RagiumMachineKeys.INFUSER)
            .put(
                HTMachinePropertyKeys.MACHINE_FACTORY,
                HTMachineEntityFactory(::HTChemicalRecipeProcessorBlockEntity),
            ).put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTChemicalMachineScreenHandler))
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.ELECTRIC_SPARK)

        helper
            .apply(RagiumMachineKeys.MIXER)
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.BUBBLE_POP)
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE)

        helper
            .apply(RagiumMachineKeys.MULTI_SMELTER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTMultiSmelterBlockEntity))
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.MULTI_SMELTER)
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.SOUL_FIRE_FLAME)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTSmallMachineScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_FIRE_EXTINGUISH)

        helper
            .apply(RagiumMachineKeys.LARGE_CHEMICAL_REACTOR)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTLargeChemicalReactorBlockEntity))
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.LARGE_MACHINE)
            .put(
                HTMachinePropertyKeys.SCREEN_FACTORY,
                HTMachineScreenHandler.Factory(::HTLargeChemicalReactorScreenHandler),
            ).put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.ELECTRIC_SPARK)

        helper
            .apply(RagiumMachineKeys.LASER_TRANSFORMER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTLaserTransformerBlockEntity))
            .put(HTMachinePropertyKeys.PARTICLE, ParticleTypes.END_ROD)
            .put(HTMachinePropertyKeys.SCREEN_FACTORY, HTMachineScreenHandler.Factory(::HTAssemblerScreenHandler))
            .put(HTMachinePropertyKeys.SOUND, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL)
    }
}
