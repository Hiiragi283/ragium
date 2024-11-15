package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.extension.getInventory
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTChemicalMachineScreenHandler(
    syncId: Int,
    playerInv: PlayerInventory,
    packet: HTMachinePacket,
    ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
) : HTMachineScreenHandlerBase(
        RagiumScreenHandlerTypes.CHEMICAL_MACHINE,
        syncId,
        playerInv,
        packet,
        ctx,
        ctx.getInventory(5),
    ) {
    init {
        inventory.onOpen(player)
        // inputs
        addSlot(0, 2, 1)
        addSlot(1, 3, 1)
        // catalyst
        addSlot(2, 4, 2)
        // outputs
        addOutputSlot(3, 5, 1)
        addOutputSlot(4, 6, 1)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val machineSlotRange: IntRange = (0..4)
}
