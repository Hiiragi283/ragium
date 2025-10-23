package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

fun HTStackView<ImmutableItemStack>.getItemStack(): ItemStack = this.getStack().stack

fun HTStackView<ImmutableItemStack>.toRecipeInput(): SingleRecipeInput = SingleRecipeInput(this.getItemStack())

fun HTStackView<ImmutableItemStack>.getCapacityAsInt(stack: ItemStack): Int = this.getCapacityAsInt(stack.toImmutable())

fun HTItemSlot.isValid(stack: ItemStack): Boolean = this.isValid(stack.toImmutable())

fun HTItemSlot.insertItem(stack: ItemStack, action: HTStorageAction, access: HTStorageAccess): ItemStack =
    this.insert(stack.toImmutable(), action, access).stack

fun HTItemSlot.extractItem(stack: ItemStack, action: HTStorageAction, access: HTStorageAccess): ItemStack =
    this.extract(stack.toImmutable(), action, access).stack

fun HTItemSlot.extractItem(amount: Int, action: HTStorageAction, access: HTStorageAccess): ItemStack =
    this.extract(amount, action, access).stack

fun HTStackView.Mutable<ImmutableItemStack>.setItemStack(stack: ItemStack) {
    setStack(stack.toImmutable())
}
