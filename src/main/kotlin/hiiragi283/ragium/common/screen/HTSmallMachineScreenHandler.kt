package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.extension.getInventory
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTSmallMachineScreenHandler(syncId: Int, playerInv: PlayerInventory, ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY) :
    HTMachineScreenHandlerBase(
        RagiumScreenHandlerTypes.SMALL_MACHINE,
        syncId,
        playerInv,
        ctx.getInventory(2),
        ctx,
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
