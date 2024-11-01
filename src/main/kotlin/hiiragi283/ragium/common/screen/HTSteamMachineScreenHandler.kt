package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.extension.machineInventory
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTSteamMachineScreenHandler(
    syncId: Int,
    playerInv: PlayerInventory,
    packet: HTMachinePacket,
    ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
) : HTMachineScreenHandlerBase(
        RagiumScreenHandlerTypes.STEAM,
        syncId,
        playerInv,
        packet,
        ctx,
        ctx.machineInventory(2),
    ) {
    init {
        inventory.onOpen(player)
        // input
        addSlot(0, 2, 1)
        // output
        addOutputSlot(1, 6, 1)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val machineSlotRange: IntRange = (0..4)

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
