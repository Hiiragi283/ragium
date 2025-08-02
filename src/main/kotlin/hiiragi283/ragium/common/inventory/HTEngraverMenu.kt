package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTEngraverMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    definition: HTMenuDefinition,
) : HTDefinitionContainerMenu(
        RagiumMenuTypes.ENGRAVER,
        containerId,
        inventory,
        pos,
        definition,
    ) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        HTMenuDefinition.empty(6),
    )

    init {
        // inputs
        addInputSlot(0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        addInputSlot(1, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
        // upgrades
        addUpgradeSlots()
        // outputs
        addOutputSlot(2, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(0.5))
        addOutputSlot(3, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0.5))
        addOutputSlot(4, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(1.5))
        addOutputSlot(5, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1.5))
        // player inventory
        addPlayerInv(inventory)
        // register property
        addDataSlots(definition.containerData)
    }
}
