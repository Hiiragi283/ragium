package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.function.andThen
import hiiragi283.ragium.api.item.component.HTItemContents
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.common.attachments.containers.item.ComponentBackedInventorySlot
 */
open class HTComponentItemSlot(
    private val parent: ItemStack,
    private val slot: Int,
    private val canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    private val canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    private val filter: Predicate<ImmutableItemStack>,
) : HTItemSlot.Basic(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    companion object {
        @JvmStatic
        fun create(
            parent: ItemStack,
            slot: Int,
            canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            filter: Predicate<ImmutableItemStack> = Predicate(ImmutableItemStack::stack.andThen(ItemStack::canFitInsideContainerItems)),
        ): HTComponentItemSlot = HTComponentItemSlot(parent, slot, canExtract, canInsert, filter)
    }

    protected val component: DataComponentType<HTItemContents> get() = RagiumDataComponents.ITEM_CONTENT

    protected fun getContents(): HTItemContents? = parent.get(component)

    final override fun isValid(stack: ImmutableItemStack): Boolean = this.filter.test(stack)

    final override fun isStackValidForInsert(stack: ImmutableItemStack, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(stack, access) && this.canInsert.test(stack, access)

    final override fun canStackExtract(stack: ImmutableItemStack, access: HTStorageAccess): Boolean =
        super.canStackExtract(stack, access) && this.canExtract.test(stack, access)

    final override fun getStack(): ImmutableItemStack? = getContents()?.getOrNull(slot)

    override fun getCapacity(stack: ImmutableItemStack?): Int = RagiumConst.ABSOLUTE_MAX_STACK_SIZE

    final override fun setStack(stack: ImmutableItemStack?) {
        val contents: HTItemContents? = getContents()?.copy()
        if (contents == null || contents.isEmpty()) {
            parent.remove(component)
        } else {
            parent.set(component, contents)
        }
    }
}
