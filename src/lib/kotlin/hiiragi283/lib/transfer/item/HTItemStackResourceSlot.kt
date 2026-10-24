package hiiragi283.lib.transfer.item

import hiiragi283.lib.transfer.HTStackResourceSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * [ItemStack]向けの[HTStackResourceSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTItemStackResourceSlot : HTStackResourceSlot<ItemStack, ItemResource> {
    override fun getResourceFrom(stack: ItemStack): ItemResource = ItemResource.of(stack)

    override fun getAmountFrom(stack: ItemStack): Int = stack.count()

    override fun isSame(stack: ItemStack, resource: ItemResource): Boolean = resource.matches(stack)

    override fun createStack(resource: ItemResource, amount: Int): ItemStack = resource.toStack(amount)
}
