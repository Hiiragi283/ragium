package hiiragi283.ragium.api.screen

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class HTOutputSlot(
    inventory: Inventory,
    index: Int,
    x: Int,
    y: Int,
) : HTSlot(inventory, index, x, y) {
    override fun onTakeItem(player: PlayerEntity, stack: ItemStack) {
        stack.onCraftByPlayer(player.world, player, stack.count)
        super.onTakeItem(player, stack)
    }

    override fun canInsert(stack: ItemStack): Boolean = false
}
