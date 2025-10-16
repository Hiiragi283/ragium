package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.neoforged.neoforge.fluids.FluidStack

fun HTFluidTank.getFluidStack(): FluidStack = this.getStack().stack

fun HTFluidTank.getCapacityAsLong(stack: FluidStack): Long = this.getCapacityAsLong(stack.toImmutable())

fun HTFluidTank.getCapacityAsInt(stack: FluidStack): Int = this.getCapacityAsInt(stack.toImmutable())

fun HTFluidTank.isValid(stack: FluidStack): Boolean = this.isValid(stack.toImmutable())

fun HTFluidTank.insertFluid(stack: FluidStack, action: HTStorageAction, access: HTStorageAccess): FluidStack =
    this.insert(stack.toImmutable(), action, access).stack

fun HTFluidTank.extractFluid(amount: Int, action: HTStorageAction, access: HTStorageAccess): FluidStack =
    this.extract(amount, action, access).stack

fun HTFluidTank.Mutable.setFluidStack(stack: FluidStack) {
    setStack(stack.toImmutable())
}
