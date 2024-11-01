package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.extension.machineInventory
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTSteamMachineScreenHandler(
    syncId: Int,
    playerInv: PlayerInventory,
    packet: HTMachinePacket,
    ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
) : HTMachineScreenHandlerBase(
        RagiumScreenHandlerTypes.STEAM,
        syncId,
        playerInv,
        packet,
        ctx,
        ctx.machineInventory(2),
    ) {
    init {
        inventory.onOpen(player)
        // input
        addSlot(0, 2, 1)
        // output
        addOutputSlot(1, 6, 1)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val machineSlotRange: IntRange = (0..1)
}
