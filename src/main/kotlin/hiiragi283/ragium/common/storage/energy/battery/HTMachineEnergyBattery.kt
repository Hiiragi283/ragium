package hiiragi283.ragium.common.storage.energy.battery

import hiiragi283.ragium.api.block.attribute.HTEnergyBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAttributeOrThrow
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.upgrade.RagiumUpgradeKeys
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
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
    listener: HTContentListener?,
) : HTBasicEnergyBattery(capacity, canExtract, canInsert, listener) {
    companion object {
        @JvmStatic
        private fun validateAttribute(blockEntity: HTMachineBlockEntity): HTEnergyBlockAttribute =
            blockEntity.blockHolder.getAttributeOrThrow<HTEnergyBlockAttribute>()

        @JvmStatic
        fun input(listener: HTContentListener?, blockEntity: HTProcessorBlockEntity<*, *>): Processor {
            val attribute: HTEnergyBlockAttribute = validateAttribute(blockEntity)
            return Processor(attribute, listener, blockEntity)
        }

        @JvmStatic
        fun <BE : HTGeneratorBlockEntity> output(listener: HTContentListener?, blockEntity: BE): Generator {
            val attribute: HTEnergyBlockAttribute = validateAttribute(blockEntity)
            return Generator(attribute, listener, blockEntity)
        }
    }

    var currentEnergyPerTick: Int = this.baseEnergyPerTick

    override fun getCapacity(): Int = blockEntity.modifyValue(RagiumUpgradeKeys.ENERGY_CAPACITY) {
        getBaseCapacity() * it * blockEntity.getBaseMultiplier()
    }

    fun getBaseCapacity(): Int = super.getCapacity()

    class Processor(attribute: HTEnergyBlockAttribute, listener: HTContentListener?, blockEntity: HTProcessorBlockEntity<*, *>) :
        HTMachineEnergyBattery<HTProcessorBlockEntity<*, *>>(
            attribute.getCapacity(),
            attribute.getUsage(),
            blockEntity,
            HTStorageAccess.NOT_EXTERNAL,
            HTPredicates.alwaysTrue(),
            listener,
        ) {
        fun consume(): Int {
            val extracted: Int = this.extract(currentEnergyPerTick, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
            return when {
                extracted >= currentEnergyPerTick -> this.extract(currentEnergyPerTick, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                else -> 0
            }
        }
    }

    class Generator(attribute: HTEnergyBlockAttribute, listener: HTContentListener?, blockEntity: HTGeneratorBlockEntity) :
        HTMachineEnergyBattery<HTGeneratorBlockEntity>(
            attribute.getCapacity(),
            attribute.getUsage(),
            blockEntity,
            HTPredicates.alwaysTrue(),
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
    }
}
