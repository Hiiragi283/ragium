package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.screen.HTMachineScreenHandler
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory

class HTLargeMachineScreenHandler(syncId: Int, playerInv: PlayerInventory, machine: HTMachineBlockEntityBase? = null) :
    HTMachineScreenHandler(
        RagiumScreenHandlerTypes.LARGE_MACHINE,
        syncId,
        playerInv,
        machine,
        7,
    ) {
    init {
        inventory.onOpen(player)
        // inputs
        addSlot(0, 1, 1)
        addSlot(1, 2, 1)
        addSlot(2, 3, 1)
        addFluidSlot(0, 2, 2)
        addFluidSlot(1, 3, 2)
        // catalyst
        addSlot(3, 4, 2)
        // outputs
        addOutputSlot(4, 5, 1)
        addOutputSlot(5, 6, 1)
        addOutputSlot(6, 7, 1)
        addFluidSlot(2, 5, 2)
        addFluidSlot(3, 6, 2)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val inputSlots: IntRange = (0..2)
    override val outputSlots: IntRange = (4..6)
}
