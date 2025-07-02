package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTEnergyNetworkAccessMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    definition: HTMenuDefinition,
) : HTDefinitionContainerMenu(RagiumMenuTypes.ENERGY_NETWORK_ACCESS, containerId, inventory, pos, definition) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        HTMenuDefinition.empty(2),
    )

    init {
        // inputs
        addSlot(0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1))
        // upgrades
        addUpgradeSlots()
        // outputs
        addSlot(1, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1))
        // player inventory
        addPlayerInv()
    }

    override val inputSlots: IntRange = 0..4
    override val outputSlots: IntRange = 5..5
}
