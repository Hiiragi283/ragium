package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack

/**
 * [FluidHandlerItemStack.getFluid]が[FluidStack.EMPTY]の時にコンポーネントを消す[FluidHandlerItemStack]
 */
open class HTComponentFluidHandler(container: ItemStack, capacity: Int) :
    FluidHandlerItemStack(
        RagiumAPI.getInstance()::getFluidComponent,
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
