package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack

class HTItemFluidHandler(stack: ItemStack, capacity: Int) : FluidHandlerItemStack(RagiumComponentTypes.FLUID_CONTENT, stack, capacity)
