package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.extension.getInventory
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTAssemblyLineScreenHandler(syncId: Int, playerInv: PlayerInventory, ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY) :
    HTMachineScreenHandlerBase(
        RagiumScreenHandlerTypes.ASSEMBLY_LINE,
        syncId,
        playerInv,
        ctx.getInventory(6),
        ctx,
    ) {
    init {
        inventory.onOpen(player)
        // inputs
        addSlot(0, 2, 0)
        addSlot(1, 3, 0)
        addSlot(2, 2, 1)
        addSlot(3, 3, 1)
        addFluidSlot(0, 2, 2)
        addFluidSlot(1, 3, 2)
        // catalyst
        addSlot(4, 4, 2)
        // outputs
        addOutputSlot(5, 5, 1)
        addFluidSlot(2, 5, 2)
        addFluidSlot(3, 6, 2)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val inputSlots: IntRange = (0..3)
    override val outputSlots: IntRange = (5..5)
}
