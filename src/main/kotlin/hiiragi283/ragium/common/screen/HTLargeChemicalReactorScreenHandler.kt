package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.screen.HTMachineScreenHandler
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import net.minecraft.entity.player.PlayerInventory

class HTLargeChemicalReactorScreenHandler(syncId: Int, playerInv: PlayerInventory, machine: HTMachineBlockEntityBase? = null) :
    HTMachineScreenHandler(
        RagiumScreenHandlerTypes.LARGE_CHEMICAL_REACTOR,
        syncId,
        playerInv,
        machine,
        7,
    ) {
    init {
        inventory.onOpen(player)
        // inputs
        addSlot(0, 1, 0)
        addSlot(1, 2, 0)
        addSlot(2, 3, 0)
        addFluidSlot(0, 1, 1)
        addFluidSlot(1, 2, 1)
        addFluidSlot(2, 3, 1)
        addFluidSlot(3, 2, 2)
        addFluidSlot(4, 3, 2)
        // catalyst
        addSlot(3, 4, 2)
        // outputs
        addOutputSlot(4, 5, 0)
        addOutputSlot(5, 6, 0)
        addOutputSlot(6, 7, 0)
        addFluidSlot(5, 5, 1)
        addFluidSlot(6, 6, 1)
        addFluidSlot(7, 7, 1)
        addFluidSlot(8, 5, 2)
        addFluidSlot(9, 6, 2)
        // player inventory
        addPlayerInv()
        // register property
        addProperties(property)
    }

    override val inputSlots: IntRange = (0..2)
    override val outputSlots: IntRange = (4..6)

    /*override fun quickMove(player: PlayerEntity, slot: Int): ItemStack {
        var result: ItemStack = ItemStack.EMPTY
        val slotIn: Slot = slots[slot]
        if (slotIn.hasStack()) {
            val stackIn: ItemStack = slotIn.stack
            result = stackIn.copy()
            when {
                slot in (0..6) -> {
                    if (!insertItem(stackIn, 6, 6 + 36, true)) {
                        return ItemStack.EMPTY
                    }
                }

                else -> {
                    if (insertItem(stackIn, 0, 6, false)) {
                        if (!insertItem(stackIn, 6, 6 + 36, false)) {
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
