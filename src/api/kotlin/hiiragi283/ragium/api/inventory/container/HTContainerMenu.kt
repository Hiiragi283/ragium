package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.inventory.HTSlotHelper
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.SlotItemHandler
import java.util.function.Supplier

/**
 * @see [mekanism.common.inventory.container.MekanismContainer]
 */
abstract class HTContainerMenu(menuType: Supplier<out MenuType<*>>, containerId: Int, inventory: Inventory) :
    AbstractContainerMenu(menuType.get(), containerId) {
    override fun stillValid(player: Player): Boolean = true

    val inputSlots: IntRange
        get() = when {
            inputSlot.isEmpty() -> IntRange.EMPTY
            else -> inputSlot.min()..inputSlot.max()
        }
    val outputSlots: IntRange
        get() = when {
            outputSlot.isEmpty() -> IntRange.EMPTY
            else -> outputSlot.min()..outputSlot.max()
        }

    val playerStartIndex: Int get() = outputSlots.last + 1

    final override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var result: ItemStack = ItemStack.EMPTY
        val slotIn: Slot = slots[index]
        if (slotIn.hasItem()) {
            val stackIn: ItemStack = slotIn.item
            result = stackIn.copy()
            when (index) {
                in inputSlots -> {
                    if (!moveItemStackTo(stackIn, playerStartIndex, playerStartIndex + 36, true)) {
                        return ItemStack.EMPTY
                    }
                }

                in outputSlots -> {
                    if (!moveItemStackTo(stackIn, playerStartIndex, playerStartIndex + 36, true)) {
                        return ItemStack.EMPTY
                    }
                }

                else -> {
                    if (moveItemStackTo(stackIn, inputSlots.first, inputSlots.last + 1, false)) {
                        if (!moveItemStackTo(stackIn, playerStartIndex, playerStartIndex + 36, false)) {
                            return ItemStack.EMPTY
                        }
                    }
                }
            }

            if (stackIn.isEmpty) {
                slotIn.setByPlayer(ItemStack.EMPTY)
            } else {
                slotIn.setChanged()
            }

            if (stackIn.count == result.count) {
                return ItemStack.EMPTY
            }
            slotIn.onTake(player, stackIn)
            if (index == 0) {
                player.drop(stackIn, false)
            }
        }
        return result
    }

    //    Extensions    //

    private var slotCount: Int = 0
    private val inputSlot: MutableList<Int> = mutableListOf()
    private val outputSlot: MutableList<Int> = mutableListOf()

    fun addInputSlot(
        handler: IItemHandler,
        index: Int,
        x: Int,
        y: Int,
    ) {
        addSlot(SlotItemHandler(handler, index, x, y))
        inputSlot.add(slotCount)
        slotCount++
    }

    fun addOutputSlot(
        handler: IItemHandler,
        index: Int,
        x: Int,
        y: Int,
    ) {
        addSlot(object : SlotItemHandler(handler, index, x, y) {
            override fun mayPlace(stack: ItemStack): Boolean = false
        })
        outputSlot.add(slotCount)
        slotCount++
    }

    protected fun addPlayerInv(inventory: Inventory, yOffset: Int = 0) {
        // inventory
        for (index: Int in 0..26) {
            addSlot(
                Slot(
                    inventory,
                    index + 9,
                    HTSlotHelper.getSlotPosX(index % 9),
                    HTSlotHelper.getSlotPosY(3 + (index / 9)) + 12 + yOffset,
                ),
            )
        }
        // hotbar
        for (index: Int in 0..8) {
            addSlot(Slot(inventory, index, HTSlotHelper.getSlotPosX(index), HTSlotHelper.getSlotPosY(7) - 2 + yOffset))
        }
    }
}
