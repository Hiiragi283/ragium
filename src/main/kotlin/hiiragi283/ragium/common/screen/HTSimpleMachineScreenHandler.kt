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
        // catalyst
        addSlot(2, 4, 2)
        // outputs
        addOutputSlot(3, 6, 1)
        addOutputSlot(4, 7, 1)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val inputSlots: IntRange = (0..1)
    override val outputSlots: IntRange = (3..4)

    /*override fun quickMove(player: PlayerEntity, slot: Int): ItemStack {
        var result: ItemStack = ItemStack.EMPTY
        val slotIn: Slot = slots[slot]
        if (slotIn.hasStack()) {
            val stackIn: ItemStack = slotIn.stack
            result = stackIn.copy()
            when {
                slot in (0..4) -> {
                    if (!insertItem(stackIn, 5, 5 + 36, true)) {
                        return ItemStack.EMPTY
                    }
                }

                else -> {
                    if (insertItem(stackIn, 0, 5, false)) {
                        if (!insertItem(stackIn, 5, 5 + 36, false)) {
                            return ItemStack.EMPTY
                        }
                    }
                }
            }

            if (stackIn.isEmpty) {
                slotIn.stack = ItemStack.EMPTY
            } else {
                slotIn.markDirty()
            }

            if (stackIn.count == result.count) {
                return ItemStack.EMPTY
            }
            slotIn.onTakeItem(player, stackIn)
            if (slot == 0) {
                player.dropItem(stackIn, false)
            }
        }
        return result
    }*/
}
