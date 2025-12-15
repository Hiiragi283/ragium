package hiiragi283.ragium.api.item

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike

//    ItemStack    //

fun <T : Any> createItemStack(
    item: ItemLike,
    type: DataComponentType<T>,
    value: T,
    count: Int = 1,
): ItemStack {
    val stack = ItemStack(item, count)
    stack.set(type, value)
    return stack
}

fun createEnchantedBook(enchantment: Holder<Enchantment>, level: Int = enchantment.value().maxLevel): ItemStack {
    val stack = ItemStack(Items.ENCHANTED_BOOK)
    stack.enchant(enchantment, level)
    return stack
}
