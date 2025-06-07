package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineMenu
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler

class HTCrusherMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    upgrades: IItemHandler,
    inputSlot: IItemHandler,
    outputsSlot: IItemHandler,
) : HTMachineMenu(RagiumMenuTypes.CRUSHER, containerId, inventory, pos, upgrades) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        emptyItemHandler(4),
        emptyItemHandler(1),
        emptyItemHandler(4),
    )

    init {
        // inputs
        addSlot(inputSlot, 0, 2.0, 0.0)
        // upgrades
        addUpgradeSlots(upgrades)
        // outputs
        addOutputSlot(outputsSlot, 0, 5.0, 0.5)
        addOutputSlot(outputsSlot, 1, 6.0, 0.5)
        addOutputSlot(outputsSlot, 2, 5.0, 1.5)
        addOutputSlot(outputsSlot, 3, 6.0, 1.5)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = 0..4
    override val outputSlots: IntRange = 5..8
}
