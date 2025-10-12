package hiiragi283.ragium.api.item

import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

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

fun <T : Any> createItemStack(
    item: ItemLike,
    type: Supplier<out DataComponentType<T>>,
    value: T,
    count: Int = 1,
): ItemStack {
    val stack = ItemStack(item, count)
    stack.set(type, value)
    return stack
}
