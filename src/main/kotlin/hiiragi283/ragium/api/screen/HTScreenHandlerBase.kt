package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.extension.getBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.world.World

abstract class HTScreenHandlerBase(
    type: ScreenHandlerType<*>,
    syncId: Int,
    val playerInv: PlayerInventory,
    val inventory: Inventory,
    ctx: ScreenHandlerContext,
) : ScreenHandler(type, syncId) {
    val player: PlayerEntity = playerInv.player
    val world: World = player.world
    val blockEntity: BlockEntity? = ctx.getBlockEntity()

    override fun canUse(player: PlayerEntity): Boolean = inventory.canPlayerUse(player)

    override fun onClosed(player: PlayerEntity) {
        super.onClosed(player)
        inventory.onClose(player)
    }

    abstract val inputSlots: IntRange
    abstract val outputSlots: IntRange

    private val playerStartIndex: Int
        get() = outputSlots.last + 1

    override fun quickMove(player: PlayerEntity, slot: Int): ItemStack {
        var result: ItemStack = ItemStack.EMPTY
        val slotIn: Slot = slots[slot]
        if (slotIn.hasStack()) {
            val stackIn: ItemStack = slotIn.stack
            result = stackIn.copy()
            when {
                slot in inputSlots -> {
                    if (!insertItem(stackIn, playerStartIndex, playerStartIndex + 36, true)) {
                        return ItemStack.EMPTY
                    }
                }

                slot in outputSlots -> {
                    if (!insertItem(stackIn, playerStartIndex, playerStartIndex + 36, true)) {
                        return ItemStack.EMPTY
                    }
                }

                else -> {
                    if (insertItem(stackIn, inputSlots.first, inputSlots.last + 1, false)) {
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

    //    Extensions    //

    val itemSlots: MutableList<Pair<Int, Int>> = mutableListOf()
    val fluidSlots: MutableMap<Int, Pair<Int, Int>> = mutableMapOf()

    private fun getSlotPosX(index: Int): Int = 8 + index * 18

    private fun getSlotPosY(index: Int): Int = 18 + index * 18

    protected fun addSlot(index: Int, x: Int, y: Int) {
        addSlot(HTSlot(inventory, index, getSlotPosX(x), getSlotPosY(y)))
        itemSlots.add(x to y)
    }

    protected fun addOutputSlot(index: Int, x: Int, y: Int) {
        addSlot(HTOutputSlot(inventory, index, getSlotPosX(x), getSlotPosY(y)))
        itemSlots.add(x to y)
    }

    protected fun addFluidSlot(index: Int, x: Int, y: Int) {
        fluidSlots.put(index, x to y)
    }

    protected fun addPlayerInv(yOffset: Int = 0) {
        // inventory
        (0..26).forEach { index: Int ->
            addSlot(Slot(playerInv, index + 9, getSlotPosX(index % 9), getSlotPosY(3 + (index / 9)) + 12 + yOffset))
        }
        // hotbar
        (0..8).forEach { index: Int ->
            addSlot(Slot(playerInv, index, getSlotPosX(index), getSlotPosY(7) - 2 + yOffset))
        }
    }
}
