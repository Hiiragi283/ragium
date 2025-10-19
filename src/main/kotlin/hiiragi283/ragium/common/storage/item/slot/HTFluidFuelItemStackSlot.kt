package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.getCraftingRemainingItem
import hiiragi283.ragium.api.stack.hasCraftingRemainingItem
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import java.util.function.Predicate

/**
 * @see [mekanism.generators.common.slot.FluidFuelInventorySlot]
 */
class HTFluidFuelItemStackSlot private constructor(
    tank: HTFluidTank,
    private val stackToAmount: (ImmutableItemStack) -> Int,
    private val amountToFuel: (Int) -> ImmutableFluidStack,
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
            stackToAmount: (ImmutableItemStack) -> Int,
            amountToFuel: (Int) -> ImmutableFluidStack,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTFluidFuelItemStackSlot = HTFluidFuelItemStackSlot(
            tank,
            stackToAmount,
            amountToFuel,
            { stack: ImmutableItemStack ->
                // stackの液体コンテナから吸いだせる場合は取り出し不可
                for (stack: ImmutableFluidStack in RagiumCapabilities.FLUID.getCapabilityStacks(stack)) {
                    if (tank.isValid(stack)) return@HTFluidFuelItemStackSlot false
                }
                // stackを燃料に変換できない場合はtrue
                stackToAmount(stack) == 0
            },
            { stack: ImmutableItemStack -> stackToAmount(stack) > 0 || fillPredicate(tank).test(stack) },
            listener,
            x,
            y,
        )
    }

    fun fillOrBurn(moveTo: HTItemSlot.Mutable) {
        if (isEmpty()) return
        val needed: Int = tank.getNeededAsInt(tank.getStack())
        if (needed > 0 && !fillTank(moveTo)) {
            val amount: Int = stackToAmount(getStack())
            if (amount in 1..needed) {
                val hasContainer: Boolean = getStack().hasCraftingRemainingItem()
                if (hasContainer && getStack().amountAsInt() > 1) return
                tank.insert(amountToFuel(amount), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                if (hasContainer) {
                    setStack(getStack().getCraftingRemainingItem())
                } else {
                    shrinkStack(1, HTStorageAction.EXECUTE)
                }
            }
        }
    }
}
