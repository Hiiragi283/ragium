package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.toResourcePair
import hiiragi283.lib.transfer.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTOutputSlot<T : Resource> {
    fun insert(resource: T, amount: Int, transaction: TransactionContext): Int

    fun canInsert(resource: T, amount: Int, transaction: TransactionContext): Boolean = insert(resource, amount, transaction) == amount

    //    Single    //

    @JvmRecord
    data class Single<T : Resource>(private val slot: HTResourceSlot<T>) : HTOutputSlot<T> {
        override fun insert(resource: T, amount: Int, transaction: TransactionContext): Int = slot.insert(resource, amount, transaction, HTTransferAccess.INTERNAL)
    }

    //    Multiple    //

    @JvmRecord
    data class Multiple<T : Resource>(private val slots: Iterable<HTResourceSlot<T>>) : HTOutputSlot<T> {
        constructor(vararg slots: HTResourceSlot<T>) : this(slots.asIterable())

        override fun insert(resource: T, amount: Int, transaction: TransactionContext): Int {
            TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
            var inserted = 0
            for (slot: HTResourceSlot<T> in slots) {
                inserted += slot.insert(resource, amount - inserted, transaction, HTTransferAccess.INTERNAL)
                if (inserted == amount) break
            }
            return inserted
        }
    }
}

//    Extensions    //

fun HTOutputSlot<FluidResource>.insert(stack: FluidStack, transaction: TransactionContext): Int {
    if (stack.isEmpty) return 0
    val (resource: FluidResource, amount: Int) = stack.toResourcePair()
    return this.insert(resource, amount, transaction)
}

fun HTOutputSlot<FluidResource>.canInsert(stack: FluidStack, transaction: TransactionContext): Boolean = this.insert(stack, transaction) == stack.amount()

fun HTOutputSlot<ItemResource>.insert(stack: ItemStack, transaction: TransactionContext): Int {
    if (stack.isEmpty) return 0
    val (resource: ItemResource, amount: Int) = stack.toResourcePair()
    return this.insert(resource, amount, transaction)
}

fun HTOutputSlot<ItemResource>.canInsert(stack: ItemStack, transaction: TransactionContext): Boolean = this.insert(stack, transaction) == stack.count()
