package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

fun HTStackView<ImmutableItemStack>.getItemStack(): ItemStack = this.getStack()?.stack ?: ItemStack.EMPTY

fun HTStackView<ImmutableItemStack>.toRecipeInput(): SingleRecipeInput = SingleRecipeInput(this.getItemStack())

fun HTStackView<ImmutableItemStack>.getCapacity(stack: ItemStack): Int = this.getCapacity(stack.toImmutable())

fun HTItemSlot.isValid(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::isValid) ?: false

fun HTItemSlot.insertItem(stack: ItemStack, action: HTStorageAction, access: HTStorageAccess): ItemStack {
    val immutable: ImmutableItemStack = stack.toImmutable() ?: return stack
    return this.insert(immutable, action, access)?.stack ?: ItemStack.EMPTY
}

fun HTItemSlot.extractItem(stack: ItemStack, action: HTStorageAction, access: HTStorageAccess): ItemStack {
    val immutable: ImmutableItemStack = stack.toImmutable() ?: return stack
    return this.extract(immutable, action, access)?.stack ?: ItemStack.EMPTY
}

fun HTItemSlot.extractItem(amount: Int, action: HTStorageAction, access: HTStorageAccess): ItemStack =
    this.extract(amount, action, access)?.stack ?: ItemStack.EMPTY

fun HTStackView.Mutable<ImmutableItemStack>.setItemStack(stack: ItemStack) {
    setStack(stack.toImmutable())
}
