package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

class HTFluidCubeFluidHandler(private var container: ItemStack, context: Void?) : IFluidHandlerItem {
    private fun getFluidStack(): FluidStack = when (container.item) {
        RagiumItems.WATER_FLUID_CUBE.asItem() -> FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME)
        RagiumItems.LAVA_FLUID_CUBE.asItem() -> FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME)
        else -> FluidStack.EMPTY
    }

    private fun getFluidCube(stack: FluidStack): ItemStack = when (stack.fluid) {
        Fluids.WATER -> RagiumItems.WATER_FLUID_CUBE
        Fluids.LAVA -> RagiumItems.LAVA_FLUID_CUBE
        else -> null
    }?.toStack() ?: ItemStack.EMPTY

    override fun getContainer(): ItemStack = container

    override fun getTanks(): Int = 1

    override fun getFluidInTank(tank: Int): FluidStack = getFluidStack()

    override fun getTankCapacity(tank: Int): Int = FluidType.BUCKET_VOLUME

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = true

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (container.count != 1) return 0
        if (resource.amount < FluidType.BUCKET_VOLUME) return 0
        if (!getFluidStack().isEmpty) return 0
        if (action.execute()) {
        }
        return FluidType.BUCKET_VOLUME
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (container.count != 1) return FluidStack.EMPTY
        if (resource.amount < FluidType.BUCKET_VOLUME) return FluidStack.EMPTY
        val stack: FluidStack = getFluidInTank(0)
        if (!stack.isEmpty && FluidStack.isSameFluidSameComponents(stack, resource)) {
            val fluidCube: ItemStack = getFluidCube(stack)
            if (!fluidCube.isEmpty) {
                if (action.execute()) {
                    container = fluidCube
                }
                return resource
            } else {
                return FluidStack.EMPTY
            }
        }
        return FluidStack.EMPTY
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (container.count != 1) return FluidStack.EMPTY
        if (maxDrain < FluidType.BUCKET_VOLUME) return FluidStack.EMPTY
        val stack: FluidStack = getFluidInTank(0)
        if (!stack.isEmpty) {
            if (action.execute()) {
                container = ItemStack(RagiumItems.EMPTY_FLUID_CUBE.get())
            }
            return stack
        }
        return FluidStack.EMPTY
    }
}
