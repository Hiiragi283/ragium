package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.HTMachineMenu
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler

class HTCrusherMenu(
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
    upgrades: IItemHandler,
    inputSlot: HTItemSlot,
    outputSlot: HTItemSlot,
) : HTMachineMenu(RagiumMenuTypes.CRUSHER, containerId, inventory, pos, upgrades) {
    constructor(containerId: Int, inventory: Inventory, registryBuf: RegistryFriendlyByteBuf?) : this(
        containerId,
        inventory,
        decodePos(registryBuf),
        emptyUpgrades(),
        emptySlot(),
        emptySlot(),
    )

    init {
        // inputs
        addSlot(inputSlot.createContainerSlot(2, 0))
        // upgrades
        addUpgradeSlots(upgrades)
        // outputs
        addSlot(outputSlot.createContainerSlot(5.0, 0.5, HTStorageIO.OUTPUT))
        // player inventory
        addPlayerInv()
        // register property
        addDataSlots()
    }

    override val inputSlots: IntRange = 0..4
    override val outputSlots: IntRange = 5..5
}
