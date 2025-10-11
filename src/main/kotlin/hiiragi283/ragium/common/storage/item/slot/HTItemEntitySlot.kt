package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import hiiragi283.ragium.api.storage.item.maxStackSize
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.inventory.Slot
import kotlin.math.min

class HTItemEntitySlot(private val entity: ItemEntity) :
    HTItemSlot.Mutable,
    HTValueSerializable.Empty {
    override fun createContainerSlot(): Slot? = null

    override fun getStack(): HTItemStorageStack = HTItemStorageStack.of(entity.item)

    override fun getCapacityAsLong(stack: HTItemStorageStack): Long {
        val limit: Long = HTItemSlot.ABSOLUTE_MAX_STACK_SIZE
        return if (stack.isEmpty()) limit else min(limit, stack.maxStackSize().toLong())
    }

    override fun isValid(stack: HTItemStorageStack): Boolean = true

    override fun onContentsChanged() {
        if (this.isEmpty()) {
            entity.discard()
        }
    }

    override fun setStack(stack: HTItemStorageStack) {
        entity.item = stack.stack
    }
}
