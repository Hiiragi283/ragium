package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTRefineryContainerMenu(
    syncId: Int,
    playerInv: Inventory,
    pos: BlockPos,
    itemHandler: IItemHandler,
) : HTMachineContainerMenu(
        RagiumMenuTypes.REFINERY,
        syncId,
        playerInv,
        pos,
        itemHandler,
    ) {
    constructor(syncId: Int, playerInv: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        syncId,
        playerInv,
        registryBuf?.let(BlockPos.STREAM_CODEC::decode) ?: BlockPos.ZERO,
        ItemStackHandler(1),
    )

    init {
        // inputs
        addFluidSlot(0, 2, 1)
        // outputs
        addOutputSlot(0, 6, 1)
        addFluidSlot(1, 7, 1)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(containerData)
    }

    override val inputSlots: IntRange = IntRange.EMPTY
    override val outputSlots: IntRange = (0..0)
}
