@file:Suppress("DEPRECATION")

package hiiragi283.ragium.api.extension

import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

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

//    Item    //

fun itemProperty(): Item.Properties = Item.Properties()

fun itemProperty(builderAction: Item.Properties.() -> Unit): Item.Properties = Item.Properties().apply(builderAction)

/**
 * 指定した[text]をアイテムの名前に設定します。
 */
fun Item.Properties.name(text: Component): Item.Properties = component(DataComponents.ITEM_NAME, text)

//    ItemStack    //

/**
 * 残りの耐久値を返します。
 */
val ItemStack.restDamage: Int get() = maxDamage - damageValue

/**
 * 現在の個数が[isMaxCount]と一致するか判定します。
 */
val ItemStack.isMaxCount: Boolean get() = count == maxStackSize

//    IItemHandler    //

val IItemHandler.slotRange: IntRange
    get() = (0 until this.slots)

inline fun IItemHandler.forEach(action: (ItemStack) -> Unit) {
    slotRange.map(this::getStackInSlot).forEach(action)
}

inline fun IItemHandler.forEachSlot(action: (Int) -> Unit) {
    slotRange.forEach(action)
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
