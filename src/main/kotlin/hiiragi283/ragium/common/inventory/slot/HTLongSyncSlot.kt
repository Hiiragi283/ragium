package hiiragi283.ragium.common.inventory.slot

import hiiragi283.ragium.api.inventory.slot.HTChangeType
import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.common.inventory.slot.payload.HTLongSyncPayload
import net.minecraft.core.RegistryAccess
import java.util.function.LongConsumer
import java.util.function.LongSupplier

/**
 * @see mekanism.common.inventory.container.sync.SyncableLong
 */
class HTLongSyncSlot(private val getter: LongSupplier, private val setter: LongConsumer) : HTSyncableSlot {
    private var lastValue: Long = 0

    fun getAmount(): Long = this.getter.asLong

    fun setAmount(amount: Long) {
        this.setter.accept(amount)
    }

    override fun getChange(): HTChangeType {
        val current: Long = this.getAmount()
        val last: Long = this.lastValue
        this.lastValue = current
        return when (current == last) {
            true -> HTChangeType.EMPTY
            false -> HTChangeType.FULL
        }
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload = HTLongSyncPayload(this.getAmount())
}
