package hiiragi283.ragium.common.transfer.energy

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.energy.HTBasicEnergyStorage
import java.util.function.IntSupplier
import java.util.function.Predicate

class HTVariableEnergyStorage(
    private val capacitySupplier: IntSupplier,
    canExtract: Predicate<HTTransferAccess>,
    canInsert: Predicate<HTTransferAccess>,
    listener: Runnable?,
) : HTBasicEnergyStorage(capacitySupplier.asInt, canExtract, canInsert, listener) {
    companion object {
        @JvmStatic
        fun input(capacity: IntSupplier, listener: Runnable?): HTVariableEnergyStorage =
            create(capacity, listener, HTTransferAccess.NOT_EXTERNAL, HTTransferPredicates.alwaysTrue())

        @JvmStatic
        fun output(capacity: IntSupplier, listener: Runnable?): HTVariableEnergyStorage =
            create(capacity, listener, HTTransferPredicates.alwaysTrue(), HTTransferAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            capacity: IntSupplier,
            listener: Runnable?,
            canExtract: Predicate<HTTransferAccess> = HTTransferPredicates.alwaysTrue(),
            canInsert: Predicate<HTTransferAccess> = HTTransferPredicates.alwaysTrue(),
        ): HTVariableEnergyStorage = HTVariableEnergyStorage(capacity, canExtract, canInsert, listener)
    }

    override val capacity: Int get() = capacitySupplier.asInt
}
