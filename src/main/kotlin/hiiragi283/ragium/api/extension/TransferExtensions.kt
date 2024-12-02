package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.util.MutableComponentMap
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.component.ComponentMapImpl
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText

//    ResourceAmount    //

operator fun <T : Any> ResourceAmount<T>.plus(amount: Long): ResourceAmount<T> = ResourceAmount(resource, this.amount + amount)

operator fun <T : Any> ResourceAmount<T>.minus(amount: Long): ResourceAmount<T> = ResourceAmount(resource, this.amount - amount)

operator fun <T : Any> ResourceAmount<T>.times(amount: Long): ResourceAmount<T> = ResourceAmount(resource, this.amount * amount)

operator fun <T : Any> ResourceAmount<T>.div(amount: Long): ResourceAmount<T> = ResourceAmount(resource, this.amount / amount)

operator fun <T : Any> ResourceAmount<T>.component1(): T = resource

operator fun <T : Any> ResourceAmount<T>.component2(): Long = amount

fun <T : TransferVariant<*>> ResourceAmount<T>.isBlank(): Boolean = resource.isBlank || amount <= 0

fun <T : Any> ResourceAmount<T>.equalsResource(other: ResourceAmount<T>): Boolean = resource == other.resource

//    Storage    //

fun fluidStorageOf(capacity: Long): SingleFluidStorage = SingleFluidStorage.withFixedCapacity(capacity) {}

fun <T : TransferVariant<*>> Storage<T>.insert(resourceAmount: ResourceAmount<T>, transaction: Transaction): Long =
    insert(resourceAmount.resource, resourceAmount.amount, transaction)

fun <T : TransferVariant<*>> Storage<T>.extract(resourceAmount: ResourceAmount<T>, transaction: Transaction): Long =
    extract(resourceAmount.resource, resourceAmount.amount, transaction)

val <T : Any> SingleSlotStorage<T>.resourceAmount: ResourceAmount<T>
    get() = ResourceAmount(resource, amount)

val <T : Any> SingleSlotStorage<T>.isFilledMax: Boolean
    get() = amount == capacity

fun <T : Any> SlottedStorage<T>.getSlotOrNull(slot: Int): SingleSlotStorage<T>? = if (slot in 0..slotCount) getSlot(slot) else null

fun <T : TransferVariant<*>> SingleVariantStorage<T>.copyTo(other: SingleVariantStorage<T>) {
    other.variant = this.resource
    other.amount = this.amount
}

//    TransferVariant    //

fun <T : Any> TransferVariant<T>.isOf(entry: RegistryEntry<T>): Boolean = isOf(entry.value())

fun <T : Any> TransferVariant<T>.isIn(registry: Registry<T>, tagKey: TagKey<T>): Boolean = registry.iterateEntries(tagKey).any(this::isOf)

fun ItemVariant.isOf(item: ItemConvertible): Boolean = isOf(item.asItem())

fun ItemVariant.isIn(tagKey: TagKey<Item>): Boolean = isIn(Registries.ITEM, tagKey)

fun FluidVariant.isIn(tagKey: TagKey<Fluid>): Boolean = isIn(Registries.FLUID, tagKey)

val FluidVariant.name: MutableText
    get() = FluidVariantAttributes.getName(this).copy()

fun ContainerItemContext.modifyComponent(
    transaction: TransactionContext,
    count: Long = 1,
    action: (MutableComponentMap) -> Unit
): Long {
    val newVariant: ItemVariant = itemVariant
        .toStack()
        .apply {
            (components as? ComponentMapImpl)?.let(MutableComponentMap::of)?.apply(action)
        }.let(ItemVariant::of)
    return exchange(newVariant, count, transaction)
}

//    Transaction    //

inline fun <R> useTransaction(parent: TransactionContext? = null, action: (Transaction) -> R): R =
    Transaction.openNested(parent).use(action)
