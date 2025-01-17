package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.common.block.machine.processor.HTDefaultMachineBlockEntity
import hiiragi283.ragium.common.block.machine.processor.HTLargeMachineBlockEntity
import hiiragi283.ragium.common.block.machine.processor.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import java.util.function.BiConsumer
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

    override fun setupMachineProperties(helper: Function<HTMachineKey, HTPropertyHolderBuilder>) {
        // Consumer

        // Generator

        // Processor
        RagiumMachineKeys.PROCESSORS
            .map(helper::apply)
            .forEach { builder: HTPropertyHolderBuilder ->
                builder.put(
                    HTMachinePropertyKeys.MACHINE_FACTORY,
                    HTMachineEntityFactory(::HTDefaultMachineBlockEntity),
                )
            }

        helper
            .apply(RagiumMachineKeys.BLAST_FURNACE)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory(::HTLargeMachineBlockEntity))
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.BLAST_FURNACE)

        helper
            .apply(RagiumMachineKeys.CHEMICAL_REACTOR)
            .put(HTMachinePropertyKeys.VALID_TIERS, ADVANCED_TIERS)

        helper
            .apply(RagiumMachineKeys.LASER_TRANSFORMER)
            .put(HTMachinePropertyKeys.VALID_TIERS, ELITE_TIERS)

        helper
            .apply(RagiumMachineKeys.MULTI_SMELTER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTMultiSmelterBlockEntity))
            .put(HTMachinePropertyKeys.MULTIBLOCK_MAP, RagiumMultiblockMaps.MULTI_SMELTER)
    }
}
