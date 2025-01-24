package hiiragi283.ragium.common.internal

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeValidator
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.block.generator.HTDefaultGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTFluidGeneratorBlockEntity
import hiiragi283.ragium.common.block.processor.HTDefaultProcessorBlockEntity
import hiiragi283.ragium.common.block.processor.HTDistillationTowerBlockEntity
import hiiragi283.ragium.common.block.processor.HTLargeProcessorBlockEntity
import hiiragi283.ragium.common.block.processor.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.BiConsumer
import java.util.function.BiPredicate
import java.util.function.Function

object DefaultMachinePlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun registerMachine(consumer: BiConsumer<HTMachineKey, HTMachineType>) {
        // consumer
        RagiumMachineKeys.CONSUMERS.forEach { consumer.accept(it, HTMachineType.CONSUMER) }
        // generators
        RagiumMachineKeys.GENERATORS.forEach { consumer.accept(it, HTMachineType.GENERATOR) }
        // processors
        RagiumMachineKeys.PROCESSORS.forEach { consumer.accept(it, HTMachineType.PROCESSOR) }
    }

    @JvmField
    val ADVANCED_TIERS: List<HTMachineTier> =
        listOf(HTMachineTier.ADVANCED, HTMachineTier.ELITE, HTMachineTier.ULTIMATE)

    @JvmField
    val ELITE_TIERS: List<HTMachineTier> = listOf(HTMachineTier.ELITE, HTMachineTier.ULTIMATE)

    private fun HTPropertyHolderBuilder.putFactory(
        factory: BlockEntityType.BlockEntitySupplier<out HTMachineBlockEntity>,
    ): HTPropertyHolderBuilder = put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(factory::create))

    private fun HTPropertyHolderBuilder.putFactory(factory: HTMachineEntityFactory): HTPropertyHolderBuilder =
        put(HTMachinePropertyKeys.MACHINE_FACTORY, factory)

    private fun HTPropertyHolderBuilder.putValidator(validator: HTMachineRecipeValidator): HTPropertyHolderBuilder =
        put(HTMachinePropertyKeys.RECIPE_VALIDATOR, validator)

    override fun setupMachineProperties(helper: Function<HTMachineKey, HTPropertyHolderBuilder>) {
        // Consumer

        // Generator
        helper
            .apply(RagiumMachineKeys.COMBUSTION_GENERATOR)
            .putFactory(::HTFluidGeneratorBlockEntity)
            .put(
                HTMachinePropertyKeys.GENERATOR_FUEL,
                setOf(
                    HTGeneratorFuel(RagiumFluidTags.NON_NITRO_FUEL, FluidType.BUCKET_VOLUME / 10),
                    HTGeneratorFuel(RagiumFluidTags.NITRO_FUEL, FluidType.BUCKET_VOLUME / 100),
                ),
            ).put(HTMachinePropertyKeys.VALID_TIERS, ADVANCED_TIERS)

        helper
            .apply(RagiumMachineKeys.GAS_TURBINE)
            .putFactory(::HTFluidGeneratorBlockEntity)
            .put(
                HTMachinePropertyKeys.GENERATOR_FUEL,
                setOf(
                    HTGeneratorFuel(RagiumFluids.METHANE, FluidType.BUCKET_VOLUME / 10),
                    HTGeneratorFuel(RagiumFluids.ETHENE, FluidType.BUCKET_VOLUME / 20),
                    HTGeneratorFuel(RagiumFluids.ACETYLENE, FluidType.BUCKET_VOLUME / 50),
                ),
            ).put(HTMachinePropertyKeys.VALID_TIERS, ADVANCED_TIERS)

        helper
            .apply(RagiumMachineKeys.NUCLEAR_REACTOR)
            .putFactory(::HTFluidGeneratorBlockEntity)
            .put(
                HTMachinePropertyKeys.GENERATOR_FUEL,
                setOf(
                    HTGeneratorFuel(RagiumFluidTags.NUCLEAR_FUEL, FluidType.BUCKET_VOLUME / 10),
                ),
            ).put(HTMachinePropertyKeys.VALID_TIERS, ELITE_TIERS)

        helper
            .apply(RagiumMachineKeys.SOLAR_GENERATOR)
            .putFactory(::HTDefaultGeneratorBlockEntity)
            .put(
                HTMachinePropertyKeys.GENERATOR_PREDICATE,
                BiPredicate { level: Level, pos: BlockPos -> level.canSeeSky(pos.above()) && level.isDay },
            )

        helper
            .apply(RagiumMachineKeys.STEAM_GENERATOR)
            .put(HTMachinePropertyKeys.VALID_TIERS, listOf(HTMachineTier.BASIC, HTMachineTier.ADVANCED))

        helper
            .apply(RagiumMachineKeys.THERMAL_GENERATOR)
            .putFactory(::HTFluidGeneratorBlockEntity)
            .put(
                HTMachinePropertyKeys.GENERATOR_FUEL,
                setOf(
                    HTGeneratorFuel(RagiumFluidTags.THERMAL_FUEL, FluidType.BUCKET_VOLUME / 10),
                ),
            ).put(HTMachinePropertyKeys.VALID_TIERS, listOf(HTMachineTier.ADVANCED, HTMachineTier.ELITE))

        helper
            .apply(RagiumMachineKeys.VIBRATION_GENERATOR)
            .put(HTMachinePropertyKeys.VALID_TIERS, ELITE_TIERS)
        // Processor
        RagiumMachineKeys.PROCESSORS
            .map(helper::apply)
            .forEach { builder: HTPropertyHolderBuilder ->
                builder.putFactory(::HTDefaultProcessorBlockEntity)
                builder.put(HTMachinePropertyKeys.CATALYST_SLOT, 2)
            }

        helper
            .apply(RagiumMachineKeys.BLAST_FURNACE)
            .putFactory(::HTLargeProcessorBlockEntity)
            .put(HTMachinePropertyKeys.CATALYST_SLOT, 3)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.BLAST_FURNACE)

        helper
            .apply(RagiumMachineKeys.CHEMICAL_REACTOR)
            .putFactory(::HTLargeProcessorBlockEntity)
            .put(HTMachinePropertyKeys.CATALYST_SLOT, 3)

        helper
            .apply(RagiumMachineKeys.CUTTING_MACHINE)
            .put(HTMachinePropertyKeys.VALID_TIERS, ADVANCED_TIERS)

        helper
            .apply(RagiumMachineKeys.DISTILLATION_TOWER)
            .putFactory(::HTDistillationTowerBlockEntity)
            .put(HTMachinePropertyKeys.CATALYST_SLOT, 1)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.DISTILLATION_TOWER)
            .putValidator { recipe: HTMachineRecipe ->
                when {
                    recipe.itemInputs.isNotEmpty() -> DataResult.error { "Distillation tower recipe not accepts item inputs!" }
                    recipe.fluidInputs.size != 1 -> DataResult.error { "Distillation tower recipe should have only one fluid input!" }
                    // recipe.catalyst.isEmpty -> DataResult.error { "Distillation tower recipe requires catalyst!" }
                    recipe.getItemOutput(1) != null -> DataResult.error { "Distillation tower recipe should have one item output!" }
                    else -> DataResult.success(recipe)
                }
            }.put(HTMachinePropertyKeys.VALID_TIERS, ADVANCED_TIERS)

        helper
            .apply(RagiumMachineKeys.GROWTH_CHAMBER)
            .put(HTMachinePropertyKeys.VALID_TIERS, ADVANCED_TIERS)

        helper
            .apply(RagiumMachineKeys.LASER_TRANSFORMER)
            .put(HTMachinePropertyKeys.VALID_TIERS, ELITE_TIERS)

        helper
            .apply(RagiumMachineKeys.MIXER)
            .putFactory(::HTLargeProcessorBlockEntity)
            .put(HTMachinePropertyKeys.CATALYST_SLOT, 3)

        helper
            .apply(RagiumMachineKeys.MULTI_SMELTER)
            .putFactory(::HTMultiSmelterBlockEntity)
            .remove(HTMachinePropertyKeys.CATALYST_SLOT)
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.MULTI_SMELTER)
            .put(HTMachinePropertyKeys.VALID_TIERS, ADVANCED_TIERS)
    }
}
