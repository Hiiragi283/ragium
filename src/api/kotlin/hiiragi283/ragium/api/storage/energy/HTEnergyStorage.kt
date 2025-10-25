package hiiragi283.ragium.api.storage.energy

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.min

interface HTEnergyStorage :
    IEnergyStorage,
    HTValueSerializable,
    HTContentListener {
    fun getAmountAsInt(): Int

    fun isEmpty(): Boolean = getAmountAsInt() <= 0

    fun isNotEmpty(): Boolean = !isEmpty()

    fun getCapacityAsInt(): Int

    fun getNeededAsInt(): Int = getCapacityAsInt() - getAmountAsInt()

    fun getStoredLevelAsDouble(): Double = getAmountAsInt() / getCapacityAsInt().toDouble()

    fun getStoredLevelAsFloat(): Float = getAmountAsInt() / getCapacityAsInt().toFloat()

    fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int

    fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int

    //    IEnergyStorage    //

    @Deprecated("Use `insertEnergy(Int, HTStorageAction, HTStorageAccess)` instead", level = DeprecationLevel.ERROR)
    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int =
        insertEnergy(toReceive, HTStorageAction.of(simulate), HTStorageAccess.EXTERNAL)

    @Deprecated("Use `extractEnergy(Int, HTStorageAction, HTStorageAccess)` instead", level = DeprecationLevel.ERROR)
    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int =
        extractEnergy(toExtract, HTStorageAction.of(simulate), HTStorageAccess.EXTERNAL)

    @Deprecated("Use 'getAmountAsInt()' instead", ReplaceWith("this.getAmountAsInt()"), DeprecationLevel.ERROR)
    override fun getEnergyStored(): Int = getAmountAsInt()

    @Deprecated("Use 'getCapacityAsInt()' instead", ReplaceWith("this.getCapacityAsInt()"), DeprecationLevel.ERROR)
    override fun getMaxEnergyStored(): Int = getCapacityAsInt()

    @Deprecated("Not used", level = DeprecationLevel.ERROR)
    override fun canExtract(): Boolean = true

    @Deprecated("Not used", level = DeprecationLevel.ERROR)
    override fun canReceive(): Boolean = true

    //    Mutable    //

    abstract class Mutable : HTEnergyStorage {
        abstract fun setAmountAsInt(amount: Int)

        override fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (amount <= 0 || !canInsert(access)) return 0
            val needed: Int = min(getInsertRate(access), getNeededAsInt())
            if (needed <= 0) return amount
            val toAdd: Int = min(amount, needed)
            if (action.execute) {
                setAmountAsInt(getAmountAsInt() + toAdd)
                onContentsChanged()
            }
            return amount - toAdd
        }

        override fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (isEmpty() || amount <= 0 || !canExtract(access)) return 0
            val toRemove: Int = min(min(getExtractRate(access), getAmountAsInt()), amount)
            if (toRemove < 0 && action.execute) {
                setAmountAsInt(getAmountAsInt() - toRemove)
                onContentsChanged()
            }
            return toRemove
        }

        protected open fun canInsert(access: HTStorageAccess): Boolean = true

        protected open fun canExtract(access: HTStorageAccess): Boolean = true

        protected open fun getInsertRate(access: HTStorageAccess): Int = Int.MAX_VALUE

        protected open fun getExtractRate(access: HTStorageAccess): Int = Int.MAX_VALUE
    }
}
