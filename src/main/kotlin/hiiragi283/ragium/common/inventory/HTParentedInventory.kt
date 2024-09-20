package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

class HTParentedInventory(size: Int) : HTSimpleInventory(size) {
    var parent: ItemStack? = null

    override fun onClose(player: PlayerEntity) {
        parent?.set(RagiumComponentTypes.INVENTORY, this)
    }
}
