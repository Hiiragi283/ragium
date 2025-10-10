package hiiragi283.ragium.api.item

import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import kotlin.math.max
import kotlin.streams.asSequence

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
