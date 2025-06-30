package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTMelterMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    definition: HTMenuDefinition,
) : HTMachineMenu(RagiumMenuTypes.MELTER, containerId, inventory, pos, definition) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        HTMenuDefinition.EMPTY,
    )

    init {
        // inputs
        addSlot(definition.getInputSlot(0).createContainerSlot(2, 1))
        // upgrades
        addUpgradeSlots()
        // outputs
        addFluidSlot(0, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(definition.containerData)
    }

    override val inputSlots: IntRange = 0..4
    override val outputSlots: IntRange = IntRange.EMPTY
}
