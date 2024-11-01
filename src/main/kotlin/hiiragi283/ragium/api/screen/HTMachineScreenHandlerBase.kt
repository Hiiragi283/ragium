package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
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
        ctx.getMachineEntity()?.property ?: ArrayPropertyDelegate(HTMachineEntity.MAX_PROPERTIES)

    fun getProgress(): Float = property.get(0).toFloat() / property.get(1).toFloat()

    abstract val machineSlotRange: IntRange

    private val playerStartIndex: Int
        get() = machineSlotRange.last + 1

    override fun quickMove(player: PlayerEntity, slot: Int): ItemStack {
        var result: ItemStack = ItemStack.EMPTY
        val slotIn: Slot = slots[slot]
        if (slotIn.hasStack()) {
            val stackIn: ItemStack = slotIn.stack
            result = stackIn.copy()
            when {
                slot in machineSlotRange -> {
                    if (!insertItem(stackIn, playerStartIndex, playerStartIndex + 36, true)) {
                        return ItemStack.EMPTY
                    }
                }

                else -> {
                    if (insertItem(stackIn, machineSlotRange.first, playerStartIndex, false)) {
                        if (!insertItem(stackIn, playerStartIndex, playerStartIndex + 36, false)) {
                            return ItemStack.EMPTY
                        }
                    }
                }
            }

            if (stackIn.isEmpty) {
                slotIn.stack = ItemStack.EMPTY
            } else {
                slotIn.markDirty()
            }

            if (stackIn.count == result.count) {
                return ItemStack.EMPTY
            }
            slotIn.onTakeItem(player, stackIn)
            if (slot == 0) {
                player.dropItem(stackIn, false)
            }
        }
        return result
    }
}
