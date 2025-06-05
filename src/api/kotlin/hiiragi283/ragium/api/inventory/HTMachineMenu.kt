package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.SimpleContainerData

abstract class HTMachineMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    pos: BlockPos,
) : HTContainerMenu(
        menuType,
        containerId,
        inventory,
        pos,
    ) {
    val machine: HTMachineBlockEntity? = level.getBlockEntity(pos) as? HTMachineBlockEntity

    val progress: Float get() = machine?.progress ?: 0f

    fun addDataSlots() {
        addDataSlots(machine?.containerData ?: SimpleContainerData(2))
    }
}
