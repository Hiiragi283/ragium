package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTItemVariantStack
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTVariantStack
import hiiragi283.ragium.api.util.HTTagValueGetter
import hiiragi283.ragium.api.util.MutableComponentMap
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Hand
import kotlin.math.min

//    Storage    //

/**
 * 指定した[stack]をストレージに搬入します。
 * @param T [TransferVariant]を継承したクラス
 * @return 搬入できた量
 */
fun <T : TransferVariant<*>> Storage<T>.insert(stack: HTVariantStack<*, T>, transaction: Transaction): Long = when {
    stack.isEmpty -> 0
    else -> insert(stack.variant, stack.amount, transaction)
}

/**
 * 指定した[stack]をストレージから搬出します。
 * @param T [TransferVariant]を継承したクラス
 * @return 搬出できた量
 */
fun <T : TransferVariant<*>> Storage<T>.extract(stack: HTVariantStack<*, T>, transaction: Transaction): Long = when {
    stack.isEmpty -> 0
    else -> extract(stack.variant, stack.amount, transaction)
}

/**
 * 指定した[player]が[Hand.MAIN_HAND]に持っている[net.minecraft.item.ItemStack]でストレージに干渉します。
 * @param storageIO この処理の搬出入を制御
 * @return 液体が移動した場合はtrue，それ以外の場合はfalse
 */
fun Storage<FluidVariant>.interactWithFluidStorage(player: PlayerEntity, storageIO: HTStorageIO = HTStorageIO.GENERIC): Boolean =
    FluidStorageUtil.interactWithFluidStorage(storageIO.wrapStorage(this), player, Hand.MAIN_HAND)

fun Storage<FluidVariant>.findMatching(tagKey: TagKey<Fluid>): FluidVariant? = StorageUtil.findStoredResource(this) { it.isIn(tagKey) }

/**
 * 指定した[SingleSlotStorage]の容量が満タンかどうか判定します。
 */
val <T : Any> SingleSlotStorage<T>.isFilledMax: Boolean
    get() = amount == capacity

/**
 * 指定した[Storage]の中身と同じ[TransferVariant]を搬入します。
 * @param T [TransferVariant]を継承したクラス
 * @param initial [Storage]が空の場合に使う[TransferVariant]
 * @return 搬入できた量
 */
fun <T : TransferVariant<*>> Storage<T>.insertSelf(initial: T, maxAmount: Long, transaction: TransactionContext): Long {
    val foundVariant: T = StorageUtil.findStoredResource(this) ?: when (initial.isBlank) {
        true -> return 0
        false -> initial
    }
    return insert(foundVariant, maxAmount, transaction)
}

/**
 * 指定した[Storage]の中身と同じ[TransferVariant]を搬出します。
 * @param T [TransferVariant]を継承したクラス
 * @return 搬出できた量
 */
fun <T : TransferVariant<*>> Storage<T>.extractSelf(maxAmount: Long, transaction: TransactionContext): Long {
    val foundVariant: T = StorageUtil.findExtractableResource(this, transaction) ?: return 0
    return extract(foundVariant, maxAmount, transaction)
}

/**
 * 指定したに[SingleItemStorage]対して[HTItemVariantStack]の取得と代入を行います。
 */
var SingleItemStorage.variantStack: HTItemVariantStack
    get() = HTItemVariantStack(variant, amount)
    set(value) {
        this.variant = value.variant
        this.amount = min(value.amount, this.capacity)
        if (value.isEmpty) {
            this.amount = 0
        }
    }

/**
 * 指定したに[SingleFluidStorage]対して[HTFluidVariantStack]の取得と代入を行います。
 */
var SingleFluidStorage.variantStack: HTFluidVariantStack
    get() = HTFluidVariantStack(variant, amount)
    set(value) {
        this.variant = value.variant
        this.amount = min(value.amount, this.capacity)
        if (value.isEmpty) {
            this.amount = 0
        }
    }

//    TransferVariant    //

/**
 * 指定した[TransferVariant]のオブジェクトが[entry]の値と一致するかどうか判定します。
 */
fun <T : Any> TransferVariant<T>.isOf(entry: RegistryEntry<T>): Boolean = isOf(entry.value())

/**
 * 指定した[TransferVariant]のオブジェクトが[tagKey]に含まれているかどうか判定します。
 * @param valueGetter [tagKey]の値を提供するブロック
 */
fun <T : Any> TransferVariant<T>.isIn(valueGetter: HTTagValueGetter<T>, tagKey: TagKey<T>): Boolean =
    valueGetter.getValues(tagKey).any(this::isOf)

/**
 * 指定した[ItemVariant]のオブジェクトが[item]と一致するか判定します。
 */
fun ItemVariant.isOf(item: ItemConvertible): Boolean = isOf(item.asItem())

/**
 * 指定した[ItemVariant]のオブジェクトが[tagKey]に含まれているか判定します。
 */
fun ItemVariant.isIn(tagKey: TagKey<Item>): Boolean = isIn(Registries.ITEM::iterateEntries, tagKey)

/**
 * 指定した[FluidVariant]のオブジェクトが[tagKey]に含まれているか判定します。
 */
fun FluidVariant.isIn(tagKey: TagKey<Fluid>): Boolean = isIn(Registries.FLUID::iterateEntries, tagKey)

/**
 * 指定した[ContainerItemContext]内の[ItemVariant]のコンポーネントを変化させます。
 */
fun ContainerItemContext.modifyComponent(transaction: TransactionContext, count: Long = 1, action: (MutableComponentMap) -> Unit): Long {
    val newVariant: ItemVariant = itemVariant
        .toStack()
        .apply {
            MutableComponentMap.orNull(components)?.apply(action)
        }.let(ItemVariant::of)
    return exchange(newVariant, count, transaction)
}

//    Transaction    //

/**
 * [Transaction]を安全に使えます。
 * @param R 戻り値の値
 * @param parent 現在開いている[TransactionContext]
 * @param action [Transaction]を扱うブロック
 * @return [action]の戻り値
 */
inline fun <R> useTransaction(parent: TransactionContext? = null, action: (Transaction) -> R): R =
    Transaction.openNested(parent).use(action)
