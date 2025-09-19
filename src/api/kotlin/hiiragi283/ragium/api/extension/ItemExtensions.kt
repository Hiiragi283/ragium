package hiiragi283.ragium.api.extension

import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier
import kotlin.math.max
import kotlin.streams.asSequence

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

//    ItemContainerContents    //

inline fun ItemContainerContents.copy(builderAction: NonNullList<ItemStack>.() -> Unit): ItemContainerContents =
    copy(this.slots, builderAction)

inline fun ItemContainerContents.copy(size: Int, builderAction: NonNullList<ItemStack>.() -> Unit): ItemContainerContents = NonNullList
    .withSize(max(this.slots, size), ItemStack.EMPTY)
    .apply(this::copyInto)
    .apply(builderAction)
    .let(ItemContainerContents::fromItems)

fun ItemContainerContents.getOrNull(index: Int): ItemStack? = when (index) {
    in (0..<slots) -> getStackInSlot(index)
    else -> null
}

fun ItemContainerContents.getOrEmpty(index: Int): ItemStack = getOrNull(index) ?: ItemStack.EMPTY

fun ItemContainerContents.asSequence(): Sequence<ItemStack> = stream().asSequence()

fun ItemContainerContents.asNonEmptySequence(): Sequence<ItemStack> = nonEmptyStream().asSequence()
