package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.extension.tankRange
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.predicate.HTItemPredicate
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import java.util.function.Predicate

/**
 * @see [mekanism.generators.common.slot.FluidFuelInventorySlot]
 */
class HTFluidFuelItemStackSlot private constructor(
    tank: HTFluidTank,
    private val stackToAmount: (ItemStack) -> Int,
    private val amountToFuel: (Int) -> FluidStack,
    canExtract: Predicate<ItemStack>,
    canInsert: Predicate<ItemStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
) : HTFluidItemStackSlot(tank, canExtract, canInsert, HTItemPredicate.TRUE, listener, x, y) {
    companion object {
        @JvmStatic
        fun create(
            tank: HTFluidTank,
            stackToAmount: (ItemStack) -> Int,
            amountToFuel: (Int) -> FluidStack,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTFluidFuelItemStackSlot = HTFluidFuelItemStackSlot(
            tank,
            stackToAmount,
            amountToFuel,
            { stack: ItemStack ->
                // stackの液体コンテナから吸いだせる場合は取り出し不可
                HTMultiCapability.FLUID.getCapability(stack)?.let { handler: IFluidHandlerItem ->
                    for (i: Int in handler.tankRange) {
                        if (tank.isFluidValid(handler.getFluidInTank(i))) return@HTFluidFuelItemStackSlot false
                    }
                }
                // stackを燃料に変換できない場合はtrue
                stackToAmount(stack) == 0
            },
            { stack: ItemStack -> stackToAmount(stack) > 0 || fillPredicate(tank).test(stack) },
            listener,
            x,
            y,
        )
    }

    fun fillOrBurn() {
        if (isEmpty) return
        val needed: Int = tank.getNeeded()
        if (needed > 0 && !fillTank()) {
            val amount: Int = stackToAmount(getStack())
            if (amount in 1..needed) {
                val hasContainer: Boolean = getStack().hasCraftingRemainingItem()
                if (hasContainer && getStack().count > 1) return
                tank.insert(amountToFuel(amount), false, HTStorageAccess.INTERNAl)
                if (hasContainer) {
                    setStack(getStack().craftingRemainingItem)
                } else {
                    shrinkStack(1, false)
                }
            }
        }
    }
}
