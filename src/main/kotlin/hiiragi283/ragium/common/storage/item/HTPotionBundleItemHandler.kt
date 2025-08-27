package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.storage.item.HTComponentItemHandler
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.MutableDataComponentHolder

class HTPotionBundleItemHandler(parent: MutableDataComponentHolder, size: Int) : HTComponentItemHandler(parent, size) {
    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = stack.isEmpty || stack.`is`(Items.POTION)
}
