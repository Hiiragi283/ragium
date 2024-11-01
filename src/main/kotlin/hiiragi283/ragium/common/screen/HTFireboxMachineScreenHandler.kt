package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.screen.HTScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.screen.PropertyDelegate

class HTFireboxMachineScreenHandler(
    syncId: Int,
    playerInv: PlayerInventory,
    inventory: Inventory = HTSimpleInventory(2),
    private val property: PropertyDelegate = ArrayPropertyDelegate(2),
) : HTScreenHandlerBase(
        RagiumScreenHandlerTypes.FIREBOX,
        syncId,
        playerInv,
        inventory,
    ) {
    init {
        inventory.onOpen(player)
        // input
        addSlot(0, 4, 2)
        // output
        addOutputSlot(1, 4, 0)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    fun isBurning(): Boolean = property.get(0) != property.get(1)

    fun getProgress(): Float = property.get(0).toFloat() / property.get(1).toFloat()

    override val machineSlotRange: IntRange = (0..1)
}
