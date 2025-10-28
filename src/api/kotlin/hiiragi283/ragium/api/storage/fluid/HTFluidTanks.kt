package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.neoforged.neoforge.fluids.FluidStack

fun HTStackView<ImmutableFluidStack>.getFluidStack(): FluidStack = this.getStack()?.stack ?: FluidStack.EMPTY

fun HTStackView<ImmutableFluidStack>.getCapacityAsInt(stack: FluidStack): Int = this.getCapacity(stack.toImmutable())

fun HTFluidTank.isValid(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::isValid) ?: false

fun HTFluidTank.insertFluid(stack: FluidStack, action: HTStorageAction, access: HTStorageAccess): FluidStack {
    val immutable: ImmutableFluidStack = stack.toImmutable() ?: return stack
    return this.insert(immutable, action, access)?.stack ?: stack
}

fun HTFluidTank.extractFluid(stack: FluidStack, action: HTStorageAction, access: HTStorageAccess): FluidStack {
    val immutable: ImmutableFluidStack = stack.toImmutable() ?: return stack
    return this.extract(immutable, action, access)?.stack ?: stack
}

fun HTFluidTank.extractFluid(amount: Int, action: HTStorageAction, access: HTStorageAccess): FluidStack =
    this.extract(amount, action, access)?.stack ?: FluidStack.EMPTY

fun HTStackView.Mutable<ImmutableFluidStack>.setFluidStack(stack: FluidStack) {
    setStack(stack.toImmutable())
}
