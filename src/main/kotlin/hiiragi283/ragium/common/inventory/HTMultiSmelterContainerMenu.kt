package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTMultiSmelterContainerMenu(
    syncId: Int,
    playerInv: Inventory,
    itemHandler: IItemHandler = ItemStackHandler(2),
    machineEntity: HTMachineBlockEntity? = null,
) : HTMachineContainerMenu(
        RagiumMenuTypes.MULTI_SMELTER,
        syncId,
        playerInv,
        itemHandler,
        machineEntity,
    ) {
    init {
        // inputs
        addSlot(0, 1, 1)
        // outputs
        addOutputSlot(1, 7, 1)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(containerData)
    }

    override val inputSlots: IntRange = (0..0)
    override val outputSlots: IntRange = (1..1)
}
