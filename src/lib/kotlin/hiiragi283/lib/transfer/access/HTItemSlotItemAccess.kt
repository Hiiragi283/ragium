package hiiragi283.lib.transfer.access

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.item.HTItemSlot
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * [HTItemSlot]に対する[ItemAccess]の実装クラスです。
 *
 * 参照 : [Mekanism - InventorySlotItemAccess](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/inventory/access/InventorySlotItemAccess.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
@JvmRecord
data class HTItemSlotItemAccess(val slot: HTItemSlot, val access: HTTransferAccess) : ItemAccess {
    override fun getResource(): ItemResource = slot.resource

    override fun getAmount(): Int = slot.amount

    override fun insert(resource: ItemResource, amount: Int, transaction: TransactionContext): Int =
        slot.insert(resource, amount, transaction, access)

    override fun extract(resource: ItemResource, amount: Int, transaction: TransactionContext): Int =
        slot.extract(resource, amount, transaction, access)
}
