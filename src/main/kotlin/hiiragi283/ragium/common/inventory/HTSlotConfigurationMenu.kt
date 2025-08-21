package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTSlotConfigurationMenu(containerId: Int, inventory: Inventory, blockEntity: HTMachineBlockEntity) :
    HTContainerWithContextMenu<HTMachineBlockEntity>(RagiumMenuTypes.SLOT_CONFIG, containerId, inventory, blockEntity)
