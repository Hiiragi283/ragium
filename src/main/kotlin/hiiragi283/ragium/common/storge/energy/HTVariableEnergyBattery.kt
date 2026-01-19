package hiiragi283.ragium.common.storge.energy

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.common.storage.energy.HTBasicEnergyBattery
import java.util.function.IntSupplier
import java.util.function.Predicate

class HTVariableEnergyBattery(
    private val capacitySupplier: IntSupplier,
    canExtract: Predicate<HTStorageAccess>,
    canInsert: Predicate<HTStorageAccess>,
) : HTBasicEnergyBattery(capacitySupplier.asInt, canExtract, canInsert) {
    companion object {
        @JvmStatic
        fun create(capacity: IntSupplier): HTBasicEnergyBattery = HTVariableEnergyBattery(
            capacity,
            HTStoragePredicates.alwaysTrue(),
            HTStoragePredicates.alwaysTrue(),
        )

        @JvmStatic
        fun input(capacity: IntSupplier): HTBasicEnergyBattery = HTVariableEnergyBattery(
            capacity,
            HTStorageAccess.NOT_EXTERNAL,
            HTStoragePredicates.alwaysTrue(),
        )

        @JvmStatic
        fun output(capacity: IntSupplier): HTBasicEnergyBattery = HTVariableEnergyBattery(
            capacity,
            HTStoragePredicates.alwaysTrue(),
            HTStorageAccess.INTERNAL_ONLY,
        )
    }

    override fun getCapacity(): Int = capacitySupplier.asInt
}
