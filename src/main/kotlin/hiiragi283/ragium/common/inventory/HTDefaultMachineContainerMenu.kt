package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTDefaultMachineContainerMenu(
    syncId: Int,
    playerInv: Inventory,
    itemHandler: IItemHandler = ItemStackHandler(5),
    machineEntity: HTMachineBlockEntity? = null,
) : HTMachineContainerMenu(
        RagiumMenuTypes.DEFAULT_MACHINE,
        syncId,
        playerInv,
        itemHandler,
        machineEntity,
    ) {
    init {
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
        // addProperties(property)
    }

    override val inputSlots: IntRange = (0..1)
    override val outputSlots: IntRange = (3..4)
}
