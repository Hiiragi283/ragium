package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

object HTFluidCapabilities : HTViewCapability<IFluidHandler, IFluidHandlerItem, ImmutableFluidStack> {
    override val block: BlockCapability<IFluidHandler, Direction?> = Capabilities.FluidHandler.BLOCK
    override val entity: EntityCapability<IFluidHandler, Direction?> = Capabilities.FluidHandler.ENTITY
    override val item: ItemCapability<IFluidHandlerItem, Void?> = Capabilities.FluidHandler.ITEM

    override fun apply(handler: IFluidHandler, context: Direction?): List<HTStackView<ImmutableFluidStack>> =
        if (handler is HTFluidHandler) {
            handler.getFluidTanks(context)
        } else {
            handler.tankRange.map { tank: Int ->
                object : HTStackView<ImmutableFluidStack> {
                    override fun getStack(): ImmutableFluidStack? = handler.getFluidInTank(tank).toImmutable()

                    override fun getCapacity(stack: ImmutableFluidStack?): Int = handler.getTankCapacity(tank)
                }
            }
        }
}
