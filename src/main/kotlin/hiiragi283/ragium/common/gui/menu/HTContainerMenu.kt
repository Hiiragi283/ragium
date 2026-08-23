package hiiragi283.ragium.common.gui.menu

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.sync.HTChangeType
import hiiragi283.lib.gui.sync.HTSyncType
import hiiragi283.lib.gui.sync.HTSyncableMenu
import hiiragi283.lib.gui.sync.HTSyncablePayload
import hiiragi283.lib.gui.sync.HTSyncableSlot
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.HTContainerItemSlot
import hiiragi283.ragium.common.network.HTUpdateMenuPacket
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.world.inventory.StackCopySlot

/**
 * Hiiragi Seriesで使用される[AbstractContainerMenu]の拡張クラスです。
 *
 * 参照 : [Mekanism - MekanismContainer](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/inventory/container/MekanismContainer.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTContainerMenu<C>(
    menuType: MenuType<*>,
    containerId: Int,
    val inventory: Inventory,
    val context: C,
) : AbstractContainerMenu(menuType, containerId),
    HTSyncableMenu {
    final override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var result: ItemStack = ItemStack.EMPTY
        val slotIn: Slot = slots.getOrNull(index) ?: return result
        if (slotIn.hasItem()) {
            val stackIn: ItemStack = slotIn.item
            result = stackIn.copy()
            when (index) {
                in widgetSlots -> {
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
        if (!moveItemStackTo(stack, inputSlots, false)) {
            if (!moveItemStackTo(stack, inventorySlots, false)) {
                return false
            }
        }
        return true
    }

    //    Extensions    //

    private var slotCount: Int = 0
    private val widgetSlots: MutableList<Int> = mutableListOf()
    private val inputSlots: MutableList<Int> = mutableListOf()
    private val hotBarSlots: MutableList<Int> = mutableListOf()
    private val inventorySlots: MutableList<Int> = mutableListOf()

    override fun addSlot(slot: Slot): Slot {
        if (slot is HTContainerItemSlot) {
            widgetSlots += slotCount
            val slotType: HTBackgroundType = slot.slotType
            if (slotType.isInput || !slotType.isOutput) {
                inputSlots += slotCount
            }
            RagiumAPI.LOGGER.info("${slot.slotType} slot: $slotCount")
        }
        slotCount++
        return super.addSlot(slot)
    }

    protected fun addPlayerInv(inventory: Inventory, yOffset: Int) {
        // hotbar
        for (index: Int in 0..8) {
            hotBarSlots.add(slotCount)
            RagiumAPI.LOGGER.debug("Hotbar slot: $slotCount")
            addSlot(Slot(inventory, index, HTSlotHelper.getSlotPosX(index), 161 + yOffset))
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
                    103 + (index / 9) * 18 + yOffset,
                ),
            )
        }
    }

    /**
     * @see AbstractContainerMenu.moveItemStackTo
     */
    protected fun moveItemStackTo(stack: ItemStack, slots: Iterable<Int>, reverseDirection: Boolean): Boolean {
        var flag = false
        // スロットの順番を反転
        val fixedRange: Iterable<Int> = when (reverseDirection) {
            true -> slots.reversed()
            false -> slots
        }
        if (stack.isStackable) {
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
                        updateCount(slot, sumCount)
                        slot.setChanged()
                        flag = true
                    } else {
                        // スロット内の個数がスロットの上限未満の場合，スロット内の個数を最大にして現在のstackを減らす
                        if (stackIn.count < maxCount) {
                            stack.shrink(maxCount - stackIn.count)
                            updateCount(slot, maxCount)
                            slot.setChanged()
                            flag = true
                        }
                    }
                }
                // 現在のスタックが空になったら即座に抜ける
                if (stack.isEmpty) break
            }
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
                    slot.setByPlayer(stack.split(minOf(stack.count, maxCount)))
                    slot.setChanged()
                    flag = true
                    break
                }
            }
        }
        // 入りきらなかったstackは残る
        // 移動処理が一つでも行えればtrue
        return flag
    }

    private fun updateCount(slot: Slot, count: Int) {
        if (slot is StackCopySlot) {
            slot.set(slot.item.copyWithCount(count))
        } else {
            slot.item.count = count
        }
    }

    //    Slot Sync    //

    val trackedSlots: List<Pair<HTSyncableSlot, HTSyncType>> field: MutableList<Pair<HTSyncableSlot, HTSyncType>> = mutableListOf()

    fun addTrackedSlot(slot: HTSyncableSlot, type: HTSyncType) {
        trackedSlots += slot to type
    }

    override fun getTrackedSlot(index: Int): HTSyncableSlot? = trackedSlots.getOrNull(index)?.first

    /**
     * 参照 : [Mekanism - MekanismContainer.broadcastChanges]
     */
    override fun broadcastChanges() {
        super.broadcastChanges()
        val player: Player = inventory.player
        val access: RegistryAccess = player.registryAccess()
        if (player is ServerPlayer) {
            HTUpdateMenuPacket.create(containerId) {
                for (i: Int in trackedSlots.indices) {
                    val (slot: HTSyncableSlot, syncType: HTSyncType) = trackedSlots[i]
                    if (!syncType.allowS2C) continue
                    val changeType: HTChangeType = slot.getChange() ?: continue
                    val payload: HTSyncablePayload = slot.createPayload(access, changeType) ?: continue
                    this[i] = payload
                    RagiumAPI.LOGGER.debug("Added sync value index: {}, payload: {}", i, payload)
                }
            }?.let { PacketDistributor.sendToPlayer(player, it) }
        }
    }

    /**
     * 参照 : [Mekanism - MekanismContainer.sendAllDataToRemote]
     */
    override fun sendAllDataToRemote() {
        super.sendAllDataToRemote()
        val player: Player = inventory.player
        val access: RegistryAccess = player.registryAccess()
        if (player is ServerPlayer) {
            HTUpdateMenuPacket.create(containerId) {
                for (i: Int in trackedSlots.indices) {
                    val (slot: HTSyncableSlot, _) = trackedSlots[i]
                    slot.getChange()
                    val payload: HTSyncablePayload = slot.createPayload(access, HTChangeType.FULL) ?: continue
                    this[i] = payload
                    RagiumAPI.LOGGER.debug("Force sync value index: {}, payload: {}", i, payload)
                }
            }?.let { PacketDistributor.sendToPlayer(player, it) }
        }
    }
}
