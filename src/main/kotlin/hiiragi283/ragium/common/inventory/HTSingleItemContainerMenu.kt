package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTSingleItemContainerMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    itemInput: IItemHandler,
    itemCatalyst: IItemHandler,
    itemOutput: IItemHandler,
) : HTMachineContainerMenu(RagiumMenuTypes.SINGLE_ITEM, containerId, inventory, pos) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        ItemStackHandler(1),
        ItemStackHandler(1),
        ItemStackHandler(1),
    )

    init {
        // inputs
        addSlot(itemInput, 0, 2, 1)
        // Catalyst
        addSlot(itemCatalyst, 0, 4, 2)
        // outputs
        addOutputSlot(itemOutput, 0, 6, 1)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = (0..0)
    override val outputSlots: IntRange = (2..2)
}
