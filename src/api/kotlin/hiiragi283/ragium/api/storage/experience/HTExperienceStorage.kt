package hiiragi283.ragium.api.storage.experience

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTAmountView
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import kotlin.math.min

interface HTExperienceStorage :
    IExperienceStorage,
    HTAmountView.LongSized,
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
     * @return 搬入された経験値量
     */
    fun insertExp(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long

    /**
     * 指定された引数から[amount]を搬出します。
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬出を行います。
     * @param access このストレージへのアクセスの種類
     * @return 搬出された経験値量
     */
    fun extractExp(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long

    //    IExperienceStorage    //

    @Deprecated("Use `insertExp(Long, HTStorageAction, HTStorageAccess)` instead", level = DeprecationLevel.ERROR)
    override fun insertExp(amount: Long, simulate: Boolean): Long =
        insertExp(amount, HTStorageAction.of(simulate), HTStorageAccess.EXTERNAL)

    @Deprecated("Use `extractExp(Long, HTStorageAction, HTStorageAccess)` instead", level = DeprecationLevel.ERROR)
    override fun extractExp(amount: Long, simulate: Boolean): Long =
        extractExp(amount, HTStorageAction.of(simulate), HTStorageAccess.EXTERNAL)

    @Deprecated("Use `getAmount()` instead", ReplaceWith("this.getAmount()"), DeprecationLevel.ERROR)
    override fun getExpStored(): Long = getAmount()

    @Deprecated("Use `getCapacity()` instead", ReplaceWith("this.getCapacity()"), DeprecationLevel.ERROR)
    override fun getMaxExpStored(): Long = getCapacity()

    //    Mutable    //

    interface Mutable : HTExperienceStorage {
        /**
         * 指定された経験値量を代入します。
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に代入を行います。
         */
        fun setAmount(amount: Long, action: HTStorageAction)
    }

    //    Basic    //

    abstract class Basic : Mutable {
        override fun insertExp(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long {
            if (amount <= 0 || !canInsert(access)) return 0
            val needed: Long = min(getInsertRate(access), getNeeded())
            if (needed <= 0) return 0
            val toAdd: Long = min(amount, needed)
            setAmount(getAmount() + toAdd, action)
            if (action.execute) {
                onContentsChanged()
            }
            return toAdd
        }

        override fun extractExp(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long {
            if (isEmpty() || amount <= 0 || !canExtract(access)) return 0
            val toRemove: Long = min(min(getExtractRate(access), getAmount()), amount)
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

        protected open fun getInsertRate(access: HTStorageAccess): Long = Long.MAX_VALUE

        protected open fun getExtractRate(access: HTStorageAccess): Long = Long.MAX_VALUE
    }
}
