package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.inventory.slot.HTContainerItemSlot
import hiiragi283.ragium.api.inventory.slot.HTHotBarSlot
import hiiragi283.ragium.api.inventory.slot.HTInventorySlot
import hiiragi283.ragium.api.inventory.slot.HTMainInventorySlot
import hiiragi283.ragium.api.inventory.slot.HTSlot
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

/**
 * Ragiumで使用する[AbstractContainerMenu]の拡張クラス
 * @see [mekanism.common.inventory.container.MekanismContainer]
 */
abstract class HTContainerMenu(menuType: HTDeferredMenuType<*>, containerId: Int, inventory: Inventory) :
    AbstractContainerMenu(menuType.get(), containerId) {
    companion object {
        @JvmStatic
        fun <SLOT> insertItem(slots: List<SLOT>, stack: ItemStack, ignoreEmpty: Boolean): ItemStack where SLOT : Slot, SLOT : HTSlot =
            insertItem(slots, stack, ignoreEmpty, false)

        @JvmStatic
        fun <SLOT> insertItem(
            slots: List<SLOT>,
            stack: ItemStack,
            ignoreEmpty: Boolean,
            simulate: Boolean,
        ): ItemStack where SLOT : Slot, SLOT : HTSlot = insertItem(slots, stack, ignoreEmpty, simulate, false)

        @JvmStatic
        fun <SLOT> insertItem(
            slots: List<SLOT>,
            stack: ItemStack,
            ignoreEmpty: Boolean,
            simulate: Boolean,
            checkAll: Boolean,
        ): ItemStack where SLOT : Slot, SLOT : HTSlot {
            if (stack.isEmpty) return stack
            var result: ItemStack = stack
            for (slot: SLOT in slots) {
                if (!checkAll && ignoreEmpty != slot.hasItem()) continue
                result = slot.insertItem(stack, simulate)
                if (result.isEmpty) break
            }
            return result
        }
    }

    init {
        onOpen(inventory.player)
    }

    protected val handlerSlot: List<HTContainerItemSlot> get() = handlerSlots1
    protected val mainInventorySlots: List<HTMainInventorySlot> get() = mainInventorySlots1
    protected val hotBarSlots: List<HTHotBarSlot> get() = hotBarSlots1

    private val handlerSlots1: MutableList<HTContainerItemSlot> = mutableListOf()
    private val mainInventorySlots1: MutableList<HTMainInventorySlot> = mutableListOf()
    private val hotBarSlots1: MutableList<HTHotBarSlot> = mutableListOf()

    override fun addSlot(slot: Slot): Slot {
        super.addSlot(slot)
        when (slot) {
            is HTContainerItemSlot -> handlerSlots1.add(slot)
            is HTMainInventorySlot -> mainInventorySlots1.add(slot)
            is HTHotBarSlot -> hotBarSlots1.add(slot)
        }
        return slot
    }

    /**
     * @see [mekanism.common.inventory.container.MekanismContainer.quickMoveStack]
     */
    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        val current: Slot = slots[index]
        if (!current.hasItem()) return ItemStack.EMPTY
        var slotStack: ItemStack = current.item
        var stackToInsert: ItemStack = slotStack
        if (current is HTContainerItemSlot) {
            val maxSize: Int = slotStack.maxStackSize
            if (slotStack.count > maxSize) {
                slotStack = slotStack.copyWithCount(maxSize)
                stackToInsert = slotStack
            }
            stackToInsert = insertItem(hotBarSlots, stackToInsert, true)
            stackToInsert = insertItem(mainInventorySlots, stackToInsert, true)

            stackToInsert = insertItem(hotBarSlots, stackToInsert, false)
            stackToInsert = insertItem(mainInventorySlots, stackToInsert, false)
        } else {
            stackToInsert = insertItem(handlerSlot, stackToInsert, true)
            if (slotStack.count == stackToInsert.count) {
                stackToInsert = insertItem(handlerSlot, stackToInsert, false)
                if (slotStack.count == stackToInsert.count) {
                    when (current) {
                        is HTMainInventorySlot -> {
                            stackToInsert = insertItem(hotBarSlots, stackToInsert, true)
                            stackToInsert = insertItem(hotBarSlots, stackToInsert, false)
                        }

                        is HTHotBarSlot -> {
                            stackToInsert = insertItem(mainInventorySlots, stackToInsert, true)
                            stackToInsert = insertItem(mainInventorySlots, stackToInsert, false)
                        }
                    }
                }
            }
        }

        if (stackToInsert.count == slotStack.count) {
            return ItemStack.EMPTY
        }
        val diff: Int = slotStack.count - stackToInsert.count
        val newStack: ItemStack = current.remove(diff)
        current.onTake(player, newStack)
        return newStack
    }

    final override fun removed(player: Player) {
        super.removed(player)
        onClose(player)
    }

    //    Extensions    //

    protected fun addPlayerInv(inventory: Inventory, yOffset: Int = 0) {
        // inventory
        for (index: Int in 0..26) {
            addSlot(
                HTInventorySlot(
                    inventory,
                    index + 9,
                    HTSlotHelper.getSlotPosX(index % 9),
                    HTSlotHelper.getSlotPosY(3 + (index / 9)) + 12 + yOffset,
                ),
            )
        }
        // hotbar
        for (index: Int in 0..8) {
            addSlot(
                HTHotBarSlot(
                    inventory,
                    index,
                    HTSlotHelper.getSlotPosX(index),
                    HTSlotHelper.getSlotPosY(7) - 2 + yOffset,
                ),
            )
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
