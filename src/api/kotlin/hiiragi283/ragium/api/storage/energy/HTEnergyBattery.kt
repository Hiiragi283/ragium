package hiiragi283.ragium.api.storage.energy

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.util.Mth
import kotlin.math.min

interface HTEnergyBattery :
    HTValueSerializable,
    HTContentListener {
    fun getAmountAsLong(): Long

    fun getAmountAsInt(): Int = Ints.saturatedCast(getAmountAsLong())

    fun getCapacityAsLong(): Long

    fun getCapacityAsInt(): Int = Ints.saturatedCast(getCapacityAsLong())

    fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int

    fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int

    fun getNeededAsLong(): Long = getCapacityAsLong() - getAmountAsLong()

    fun getNeededAsInt(): Int = getCapacityAsInt() - getAmountAsInt()

    fun getStoredLevelAsDouble(): Double = getAmountAsLong() / getCapacityAsLong().toDouble()

    fun getStoredLevelAsFloat(): Float = getAmountAsLong() / getCapacityAsLong().toFloat()

    val isEmpty: Boolean get() = getAmountAsInt() <= 0

    //    Mutable    //

    interface Mutable : HTEnergyBattery {
        fun setAmountAsLong(amount: Long)

        fun setAmountAsInt(amount: Int) {
            setAmountAsLong(amount.toLong())
        }

        override fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (amount <= 0) return 0
            val received: Int = Mth.clamp(getNeededAsInt(), 0, amount)
            if (action.execute) {
                setAmountAsInt(getAmountAsInt() + received)
            }
            return received
        }

        override fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (amount <= 0) return 0
            val extracted: Int = min(getAmountAsInt(), amount)
            if (action.execute) {
                setAmountAsInt(getAmountAsInt() - extracted)
            }
            return extracted
        }

        fun setEmpty() {
            setAmountAsInt(0)
        }
    }
}
