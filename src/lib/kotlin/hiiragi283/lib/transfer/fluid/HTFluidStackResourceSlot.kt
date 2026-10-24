package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.transfer.HTStackResourceSlot
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource

/**
 * [FluidStack]向けの[HTStackResourceSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTFluidStackResourceSlot : HTStackResourceSlot<FluidStack, FluidResource> {
    override fun getResourceFrom(stack: FluidStack): FluidResource = FluidResource.of(stack)

    override fun getAmountFrom(stack: FluidStack): Int = stack.amount()

    override fun isSame(stack: FluidStack, resource: FluidResource): Boolean = resource.matches(stack)

    override fun createStack(resource: FluidResource, amount: Int): FluidStack = resource.toStack(amount)
}
