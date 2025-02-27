package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTInfuserContainerMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    inputSlot: HTItemSlot,
    outputSlot: HTItemSlot,
) : HTMachineContainerMenu(RagiumMenuTypes.INFUSER, containerId, inventory, pos) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        RagiumAPI.getInstance().emptyItemSlot(),
        RagiumAPI.getInstance().emptyItemSlot(),
    )

    init {
        // inputs
        addFluidSlot(0, 1, 1)
        addSlot(inputSlot.createContainerSlot(2, 1))
        // outputs
        addSlot(outputSlot.createContainerSlot(6, 1, HTStorageIO.OUTPUT))
        addFluidSlot(1, 7, 1)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = (0..0)
    override val outputSlots: IntRange = (1..1)
}
