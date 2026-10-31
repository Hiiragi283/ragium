package hiiragi283.ragium.common.gui.menu

import com.google.common.collect.Iterables
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.sync.HTChangeType
import hiiragi283.lib.gui.sync.HTSyncType
import hiiragi283.lib.gui.sync.HTSyncableMenu
import hiiragi283.lib.gui.sync.HTSyncablePayload
import hiiragi283.lib.gui.sync.HTSyncableSlot
import hiiragi283.lib.transfer.item.HTContainerItemSlot
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.toResourcePair
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.slot.HTItemContainerSlot
import hiiragi283.ragium.common.gui.slot.HTTransactionSlot
import hiiragi283.ragium.common.network.HTUpdateMenuPacket
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * Hiiragi Seriesで使用される[AbstractContainerMenu]の拡張クラスです。
 *
 * 参照 : [Mekanism - MekanismContainer](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/inventory/container/MekanismContainer.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTContainerMenu<C>(menuType: MenuType<*>, containerId: Int, val inventory: Inventory, val context: C) :
    AbstractContainerMenu(menuType, containerId),
    HTSyncableMenu {

    private val containerSlots: MutableList<HTItemContainerSlot> = ObjectArrayList()
    private val hotBarSlots: MutableList<HTItemContainerSlot> = ObjectArrayList()
    private val inventorySlots: MutableList<HTItemContainerSlot> = ObjectArrayList()

    override fun addSlot(slot: Slot): Slot {
        if (slot is HTItemContainerSlot) {
            if (isHotBar(slot)) {
                hotBarSlots += slot
            } else if (isInventory(slot)) {
                inventorySlots += slot
            } else {
                containerSlots += slot
            }
        }
        return super.addSlot(slot)
    }

    private fun isInventory(slot: Slot, indices: IntRange): Boolean {
        if (slot is HTItemContainerSlot) {
            val slotIn: HTItemSlot = slot.slot
            if (slotIn is HTContainerItemSlot) {
                val (containerIn: Container, indexIn: Int) = slotIn
                return containerIn is Inventory && indexIn in indices
            }
        }
        return false
    }

    private fun isHotBar(slot: Slot): Boolean = isInventory(slot, 0 until Inventory.SELECTION_SIZE)

    private fun isInventory(slot: Slot): Boolean = isInventory(slot, Inventory.SELECTION_SIZE until Inventory.INVENTORY_SIZE)

    protected fun addPlayerInv(inventory: Inventory, yOffset: Int) {
        // hotbar
        for (index: Int in 0..8) {
            addSlot(
                HTItemContainerSlot.create(
                    HTContainerItemSlot(inventory, index),
                    HTSlotHelper.getSlotPosX(index),
                    161 + yOffset,
                ),
            )
        }
        // inventory
        for (index: Int in 0..26) {
            addSlot(
                HTItemContainerSlot.create(
                    HTContainerItemSlot(inventory, index + 9),
                    HTSlotHelper.getSlotPosX(index % 9),
                    103 + (index / 9) * 18 + yOffset,
                ),
            )
        }
    }

    //    AbstractContainerMenu    //

    final override fun quickMoveStack(player: Player, index: Int): ItemStack {
        @Suppress("RedundantNullableReturnType")
        val currentSlot: Slot? = getSlot(index)
        if (currentSlot == null || currentSlot.item.isEmpty) return ItemStack.EMPTY
        var inserted: Int
        useTransaction { transaction: Transaction ->
            val stackInSlot: ItemStack = currentSlot.item
            val (itemToInsert: ItemResource, countToInsert: Int) = stackInSlot.toResourcePair()
            if (currentSlot in containerSlots) {
                inserted = insertItem(
                    Iterables.concat(hotBarSlots, inventorySlots),
                    itemToInsert,
                    countToInsert,
                    transaction,
                )
            } else {
                inserted = insertItem(containerSlots, itemToInsert, countToInsert, transaction, true)
                if (inserted == 0) {
                    inserted = insertItem(containerSlots, itemToInsert, countToInsert, transaction, false)
                    if (inserted == 0) {
                        if (isInventory(currentSlot)) {
                            inserted = insertItem(hotBarSlots, itemToInsert, countToInsert, transaction)
                        } else if (isHotBar(currentSlot)) {
                            inserted = insertItem(inventorySlots, itemToInsert, countToInsert, transaction)
                        }
                    }
                }
            }
            if (inserted == 0) {
                return ItemStack.EMPTY
            }
            transaction.commit()
            val newStack: ItemStack = currentSlot.remove(inserted)
            currentSlot.onTake(player, newStack)
            return newStack
        }
    }

    companion object {
        @JvmStatic
        fun insertItem(slots: Iterable<HTItemContainerSlot>, item: ItemResource, amount: Int, transaction: TransactionContext): Int {
            var inserted = 0
            inserted += insertItem(slots, item, amount, transaction, true)
            inserted += insertItem(slots, item, amount - inserted, transaction, false)
            return inserted
        }

        @JvmStatic
        fun insertItem(
            slots: Iterable<HTItemContainerSlot>,
            item: ItemResource,
            amount: Int,
            transaction: TransactionContext,
            ignoreEmpty: Boolean,
        ): Int {
            var inserted = 0
            if (!item.isEmpty && amount > 0) {
                for (slot: HTItemContainerSlot in slots) {
                    if (ignoreEmpty != slot.hasItem()) continue
                    inserted += slot.insert(item, amount - inserted, transaction)
                    if (inserted == amount) break
                }
            }
            return inserted
        }
    }

    override fun canTakeItemForPickAll(carried: ItemStack, target: Slot): Boolean = when (target) {
        is HTTransactionSlot if !target.canMergeWith(carried) -> false
        else -> super.canTakeItemForPickAll(carried, target)
    }

    //    Slot Sync    //

    val trackedSlots: List<Pair<HTSyncableSlot, HTSyncType>>
        field: MutableList<Pair<HTSyncableSlot, HTSyncType>> = ObjectArrayList()

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
