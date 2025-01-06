package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.extension.getInventory
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTRockGeneratorScreenHandler(syncId: Int, playerInv: PlayerInventory, ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY) :
    HTMachineScreenHandlerBase(
        RagiumScreenHandlerTypes.ROCK_GENERATOR,
        syncId,
        playerInv,
        ctx.getInventory(2),
        ctx,
    ) {
    init {
        inventory.onOpen(player)
        // inputs
        addFluidSlot(0, 3, 2)
        // catalyst
        addSlot(0, 4, 2)
        // outputs
        addOutputSlot(1, 5, 1)
        addFluidSlot(1, 5, 2)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val inputSlots: IntRange = IntRange.EMPTY
    override val outputSlots: IntRange = (1..1)
}
