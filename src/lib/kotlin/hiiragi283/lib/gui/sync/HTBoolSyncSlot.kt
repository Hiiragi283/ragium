package hiiragi283.lib.gui.sync

import net.minecraft.core.RegistryAccess
import org.apache.commons.lang3.function.BooleanConsumer
import java.util.function.BooleanSupplier
import kotlin.reflect.KMutableProperty0

/**
 * [Boolean]向けの[HTSyncableSlot]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - SyncableBoolean](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/inventory/container/sync/SyncableBoolean.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTBoolSyncSlot : HTSyncableSlot {
    companion object {
        @JvmStatic
        fun create(array: BooleanArray, index: Int): HTBoolSyncSlot = create({ array[index] }, { array[index] = it })

        @JvmStatic
        fun create(list: MutableList<Boolean>, index: Int): HTBoolSyncSlot = create({ list[index] }, { list[index] = it })

        @JvmStatic
        fun create(property: KMutableProperty0<Boolean>): HTBoolSyncSlot = create(property::get, property::set)

        @JvmStatic
        fun create(getter: BooleanSupplier, setter: BooleanConsumer): HTBoolSyncSlot = Impl(getter, setter)
    }

    var amountAsBool: Boolean

    private class Impl(private val getter: BooleanSupplier, private val setter: BooleanConsumer) : HTBoolSyncSlot {
        private var lastValue: Boolean = false

        override var amountAsBool: Boolean
            get() = this.getter.asBoolean
            set(value) {
                this.setter.accept(value)
            }

        override fun getChange(): HTChangeType? {
            val current: Boolean = this.amountAsBool
            val last: Boolean = this.lastValue
            this.lastValue = current
            return when (current == last) {
                true -> null
                false -> HTChangeType.FULL
            }
        }

        override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTBoolSyncPayload = HTBoolSyncPayload(this.amountAsBool)

        override fun toString(): String = "HTBoolSyncSlot(amount=$amountAsBool)"
    }
}
