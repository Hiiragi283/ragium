package hiiragi283.ragium.api.inventory.container

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import hiiragi283.ragium.api.RagiumAPI
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
import kotlin.math.min

abstract class HTContainerMenu(menuType: HTDeferredMenuType<*, *>, containerId: Int, inventory: Inventory) :
    AbstractContainerMenu(menuType.get(), containerId) {
    final override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var result: ItemStack = ItemStack.EMPTY
        val slotIn: Slot = slots.getOrNull(index) ?: return result
        if (slotIn.hasItem()) {
            val stackIn: ItemStack = slotIn.item
            result = stackIn.copy()
            when (index) {
                in slotMap.values() -> {
                    if (!moveItemStackTo(stackIn, hotBarSlots.min(), inventorySlots.max() + 1, false)) {
                        return ItemStack.EMPTY
                    }
                }
                in inventorySlots -> if (!moveToInventory(stackIn, hotBarSlots)) {
                    return ItemStack.EMPTY
                }
                in hotBarSlots -> if (!moveToInventory(stackIn, inventorySlots)) {
                    return ItemStack.EMPTY
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

    private fun moveToInventory(stack: ItemStack, inventorySlots: Iterable<Int>): Boolean {
        val inputSlots: Collection<Int> = slotMap[HTContainerItemSlot.Type.INPUT]
        val bothSlots: Collection<Int> = slotMap[HTContainerItemSlot.Type.BOTH]
        if (!moveItemStackTo(stack, inputSlots, false)) {
            if (!moveItemStackTo(stack, bothSlots, false)) {
                if (!moveItemStackTo(stack, inventorySlots, false)) {
                    return false
                }
            }
        }
        return true
    }

    //    Extensions    //

    private var slotCount: Int = 0
    private val slotMap: Multimap<HTContainerItemSlot.Type, Int> = HashMultimap.create()
    private val hotBarSlots: MutableList<Int> = mutableListOf()
    private val inventorySlots: MutableList<Int> = mutableListOf()

    override fun addSlot(slot: Slot): Slot {
        if (slot is HTContainerItemSlot) {
            slotMap.put(slot.slotType, slotCount)
            RagiumAPI.LOGGER.info("${slot.slotType} slot: $slotCount")
        }
        slotCount++
        return super.addSlot(slot)
    }

    protected fun addPlayerInv(inventory: Inventory, yOffset: Int = 0) {
        // hotbar
        for (index: Int in 0..8) {
            hotBarSlots.add(slotCount)
            RagiumAPI.LOGGER.debug("Hotbar slot: $slotCount")
            addSlot(Slot(inventory, index, HTSlotHelper.getSlotPosX(index), HTSlotHelper.getSlotPosY(7) - 2 + yOffset))
        }
        // inventory
        for (index: Int in 0..26) {
            inventorySlots.add(slotCount)
            RagiumAPI.LOGGER.debug("Inventory slot: $slotCount")
            addSlot(
                Slot(
                    inventory,
                    index + 9,
                    HTSlotHelper.getSlotPosX(index % 9),
                    HTSlotHelper.getSlotPosY(3 + (index / 9)) + 12 + yOffset,
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

    protected fun moveItemStackTo(stack: ItemStack, slots: Iterable<Int>, reverseDirection: Boolean): Boolean {
        var flag = false
        if (stack.isStackable) {
            // スロットの順番を反転
            val fixedRange: Iterable<Int> = when (reverseDirection) {
                true -> slots.reversed()
                false -> slots
            }
            // 各スロットに対して移動を行う
            for (i: Int in fixedRange) {
                val slot: Slot = getSlot(i)
                val stackIn: ItemStack = slot.item
                // 現在のstackとスロット内のstackInが同じ種類の場合
                if (!stackIn.isEmpty && ItemStack.isSameItemSameComponents(stack, stackIn)) {
                    val sumCount: Int = stackIn.count + stack.count
                    val maxCount: Int = slot.getMaxStackSize(stackIn)
                    // 個数の合計値がスロットの上限以下の場合，スロット内の個数を変えて現在のstackを無効化
                    if (sumCount <= maxCount) {
                        stack.count = 0
                        stackIn.count = sumCount
                        slot.setChanged()
                        flag = true
                    } else {
                        // スロット内の個数がスロットの上限未満の場合，スロット内の個数を最大にして現在のstackを減らす
                        if (stackIn.count < maxCount) {
                            stack.shrink(maxCount - stackIn.count)
                            stackIn.count = maxCount
                            slot.setChanged()
                            flag = true
                        }
                    }
                }
                // 現在のスタックが空になったら即座に抜ける
                if (stack.isEmpty) break
            }
            // 上の処理で現在のstackが空にならなかった場合
            if (!stack.isEmpty) {
                // 再び各スロットに対して移動を行う
                for (i: Int in fixedRange) {
                    val slot: Slot = getSlot(i)
                    val stackIn: ItemStack = slot.item
                    // スロットが空で現在のstackを配置可能な場合，スロットに入るだけ現在のstackを入れる
                    if (stackIn.isEmpty && slot.mayPlace(stack)) {
                        val maxCount: Int = slot.getMaxStackSize(stack)
                        slot.setByPlayer(stack.split(min(stack.count, maxCount)))
                        slot.setChanged()
                        flag = true
                        break
                    }
                }
            }
            // 入りきらなかったstackは残る
        }
        // 移動処理が一つでも行えればtrue
        return flag
    }

    protected open fun onOpen(player: Player) {}

    protected open fun onClose(player: Player) {}
}
