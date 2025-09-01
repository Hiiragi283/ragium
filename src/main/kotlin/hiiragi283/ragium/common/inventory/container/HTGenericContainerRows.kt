package hiiragi283.ragium.common.inventory.container

import com.google.common.base.Predicates
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.common.storage.item.HTItemStackSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

interface HTGenericContainerRows {
    val rows: Int

    companion object {
        @JvmStatic
        fun createHandler(
            rows: Int,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: Predicate<ItemStack> = Predicates.alwaysTrue(),
            canInsert: Predicate<ItemStack> = Predicates.alwaysTrue(),
            filter: Predicate<ItemStack> = Predicates.alwaysTrue(),
        ): HTItemHandler = HTItemStackHandler(
            (0 until rows * 9).map { index: Int ->
                HTItemStackSlot.create(
                    null,
                    HTSlotHelper.getSlotPosX(index % 9),
                    HTSlotHelper.getSlotPosY(index / 9),
                    limit,
                    { stack: ItemStack, access: HTStorageAccess -> access == HTStorageAccess.MANUAL && canExtract.test(stack) },
                    { stack: ItemStack, access: HTStorageAccess -> access == HTStorageAccess.MANUAL && canInsert.test(stack) },
                    filter,
                )
            },
            null,
        )
    }
}
