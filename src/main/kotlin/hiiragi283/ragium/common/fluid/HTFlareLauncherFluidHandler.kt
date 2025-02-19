package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack

class HTFlareLauncherFluidHandler(stack: ItemStack, capacity: Int) :
    FluidHandlerItemStack(RagiumComponentTypes.FLUID_CONTENT, stack, capacity) {
    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = stack.`is`(RagiumVirtualFluids.LIQUID_GLOW.commonTag)
}
