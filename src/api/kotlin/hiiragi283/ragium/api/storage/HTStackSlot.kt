package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.util.HTContentListener
import kotlin.math.min

/**
 * 単一の[STACK]を保持し，出し入れが可能な[HTStackView]の拡張インターフェース
 * @param STACK 保持するスタックのクラス
 */
interface HTStackSlot<STACK : ImmutableStack<*, STACK>> :
    HTStackView<STACK>,
    HTValueSerializable,
    HTContentListener {
    /**
     * 指定した[stack]がスロットに有効かどうか判定します。
     * @return 有効な場合は`true`
     */
    fun isValid(stack: STACK): Boolean

    /**
     * 指定された引数から[STACK]を搬入します。
     * @param stack 搬入される[STACK]
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬入を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬入されなかった[STACK]
     */
    fun insert(stack: STACK?, action: HTStorageAction, access: HTStorageAccess): STACK?

    /**
     * 指定された引数から[STACK]を搬出します。
     * @param stack 搬出される[STACK]
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬出を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬出された[STACK]
     */
    fun extract(stack: STACK?, action: HTStorageAction, access: HTStorageAccess): STACK?

    /**
     * 指定された引数から[STACK]を搬出します。
     * @param amount 搬出する個数の最大値
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬出を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬出された[STACK]
     */
    fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): STACK?

    /**
     * 指定された[other]と[getStack]が等価か判定します。
     */
    fun isSameStack(other: STACK?): Boolean

    //    Basic    //

    /**
     * [HTStackSlot]の基本的な実装
     */
    abstract class Basic<STACK : ImmutableStack<*, STACK>> : HTStackSlot<STACK> {
        /**
         * 指定された[stack]を代入します。
         * @param stack 新しいスタック
         */
        protected abstract fun setStack(stack: STACK?)

        /**
         * 指定された引数をもとに，現在保持しているスタックの個数を変更します。
         * @param stack 現在のスタック
         * @param amount 新しい数量
         */
        protected abstract fun updateAmount(stack: STACK, amount: Int)

        protected fun growAmount(stack: STACK, amount: Int) {
            updateAmount(stack, stack.amount() + amount)
        }

        protected fun shrinkAmount(stack: STACK, amount: Int) {
            updateAmount(stack, stack.amount() - amount)
        }

        /**
         * @see mekanism.common.inventory.slot.BasicInventorySlot.insertItem
         * @see mekanism.common.capabilities.fluid.BasicFluidTank.insert
         */
        override fun insert(stack: STACK?, action: HTStorageAction, access: HTStorageAccess): STACK? {
            if (stack == null) return null
            val needed: Int = getNeeded(stack)
            if (needed <= 0 || !isStackValidForInsert(stack, access)) return stack

            val stackIn: STACK? = this.getStack()
            val sameType: Boolean = isSameStack(stack)
            if (stackIn == null || sameType) {
                val toAdd: Int = min(stack.amount(), needed)
                if (action.execute) {
                    if (sameType && stackIn != null) {
                        growAmount(stackIn, toAdd)
                        onContentsChanged()
                    } else {
                        setStack(stack.copyWithAmount(toAdd))
                    }
                }
                return stack.copyWithAmount(stack.amount() - toAdd)
            }
            return stack
        }

        final override fun extract(stack: STACK?, action: HTStorageAction, access: HTStorageAccess): STACK? = when {
            stack == null -> null
            this.getStack() != null && isSameStack(stack) -> extract(stack.amount(), action, access)
            else -> null
        }

        /**
         * @see mekanism.common.inventory.slot.BasicInventorySlot.extractItem
         * @see mekanism.common.capabilities.fluid.BasicFluidTank.extract
         */
        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): STACK? {
            val stack: STACK? = this.getStack()
            if (stack == null || amount < 1 || !canStackExtract(stack, access)) return null
            val fixedAmount: Int = min(amount, getAmount())
            val result: STACK? = stack.copyWithAmount(fixedAmount)
            if (result != null && action.execute) {
                shrinkAmount(stack, fixedAmount)
                onContentsChanged()
            }
            return result
        }

        /**
         * 指定された[stack]をこのスロットに搬入できるか判定します。
         * @param stack 搬入される[STACK]
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        protected open fun isStackValidForInsert(stack: STACK, access: HTStorageAccess): Boolean = isValid(stack)

        /**
         * 指定された[stack]をこのスロットに搬出できるか判定します。
         * @param stack 搬出される[STACK]
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        protected open fun canStackExtract(stack: STACK, access: HTStorageAccess): Boolean = true
    }
}
