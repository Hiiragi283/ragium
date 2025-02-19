package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTMixerContainerMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    itemOutput: IItemHandler,
) : HTMachineContainerMenu(RagiumMenuTypes.MIXER, containerId, inventory, pos) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        ItemStackHandler(1),
    )

    init {
        // inputs

        addFluidSlot(0, 2, 1)
        addFluidSlot(1, 3, 1)
        // outputs
        addOutputSlot(itemOutput, 0, 6, 1)
        addFluidSlot(2, 7, 1)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = IntRange.EMPTY
    override val outputSlots: IntRange = (0..0)
}
