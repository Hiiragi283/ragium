package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTDistillationTowerContainerMenu(
    syncId: Int,
    playerInv: Inventory,
    itemHandler: IItemHandler = ItemStackHandler(2),
    machineEntity: HTMachineBlockEntity? = null,
) : HTMachineContainerMenu(
        RagiumMenuTypes.DISTILLATION_TOWER,
        syncId,
        playerInv,
        itemHandler,
        machineEntity,
    ) {
    init {
        // inputs
        addFluidSlot(0, 3, 2)
        // catalyst
        addSlot(0, 4, 2)
        // outputs
        addOutputSlot(1, 5, 1)
        addFluidSlot(1, 5, 2)
        addFluidSlot(2, 6, 2)
        addFluidSlot(3, 7, 2)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(containerData)
    }

    override val inputSlots: IntRange = IntRange.EMPTY
    override val outputSlots: IntRange = (1..1)
}
