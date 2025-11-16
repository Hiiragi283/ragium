package hiiragi283.ragium.common.inventory.slot

import hiiragi283.ragium.api.inventory.slot.HTChangeType
import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.common.inventory.slot.payload.HTIntSyncPayload
import net.minecraft.core.RegistryAccess
import java.util.function.IntConsumer
import java.util.function.IntSupplier
import kotlin.reflect.KMutableProperty0

/**
 * @see mekanism.common.inventory.container.sync.SyncableInt
 */
class HTIntSyncSlot(private val getter: IntSupplier, private val setter: IntConsumer) : HTSyncableSlot {
    constructor(property: KMutableProperty0<Int>) : this(property::get, property::set)

    private var lastValue: Int = 0

    fun getAmount(): Int = this.getter.asInt

    fun setAmount(amount: Int) {
        this.setter.accept(amount)
    }

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
}
