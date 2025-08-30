package hiiragi283.ragium.api.storage.proxy

import hiiragi283.ragium.api.storage.fluid.HTSidedFluidHandler
import hiiragi283.ragium.api.storage.fluid.IFluidHandlerModifiable
import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [IFluidHandlerModifiable]向けの[HTProxyHandler]の実装クラス
 * @param handler ラップ対象の[HTSidedFluidHandler]
 * @param side 現在の向き
 * @param holder 搬入出の制御
 * @see [mekanism.common.capabilities.proxy.ProxyFluidHandler]
 */
class HTProxyFluidHandler(private val handler: HTSidedFluidHandler, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    IFluidHandlerModifiable {
    override fun setFluidInTank(tank: Int, stack: FluidStack) {
        if (!readOnly) {
            handler.setFluidInTank(tank, stack, side)
        }
    }

    override fun insertFluid(tank: Int, stack: FluidStack, simulate: Boolean): FluidStack = when (readOnlyInsert) {
        true -> stack
        false -> handler.insertFluid(tank, stack, simulate, side)
    }

    override fun extractFluid(tank: Int, amount: Int, simulate: Boolean): FluidStack = when (readOnlyExtract) {
        true -> FluidStack.EMPTY
        false -> handler.extractFluid(tank, amount, simulate, side)
    }

    override fun insertFluid(stack: FluidStack, simulate: Boolean): FluidStack = when (readOnlyInsert) {
        true -> stack
        false -> handler.insertFluid(stack, simulate, side)
    }

    override fun extractFluid(amount: Int, simulate: Boolean): FluidStack = when (readOnlyExtract) {
        true -> FluidStack.EMPTY
        false -> handler.extractFluid(amount, simulate, side)
    }

    override fun extractFluid(stack: FluidStack, simulate: Boolean): FluidStack = when (readOnlyExtract) {
        true -> FluidStack.EMPTY
        false -> handler.extractFluid(stack, simulate, side)
    }

    override fun getTanks(): Int = handler.getTanks(side)

    override fun getFluidInTank(tank: Int): FluidStack = handler.getFluidInTank(tank, side)

    override fun getTankCapacity(tank: Int): Int = handler.getTankCapacity(tank, side)

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = handler.isFluidValid(tank, stack, side)
}
