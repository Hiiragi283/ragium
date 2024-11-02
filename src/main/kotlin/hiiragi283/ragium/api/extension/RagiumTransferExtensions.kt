package hiiragi283.ragium.api.extension

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey

//    ResourceAmount    //

/*
fun <O : TransferVariant<*>, S : Any> stackMapper(objGetter: (S) -> O, numGetter: (S) -> Long): Function<S, ResourceAmount<O>> =
    Function { stack: S -> ResourceAmount(objGetter(stack), numGetter(stack)) }

@JvmField
val ITEM_STACK_MAPPER: Function<ItemStack, ResourceAmount<ItemVariant>> =
    stackMapper(ItemVariant::of) { it.count.toLong() }

@JvmField
val FLUID_STACK_MAPPER: Function<HTFluidStack, ResourceAmount<FluidVariant>> =
    stackMapper({ FluidVariant.of(it.fluid, it.getComponentChanges()) }) { it.amount }
*/

operator fun <T : Any> ResourceAmount<T>.plus(amount: Long): ResourceAmount<T> = ResourceAmount(resource, this.amount + amount)

operator fun <T : Any> ResourceAmount<T>.minus(amount: Long): ResourceAmount<T> = ResourceAmount(resource, this.amount - amount)

operator fun <T : Any> ResourceAmount<T>.times(amount: Long): ResourceAmount<T> = ResourceAmount(resource, this.amount * amount)

operator fun <T : Any> ResourceAmount<T>.div(amount: Long): ResourceAmount<T> = ResourceAmount(resource, this.amount / amount)

operator fun <T : Any> ResourceAmount<T>.component1(): T = resource

operator fun <T : Any> ResourceAmount<T>.component2(): Long = amount

fun <T : TransferVariant<*>> ResourceAmount<T>.isBlank(): Boolean = resource.isBlank || amount <= 0

fun <T : Any> ResourceAmount<T>.equalsResource(other: ResourceAmount<T>): Boolean = resource == other.resource

//    Storage    //

fun <T : TransferVariant<*>> Storage<T>.insert(resourceAmount: ResourceAmount<T>, transaction: Transaction): Long =
    insert(resourceAmount.resource, resourceAmount.amount, transaction)

fun <T : TransferVariant<*>> Storage<T>.extract(resourceAmount: ResourceAmount<T>, transaction: Transaction): Long =
    extract(resourceAmount.resource, resourceAmount.amount, transaction)

val <T : Any> SingleSlotStorage<T>.resourceAmount: ResourceAmount<T>
    get() = ResourceAmount(resource, amount)

fun <T : Any> SlottedStorage<T>.getSlotOrNull(slot: Int): SingleSlotStorage<T>? = if (slot in 0..slotCount) getSlot(slot) else null

//    TransferVariant    //

fun <T : Any> TransferVariant<T>.isOf(entry: RegistryEntry<T>): Boolean = isOf(entry.value())

fun <T : Any> TransferVariant<T>.isIn(registry: Registry<T>, tagKey: TagKey<T>): Boolean = registry.iterateEntries(tagKey).any(this::isOf)

fun ItemVariant.isOf(item: ItemConvertible): Boolean = isOf(item.asItem())

//    Transaction    //

inline fun <R> useTransaction(action: (Transaction) -> R): R = Transaction.openOuter().use(action)
