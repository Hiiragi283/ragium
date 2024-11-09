package hiiragi283.ragium.api.inventory

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.InventoryChangedListener
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

open class HTSimpleInventory : Inventory {
    private val stacks: DefaultedList<ItemStack>
    private val slotFilter: (Int, ItemStack) -> Boolean

    constructor(size: Int, filter: (Int, ItemStack) -> Boolean = HTStorageBuilder.ACCEPT_ALL) {
        stacks = DefaultedList.ofSize(size, ItemStack.EMPTY)
        slotFilter = filter
    }

    constructor(builder: HTStorageBuilder) : this(builder.size, builder.slotFilter)

    constructor(stacks: List<ItemStack>) : this(*stacks.toTypedArray())

    constructor(vararg stacks: ItemStack) {
        this.stacks = DefaultedList.copyOf(ItemStack.EMPTY, *stacks)
        this.slotFilter = HTStorageBuilder.ACCEPT_ALL
    }

    override fun toString(): String = "[${stacks.joinToString(separator = ", ", transform = ItemStack::toString)}]"

    //    Inventory    //

    override fun clear() {
        stacks.clear()
    }

    override fun size(): Int = stacks.size

    override fun isEmpty(): Boolean = stacks.isEmpty() || stacks.all(ItemStack::isEmpty)

    override fun getStack(slot: Int): ItemStack = stacks.getOrNull(slot) ?: ItemStack.EMPTY.copy()

    override fun removeStack(slot: Int, amount: Int): ItemStack = Inventories.splitStack(stacks, slot, amount)

    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(stacks, slot)

    override fun setStack(slot: Int, stack: ItemStack) {
        stacks[slot] = stack
    }

    private val listeners: MutableList<InventoryChangedListener> = mutableListOf()

    fun addListener(listener: InventoryChangedListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: InventoryChangedListener) {
        listeners.remove(listener)
    }

    override fun markDirty() {
        listeners.forEach { it.onInventoryChanged(this) }
    }

    override fun canPlayerUse(player: PlayerEntity?): Boolean = true

    override fun isValid(slot: Int, stack: ItemStack): Boolean = slotFilter(slot, stack)

    private var callback: (PlayerEntity) -> Unit = { }

    fun setCallback(action: (PlayerEntity) -> Unit) {
        callback = action
    }

    override fun onClose(player: PlayerEntity) {
        callback
    }
}
