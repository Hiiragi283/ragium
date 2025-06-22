package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineMenu
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory

class HTBlockBreakerMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    definition: HTMenuDefinition,
) : HTMachineMenu(
        RagiumMenuTypes.BLOCK_BREAKER,
        containerId,
        inventory,
        pos,
        definition,
    ) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        HTMenuDefinition.EMPTY,
    )

    init {
        // inputs
        addSlot(definition.getInputSlot(0).createContainerSlot(4, 1))
        // upgrades
        addUpgradeSlots()
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots(definition.containerData)
    }

    override val inputSlots: IntRange = 0..0
    override val outputSlots: IntRange = IntRange.EMPTY
}
