package hiiragi283.ragium.api.storage.item

import com.google.common.util.concurrent.Runnables
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.constFunction2
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.storage.HTSingleVariantStorage
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.util.HTEnchantmentListener
import hiiragi283.ragium.api.util.HTNbtCodec
import net.minecraft.core.BlockPos
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class HTItemSlot(private val validator: (HTItemVariant) -> Boolean, private val callback: Runnable) :
    HTSingleVariantStorage<HTItemVariant>(),
    HTEnchantmentListener,
    HTNbtCodec {
    val stack: ItemStack get() = resource.toStack(amount)

    fun canInsert(stack: ItemStack): Boolean = insert(stack, true) > 0

    fun canExtract(maxAmount: Int): Boolean = extract(resource, maxAmount, true) > 0

    fun insert(stack: ItemStack, simulate: Boolean): Int = insert(HTItemVariant.of(stack), stack.count, simulate)

    fun extract(maxAmount: Int, simulate: Boolean): Int = extract(resource, maxAmount, simulate)

    fun createContainerSlot(x: Int, y: Int): Slot = createContainerSlot(x, y, HTStorageIO.GENERIC)

    abstract fun createContainerSlot(x: Int, y: Int, storageIO: HTStorageIO): Slot

    fun dropStack(entity: Entity) {
        dropStackAt(entity, stack)
        clear()
    }

    fun dropStack(level: Level, pos: BlockPos) {
        dropStackAt(level, pos, stack)
        clear()
    }

    //    HTSingleVariantStorage    //

    override fun getEmptyVariant(): HTItemVariant = HTItemVariant.EMPTY

    override fun isValid(variant: HTItemVariant): Boolean = validator(variant)

    override fun onContentsChanged() {
        callback.run()
    }

    //    Builder    //

    class Builder {
        private var capacity: Int = Item.ABSOLUTE_MAX_STACK_SIZE
        private var validator: (HTItemVariant) -> Boolean = constFunction2(true)
        private var callback: Runnable = Runnables.doNothing()

        fun setCapacity(capacity: Int): Builder = apply {
            this.capacity = capacity
        }

        fun setValidator(tagKey: TagKey<Item>): Builder = setValidator { variant: HTItemVariant -> variant.isIn(tagKey) }

        fun setValidator(validator: (HTItemVariant) -> Boolean): Builder = apply {
            this.validator = validator
        }

        fun setCallback(callback: Runnable): Builder = apply {
            this.callback = callback
        }

        fun build(nbtKey: String): HTItemSlot = RagiumAPI.getInstance().buildItemSlot(
            nbtKey,
            capacity,
            validator,
            callback,
        )
    }
}
