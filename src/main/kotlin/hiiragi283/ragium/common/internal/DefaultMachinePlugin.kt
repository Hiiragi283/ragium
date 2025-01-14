package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.machine.HTMachineEntityFactory
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.common.block.machine.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.init.RagiumMachineKeys
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

    override fun setupMachineProperties(helper: Function<HTMachineKey, HTPropertyHolderBuilder>) {
        // Consumer
        // Generator
        // Processor

        helper
            .apply(RagiumMachineKeys.MULTI_SMELTER)
            .put(HTMachinePropertyKeys.MACHINE_FACTORY, HTMachineEntityFactory.of(::HTMultiSmelterBlockEntity))
    }
}
