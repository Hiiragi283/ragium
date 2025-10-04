package hiiragi283.ragium.api.storage

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.storage.value.HTValueSerializable

interface HTStackSlot<STACK : Any> :
    HTValueSerializable,
    HTContentListener {
    /**
     * 保持している[STACK]を返します。
     */
    fun getStack(): STACK

    fun getAmountAsLong(): Long

    fun getAmountAsInt(): Int = Ints.saturatedCast(getAmountAsLong())

    fun getCapacityAsLong(stack: STACK): Long

    fun getCapacityAsInt(stack: STACK): Int = Ints.saturatedCast(getCapacityAsLong(stack))

    fun isValid(stack: STACK): Boolean

    /**
     * 指定された引数から[STACK]を搬入します。
     * @param stack 搬入される[STACK]
     * @param simulate `true`の場合のみ実際に搬入を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬入されなかった[STACK]
     */
    fun insert(stack: STACK, simulate: Boolean, access: HTStorageAccess): STACK

    /**
     * 指定された引数から[STACK]を搬出します。
     * @param amount 搬出する個数の最大値
     * @param simulate `true`の場合のみ実際に搬出を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬出された[STACK]
     */
    fun extract(amount: Int, simulate: Boolean, access: HTStorageAccess): STACK

    fun isEmpty(): Boolean

    fun getNeededAsLong(stack: STACK): Long = getCapacityAsLong(stack) - getAmountAsLong()

    fun getNeededAsInt(stack: STACK): Int = getCapacityAsInt(stack) - getAmountAsInt()

    fun getStoredLevelAsDouble(stack: STACK): Double = getAmountAsLong() / getCapacityAsLong(stack).toDouble()

    fun getStoredLevelAsFloat(stack: STACK): Float = getAmountAsLong() / getCapacityAsLong(stack).toFloat()

    //    Mutable    //

    interface Mutable<STACK : Any> : HTStackSlot<STACK> {
        /**
         * 指定された[stack]を保持します。
         */
        fun setStack(stack: STACK)

        /**
         * このスロットを空にします。
         */
        fun setEmpty()
    }
}
