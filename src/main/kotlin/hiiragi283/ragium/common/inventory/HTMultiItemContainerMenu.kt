package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTMultiItemContainerMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    firstInputSlot: HTItemSlot,
    secondInputSlot: HTItemSlot,
    thirdInputSlot: HTItemSlot,
    outputSlot: HTItemSlot,
) : HTMachineContainerMenu(RagiumMenuTypes.MULTI_ITEM, containerId, inventory, pos) {
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
        addSlot(firstInputSlot.createContainerSlot(1, 1))
        addSlot(secondInputSlot.createContainerSlot(2, 1))
        addSlot(thirdInputSlot.createContainerSlot(3, 1))
        addFluidSlot(0, 1, 2)
        // outputs
        addSlot(outputSlot.createContainerSlot(6, 1, HTStorageIO.OUTPUT))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = (0..2)
    override val outputSlots: IntRange = (3..3)
}
