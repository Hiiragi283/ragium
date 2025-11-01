package hiiragi283.ragium.common.storage.proxy

import hiiragi283.ragium.api.storage.fluid.HTSidedFluidHandler
import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * [IFluidHandler]向けの[HTProxyHandler]の実装クラス
 * @param handler ラップ対象の[HTSidedFluidHandler]
 * @param side 現在の向き
 * @param holder 搬入出の制御
 * @see mekanism.common.capabilities.proxy.ProxyFluidHandler
 */
class HTProxyFluidHandler(private val handler: HTSidedFluidHandler, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    IFluidHandler {
    override fun getTanks(): Int = handler.getTanks(side)

    override fun getFluidInTank(tank: Int): FluidStack = handler.getFluidInTank(tank, side)

    override fun getTankCapacity(tank: Int): Int = handler.getTankCapacity(tank, side)

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = !readOnly || handler.isFluidValid(tank, stack, side)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = when (readOnlyInsert) {
        true -> 0
        false -> handler.fill(resource, action, side)
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = when (readOnlyExtract) {
        true -> FluidStack.EMPTY
        false -> handler.drain(resource, action, side)
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = when (readOnlyExtract) {
        true -> FluidStack.EMPTY
        false -> handler.drain(maxDrain, action, side)
    }
}
