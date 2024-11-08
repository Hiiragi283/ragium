package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.screen.ScreenHandlerContext

class HTFluidDrillScreenHandler(
    syncId: Int,
    playerInv: PlayerInventory,
    packet: HTMachinePacket,
    ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
) : HTMachineScreenHandlerBase(
        RagiumScreenHandlerTypes.FLUID_DRILL,
        syncId,
        playerInv,
        packet,
        ctx,
        SimpleInventory(0),
    ) {
    init {
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val machineSlotRange: IntRange = (0..0)
}
