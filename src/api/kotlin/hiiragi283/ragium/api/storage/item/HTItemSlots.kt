package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

fun HTStackView<ImmutableItemStack>.getItemStack(): ItemStack = this.getStack()?.unwrap() ?: ItemStack.EMPTY

fun HTStackView<ImmutableItemStack>.toRecipeInput(): SingleRecipeInput = SingleRecipeInput(this.getItemStack())

fun HTItemSlot.isValid(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::isValid) ?: false
