package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.extension.getMachineAccess
import hiiragi283.ragium.api.machine.HTMachineAccess
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.SimpleContainerData
import java.util.function.Supplier

abstract class HTMachineContainerMenu(
    menuType: Supplier<out MenuType<*>>,
    containerId: Int,
    playerInv: Inventory,
    pos: BlockPos,
) : HTContainerMenu(
        menuType,
        containerId,
        playerInv,
        pos,
    ) {
    val machine: HTMachineAccess? = level.getMachineAccess(pos)

    fun getProgress(): Float = machine?.getProgress() ?: 0f

    fun addDataSlots() {
        addDataSlots(machine?.containerData ?: SimpleContainerData(2))
    }
}
