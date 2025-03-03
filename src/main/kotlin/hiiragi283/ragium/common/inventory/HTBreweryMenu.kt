package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTMachineMenu
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTBreweryMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    firstInputSlot: HTItemSlot,
    secondInputSlot: HTItemSlot,
    thirdInputSlot: HTItemSlot,
    outputSlot: HTItemSlot,
) : HTMachineMenu(RagiumMenuTypes.BREWERY, containerId, inventory, pos) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        RagiumAPI.getInstance().emptyItemSlot(),
        RagiumAPI.getInstance().emptyItemSlot(),
        RagiumAPI.getInstance().emptyItemSlot(),
        RagiumAPI.getInstance().emptyItemSlot(),
    )

    init {
        // inputs
        addSlot(firstInputSlot.createContainerSlot(2, 0))
        addSlot(secondInputSlot.createContainerSlot(4, 0))
        addSlot(thirdInputSlot.createContainerSlot(6, 0))
        addFluidSlot(0, 2, 2)
        // outputs
        addSlot(outputSlot.createContainerSlot(6, 2, HTStorageIO.OUTPUT))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = (0..2)
    override val outputSlots: IntRange = (3..3)
}
