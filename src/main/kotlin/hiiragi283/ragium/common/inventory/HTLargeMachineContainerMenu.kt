package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTLargeMachineContainerMenu(
    syncId: Int,
    playerInv: Inventory,
    pos: BlockPos,
    itemHandler: IItemHandler,
) : HTMachineContainerMenu(
        RagiumMenuTypes.LARGE_MACHINE,
        syncId,
        playerInv,
        pos,
        itemHandler,
    ) {
    constructor(syncId: Int, playerInv: Inventory, registryBuf: RegistryFriendlyByteBuf) : this(
        syncId,
        playerInv,
        BlockPos.STREAM_CODEC.decode(registryBuf),
        ItemStackHandler(6),
    )

    init {
        // inputs
        addSlot(0, 1, 1)
        addSlot(1, 2, 1)
        addSlot(2, 3, 1)
        addFluidSlot(0, 2, 2)
        addFluidSlot(1, 3, 2)
        // outputs
        addOutputSlot(3, 5, 1)
        addOutputSlot(4, 6, 1)
        addOutputSlot(5, 7, 1)
        addFluidSlot(2, 5, 2)
        addFluidSlot(3, 6, 2)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(containerData)
    }

    override val inputSlots: IntRange = (0..2)
    override val outputSlots: IntRange = (3..5)
}
