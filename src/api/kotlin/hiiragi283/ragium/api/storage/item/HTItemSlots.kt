package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import net.minecraft.world.item.ItemStack

typealias HTItemView = HTStackView<ImmutableItemStack>

fun HTItemView.getItemStack(): ItemStack = this.getStack()?.unwrap() ?: ItemStack.EMPTY

fun HTItemSlot.isValid(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::isValid) ?: false
