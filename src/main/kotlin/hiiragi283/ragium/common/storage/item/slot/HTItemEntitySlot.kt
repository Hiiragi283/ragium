package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack

class HTItemEntitySlot(private val entity: ItemEntity) :
    HTItemSlot.Basic(),
    HTValueSerializable.Empty {
    override fun getStack(): ImmutableItemStack? = entity.item.toImmutable()

    override fun getCapacity(stack: ImmutableItemStack?): Int = HTItemSlot.getMaxStackSize(stack)

    override fun isValid(stack: ImmutableItemStack): Boolean = true

    override fun onContentsChanged() {
        if (this.getStack() == null) {
            entity.discard()
        }
    }

    override fun setStack(stack: ImmutableItemStack?) {
        entity.item = stack?.unwrap() ?: ItemStack.EMPTY
        onContentsChanged()
    }

    override fun updateAmount(stack: ImmutableItemStack, amount: Int) {
        entity.item.count = amount
    }
}
