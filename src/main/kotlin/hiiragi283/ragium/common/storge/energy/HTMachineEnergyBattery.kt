package hiiragi283.ragium.common.storge.energy

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.common.storage.energy.HTBasicEnergyBattery
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.processing.HTProcessorBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import java.util.function.Predicate

/**
 * @see mekanism.common.capabilities.energy.MachineEnergyContainer
 */
sealed class HTMachineEnergyBattery<BE : HTMachineBlockEntity>(
    capacity: Int,
    val baseEnergyPerTick: Int,
    val blockEntity: BE,
    canExtract: Predicate<HTStorageAccess>,
    canInsert: Predicate<HTStorageAccess>,
) : HTBasicEnergyBattery(capacity, canExtract, canInsert) {
    companion object {
        @JvmStatic
        fun input(blockEntity: HTProcessorBlockEntity): Processor = Processor(blockEntity.getConfig(), blockEntity)

        /*fun <BE : HTGeneratorBlockEntity> output(listener: HTContentListener?, blockEntity: BE): Generator {
            val attribute: HTEnergyBlockAttribute = validateAttribute(blockEntity)
            return Generator(attribute, listener, blockEntity)
        }*/
    }

    var currentEnergyPerTick: Int = this.baseEnergyPerTick

    override fun getCapacity(): Int = HTUpgradeHelper.getEnergyCapacity(blockEntity, super.getCapacity())

    class Processor(config: HTMachineConfig, blockEntity: HTProcessorBlockEntity) :
        HTMachineEnergyBattery<HTProcessorBlockEntity>(
            config.getCapacity(),
            config.getUsage(),
            blockEntity,
            HTStorageAccess.NOT_EXTERNAL,
            HTStoragePredicates.alwaysTrue(),
        ) {
        fun consume(): Int {
            val extracted: Int = this.extract(currentEnergyPerTick, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
            return when {
                extracted >= currentEnergyPerTick -> this.extract(currentEnergyPerTick, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                else -> 0
            }
        }
    }

    /*class Generator(config: HTMachineConfig, listener: HTContentListener?, blockEntity: HTGeneratorBlockEntity) :
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
