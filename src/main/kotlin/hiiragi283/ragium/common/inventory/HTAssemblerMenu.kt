package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTMachineMenu
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTAssemblerMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    firstInputSlot: HTItemSlot,
    secondInputSlot: HTItemSlot,
    thirdInputSlot: HTItemSlot,
    outputSlot: HTItemSlot,
) : HTMachineMenu(RagiumMenuTypes.ASSEMBLER, containerId, inventory, pos) {
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
        addSlot(firstInputSlot.createContainerSlot(0, 1))
        addSlot(secondInputSlot.createContainerSlot(2, 1))
        addSlot(thirdInputSlot.createContainerSlot(4, 1))
        addFluidSlot(0, 6, 1)
        // outputs
        addSlot(outputSlot.createContainerSlot(8, 1, HTStorageIO.OUTPUT))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = (0..2)
    override val outputSlots: IntRange = (3..3)
}
