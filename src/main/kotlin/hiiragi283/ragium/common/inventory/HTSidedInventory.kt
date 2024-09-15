package hiiragi283.ragium.common.inventory

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.Direction

class HTSidedInventory(private val ioTypes: Array<HTStorageIO>, private val slotsMapper: (Direction) -> IntArray) : SidedInventory {
    private val stacks: DefaultedList<ItemStack> = DefaultedList.ofSize(ioTypes.size, ItemStack.EMPTY)

    fun writeNbt(nbt: NbtCompound, lookup: RegistryWrapper.WrapperLookup) {
        Inventories.writeNbt(nbt, stacks, lookup)
    }

    fun readNbt(nbt: NbtCompound, lookup: RegistryWrapper.WrapperLookup) {
        Inventories.readNbt(nbt, stacks, lookup)
    }

    fun modifyStack(slot: Int, mapping: (ItemStack) -> ItemStack) {
        val stackIn: ItemStack = getStack(slot)
        setStack(slot, mapping(stackIn))
    }

    //    SidedInventory    //

    override fun clear() {
        stacks.clear()
    }

    override fun size(): Int = stacks.size

    override fun isEmpty(): Boolean = stacks.isEmpty()

    override fun getStack(slot: Int): ItemStack = stacks.getOrNull(slot) ?: ItemStack.EMPTY

    override fun removeStack(slot: Int, amount: Int): ItemStack = Inventories.splitStack(stacks, slot, amount)

    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(stacks, slot)

    override fun setStack(slot: Int, stack: ItemStack) {
        stacks[slot] = stack
    }

    override fun markDirty() = Unit

    override fun canPlayerUse(player: PlayerEntity?): Boolean = true

    override fun getAvailableSlots(side: Direction): IntArray = slotsMapper(side)

    override fun canInsert(slot: Int, stack: ItemStack, dir: Direction?): Boolean =
        dir?.let { ioTypes[slot].canInsert && slot in slotsMapper(it) } ?: false

    override fun canExtract(slot: Int, stack: ItemStack, dir: Direction): Boolean = ioTypes[slot].canExtract && slot in slotsMapper(dir)
}
