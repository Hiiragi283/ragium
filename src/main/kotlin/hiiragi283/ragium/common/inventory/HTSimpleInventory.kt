package hiiragi283.ragium.common.inventory

import com.mojang.serialization.Codec
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.collection.DefaultedList

open class HTSimpleInventory : Inventory {
    companion object {
        @JvmField
        val CODEC: Codec<HTSimpleInventory> =
            ItemStack.VALIDATED_CODEC.listOf().xmap(::HTSimpleInventory, HTSimpleInventory::stacks)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTSimpleInventory> = PacketCodec.tuple(
            ItemStack.LIST_PACKET_CODEC,
            HTSimpleInventory::stacks,
            ::HTSimpleInventory,
        )
    }

    protected val stacks: DefaultedList<ItemStack>

    constructor(size: Int) {
        stacks = DefaultedList.ofSize(size, ItemStack.EMPTY)
    }

    constructor(stacks: List<ItemStack>) : this(*stacks.toTypedArray())

    constructor(vararg stacks: ItemStack) {
        this.stacks = DefaultedList.copyOf(ItemStack.EMPTY, *stacks)
    }

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

    //    Inventory    //

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
}
