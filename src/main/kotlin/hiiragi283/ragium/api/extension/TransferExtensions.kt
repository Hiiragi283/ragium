package hiiragi283.ragium.api.extension

import com.google.common.base.Predicates
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTItemVariantStack
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTVariantStack
import hiiragi283.ragium.api.util.HTTagValueGetter
import hiiragi283.ragium.api.util.MutableComponentMap
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.util.Hand
import kotlin.math.min

//    Storage    //

fun fluidStorageOf(capacity: Long): SingleFluidStorage = SingleFluidStorage.withFixedCapacity(capacity) {}

fun <T : TransferVariant<*>> Storage<T>.insert(stack: HTVariantStack<*, T>, transaction: Transaction): Long = when {
    stack.isEmpty -> 0
    else -> insert(stack.variant, stack.amount, transaction)
}

fun <T : TransferVariant<*>> Storage<T>.extract(stack: HTVariantStack<*, T>, transaction: Transaction): Long = when {
    stack.isEmpty -> 0
    else -> extract(stack.variant, stack.amount, transaction)
}

fun Storage<FluidVariant>.interactWithFluidStorage(player: PlayerEntity, storageIO: HTStorageIO = HTStorageIO.GENERIC): Boolean =
    FluidStorageUtil.interactWithFluidStorage(storageIO.wrapStorage(this), player, Hand.MAIN_HAND)

val <T : Any> SingleSlotStorage<T>.isFilledMax: Boolean
    get() = amount == capacity

fun <T : TransferVariant<*>> Storage<T>.insertSelf(maxAmount: Long, transaction: TransactionContext): Long {
    val foundVariant: T = StorageUtil.findStoredResource(this, Predicates.alwaysTrue()) ?: return 0
    return insert(foundVariant, maxAmount, transaction)
}

fun <T : TransferVariant<*>> Storage<T>.insertSelf(initial: T, maxAmount: Long, transaction: TransactionContext): Long {
    val foundVariant: T = StorageUtil.findStoredResource(this, Predicates.alwaysTrue()) ?: when (initial.isBlank) {
        true -> return 0
        false -> initial
    }
    return insert(foundVariant, maxAmount, transaction)
}

fun <T : TransferVariant<*>> Storage<T>.extractSelf(maxAmount: Long, transaction: TransactionContext): Long {
    val foundVariant: T = StorageUtil.findExtractableResource(this, transaction) ?: return 0
    return extract(foundVariant, maxAmount, transaction)
}

val SingleItemStorage.variantStack: HTItemVariantStack
    get() = HTItemVariantStack(variant, amount)

var SingleFluidStorage.variantStack: HTFluidVariantStack
    get() = HTFluidVariantStack(variant, amount)
    set(value) {
        this.variant = value.variant
        this.amount = min(value.amount, this.capacity)
        if (value.isEmpty) {
            this.amount = 0
        }
    }

fun <T : Any> SlottedStorage<T>.getSlotOrNull(slot: Int): SingleSlotStorage<T>? = if (slot in 0..slotCount) getSlot(slot) else null

fun <T : TransferVariant<*>> SingleVariantStorage<T>.copyTo(other: SingleVariantStorage<T>) {
    other.variant = this.resource
    other.amount = this.amount
}

//    TransferVariant    //

fun <T : Any> TransferVariant<T>.isOf(entry: RegistryEntry<T>): Boolean = isOf(entry.value())

fun <T : Any> TransferVariant<T>.isIn(valueGetter: HTTagValueGetter<T>, tagKey: TagKey<T>): Boolean =
    valueGetter.getEntries(tagKey).any(this::isOf)

fun ItemVariant.isOf(item: ItemConvertible): Boolean = isOf(item.asItem())

fun ItemVariant.isIn(tagKey: TagKey<Item>): Boolean = isIn(Registries.ITEM::iterateEntries, tagKey)

fun FluidVariant.isIn(tagKey: TagKey<Fluid>): Boolean = isIn(Registries.FLUID::iterateEntries, tagKey)

val FluidVariant.name: MutableText
    get() = FluidVariantAttributes.getName(this).copy()

fun ContainerItemContext.modifyComponent(transaction: TransactionContext, count: Long = 1, action: (MutableComponentMap) -> Unit): Long {
    val newVariant: ItemVariant = itemVariant
        .toStack()
        .apply {
            MutableComponentMap.orNull(components)?.apply(action)
        }.let(ItemVariant::of)
    return exchange(newVariant, count, transaction)
}

//    Transaction    //

inline fun <R> useTransaction(parent: TransactionContext? = null, action: (Transaction) -> R): R =
    Transaction.openNested(parent).use(action)
