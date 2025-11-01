package hiiragi283.ragium.common.storage.energy.battery

import hiiragi283.ragium.api.block.attribute.HTEnergyBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAttributeOrThrow
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTConsumerBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import java.util.function.Predicate

/**
 * @see mekanism.common.capabilities.energy.MachineEnergyContainer
 */
class HTMachineEnergyBattery<BE : HTMachineBlockEntity>(
    capacity: Int,
    val energyPerTick: Int,
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
        fun <BE : HTConsumerBlockEntity> input(listener: HTContentListener?, blockEntity: BE): HTMachineEnergyBattery<BE> {
            val attribute: HTEnergyBlockAttribute = validateAttribute(blockEntity)
            return HTMachineEnergyBattery(
                attribute.getCapacity(),
                attribute.getUsage(),
                blockEntity,
                HTStorageAccess.NOT_EXTERNAL,
                HTPredicates.alwaysTrue(),
                listener,
            )
        }

        @JvmStatic
        fun <BE : HTGeneratorBlockEntity> output(listener: HTContentListener?, blockEntity: BE): HTMachineEnergyBattery<BE> {
            val attribute: HTEnergyBlockAttribute = validateAttribute(blockEntity)
            return HTMachineEnergyBattery(
                attribute.getCapacity(),
                attribute.getUsage(),
                blockEntity,
                HTPredicates.alwaysTrue(),
                HTStorageAccess.INTERNAL_ONLY,
                listener,
            )
        }
    }
}
