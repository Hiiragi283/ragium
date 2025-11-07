package hiiragi283.ragium.common.inventory.slot

import hiiragi283.ragium.api.inventory.slot.HTChangeType
import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.api.storage.HTAmountSetter
import hiiragi283.ragium.api.storage.HTAmountView
import hiiragi283.ragium.common.inventory.slot.payload.HTIntSyncPayload
import net.minecraft.core.RegistryAccess
import java.util.function.IntConsumer
import java.util.function.IntSupplier

/**
 * @see mekanism.common.inventory.container.sync.SyncableInt
 */
class HTIntSyncSlot(private val getter: IntSupplier, private val setter: IntConsumer) :
    HTSyncableSlot,
    HTAmountView.IntSized,
    HTAmountSetter.IntSized {
    private var lastValue: Int = 0

    override fun getChange(): HTChangeType {
        val current: Int = this.getAmount()
        val last: Int = this.lastValue
        this.lastValue = current
        return when (current == last) {
            true -> HTChangeType.EMPTY
            false -> HTChangeType.FULL
        }
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload = HTIntSyncPayload(this.getAmount())

    override fun getAmount(): Int = this.getter.asInt

    override fun getCapacity(): Int = Int.MAX_VALUE

    override fun setAmount(amount: Int) {
        this.setter.accept(amount)
    }
}
