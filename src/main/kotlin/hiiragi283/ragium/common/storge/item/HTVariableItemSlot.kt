package hiiragi283.ragium.common.storge.item

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import java.util.function.BiPredicate
import java.util.function.IntSupplier
import java.util.function.Predicate

class HTVariableItemSlot(
    private val capacitySupplier: IntSupplier,
    canExtract: BiPredicate<HTItemResourceType, HTStorageAccess>,
    canInsert: BiPredicate<HTItemResourceType, HTStorageAccess>,
    filter: Predicate<HTItemResourceType>,
    listener: HTContentListener?,
) : HTBasicItemSlot(capacitySupplier.asInt, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: IntSupplier,
            canExtract: BiPredicate<HTItemResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<HTItemResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<HTItemResourceType> = HTStoragePredicates.alwaysTrue(),
        ): HTBasicItemSlot = HTVariableItemSlot(capacity, canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: IntSupplier,
            canInsert: Predicate<HTItemResourceType> = HTStoragePredicates.alwaysTrue(),
            filter: Predicate<HTItemResourceType> = canInsert,
        ): HTBasicItemSlot = create(
            listener,
            capacity,
            HTStoragePredicates.notExternal(),
            { resource: HTItemResourceType, _ -> canInsert.test(resource) },
            filter,
        )
    }

    override fun getCapacity(resource: HTItemResourceType?): Int = capacitySupplier.asInt
}
