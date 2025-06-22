package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTExtractorMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    definition: HTMenuDefinition,
) : HTMachineMenu(RagiumMenuTypes.EXTRACTOR, containerId, inventory, pos, definition) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        HTMenuDefinition.EMPTY,
    )

    init {
        // inputs
        addSlot(definition.getInputSlot(0).createContainerSlot(2, 0))
        // upgrades
        addUpgradeSlots()
        // outputs
        addSlot(definition.getOutputSlot(0).createContainerSlot(5.0, 0.5, HTStorageIO.OUTPUT))
        addSlot(definition.getOutputSlot(1).createContainerSlot(6.0, 0.5, HTStorageIO.OUTPUT))
        addSlot(definition.getOutputSlot(2).createContainerSlot(5.0, 1.5, HTStorageIO.OUTPUT))
        addSlot(definition.getOutputSlot(3).createContainerSlot(6.0, 1.5, HTStorageIO.OUTPUT))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(definition.containerData)
    }

    override val inputSlots: IntRange = 0..4
    override val outputSlots: IntRange = 5..8
}
