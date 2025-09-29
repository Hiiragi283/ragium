package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

class HTAccessConfigurationMenu(containerId: Int, inventory: Inventory, blockEntity: HTMachineBlockEntity) :
    HTContainerWithContextMenu<HTMachineBlockEntity>(RagiumMenuTypes.ACCESS_CONFIG, containerId, inventory, blockEntity) {
    override fun stillValid(player: Player): Boolean = !context.isRemoved && context.level?.isInWorldBounds(context.blockPos) == true
}
