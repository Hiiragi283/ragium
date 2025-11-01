package hiiragi283.ragium.api.storage.energy

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.item.HTAmountSlot
import hiiragi283.ragium.api.util.HTContentListener
import kotlin.math.min

/**
 * @see mekanism.api.energy.IEnergyContainer
 */
interface HTEnergyBattery :
    HTAmountSlot.IntSized,
    HTValueSerializable,
    HTContentListener {
    //    Basic    //

    abstract class Basic : HTEnergyBattery {
        /**
         * 指定された電気量を代入します。
         */
        protected abstract fun setAmount(amount: Int)

        override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (amount <= 0 || !canInsert(access)) return 0
            val needed: Int = min(getInsertRate(access), getNeeded())
            if (needed <= 0) return 0
            val toAdd: Int = min(amount, needed)
            if (action.execute) {
                setAmount(getAmount() + toAdd)
                onContentsChanged()
            }
            return toAdd
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (isEmpty() || amount <= 0 || !canExtract(access)) return 0
            val toRemove: Int = min(min(getExtractRate(access), getAmount()), amount)
            if (toRemove > 0 && action.execute) {
                setAmount(getAmount() - toRemove)
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
