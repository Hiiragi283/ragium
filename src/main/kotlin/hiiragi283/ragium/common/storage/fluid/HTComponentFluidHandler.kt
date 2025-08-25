package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack

/**
 * [FluidHandlerItemStack.getFluid]が[FluidStack.EMPTY]の時にコンポーネントを消す[FluidHandlerItemStack]
 */
open class HTComponentFluidHandler(container: ItemStack, capacity: Int) :
    FluidHandlerItemStack(
        RagiumDataComponents.FLUID_CONTENT,
        container,
        capacity,
    ) {
    override fun setFluid(fluid: FluidStack) {
        super.setFluid(fluid)
        if (getFluid().isEmpty) {
            container.remove(componentType)
        }
    }
}
