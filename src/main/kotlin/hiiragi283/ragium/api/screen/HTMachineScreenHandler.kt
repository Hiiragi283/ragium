package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandlerType

abstract class HTMachineScreenHandler(
    type: ScreenHandlerType<*>,
    syncId: Int,
    playerInv: PlayerInventory,
    protected val machine: HTMachineBlockEntityBase?,
    defaultSlots: Int,
) : HTScreenHandlerBase(
        type,
        syncId,
        playerInv,
        machine?.asInventory() ?: SimpleInventory(defaultSlots),
        machine.createContext(),
    ) {
    protected val property: PropertyDelegate = machine?.property ?: ArrayPropertyDelegate(3)

    fun getProgress(): Float = property.get(0).toFloat() / property.get(1).toFloat()

    fun interface Factory {
        fun createMenu(syncId: Int, playerInv: PlayerInventory, machine: HTMachineBlockEntityBase?): HTMachineScreenHandler
    }
}
