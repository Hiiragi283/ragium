package hiiragi283.ragium.api.storage.energy

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import kotlin.math.min

/**
 * @see mekanism.api.energy.IEnergyContainer
 */
interface HTEnergyBattery :
    HTValueSerializable,
    HTContentListener {
    fun getAmountAsLong(): Long

    fun getAmountAsInt(): Int = Ints.saturatedCast(getAmountAsLong())

    fun getCapacityAsLong(): Long

    fun getCapacityAsInt(): Int = Ints.saturatedCast(getCapacityAsLong())

    fun insertEnergy(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long

    fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
        insertEnergy(amount.toLong(), action, access).let(Ints::saturatedCast)

    fun extractEnergy(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long

    fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
        extractEnergy(amount.toLong(), action, access).let(Ints::saturatedCast)

    fun getNeededAsLong(): Long = getCapacityAsLong() - getAmountAsLong()

    fun getNeededAsInt(): Int = getCapacityAsInt() - getAmountAsInt()

    fun getStoredLevelAsDouble(): Double = getAmountAsLong() / getCapacityAsLong().toDouble()

    fun getStoredLevelAsFloat(): Float = getAmountAsLong() / getCapacityAsLong().toFloat()

    fun isEmpty(): Boolean = getAmountAsInt() <= 0

    //    Mutable    //

    abstract class Mutable : HTEnergyBattery {
        abstract fun setAmountAsLong(amount: Long)

        fun setAmountAsInt(amount: Int) {
            setAmountAsLong(amount.toLong())
        }

        override fun insertEnergy(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long {
            if (amount <= 0 || !canInsert(access)) return 0
            val needed: Long = min(getInsertRate(access), getNeededAsLong())
            if (needed <= 0) return amount
            val toAdd: Long = min(amount, needed)
            if (action.execute) {
                setAmountAsLong(getAmountAsLong() + toAdd)
                onContentsChanged()
            }
            return amount - toAdd
        }

        override fun extractEnergy(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long {
            if (isEmpty() || amount <= 0 || !canExtract(access)) return 0
            val toRemove: Long = min(min(getExtractRate(access), getAmountAsLong()), amount)
            if (toRemove < 0 && action.execute) {
                setAmountAsLong(getAmountAsLong() - toRemove)
                onContentsChanged()
            }
            return toRemove
        }

        protected open fun canInsert(access: HTStorageAccess): Boolean = true

        protected open fun canExtract(access: HTStorageAccess): Boolean = true

        protected open fun getInsertRate(access: HTStorageAccess): Long = Long.MAX_VALUE

        protected open fun getExtractRate(access: HTStorageAccess): Long = Long.MAX_VALUE
    }
}
