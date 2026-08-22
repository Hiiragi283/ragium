package hiiragi283.ragium.transfer.energy

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.energy.HTBasicEnergyHandler
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.block.entity.HTBaseMachineBlockEntity
import hiiragi283.ragium.block.entity.machine.HTProcessorBlockEntity
import java.util.function.Predicate

/**
 * @see mekanism.common.capabilities.energy.MachineEnergyContainer
 */
sealed class HTMachineEnergyHandler<BE : HTBaseMachineBlockEntity>(
    capacity: Int,
    val baseEnergyPerTick: Int,
    val blockEntity: BE,
    canExtract: Predicate<HTTransferAccess>,
    canInsert: Predicate<HTTransferAccess>,
    listener: Runnable?,
) : HTBasicEnergyHandler(capacity, canExtract, canInsert, listener) {
    companion object {
        @JvmStatic
        fun input(listener: Runnable?, blockEntity: HTProcessorBlockEntity.Energized): Processor = Processor(blockEntity.getConfig(), listener, blockEntity)

        /*fun <BE : HTGeneratorBlockEntity> output(listener: Runnable?, blockEntity: BE): Generator {
            val attribute: HTEnergyBlockAttribute = validateAttribute(blockEntity)
            return Generator(attribute, listener, blockEntity)
        }*/
    }

    var currentEnergyPerTick: Int = this.baseEnergyPerTick

    // override fun getCapacity(): Int = HTUpgradeHelper.getEnergyCapacity(blockEntity, super.getCapacity())

    class Processor(config: HTEnergyConfig, listener: Runnable?, blockEntity: HTProcessorBlockEntity) :
        HTMachineEnergyHandler<HTProcessorBlockEntity>(
            config.getCapacity(),
            config.getUsage(),
            blockEntity,
            HTTransferAccess.NOT_EXTERNAL,
            HTTransferPredicates.alwaysTrue(),
            listener,
        ) {
        fun consume(): Int = useTransaction {
            val extracted: Int = this.extract(currentEnergyPerTick, it, HTTransferAccess.INTERNAL)
            if (extracted > 0) {
                it.commit()
            }
            extracted
        }
    }

    /*class Generator(config: HTMachineConfig, listener: Runnable?, blockEntity: HTGeneratorBlockEntity) :
        HTMachineEnergyBattery<HTGeneratorBlockEntity>(
            config.getCapacity(),
            config.getUsage(),
            blockEntity,
            HTStoragePredicates.alwaysTrue(),
            HTStorageAccess.INTERNAL_ONLY,
            listener,
        ) {
        fun generate(): Int {
            val inserted: Int = this.insert(currentEnergyPerTick, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
            return when {
                inserted > 0 -> this.insert(currentEnergyPerTick, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                else -> 0
            }
        }
    }*/
}
