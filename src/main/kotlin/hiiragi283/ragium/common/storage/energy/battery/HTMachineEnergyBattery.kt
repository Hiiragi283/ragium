package hiiragi283.ragium.common.storage.energy.battery

import hiiragi283.ragium.api.block.attribute.HTEnergyBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAttributeOrThrow
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
import java.util.function.Predicate

/**
 * @see mekanism.common.capabilities.energy.MachineEnergyContainer
 */
class HTMachineEnergyBattery<BE : HTMachineBlockEntity>(
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
        fun <BE : HTProcessorBlockEntity<*, *>> input(listener: HTContentListener?, blockEntity: BE): HTMachineEnergyBattery<BE> {
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

    var currentCapacity: Int = capacity
        private set
    var currentEnergyPerTick: Int = this.baseEnergyPerTick

    override fun getCapacity(): Int = currentCapacity

    fun setCapacity(capacity: Int) {
        this.currentCapacity = capacity
        setAmount(getAmount())
    }

    fun getBaseCapacity(): Int = super.getCapacity()

    fun consume(): Int {
        if (blockEntity !is HTProcessorBlockEntity<*, *>) return 0
        val simulated: Int = this.extract(currentEnergyPerTick, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        return when {
            simulated >= currentEnergyPerTick -> this.extract(currentEnergyPerTick, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            else -> 0
        }
    }

    fun generate(): Int {
        if (blockEntity !is HTGeneratorBlockEntity) return 0
        val simulated: Int = this.insert(currentEnergyPerTick, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        return when {
            simulated > 0 -> this.insert(currentEnergyPerTick, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            else -> 0
        }
    }
}
