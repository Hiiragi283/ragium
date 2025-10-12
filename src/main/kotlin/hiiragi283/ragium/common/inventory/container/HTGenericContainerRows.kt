package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageStack
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import java.util.function.Predicate

interface HTGenericContainerRows {
    val rows: Int

    companion object {
        @JvmStatic
        fun createHandler(
            rows: Int,
            limit: Long = RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: Predicate<HTItemStorageStack> = HTStorageStack.alwaysTrue(),
            canInsert: Predicate<HTItemStorageStack> = HTStorageStack.alwaysTrue(),
            filter: Predicate<HTItemStorageStack> = HTStorageStack.alwaysTrue(),
        ): HTItemHandler = HTItemStackHandler(
            (0..<(rows * 9)).map { index: Int ->
                HTItemStackSlot.create(
                    null,
                    HTSlotHelper.getSlotPosX(index % 9),
                    HTSlotHelper.getSlotPosY(index / 9),
                    limit,
                    { stack: HTItemStorageStack, access: HTStorageAccess -> access == HTStorageAccess.MANUAL && canExtract.test(stack) },
                    { stack: HTItemStorageStack, access: HTStorageAccess -> access == HTStorageAccess.MANUAL && canInsert.test(stack) },
                    filter,
                )
            },
            null,
        )
    }
}
