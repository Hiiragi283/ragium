package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.extension.tankRange
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.HTStorageStack
import hiiragi283.ragium.api.storage.fluid.HTFluidStorageStack
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.isValid
import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import hiiragi283.ragium.api.storage.item.getCraftingRemainingItem
import hiiragi283.ragium.api.storage.item.hasCraftingRemainingItem
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import java.util.function.Predicate

/**
 * @see [mekanism.generators.common.slot.FluidFuelInventorySlot]
 */
class HTFluidFuelItemStackSlot private constructor(
    tank: HTFluidTank,
    private val stackToAmount: (HTItemStorageStack) -> Int,
    private val amountToFuel: (Int) -> HTFluidStorageStack,
    canExtract: Predicate<HTItemStorageStack>,
    canInsert: Predicate<HTItemStorageStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
    slotType: HTContainerItemSlot.Type,
) : HTFluidItemStackSlot(tank, canExtract, canInsert, HTStorageStack.alwaysTrue(), listener, x, y, slotType) {
    companion object {
        @JvmStatic
        fun create(
            tank: HTFluidTank,
            stackToAmount: (HTItemStorageStack) -> Int,
            amountToFuel: (Int) -> HTFluidStorageStack,
            listener: HTContentListener?,
            x: Int,
            y: Int,
            slotType: HTContainerItemSlot.Type = HTContainerItemSlot.Type.BOTH,
        ): HTFluidFuelItemStackSlot = HTFluidFuelItemStackSlot(
            tank,
            stackToAmount,
            amountToFuel,
            { stack: HTItemStorageStack ->
                // stackの液体コンテナから吸いだせる場合は取り出し不可
                HTMultiCapability.FLUID.getCapability(stack)?.let { handler: IFluidHandlerItem ->
                    for (i: Int in handler.tankRange) {
                        if (tank.isValid(handler.getFluidInTank(i))) return@HTFluidFuelItemStackSlot false
                    }
                }
                // stackを燃料に変換できない場合はtrue
                stackToAmount(stack) == 0
            },
            { stack: HTItemStorageStack -> stackToAmount(stack) > 0 || fillPredicate(tank).test(stack) },
            listener,
            x,
            y,
            slotType,
        )
    }

    fun fillOrBurn() {
        if (isEmpty()) return
        val needed: Int = tank.getNeededAsInt(tank.getStack())
        if (needed > 0 && !fillTank()) {
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
