package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTPrimitiveBlastFurnaceContainerMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    firstItemSlot: HTItemSlot,
    secondItemSlot: HTItemSlot,
    outputSlot: HTItemSlot,
) : HTMachineContainerMenu(RagiumMenuTypes.PRIMITIVE_BLAST_FURNACE, containerId, inventory, pos) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        RagiumAPI.getInstance().emptyItemSlot(),
        RagiumAPI.getInstance().emptyItemSlot(),
        RagiumAPI.getInstance().emptyItemSlot(),
    )

    init {
        // inputs
        addSlot(firstItemSlot.createContainerSlot(2, 1))
        addSlot(secondItemSlot.createContainerSlot(3, 1))
        // outputs
        addSlot(outputSlot.createContainerSlot(6, 1, HTStorageIO.OUTPUT))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = (0..1)
    override val outputSlots: IntRange = (2..2)
}
