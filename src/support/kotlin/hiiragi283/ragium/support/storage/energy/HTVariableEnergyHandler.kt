package hiiragi283.ragium.support.storage.energy

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.support.storage.energy.HTBasicEnergyHandler
import java.util.function.IntSupplier
import java.util.function.Predicate

class HTVariableEnergyHandler(
    private val capacitySupplier: IntSupplier,
    canExtract: Predicate<HTStorageAccess>,
    canInsert: Predicate<HTStorageAccess>,
    listener: HTContentListener?,
) : HTBasicEnergyHandler(capacitySupplier.asInt, canExtract, canInsert, listener) {
    companion object {
        @JvmStatic
        fun create(listener: HTContentListener?, capacity: IntSupplier): HTBasicEnergyHandler = HTVariableEnergyHandler(
            capacity,
            HTStoragePredicates.alwaysTrue(),
            HTStoragePredicates.alwaysTrue(),
            listener,
        )

        @JvmStatic
        fun input(listener: HTContentListener?, capacity: IntSupplier): HTBasicEnergyHandler = HTVariableEnergyHandler(
            capacity,
            HTStorageAccess.NOT_EXTERNAL,
            HTStoragePredicates.alwaysTrue(),
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: IntSupplier): HTBasicEnergyHandler = HTVariableEnergyHandler(
            capacity,
            HTStoragePredicates.alwaysTrue(),
            HTStorageAccess.INTERNAL_ONLY,
            listener,
        )
    }

    override fun getCapacity(): Int = capacitySupplier.asInt
}
