package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineMenu
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTCrusherMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    inputSlot: HTItemSlot,
    outputSlot: HTItemSlot,
) : HTMachineMenu(RagiumMenuTypes.CRUSHER, containerId, inventory, pos) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        emptySlot(),
        emptySlot(),
    )

    init {
        // inputs
        addSlot(inputSlot.createContainerSlot(2, 0))
        // outputs
        addSlot(outputSlot.createContainerSlot(5, 1, HTStorageIO.OUTPUT))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = 0..0
    override val outputSlots: IntRange = 1..1
}
