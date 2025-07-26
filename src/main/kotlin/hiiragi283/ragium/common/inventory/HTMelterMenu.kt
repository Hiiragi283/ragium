package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
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
) : HTDefinitionContainerMenu(RagiumMenuTypes.MELTER, containerId, inventory, pos, definition) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        HTMenuDefinition.empty(1),
    )

    init {
        // inputs
        addSlot(0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0.5))
        // upgrades
        addUpgradeSlots()
        // outputs
        addFluidSlot(0, HTSlotHelper.getSlotPosX(5.5) - 4, HTSlotHelper.getSlotPosY(1) - 4, 24, 24)
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(definition.containerData)
    }

    override val inputSlots: IntRange = 0..4
    override val outputSlots: IntRange = IntRange.EMPTY
}
