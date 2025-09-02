package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTTeleportKeyFluidHandler(container: ItemStack, capacity: Int) : HTComponentFluidHandler(container, capacity) {
    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean =
        super.isFluidValid(tank, stack) && RagiumFluidContents.DEW_OF_THE_WARP.isOf(stack)
}
