package hiiragi283.ragium.common.storage.attachments

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTStackSlot
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.attachments.HTAttachedContainers
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

abstract class HTComponentSlot<STACK : ImmutableStack<*, STACK>, ATTACHED : HTAttachedContainers<STACK?, ATTACHED>>(
    protected val attachedTo: ItemStack,
    private val size: Int,
    protected val slot: Int,
    protected val capacity: Int,
    private val canExtract: BiPredicate<STACK, HTStorageAccess>,
    private val canInsert: BiPredicate<STACK, HTStorageAccess>,
    private val filter: Predicate<STACK>,
) : HTStackSlot.Basic<STACK>(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    protected fun getAttached(): ATTACHED = capabilityCodec().getOrCreate(attachedTo, size)

    protected abstract fun capabilityCodec(): HTCapabilityCodec<*, ATTACHED>

    final override fun isValid(stack: STACK): Boolean = this.filter.test(stack)

    final override fun isStackValidForInsert(stack: STACK, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(stack, access) && this.canInsert.test(stack, access)

    final override fun canStackExtract(stack: STACK, access: HTStorageAccess): Boolean =
        super.canStackExtract(stack, access) && this.canExtract.test(stack, access)

    override fun getStack(): STACK? = getAttached().getOrNull(slot)

    override fun getCapacity(stack: STACK?): Int = capacity

    final override fun setStack(stack: STACK?) {
        capabilityCodec().updateAttached(attachedTo, getAttached().with(slot, stack))
    }

    override fun updateAmount(stack: STACK, amount: Int) {
        setStack(stack.copyWithAmount(amount))
    }
}
