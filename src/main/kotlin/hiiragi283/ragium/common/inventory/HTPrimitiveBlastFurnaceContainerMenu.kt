package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTPrimitiveBlastFurnaceContainerMenu(
    containerId: Int,
    playerInv: Inventory,
    pos: BlockPos,
    itemInput: IItemHandler,
    itemOutput: IItemHandler,
) : HTMachineContainerMenu(RagiumMenuTypes.PRIMITIVE_BLAST_FURNACE, containerId, playerInv, pos) {
    constructor(containerId: Int, playerInv: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        playerInv,
        decodePos(registryBuf),
        ItemStackHandler(2),
        ItemStackHandler(1),
    )

    init {
        // inputs
        addSlot(itemInput, 0, 2, 1)
        addSlot(itemInput, 1, 3, 1)
        // outputs
        addOutputSlot(itemOutput, 0, 6, 1)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = (0..1)
    override val outputSlots: IntRange = (2..2)
}
