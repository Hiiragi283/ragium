package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTSlotConfigurationMenu(containerId: Int, inventory: Inventory, blockEntity: HTMachineBlockEntity) :
    HTBlockEntityContainerMenu<HTMachineBlockEntity>(RagiumMenuTypes.SLOT_CONFIG, containerId, inventory, blockEntity)
