package hiiragi283.ragium.api.inventory

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

open class HTSimpleInventory : Inventory {
    companion object {
        @JvmField
        val CODEC: Codec<HTSimpleInventory> =
            ItemStack.OPTIONAL_CODEC.listOf().xmap(::HTSimpleInventory, HTSimpleInventory::stacks)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTSimpleInventory> = PacketCodec.tuple(
            ItemStack.LIST_PACKET_CODEC,
            HTSimpleInventory::stacks,
            ::HTSimpleInventory,
        )
    }

    protected val stacks: DefaultedList<ItemStack>
    protected val slotFilter: (Int, ItemStack) -> Boolean

    constructor(size: Int, filter: (Int, ItemStack) -> Boolean = HTSidedStorageBuilder.ACCEPT_ALL) {
        stacks = DefaultedList.ofSize(size, ItemStack.EMPTY)
        slotFilter = filter
    }

    constructor(builder: HTSidedStorageBuilder) : this(builder.size, builder.slotFilter)

    constructor(stacks: List<ItemStack>) : this(*stacks.toTypedArray())

    constructor(vararg stacks: ItemStack) {
        this.stacks = DefaultedList.copyOf(ItemStack.EMPTY, *stacks)
        this.slotFilter = HTSidedStorageBuilder.ACCEPT_ALL
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

    fun sendS2CPacket(player: ServerPlayerEntity, pos: BlockPos) {
        stacks.forEachIndexed { slot: Int, stack: ItemStack ->
            RagiumNetworks.sendItemSync(player, pos, slot, stack)
        }
    }

    //    Inventory    //

    override fun clear() {
        stacks.clear()
    }

    override fun size(): Int = stacks.size

    override fun isEmpty(): Boolean = stacks.isEmpty() || stacks.all(ItemStack::isEmpty)

    override fun getStack(slot: Int): ItemStack = stacks.getOrNull(slot) ?: ItemStack.EMPTY

    override fun removeStack(slot: Int, amount: Int): ItemStack = Inventories.splitStack(stacks, slot, amount)

    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(stacks, slot)

    override fun setStack(slot: Int, stack: ItemStack) {
        stacks[slot] = stack
    }

    override fun markDirty() = Unit

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
