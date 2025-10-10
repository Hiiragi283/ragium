package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.storage.fluid.HTFluidStorageStack
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.ItemStack

class HTTeleportKeyFluidHandler(stack: ItemStack, capacity: Long) : HTComponentFluidHandler(stack, capacity) {
    override fun createTank(capacity: Long): HTFluidTank = object : ComponentTank(stack, capacity) {
        override fun isValid(stack: HTFluidStorageStack): Boolean = RagiumFluidContents.DEW_OF_THE_WARP.isOf(stack)
    }
}
