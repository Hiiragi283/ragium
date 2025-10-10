package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

abstract class HTContainerMenu(menuType: HTDeferredMenuType<*>, containerId: Int, inventory: Inventory) :
    AbstractContainerMenu(menuType.get(), containerId) {
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

    override fun addSlot(slot: Slot): Slot {
        if (slot is HTContainerItemSlot) {
            when (slot.slotType) {
                HTContainerItemSlot.Type.INPUT -> inputSlot.add(slotCount)
                HTContainerItemSlot.Type.OUTPUT -> outputSlot.add(slotCount)
                HTContainerItemSlot.Type.BOTH -> inputSlot.add(slotCount)
            }
        }
        slotCount++
        return super.addSlot(slot)
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

    protected fun addSlots(handler: HTItemHandler) {
        handler
            .getItemSlots(handler.getItemSideFor())
            .mapNotNull(HTItemSlot::createContainerSlot)
            .forEach(::addSlot)
    }

    protected open fun onOpen(player: Player) {}

    protected open fun onClose(player: Player) {}
}
