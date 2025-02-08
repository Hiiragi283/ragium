@file:Suppress("DEPRECATION")

package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumReferences
import hiiragi283.ragium.api.machine.HTMachineKey
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.IWithData

//    ItemLike    //

/**
 * この[ItemLike]から[Holder]を返します。
 */
fun ItemLike.asHolder(): Holder.Reference<Item> = asItem().builtInRegistryHolder()

/**
 * この[ItemLike]から[ItemStack]を返します。
 * @param count [ItemStack]の個数
 */
fun ItemLike.toStack(count: Int = 1): ItemStack = ItemStack(asItem(), count)

/**
 * 指定した[type]で[ItemLike]から[T]を返します。
 * @return [IWithData.getData]が`null`の場合は`null`
 */
fun <T : Any> ItemLike.getItemData(type: DataMapType<Item, T>): T? = asHolder().getData(type)

/**
 * 指定した[type]で[ItemStack]から[T]を返します。
 * @return [IWithData.getData]が`null`の場合は`null`
 */
fun <T : Any> ItemStack.getItemData(type: DataMapType<Item, T>): T? = itemHolder.getData(type)

/**
 * 指定した[type]で[BlockState]から[T]を返します。
 * @return [IWithData.getData]が`null`の場合は`null`
 */
fun <T : Any> BlockState.getItemData(type: DataMapType<Item, T>): T? = block.getItemData(type)

val ItemLike.machineKey: HTMachineKey? get() = getItemData(RagiumReferences.DataMapTypes.MACHINE_KEY)

//    Item    //

fun itemProperty(): Item.Properties = Item.Properties()

fun itemProperty(builderAction: Item.Properties.() -> Unit): Item.Properties = Item.Properties().apply(builderAction)

/**
 * 指定した[text]をアイテムの名前に設定します。
 */
fun Item.Properties.name(text: Component): Item.Properties = component(DataComponents.ITEM_NAME, text)

//    ItemStack    //

/**
 * 指定した[item]とアイテムが一致するか判定します。
 */
fun ItemStack.isOf(item: ItemLike): Boolean = `is`(item.asItem())

/**
 * 残りの耐久値を返します。
 */
val ItemStack.restDamage: Int get() = maxDamage - damageValue

/**
 * 現在の個数が[isMaxCount]と一致するか判定します。
 */
val ItemStack.isMaxCount: Boolean get() = count == maxStackSize

//    IItemHandler    //

inline fun IItemHandler.forEach(action: (ItemStack) -> Unit) {
    (0 until this.slots).map(this::getStackInSlot).forEach(action)
}

inline fun IItemHandler.forEachSlot(action: (Int) -> Unit) {
    (0 until this.slots).forEach(action)
}

/**
 * この[IItemHandler]に保存されたすべての[ItemStack]を指定した[pos]にドロップします。
 */
fun IItemHandler.dropStacks(level: Level, pos: BlockPos) {
    forEach { dropStackAt(level, pos, it) }
}

/**
 * 指定した[stack]がこの[IItemHandler]に入れられるか判定します。
 */
fun IItemHandler.canInsert(stack: ItemStack): Boolean = ItemHandlerHelper.insertItem(this, stack, true).isEmpty

/**
 * 指定した[stack]をこの[IItemHandler]に入れようとします。
 *
 * 入らなかった場合は[dropStackAt]を通じてドロップします。
 */
fun IItemHandler.insertOrDrop(level: Level, pos: BlockPos, stack: ItemStack) {
    val remain: ItemStack = ItemHandlerHelper.insertItem(this, stack, false)
    dropStackAt(level, pos, remain)
}
