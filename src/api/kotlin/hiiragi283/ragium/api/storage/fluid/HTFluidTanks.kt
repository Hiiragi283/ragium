package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.neoforged.neoforge.fluids.FluidStack

fun HTFluidTank.getFluidStack(): FluidStack = this.getStack().stack

fun HTFluidTank.getCapacityAsLong(stack: FluidStack): Long = this.getCapacityAsLong(HTFluidStorageStack.of(stack))

fun HTFluidTank.getCapacityAsInt(stack: FluidStack): Int = this.getCapacityAsInt(HTFluidStorageStack.of(stack))

fun HTFluidTank.isValid(stack: FluidStack): Boolean = this.isValid(HTFluidStorageStack.of(stack))

fun HTFluidTank.insertFluid(stack: FluidStack, action: HTStorageAction, access: HTStorageAccess): FluidStack =
    this.insert(HTFluidStorageStack.of(stack), action, access).stack

fun HTFluidTank.extractFluid(amount: Int, action: HTStorageAction, access: HTStorageAccess): FluidStack =
    this.extract(amount, action, access).stack

fun HTFluidTank.Mutable.setFluidStack(stack: FluidStack) {
    setStack(HTFluidStorageStack.of(stack))
}
