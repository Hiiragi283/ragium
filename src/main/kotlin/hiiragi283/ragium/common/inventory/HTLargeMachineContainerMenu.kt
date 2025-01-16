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
        addFluidSlot(0, 1, 2)
        addFluidSlot(1, 2, 2)
        // catalyst
        addSlot(3, 4, 2)
        // outputs
        addOutputSlot(4, 6, 1)
        addOutputSlot(5, 7, 1)
        addOutputSlot(6, 8, 1)
        addFluidSlot(2, 5, 2)
        addFluidSlot(3, 6, 2)
        // player inventory
        addPlayerInv()
        // register property
        // addProperties(property)
    }

    override val inputSlots: IntRange = (0..1)
    override val outputSlots: IntRange = (3..4)
}
