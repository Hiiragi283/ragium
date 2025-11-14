package hiiragi283.ragium.api.storage.experience

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTAmountSlot
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import kotlin.math.min

interface HTExperienceTank :
    HTAmountSlot.LongSized,
    HTValueSerializable,
    HTContentListener {
    //    Basic    //

    abstract class Basic : HTExperienceTank {
        /**
         * 指定された経験値量を代入します。
         */
        protected abstract fun setAmount(amount: Long)

        override fun insert(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long {
            if (amount <= 0 || !canInsert(access)) return 0
            val needed: Long = min(getInsertRate(access), getNeeded())
            if (needed <= 0) return 0
            val toAdd: Long = min(amount, needed)
            if (action.execute) {
                setAmount(getAmount() + toAdd)
                onContentsChanged()
            }
            return toAdd
        }

        override fun extract(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long {
            if (isEmpty() || amount <= 0 || !canExtract(access)) return 0
            val toRemove: Long = min(min(getExtractRate(access), getAmount()), amount)
            if (toRemove > 0 && action.execute) {
                setAmount(getAmount() - toRemove)
                onContentsChanged()
            }
            return toRemove
        }

        protected open fun canInsert(access: HTStorageAccess): Boolean = true

        protected open fun canExtract(access: HTStorageAccess): Boolean = true

        protected open fun getInsertRate(access: HTStorageAccess): Long = Long.MAX_VALUE

        protected open fun getExtractRate(access: HTStorageAccess): Long = Long.MAX_VALUE

        override fun toString(): String = "HTExperienceTank(amount=${getAmount()}, capacity=${getCapacity()})"
    }
}
