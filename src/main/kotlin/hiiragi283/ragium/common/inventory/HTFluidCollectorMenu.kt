package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTFluidCollectorMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    definition: HTMenuDefinition,
) : HTDefinitionContainerMenu(RagiumMenuTypes.FLUID_COLLECTOR, containerId, inventory, pos, definition) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        HTMenuDefinition.empty(0),
    )

    init {
        // inputs
        addFluidSlot(0, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        // upgrades
        addUpgradeSlots()
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(definition.containerData)
    }
}
