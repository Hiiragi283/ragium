package hiiragi283.lib.transfer.item

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler

//    ItemResource    //

/**
 * この[ItemStack][this]を[ItemResource]と個数に分解します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStack.toResourcePair(): Pair<ItemResource, Int> = ItemResource.of(this) to this.count

/**
 * この[ItemStackTemplate][this]を[ItemResource]と個数に分解します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStackTemplate.toResourcePair(): Pair<ItemResource, Int> = ItemResource.of(this) to this.count

//    ResourceHandler    //

/**
 * [ItemResource]向けの[ResourceHandler]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias ItemResourceHandler = ResourceHandler<ItemResource>

/**
 * [ItemStack]のコピーを取得します。
 * @param index [ItemStack]を取得するスロットのインデックス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemResourceHandler.getItemStack(index: Int): ItemStack = this.getResource(index).toStack(this.getAmountAsInt(index))

/**
 * この[ItemStacksResourceHandler][this]の中身を直接置き換えます。
 * @param index 置き換えるスロットのインデックス
 * @param stack 置き換え後のアイテム
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun ItemStacksResourceHandler.set(index: Int, stack: ItemStack) {
    val (resource: ItemResource, amount: Int) = stack.toResourcePair()
    this.set(index, resource, amount)
}
