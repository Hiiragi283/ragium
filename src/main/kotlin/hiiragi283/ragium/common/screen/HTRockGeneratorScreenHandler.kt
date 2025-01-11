package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.screen.HTMachineScreenHandler
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory

class HTRockGeneratorScreenHandler(syncId: Int, playerInv: PlayerInventory, machine: HTMachineBlockEntityBase? = null) :
    HTMachineScreenHandler(
        RagiumScreenHandlerTypes.ROCK_GENERATOR,
        syncId,
        playerInv,
        machine,
        2,
    ) {
    init {
        inventory.onOpen(player)
        // inputs
        addFluidSlot(0, 3, 2)
        // catalyst
        addSlot(0, 4, 2)
        // outputs
        addOutputSlot(1, 5, 1)
        addFluidSlot(1, 5, 2)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val inputSlots: IntRange = IntRange.EMPTY
    override val outputSlots: IntRange = (1..1)
}
