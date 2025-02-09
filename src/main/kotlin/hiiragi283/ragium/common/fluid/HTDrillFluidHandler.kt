package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumEnchantments
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack

class HTDrillFluidHandler(container: ItemStack, capacity: Int) :
    FluidHandlerItemStack(RagiumComponentTypes.FLUID_CONTENT, container, capacity) {
    companion object {
        @JvmStatic
        fun create(stack: ItemStack, context: Void?): HTDrillFluidHandler {
            val modifier: Int =
                stack.getLevel(RagiumAPI.getInstance().getCurrentLookup(), RagiumEnchantments.CAPACITY) + 1
            return HTDrillFluidHandler(stack, RagiumAPI.DEFAULT_TANK_CAPACITY * modifier)
        }
    }

    override fun canFillFluidType(fluid: FluidStack): Boolean = fluid.`is`(RagiumFluidTags.NON_NITRO_FUEL)
}
