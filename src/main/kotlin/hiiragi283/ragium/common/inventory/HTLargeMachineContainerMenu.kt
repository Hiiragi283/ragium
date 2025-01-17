package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTLargeMachineContainerMenu(
    syncId: Int,
    playerInv: Inventory,
    itemHandler: IItemHandler = ItemStackHandler(7),
    machineEntity: HTMachineBlockEntity? = null,
) : HTMachineContainerMenu(
        RagiumMenuTypes.LARGE_MACHINE,
        syncId,
        playerInv,
        itemHandler,
        machineEntity,
    ) {
    init {
        // inputs
        addSlot(0, 1, 1)
        addSlot(1, 2, 1)
        addSlot(2, 3, 1)
        addFluidSlot(0, 2, 2)
        addFluidSlot(1, 3, 2)
        // catalyst
        addSlot(3, 4, 2)
        // outputs
        addOutputSlot(4, 5, 1)
        addOutputSlot(5, 6, 1)
        addOutputSlot(6, 7, 1)
        addFluidSlot(2, 5, 2)
        addFluidSlot(3, 6, 2)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(containerData)
    }

    override val inputSlots: IntRange = (0..2)
    override val outputSlots: IntRange = (4..6)
}
