package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

fun HTItemSlot.getItemStack(): ItemStack = this.getStack().stack

fun HTItemSlot.toRecipeInput(): SingleRecipeInput = SingleRecipeInput(this.getItemStack())

fun HTItemSlot.getCapacityAsLong(stack: ItemStack): Long = this.getCapacityAsLong(stack.toImmutable())

fun HTItemSlot.getCapacityAsInt(stack: ItemStack): Int = this.getCapacityAsInt(stack.toImmutable())

fun HTItemSlot.isValid(stack: ItemStack): Boolean = this.isValid(stack.toImmutable())

fun HTItemSlot.insertItem(stack: ItemStack, action: HTStorageAction, access: HTStorageAccess): ItemStack =
    this.insert(stack.toImmutable(), action, access).stack

fun HTItemSlot.extractItem(amount: Int, action: HTStorageAction, access: HTStorageAccess): ItemStack =
    this.extract(amount, action, access).stack

fun HTItemSlot.Mutable.setItemStack(stack: ItemStack) {
    setStack(stack.toImmutable())
}
