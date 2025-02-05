package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTPrimitiveBlastFurnaceContainerMenu(
    syncId: Int,
    playerInv: Inventory,
    pos: BlockPos,
    itemHandler: IItemHandler,
) : HTMachineContainerMenu(
        RagiumMenuTypes.PRIMITIVE_BLAST_FURNACE,
        syncId,
        playerInv,
        pos,
        itemHandler,
    ) {
    constructor(syncId: Int, playerInv: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        syncId,
        playerInv,
        registryBuf?.let(BlockPos.STREAM_CODEC::decode) ?: BlockPos.ZERO,
        ItemStackHandler(3),
    )

    init {
        // inputs
        addSlot(0, 2, 1)
        addSlot(1, 3, 1)
        // outputs
        addOutputSlot(2, 5, 1)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(containerData)
    }

    override val inputSlots: IntRange = (0..1)
    override val outputSlots: IntRange = (2..2)
}
