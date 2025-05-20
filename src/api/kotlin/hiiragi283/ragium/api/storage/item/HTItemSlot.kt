package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.enchantment.HTEnchantmentListener
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.HTSingleVariantStorage
import hiiragi283.ragium.api.storage.HTStackStorage
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

abstract class HTItemSlot :
    HTSingleVariantStorage<HTItemVariant>(),
    HTEnchantmentListener,
    HTNbtCodec,
    HTStackStorage<ItemStack> {
    fun canExtract(maxAmount: Int): Boolean = extract(resource, maxAmount, true) == maxAmount

    fun extract(maxAmount: Int, simulate: Boolean): Int = extract(resource, maxAmount, simulate)

    fun createContainerSlot(x: Int, y: Int): Slot = createContainerSlot(x, y, HTStorageIO.GENERIC)

    abstract fun createContainerSlot(x: Int, y: Int, storageIO: HTStorageIO): Slot

    fun dropStack(entity: Entity) {
        useStack { stack: ItemStack ->
            dropStackAt(entity, stack)
            ItemStack.EMPTY
        }
    }

    fun dropStack(level: Level, pos: BlockPos) {
        useStack { stack: ItemStack ->
            dropStackAt(level, pos, stack)
            ItemStack.EMPTY
        }
    }

    //    HTSingleVariantStorage    //

    final override fun getEmptyVariant(): HTItemVariant = HTItemVariant.EMPTY

    //    HTStackStorage    //

    final override val stack: ItemStack get() = resource.toStack(amount)

    final override fun replace(stack: ItemStack, shouldUpdate: Boolean) {
        if (stack.isEmpty) {
            clear()
            return
        }
        resource = HTItemVariant.of(stack)
        amount = stack.count
        if (shouldUpdate) {
            onContentsChanged()
        }
    }

    final override fun canInsert(stack: ItemStack): Boolean = insert(stack, true) == stack.count

    override fun insert(stack: ItemStack, simulate: Boolean): Int {
        val inserted: Int = insert(HTItemVariant.of(stack), stack.count, simulate)
        if (!simulate) {
            stack.count -= inserted
        }
        return inserted
    }

    //    Builder    //

    companion object {
        @JvmStatic
        fun create(nbtKey: String, builderAction: Builder.() -> Unit = {}): HTItemSlot = Builder().apply(builderAction).build(nbtKey)

        @JvmStatic
        fun create(nbtKey: String, blockEntity: BlockEntity, builderAction: Builder.() -> Unit = {}): HTItemSlot = Builder()
            .apply {
                callback = blockEntity::setChanged
                builderAction()
            }.build(nbtKey)
    }

    class Builder internal constructor() {
        var capacity: Int = Item.ABSOLUTE_MAX_STACK_SIZE
        var validator: (HTItemVariant) -> Boolean = { true }
        var callback: () -> Unit = {}

        fun build(nbtKey: String): HTItemSlot = RagiumAPI.getInstance().buildItemSlot(
            nbtKey,
            capacity,
            validator,
            callback,
        )
    }
}
