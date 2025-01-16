package hiiragi283.ragium.common.capability

import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack

class HTCubeFluidHandler(val fluidCube: RagiumItems.FluidCubes, container: ItemStack) :
    FluidHandlerItemStack(RagiumComponentTypes.FLUID_CONTENT, container, FluidType.BUCKET_VOLUME) {
    override fun getFluid(): FluidStack = runCatching {
        FluidStack(fluidCube.containment.get(), FluidType.BUCKET_VOLUME)
    }.getOrDefault(FluidStack.EMPTY)

    override fun setFluid(fluid: FluidStack) {}

    override fun canFillFluidType(fluid: FluidStack): Boolean = false

    override fun setContainerToEmpty() {
        container = ItemStack(Items.GLASS)
    }
}
