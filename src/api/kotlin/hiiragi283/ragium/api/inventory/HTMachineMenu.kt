package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.SimpleContainerData
import java.util.function.Supplier

abstract class HTMachineMenu(
    menuType: Supplier<out MenuType<*>>,
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

    fun getProgress(): Float = machine?.getProgress() ?: 0f

    fun addDataSlots() {
        addDataSlots(machine?.containerData ?: SimpleContainerData(2))
    }
}
