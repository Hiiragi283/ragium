package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineMenu
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHelper
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.neoforged.neoforge.items.IItemHandler

class HTCrusherMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    upgrades: IItemHandler,
    inputSlot: HTItemSlot,
    outputSlots: List<HTItemSlot>,
    containerData: ContainerData,
) : HTMachineMenu(RagiumMenuTypes.CRUSHER, containerId, inventory, pos, upgrades, containerData) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        emptyItemHandler(4),
        emptySlot(),
        HTItemSlotHelper.createEmptySlotList(4),
        SimpleContainerData(2),
    )

    init {
        // inputs
        addSlot(inputSlot.createContainerSlot(2, 0))
        // upgrades
        addUpgradeSlots(upgrades)
        // outputs
        addSlot(outputSlots[0].createContainerSlot(5.0, 0.5, HTStorageIO.OUTPUT))
        addSlot(outputSlots[1].createContainerSlot(6.0, 0.5, HTStorageIO.OUTPUT))
        addSlot(outputSlots[2].createContainerSlot(5.0, 1.5, HTStorageIO.OUTPUT))
        addSlot(outputSlots[3].createContainerSlot(6.0, 1.5, HTStorageIO.OUTPUT))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(containerData)
    }

    override val inputSlots: IntRange = 0..4
    override val outputSlots: IntRange = 5..8
}
