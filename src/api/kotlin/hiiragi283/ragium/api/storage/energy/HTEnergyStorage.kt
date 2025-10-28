package hiiragi283.ragium.api.storage.energy

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTAmountView
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.min

/**
 * @see mekanism.api.energy.IEnergyContainer
 */
interface HTEnergyStorage :
    IEnergyStorage,
    HTAmountView.IntSized,
    HTValueSerializable,
    HTContentListener {
    /**
     * このストレージが空かどうか判定します。
     * @return 空の場合は`true`
     */
    fun isEmpty(): Boolean = getAmount() <= 0

    fun isNotEmpty(): Boolean = !isEmpty()

    /**
     * 指定された引数から[amount]を搬入します。
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬入を行います。
     * @param access このストレージへのアクセスの種類
     * @return 搬入された電気量
     */
    fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int

    /**
     * 指定された引数から[amount]を搬出します。
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬出を行います。
     * @param access このストレージへのアクセスの種類
     * @return 搬出された電気量
     */
    fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int

    //    IEnergyStorage    //

    @Deprecated("Use `insertEnergy(Int, HTStorageAction, HTStorageAccess)` instead", level = DeprecationLevel.ERROR)
    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int =
        insertEnergy(toReceive, HTStorageAction.of(simulate), HTStorageAccess.EXTERNAL)

    @Deprecated("Use `extractEnergy(Int, HTStorageAction, HTStorageAccess)` instead", level = DeprecationLevel.ERROR)
    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int =
        extractEnergy(toExtract, HTStorageAction.of(simulate), HTStorageAccess.EXTERNAL)

    @Deprecated("Use 'getAmount()' instead", ReplaceWith("this.getAmount()"), DeprecationLevel.ERROR)
    override fun getEnergyStored(): Int = getAmount()

    @Deprecated("Use 'getCapacity()' instead", ReplaceWith("this.getCapacity()"), DeprecationLevel.ERROR)
    override fun getMaxEnergyStored(): Int = getCapacity()

    @Deprecated("Not used", level = DeprecationLevel.ERROR)
    override fun canExtract(): Boolean = true

    @Deprecated("Not used", level = DeprecationLevel.ERROR)
    override fun canReceive(): Boolean = true

    //    Mutable    //

    interface Mutable : HTEnergyStorage {
        /**
         * 指定された電気量を代入します。
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に代入を行います。
         */
        fun setAmount(amount: Int, action: HTStorageAction)
    }

    //    Basic    //

    abstract class Basic : Mutable {
        override fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (amount <= 0 || !canInsert(access)) return 0
            val needed: Int = min(getInsertRate(access), getNeeded())
            if (needed <= 0) return 0
            val toAdd: Int = min(amount, needed)
            setAmount(getAmount() + toAdd, action)
            if (action.execute) {
                onContentsChanged()
            }
            return toAdd
        }

        override fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (isEmpty() || amount <= 0 || !canExtract(access)) return 0
            val toRemove: Int = min(min(getExtractRate(access), getAmount()), amount)
            if (toRemove > 0) {
                setAmount(getAmount() - toRemove, action)
                if (action.execute) {
                    onContentsChanged()
                }
            }
            return toRemove
        }

        protected open fun canInsert(access: HTStorageAccess): Boolean = true

        protected open fun canExtract(access: HTStorageAccess): Boolean = true

        protected open fun getInsertRate(access: HTStorageAccess): Int = Int.MAX_VALUE

        protected open fun getExtractRate(access: HTStorageAccess): Int = Int.MAX_VALUE
    }
}
