package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.screen.HTScreenHandlerBase
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.screen.ScreenHandlerContext

class HTExporterScreenHandler(
    syncId: Int,
    playerInv: PlayerInventory,
    val pipeType: HTPipeType,
    ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
) : HTScreenHandlerBase(RagiumScreenHandlerTypes.EXPORTER, syncId, playerInv, SimpleInventory(0), ctx) {
    init {
        // player inventory
        addPlayerInv()
    }

    override val machineSlotRange: IntRange = IntRange.EMPTY
}
