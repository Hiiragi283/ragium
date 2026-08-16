package hiiragi283.lib.transfer.item

import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.HTResourceView
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource

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
 * [ItemResource]向けの[HTResourceView]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTItemView = HTResourceView<ItemResource>

/**
 * [ItemResource]向けの[HTResourceSlot]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTItemSlot = HTResourceSlot<ItemResource>

/**
 * この[HTItemView][this]から[ItemStack]を取得します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun HTItemView.getItemStack(): ItemStack = this.resource.toStack(this.amount)

/**
 * [ItemResource]向けの[ResourceHandler]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias ItemResourceHandler = ResourceHandler<ItemResource>
