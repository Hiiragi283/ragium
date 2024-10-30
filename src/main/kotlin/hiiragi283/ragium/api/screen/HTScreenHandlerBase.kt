package hiiragi283.ragium.api.screen

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.world.World

@Suppress("LeakingThis")
abstract class HTScreenHandlerBase(
    type: ScreenHandlerType<*>,
    syncId: Int,
    val playerInv: PlayerInventory,
    val inventory: Inventory,
) : ScreenHandler(type, syncId) {
    val player: PlayerEntity = playerInv.player
    val world: World = player.world

    override fun canUse(player: PlayerEntity): Boolean = inventory.canPlayerUse(player)

    override fun onClosed(player: PlayerEntity) {
        super.onClosed(player)
        inventory.onClose(player)
    }

    //    Extensions    //

    protected fun getSlotPosX(index: Int): Int = 8 + index * 18

    protected fun getSlotPosY(index: Int): Int = 18 + index * 18

    protected fun addSlot(index: Int, x: Int, y: Int) {
        addSlot(HTSlot(inventory, index, getSlotPosX(x), getSlotPosY(y)))
    }

    protected fun addOutputSlot(index: Int, x: Int, y: Int) {
        addSlot(HTOutputSlot(inventory, index, getSlotPosX(x), getSlotPosY(y)))
    }

    protected fun addPlayerInv() {
        // inventory
        (0..26).forEach { index: Int ->
            addSlot(Slot(playerInv, index + 9, getSlotPosX(index % 9), getSlotPosY(3 + (index / 9)) + 12))
        }
        // hotbar
        (0..8).forEach { index: Int ->
            addSlot(Slot(playerInv, index, getSlotPosX(index), getSlotPosY(7) - 2))
        }
    }
}
