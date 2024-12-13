package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.extension.getInventory
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTChemicalMachineScreenHandler(syncId: Int, playerInv: PlayerInventory, ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY) :
    HTMachineScreenHandlerBase(
        RagiumScreenHandlerTypes.CHEMICAL_MACHINE,
        syncId,
        playerInv,
        ctx.getInventory(5),
        ctx,
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

    override val inputSlots: IntRange = (0..1)
    override val outputSlots: IntRange = (3..4)
}
