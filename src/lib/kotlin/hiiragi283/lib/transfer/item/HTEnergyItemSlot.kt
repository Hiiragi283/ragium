package hiiragi283.lib.transfer.item

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.energy.HTEnergyStorage
import hiiragi283.lib.transfer.useTransaction
import net.minecraft.world.item.Item
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * 保持しているアイテムに対して充放電が可能な[HTBasicItemSlot]の拡張クラスです。
 *
 * 参照 : [Mekanism - EnergyInventorySlot](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/inventory/slot/EnergyInventorySlot.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
class HTEnergyItemSlot(
    private val storage: HTEnergyStorage,
    canExtract: BiPredicate<ItemResource, HTTransferAccess>,
    canInsert: BiPredicate<ItemResource, HTTransferAccess>,
    filter: Predicate<ItemResource>,
    listener: Runnable?,
) : HTBasicItemSlot(Item.ABSOLUTE_MAX_STACK_SIZE, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        fun fill(
            storage: HTEnergyStorage,
            listener: Runnable?,
            filter: Predicate<ItemResource> = HTTransferPredicates.alwaysTrue(),
        ): HTEnergyItemSlot = HTEnergyItemSlot(
            storage,
            HTTransferPredicates.notExternal<ItemResource>().or { resource: ItemResource, _ -> !canFill(resource) },
            HTTransferPredicates.internalOnly<ItemResource>().or { resource: ItemResource, _ -> canFill(resource) },
            filter,
            listener,
        )

        @JvmStatic
        fun drain(
            storage: HTEnergyStorage,
            listener: Runnable?,
            filter: Predicate<ItemResource> = HTTransferPredicates.alwaysTrue(),
        ): HTEnergyItemSlot = HTEnergyItemSlot(
            storage,
            HTTransferPredicates.notExternal<ItemResource>().or { resource: ItemResource, _ ->
                !canDrain(storage, resource)
            },
            HTTransferPredicates.internalOnly<ItemResource>().or { resource: ItemResource, _ ->
                canDrain(storage, resource)
            },
            filter,
            listener,
        )

        @JvmStatic
        fun canDrain(storage: HTEnergyStorage, resource: ItemResource): Boolean {
            val handler: EnergyHandler = resource.toStack().let(ItemAccess::forStack).let(HTEnergyCapabilities::getCapability) ?: return false
            return canDrain(storage, handler)
        }

        @JvmStatic
        private fun canDrain(storage: HTEnergyStorage, handler1: EnergyHandler): Boolean {
            if (storage.amount == 0) {
                return handler1.amountAsLong < handler1.capacityAsLong
            }
            return useTransaction { transaction: Transaction ->
                handler1.insert(storage.capacity, transaction) > 0
            }
        }

        @JvmStatic
        fun canFill(resource: ItemResource): Boolean {
            val handler1: EnergyHandler =
                resource.toStack().let(ItemAccess::forStack).let(HTEnergyCapabilities::getCapability) ?: return false
            return useTransaction { transaction: Transaction -> handler1.extract(Int.MAX_VALUE, transaction) > 0 }
        }
    }

    /**
     * スロット内のアイテムからエネルギーを移動します
     */
    fun fillContainerFromSlot(parent: TransactionContext?): Boolean {
        if (this.isEmpty || this.storage.needed == 0) {
            return false
        }
        val handlerInItem: EnergyHandler = HTEnergyCapabilities.getCapability(this.itemAccess) ?: return false
        var maxExtract: Int
        useTransaction(parent) { transaction: Transaction ->
            maxExtract = this.storage.insert(this.storage.needed, transaction, HTTransferAccess.INTERNAL)
            if (maxExtract == 0) return false
        }
        useTransaction(parent) { transaction: Transaction ->
            val extracted: Int = handlerInItem.extract(maxExtract, transaction)
            if (extracted > 0 && this.storage.insert(extracted, transaction, HTTransferAccess.INTERNAL) == extracted) {
                transaction.commit()
                return true
            }
        }
        return false
    }

    /**
     * スロット内のアイテムにエネルギーを移動します
     */
    fun drainContainerToSlot(parent: TransactionContext?): Boolean {
        if (this.isEmpty || this.storage.isEmpty) {
            return false
        }
        val handlerInItem: EnergyHandler = HTEnergyCapabilities.getCapability(this.itemAccess) ?: return false
        var maxInsert: Int
        useTransaction(parent) { transaction: Transaction ->
            maxInsert = this.storage.extract(this.storage.amount, transaction, HTTransferAccess.INTERNAL)
            if (maxInsert == 0) return false
        }
        useTransaction(parent) { transaction: Transaction ->
            val inserted: Int = handlerInItem.insert(maxInsert, transaction)
            if (inserted > 0 && this.storage.extract(inserted, transaction, HTTransferAccess.INTERNAL) == inserted) {
                transaction.commit()
                return true
            }
        }
        return false
    }
}
