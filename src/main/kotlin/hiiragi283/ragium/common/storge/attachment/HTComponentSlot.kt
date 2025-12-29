package hiiragi283.ragium.common.storge.attachment

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.attachments.HTAttachedContainers
import hiiragi283.core.api.storage.resource.HTResourceSlot
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.common.storage.HTCapabilityCodec
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

abstract class HTComponentSlot<RESOURCE : HTResourceType<*>, STACK : Any, ATTACHED : HTAttachedContainers<STACK, ATTACHED>>(
    protected val attachedTo: ItemStack,
    private val size: Int,
    protected val slot: Int,
    protected val capacity: Int,
    private val canExtract: BiPredicate<RESOURCE, HTStorageAccess>,
    private val canInsert: BiPredicate<RESOURCE, HTStorageAccess>,
    private val filter: Predicate<RESOURCE>,
) : HTResourceSlot.Basic<RESOURCE>(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    protected fun getAttached(): ATTACHED = capabilityCodec().getOrCreate(attachedTo, size)

    protected abstract fun capabilityCodec(): HTCapabilityCodec<*, ATTACHED>

    protected abstract fun createResource(stack: STACK): RESOURCE?

    protected abstract fun createStack(resource: RESOURCE?, amount: Int): STACK

    protected abstract fun getAmount(stack: STACK): Int

    override fun setResource(resource: RESOURCE?) {
        capabilityCodec().updateAttached(attachedTo, getAttached().with(slot, createStack(resource, getAmount())))
    }

    override fun setAmount(amount: Int) {
        capabilityCodec().updateAttached(attachedTo, getAttached().with(slot, createStack(getResource(), amount)))
    }

    final override fun isValid(resource: RESOURCE): Boolean = this.filter.test(resource)

    final override fun isStackValidForInsert(resource: RESOURCE, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(resource, access) && this.canInsert.test(resource, access)

    final override fun canStackExtract(resource: RESOURCE, access: HTStorageAccess): Boolean =
        super.canStackExtract(resource, access) && this.canExtract.test(resource, access)

    override fun getResource(): RESOURCE? = getAttached().getOrNull(slot)?.let(::createResource)

    override fun getCapacity(resource: RESOURCE?): Int = capacity

    override fun getAmount(): Int = getAttached().getOrNull(slot)?.let(::getAmount) ?: 0
}
