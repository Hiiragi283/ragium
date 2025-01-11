package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.screen.HTMachineScreenHandler
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory

class HTSmallMachineScreenHandler(syncId: Int, playerInv: PlayerInventory, machine: HTMachineBlockEntityBase? = null) :
    HTMachineScreenHandler(
        RagiumScreenHandlerTypes.SMALL_MACHINE,
        syncId,
        playerInv,
        machine,
        2,
    ) {
    init {
        inventory.onOpen(player)
        // input
        addSlot(0, 2, 1)
        addFluidSlot(0, 2, 2)
        // output
        addOutputSlot(1, 6, 1)
        addFluidSlot(1, 6, 2)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val inputSlots: IntRange = (0..0)
    override val outputSlots: IntRange = (1..1)
}
