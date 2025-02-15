package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTDisenchanterContainerMenu(
    containerId: Int,
    playerInv: Inventory,
    pos: BlockPos,
    ticketInput: IItemHandler,
    toolInput: IItemHandler,
    bookInput: IItemHandler,
) : HTContainerMenu(TODO(), containerId, playerInv, pos) {
    constructor(containerId: Int, playerInv: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        playerInv,
        decodePos(registryBuf),
        ItemStackHandler(1),
        ItemStackHandler(1),
        ItemStackHandler(1),
    )

    init {
        // inputs
        addSlot(ticketInput, 0, 1, 1)
        addSlot(toolInput, 0, 2, 1)
        addSlot(bookInput, 0, 3, 1)
        // outputs
        // player inventory
        addPlayerInv()
    }

    override val inputSlots: IntRange = (0..2)
    override val outputSlots: IntRange = IntRange.EMPTY
}
