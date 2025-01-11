package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.extension.getInventory
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTSimpleMachineScreenHandler(syncId: Int, playerInv: PlayerInventory, ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY) :
    HTMachineScreenHandlerBase(
        RagiumScreenHandlerTypes.SIMPLE_MACHINE,
        syncId,
        playerInv,
        ctx.getInventory(5),
        ctx,
    ) {
    init {
        inventory.onOpen(player)
        // inputs
        addSlot(0, 1, 1)
        addSlot(1, 2, 1)
        addFluidSlot(0, 2, 2)
        // catalyst
        addSlot(2, 4, 2)
        // outputs
        addOutputSlot(3, 6, 1)
        addOutputSlot(4, 7, 1)
        addFluidSlot(1, 6, 2)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val inputSlots: IntRange = (0..1)
    override val outputSlots: IntRange = (3..4)
}
