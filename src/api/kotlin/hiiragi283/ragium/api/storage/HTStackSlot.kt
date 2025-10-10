package hiiragi283.ragium.api.storage

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.storage.value.HTValueSerializable
import kotlin.math.min

interface HTStackSlot<STACK : HTStorageStack<*>> :
    HTValueSerializable,
    HTContentListener {
    /**
     * 保持している[STACK]を返します。
     */
    fun getStack(): STACK

    fun isEmpty(): Boolean = getStack().isEmpty()

    fun getAmountAsLong(): Long = getStack().amountAsLong()

    fun getAmountAsInt(): Int = getStack().amountAsInt()

    fun getCapacityAsLong(stack: STACK): Long

    fun getCapacityAsInt(stack: STACK): Int = Ints.saturatedCast(getCapacityAsLong(stack))

    fun isValid(stack: STACK): Boolean

    /**
     * 指定された引数から[STACK]を搬入します。
     * @param stack 搬入される[STACK]
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬入を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬入されなかった[STACK]
     */
    fun insert(stack: STACK, action: HTStorageAction, access: HTStorageAccess): STACK

    /**
     * 指定された引数から[STACK]を搬出します。
     * @param amount 搬出する個数の最大値
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬出を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬出された[STACK]
     */
    fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): STACK

    fun getNeededAsLong(stack: STACK): Long = getCapacityAsLong(stack) - getAmountAsLong()

    fun getNeededAsInt(stack: STACK): Int = getCapacityAsInt(stack) - getAmountAsInt()

    fun getStoredLevelAsDouble(stack: STACK): Double = getAmountAsLong() / getCapacityAsLong(stack).toDouble()

    fun getStoredLevelAsFloat(stack: STACK): Float = getAmountAsLong() / getCapacityAsLong(stack).toFloat()

    //    Mutable    //

    interface Mutable<STACK : HTStorageStack<*>> : HTStackSlot<STACK> {
        /**
         * 指定された[stack]を保持します。
         */
        fun setStack(stack: STACK)

        /**
         * 指定された[amount]から，現在の個数を置換します。
         * @param amount 置換する個数の最大値
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に置換を行います。
         * @return 実際に置換された個数
         */
        fun setStackSize(amount: Int, action: HTStorageAction): Int

        /**
         * 指定された[amount]から，現在の個数に追加します。
         * @param amount 追加する個数の最大値
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に追加を行います。
         * @return 実際に追加された個数
         */
        fun growStack(amount: Int, action: HTStorageAction): Int {
            val current: Int = getAmountAsInt()
            if (current == 0) return 0
            val fixedAmount: Int = if (amount > 0) {
                min(amount, getCapacityAsInt(getStack()))
            } else {
                amount
            }
            val newSize: Int = setStackSize(current + fixedAmount, action)
            return newSize - current
        }

        /**
         * 指定された[amount]から，現在の個数を削除します。
         * @param amount 削除する個数の最大値
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に削除を行います。
         * @return 実際に削除された個数
         */
        fun shrinkStack(amount: Int, action: HTStorageAction): Int = -growStack(-amount, action)
    }
}
