package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.machine.HTMachinePacket
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.math.BlockPos

abstract class HTMachineScreenHandlerBase(
    type: ScreenHandlerType<*>,
    syncId: Int,
    playerInv: PlayerInventory,
    packet: HTMachinePacket,
    ctx: ScreenHandlerContext,
    inventory: Inventory,
) : HTScreenHandlerBase(
        type,
        syncId,
        playerInv,
        inventory,
    ) {
    val pos: BlockPos = packet.pos
    protected val property: PropertyDelegate =
        ctx.getMachineEntity()?.property ?: ArrayPropertyDelegate(3)

    fun getProgress(): Float = property.get(0).toFloat() / property.get(1).toFloat()
}
