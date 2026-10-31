package hiiragi283.lib.transfer.energy

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.minecraft.world.item.Item
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.Predicate

/**
 * [ItemAccess]に基づいた[HTEnergyStorage]の実装クラスです。
 * @see net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
open class HTItemAccessEnergyStorage(
    protected val itemAccess: ItemAccess,
    private val capacityPerItem: Int,
    protected val canExtract: Predicate<HTTransferAccess>,
    protected val canInsert: Predicate<HTTransferAccess>,
) : HTEnergyStorage {
    protected val validItem: Item = itemAccess.resource.item

    protected fun getAmount(resource: ItemResource): Int = when {
        !resource.`is`(validItem) -> 0
        else -> resource.getOrDefault(RagiumDataComponents.ENERGY, 0)
    }

    protected fun updateResource(resource: ItemResource, newAmount: Int): ItemResource = when {
        newAmount > 0 -> resource.with(RagiumDataComponents.ENERGY, newAmount)
        else -> resource.without(RagiumDataComponents.ENERGY) // 新しい量が0になるときはコンポーネントを消す
    }

    override fun insert(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        TransferPreconditions.checkNonNegative(amount)
        if (!this.canInsert.test(access)) return 0
        val accessAmount: Int = itemAccess.amount
        if (accessAmount == 0) return 0
        val amountPerItem: Int = minOf(capacityPerItem, amount / accessAmount)
        if (amountPerItem == 0) return 0

        val resourceIn: ItemResource = itemAccess.resource
        val currentAmountPerItem: Int = getAmount(resourceIn)
        val insertedPerItem: Int = minOf(amountPerItem, capacityPerItem - currentAmountPerItem)
        if (insertedPerItem > 0) {
            val filledResource: ItemResource = updateResource(resourceIn, currentAmountPerItem + insertedPerItem)
            if (!filledResource.isEmpty) {
                return insertedPerItem * itemAccess.exchange(filledResource, accessAmount, transaction)
            }
        }
        return 0
    }

    override fun extract(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        TransferPreconditions.checkNonNegative(amount)
        if (!this.canExtract.test(access)) return 0
        val accessAmount: Int = itemAccess.amount
        if (accessAmount == 0) return 0
        val amountPerItem: Int = minOf(capacityPerItem, amount / accessAmount)
        if (amountPerItem == 0) return 0

        val resourceIn: ItemResource = itemAccess.resource
        val currentAmountPerItem: Int = getAmount(resourceIn)
        val extractedPerItem: Int = minOf(amountPerItem, currentAmountPerItem)
        if (extractedPerItem > 0) {
            val emptiedResource: ItemResource = updateResource(resourceIn, currentAmountPerItem - extractedPerItem)
            if (!emptiedResource.isEmpty) {
                return extractedPerItem * itemAccess.exchange(emptiedResource, accessAmount, transaction)
            }
        }
        return 0
    }

    final override val amount: Int get() = getAmount(itemAccess.resource) * itemAccess.amount
    override val capacity: Int get() = this.capacityPerItem * itemAccess.amount
}
