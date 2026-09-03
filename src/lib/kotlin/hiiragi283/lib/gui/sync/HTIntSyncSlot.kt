package hiiragi283.lib.gui.sync

import net.minecraft.core.RegistryAccess
import java.util.function.IntConsumer
import java.util.function.IntSupplier
import kotlin.reflect.KMutableProperty0

/**
 * [Int]向けの[HTSyncableSlot]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - SyncableInt](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/inventory/container/sync/SyncableInt.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTIntSyncSlot : HTSyncableSlot {
    companion object {
        @JvmStatic
        fun create(array: IntArray, index: Int): HTIntSyncSlot = create({ array[index] }, { array[index] = it })

        @JvmStatic
        fun create(list: MutableList<Int>, index: Int): HTIntSyncSlot = create({ list[index] }, { list[index] = it })

        @JvmStatic
        fun create(property: KMutableProperty0<Int>): HTIntSyncSlot = create(property::get, property::set)

        @JvmStatic
        fun create(getter: IntSupplier, setter: IntConsumer): HTIntSyncSlot = Impl(getter, setter)
    }

    var amountAsInt: Int

    private class Impl(private val getter: IntSupplier, private val setter: IntConsumer) : HTIntSyncSlot {
        private var lastValue: Int = 0

        override var amountAsInt: Int
            get() = this.getter.asInt
            set(value) {
                this.setter.accept(value)
            }

        override fun getChange(): HTChangeType? {
            val current: Int = this.amountAsInt
            val last: Int = this.lastValue
            this.lastValue = current
            return when (current == last) {
                true -> null
                false -> HTChangeType.FULL
            }
        }

        override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTIntSyncPayload =
            HTIntSyncPayload(this.amountAsInt)

        override fun toString(): String = "HTIntSyncSlot(amount=$amountAsInt)"
    }
}
