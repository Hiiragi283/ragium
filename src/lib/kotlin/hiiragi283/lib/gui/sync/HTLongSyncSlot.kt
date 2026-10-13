package hiiragi283.lib.gui.sync

import net.minecraft.core.RegistryAccess
import java.util.function.LongConsumer
import java.util.function.LongSupplier
import kotlin.reflect.KMutableProperty0

/**
 * [Long]向けの[HTSyncableSlot]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - SyncableLong](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/inventory/container/sync/SyncableLong.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
interface HTLongSyncSlot : HTSyncableSlot {
    companion object {
        @JvmStatic
        fun create(array: LongArray, index: Int): HTLongSyncSlot = create({ array[index] }, { array[index] = it })

        @JvmStatic
        fun create(list: MutableList<Long>, index: Int): HTLongSyncSlot = create({ list[index] }, { list[index] = it })

        @JvmStatic
        fun create(property: KMutableProperty0<Long>): HTLongSyncSlot = create(property::get, property::set)

        @JvmStatic
        fun create(getter: LongSupplier, setter: LongConsumer): HTLongSyncSlot = Impl(getter, setter)
    }

    var amountAsLong: Long

    private class Impl(private val getter: LongSupplier, private val setter: LongConsumer) : HTLongSyncSlot {
        private var lastValue: Long = 0

        override var amountAsLong: Long
            get() = this.getter.asLong
            set(value) {
                this.setter.accept(value)
            }

        override fun getChange(): HTChangeType? {
            val current: Long = this.amountAsLong
            val last: Long = this.lastValue
            this.lastValue = current
            return when (current == last) {
                true -> null
                false -> HTChangeType.FULL
            }
        }

        override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTLongSyncPayload =
            HTLongSyncPayload(this.amountAsLong)

        override fun toString(): String = "HTLongSyncSlot(amount=$amountAsLong)"
    }
}
