package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTTeleportKeyFluidHandler(container: ItemStack, capacity: Int) : HTComponentFluidHandler(container, capacity) {
    override fun createTank(capacity: Int): HTFluidTank = object : ComponentTank(container, capacity) {
        override fun isFluidValid(stack: FluidStack): Boolean = RagiumFluidContents.DEW_OF_THE_WARP.test(stack)
    }
}
