package hiiragi283.lib.gui.sync

import net.minecraft.core.RegistryAccess
import kotlin.reflect.KMutableProperty0

/**
 * [Float]向けの[HTSyncableSlot]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - SyncableFloat](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/inventory/container/sync/SyncableFloat.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.14
 */
interface HTFloatSyncSlot : HTSyncableSlot {
    companion object {
        @JvmStatic
        fun create(array: FloatArray, index: Int): HTFloatSyncSlot = create({ array[index] }, { array[index] = it })

        @JvmStatic
        fun create(list: MutableList<Float>, index: Int): HTFloatSyncSlot = create({ list[index] }, { list[index] = it })

        @JvmStatic
        fun create(property: KMutableProperty0<Float>): HTFloatSyncSlot = create(property::get, property::set)

        @JvmStatic
        fun create(getter: () -> Float, setter: (Float) -> Unit): HTFloatSyncSlot = Impl(getter, setter)
    }

    var amountAsFloat: Float

    private class Impl(private val getter: () -> Float, private val setter: (Float) -> Unit) : HTFloatSyncSlot {
        private var lastValue: Float = 0f

        override var amountAsFloat: Float
            get() = this.getter()
            set(value) {
                this.setter(value)
            }

        override fun getChange(): HTChangeType? {
            val current: Float = this.amountAsFloat
            val last: Float = this.lastValue
            this.lastValue = current
            return when (current == last) {
                true -> null
                false -> HTChangeType.FULL
            }
        }

        override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTFloatSyncPayload = HTFloatSyncPayload(this.amountAsFloat)

        override fun toString(): String = "HTFloatSyncSlot(amount=$amountAsFloat)"
    }
}
