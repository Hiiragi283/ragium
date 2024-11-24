package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.extension.getInventory
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTDistillationTowerScreenHandler(
    syncId: Int,
    playerInv: PlayerInventory,
    packet: HTMachinePacket,
    ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
) : HTMachineScreenHandlerBase(
        RagiumScreenHandlerTypes.DISTILLATION_TOWER,
        syncId,
        playerInv,
        packet,
        ctx,
        ctx.getInventory(2),
    ) {
    init {
        inventory.onOpen(player)
        // catalyst
        addSlot(0, 4, 2)
        // outputs
        addOutputSlot(1, 5, 1)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val machineSlotRange: IntRange = (0..1)
}
