package hiiragi283.ragium.common.storge.energy

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.attachments.HTAttachedEnergy
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storge.attachment.HTComponentHandler
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

/**
 * @see mekanism.common.attachments.containers.energy.ComponentBackedEnergyContainer
 */
open class HTComponentEnergyBattery(
    private val attachedTo: ItemStack,
    private val size: Int,
    private val slot: Int,
    private val capacity: Int,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
) : HTEnergyBattery.Basic(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    companion object {
        @JvmStatic
        fun create(
            context: HTComponentHandler.ContainerContext,
            capacity: Int,
            canExtract: Predicate<HTStorageAccess> = HTStoragePredicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = HTStoragePredicates.alwaysTrue(),
        ): HTComponentEnergyBattery =
            HTComponentEnergyBattery(context.attachedTo, context.size, context.index, capacity, canExtract, canInsert)
    }

    protected fun getAttached(): HTAttachedEnergy = HTCapabilityCodec.ENERGY.getOrCreate(attachedTo, size)

    override fun setAmount(amount: Int) {
        HTCapabilityCodec.ENERGY.updateAttached(attachedTo, getAttached().with(slot, amount))
    }

    final override fun canInsert(access: HTStorageAccess): Boolean = this.canInsert.test(access)

    final override fun canExtract(access: HTStorageAccess): Boolean = this.canExtract.test(access)

    override fun getAmount(): Int = getAttached().getOrNull(slot) ?: 0

    override fun getCapacity(): Int = capacity
}
