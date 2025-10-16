package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.entity.item.ItemEntity
import kotlin.math.min

class HTItemEntitySlot(private val entity: ItemEntity) :
    HTItemSlot.Mutable(),
    HTValueSerializable.Empty {
    override fun getStack(): ImmutableItemStack = entity.item.toImmutable()

    override fun getCapacityAsLong(stack: ImmutableItemStack): Long {
        val limit: Long = RagiumConst.ABSOLUTE_MAX_STACK_SIZE
        return if (stack.isEmpty()) limit else min(limit, stack.maxStackSize().toLong())
    }

    override fun isValid(stack: ImmutableItemStack): Boolean = true

    override fun onContentsChanged() {
        if (this.isEmpty()) {
            entity.discard()
        }
    }

    override fun setStack(stack: ImmutableItemStack) {
        entity.item = stack.stack
    }
}
