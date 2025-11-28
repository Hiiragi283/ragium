package hiiragi283.ragium.common.inventory.slot

import hiiragi283.ragium.api.inventory.slot.HTChangeType
import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot
import hiiragi283.ragium.common.inventory.slot.payload.HTIntSyncPayload
import net.minecraft.core.RegistryAccess
import java.util.function.IntConsumer
import java.util.function.IntSupplier
import kotlin.reflect.KMutableProperty0

/**
 * @see mekanism.common.inventory.container.sync.SyncableInt
 */
interface HTIntSyncSlot : HTSyncableSlot {
    companion object {
        @JvmStatic
        fun create(array: IntArray, index: Int): HTIntSyncSlot = create({ array[index] }, { array[index] = it })

        @JvmStatic
        fun create(property: KMutableProperty0<Int>): HTIntSyncSlot = create(property::get, property::set)

        @JvmStatic
        fun create(getter: IntSupplier, setter: IntConsumer): HTIntSyncSlot = Impl(getter, setter)
    }

    fun getAmountAsInt(): Int

    fun setAmountAsInt(amount: Int)

    private class Impl(private val getter: IntSupplier, private val setter: IntConsumer) : HTIntSyncSlot {
        private var lastValue: Int = 0

        override fun getAmountAsInt(): Int = this.getter.asInt

        override fun setAmountAsInt(amount: Int) {
            this.setter.accept(amount)
        }

        override fun getChange(): HTChangeType {
            val current: Int = this.getAmountAsInt()
            val last: Int = this.lastValue
            this.lastValue = current
            return when (current == last) {
                true -> HTChangeType.EMPTY
                false -> HTChangeType.FULL
            }
        }

        override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTIntSyncPayload =
            HTIntSyncPayload(this.getAmountAsInt())
    }
}
