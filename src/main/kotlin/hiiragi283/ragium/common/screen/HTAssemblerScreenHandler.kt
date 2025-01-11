package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.screen.HTMachineScreenHandler
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory

class HTAssemblerScreenHandler(syncId: Int, playerInv: PlayerInventory, machine: HTMachineBlockEntityBase? = null) :
    HTMachineScreenHandler(
        RagiumScreenHandlerTypes.ASSEMBLER,
        syncId,
        playerInv,
        machine,
        6,
    ) {
    init {
        inventory.onOpen(player)
        // inputs
        addSlot(0, 2, 1)
        addSlot(1, 3, 1)
        addSlot(2, 2, 2)
        addSlot(3, 3, 2)
        // catalyst
        addSlot(4, 4, 2)
        // outputs
        addOutputSlot(4, 5, 1)

        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val inputSlots: IntRange = (0..2)
    override val outputSlots: IntRange = (4..6)
}
