package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.screen.HTScreenHandlerBase
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory

class HTFilteringPipeScreenHandler(syncId: Int, playerInv: PlayerInventory, val pipeType: HTPipeType) :
    HTScreenHandlerBase(RagiumScreenHandlerTypes.FILTERING_PIPE, syncId, playerInv, SimpleInventory(0)) {
    init {
        addPlayerInv(103)
    }

    override val machineSlotRange: IntRange = IntRange.EMPTY
}
