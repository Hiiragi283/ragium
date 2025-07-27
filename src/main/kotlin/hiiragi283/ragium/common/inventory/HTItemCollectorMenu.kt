package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTItemCollectorMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    definition: HTMenuDefinition,
) : HTDefinitionContainerMenu(RagiumMenuTypes.ITEM_COLLECTOR, containerId, inventory, pos, definition) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        HTMenuDefinition.empty(9),
    )

    init {
        // inputs
        for (index: Int in (0..8)) {
            addInputSlot(index, HTSlotHelper.getSlotPosX(3 + index % 3), HTSlotHelper.getSlotPosY(index / 3))
        }
        // upgrades
        addUpgradeSlots()
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(definition.containerData)
    }
}
