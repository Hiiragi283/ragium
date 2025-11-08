package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.getCraftingRemainingItem
import hiiragi283.ragium.api.stack.hasCraftingRemainingItem
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.util.HTStackSlotHelper
import java.util.function.IntFunction
import java.util.function.Predicate
import java.util.function.ToIntFunction

/**
 * @see mekanism.generators.common.slot.FluidFuelInventorySlot
 */
class HTFluidFuelItemStackSlot private constructor(
    tank: HTFluidTank,
    private val stackToAmount: ToIntFunction<ImmutableItemStack>,
    private val amountToFuel: IntFunction<ImmutableFluidStack?>,
    canExtract: Predicate<ImmutableItemStack>,
    canInsert: Predicate<ImmutableItemStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
) : HTFluidItemStackSlot(tank, canExtract, canInsert, listener, x, y) {
    companion object {
        @JvmStatic
        fun create(
            tank: HTFluidTank,
            stackToAmount: ToIntFunction<ImmutableItemStack>,
            amountToFuel: IntFunction<ImmutableFluidStack?>,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTFluidFuelItemStackSlot = HTFluidFuelItemStackSlot(
            tank,
            stackToAmount,
            amountToFuel,
            { stack: ImmutableItemStack ->
                // stackの液体コンテナから吸いだせる場合は取り出し不可
                for (view: HTFluidView in HTFluidCapabilities.getFluidViews(stack)) {
                    val stack: ImmutableFluidStack = view.getStack() ?: continue
                    if (tank.isValid(stack)) return@HTFluidFuelItemStackSlot false
                }
                // stackを燃料に変換できない場合はtrue
                stackToAmount.applyAsInt(stack) == 0
            },
            { stack: ImmutableItemStack -> stackToAmount.applyAsInt(stack) > 0 || fillPredicate(tank).test(stack) },
            listener,
            x,
            y,
        )
    }

    fun fillOrBurn() {
        val stack: ImmutableItemStack = this.getStack() ?: return
        val needed: Int = tank.getNeeded()
        val fluidInteracted: Boolean = HTStackSlotHelper.moveFluid(this, this::setStackUnchecked, this.tank)
        if (needed > 0 && !fluidInteracted) {
            val amount: Int = stackToAmount.applyAsInt(stack)
            if (amount in 1..needed) {
                val hasContainer: Boolean = stack.hasCraftingRemainingItem()
                if (hasContainer && stack.amount() > 1) return
                val fuel: ImmutableFluidStack = amountToFuel.apply(amount) ?: return
                tank.insert(fuel, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                if (hasContainer) {
                    setStack(stack.getCraftingRemainingItem())
                } else {
                    shrinkAmount(stack, 1)
                }
            }
        }
    }
}
