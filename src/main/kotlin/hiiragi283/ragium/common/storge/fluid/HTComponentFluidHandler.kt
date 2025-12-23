package hiiragi283.ragium.common.storge.fluid

import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.storage.attachments.HTAttachedFluids
import hiiragi283.core.api.storage.fluid.HTFluidHandler
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storge.attachment.HTComponentHandler
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * [HTFluidHandler]に基づいたコンポーネント向けの実装
 * @see mekanism.common.attachments.containers.fluid.ComponentBackedFluidHandler
 */
class HTComponentFluidHandler(attachedTo: ItemStack, size: Int, containerFactory: ContainerFactory<HTFluidTank>) :
    HTComponentHandler<ImmutableFluidStack?, HTFluidTank, HTAttachedFluids>(attachedTo, size, containerFactory),
    HTFluidHandler,
    IFluidHandlerItem {
    override fun capabilityCodec(): HTCapabilityCodec<HTFluidTank, HTAttachedFluids> = HTCapabilityCodec.FLUID

    override fun getFluidTanks(side: Direction?): List<HTFluidTank> = getContainers()

    override fun getFluidTank(tank: Int, side: Direction?): HTFluidTank = getContainer(tank)

    override fun getTanks(side: Direction?): Int = size

    override fun getFluidInTank(tank: Int, side: Direction?): FluidStack = getContents(tank)?.unwrap() ?: FluidStack.EMPTY

    override fun getContainer(): ItemStack = attachedTo
}
