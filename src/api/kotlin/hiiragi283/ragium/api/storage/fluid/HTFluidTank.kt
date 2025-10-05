package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.storage.HTStackSlot
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.predicate.HTFluidPredicates
import hiiragi283.ragium.api.storage.value.HTValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import kotlin.math.min

interface HTFluidTank :
    HTStackSlot<FluidStack>,
    IFluidTank {
    override fun getAmountAsLong(): Long = getStack().amount.toLong()

    override fun isEmpty(): Boolean = getStack().isEmpty

    override fun serialize(output: HTValueOutput) {
        output.store(RagiumConst.FLUID, BiCodecs.fluidStack(true), getStack())
    }

    fun toSingleHandler(): IFluidHandler = HTFluidTankWrapper(this)

    //    IFluidTank    //

    @Deprecated("Use `getStack()` instead", ReplaceWith("this.getStack()"), DeprecationLevel.ERROR)
    override fun getFluid(): FluidStack = getStack()

    @Deprecated("Use `getAmountAsInt()` instead", ReplaceWith("this.getAmountAsInt()"), DeprecationLevel.ERROR)
    override fun getFluidAmount(): Int = getStack().amount

    @Deprecated("Use `getCapacityAsInt(FluidStack)` instead", level = DeprecationLevel.ERROR)
    override fun getCapacity(): Int = getCapacityAsInt(getStack())

    @Deprecated("Use `isValid(FluidStack)` instead", ReplaceWith("this.isValid(stack)"), DeprecationLevel.ERROR)
    override fun isFluidValid(stack: FluidStack): Boolean = isValid(stack)

    @Deprecated("Use `insert(FluidStack, Boolean, HTStorageAccess) `instead")
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int =
        resource.amount - insert(resource, action.simulate(), HTStorageAccess.EXTERNAL).amount

    @Deprecated("Use `extract(FluidStack, Boolean, HTStorageAccess)` instead")
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack =
        extract(maxDrain, action.simulate(), HTStorageAccess.EXTERNAL)

    @Deprecated("Use `extract(FluidStack, Boolean, HTStorageAccess)` instead")
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (!HTFluidPredicates.byFluidAndComponent(resource).test(getStack())) return FluidStack.EMPTY
        return extract(resource.amount, action.simulate(), HTStorageAccess.EXTERNAL)
    }

    //    Mutable    //

    interface Mutable :
        HTFluidTank,
        HTStackSlot.Mutable<FluidStack> {
        override fun setEmpty() {
            setStack(FluidStack.EMPTY)
        }

        override fun insert(stack: FluidStack, simulate: Boolean, access: HTStorageAccess): FluidStack {
            if (stack.isEmpty) return FluidStack.EMPTY

            val needed: Int = getNeededAsInt(stack)
            if (needed <= 0 || !isFluidValidForInsert(stack, access)) return stack

            val sameType: Boolean = FluidStack.isSameFluidSameComponents(getStack(), stack)
            if (isEmpty() || sameType) {
                val toAdd: Int = min(stack.amount, needed)
                if (!simulate) {
                    if (sameType) {
                        growStack(toAdd, false)
                        onContentsChanged()
                    } else {
                        setStack(stack.copyWithAmount(toAdd))
                    }
                }
                return stack.copyWithAmount(stack.amount - toAdd)
            }
            return stack
        }

        override fun extract(amount: Int, simulate: Boolean, access: HTStorageAccess): FluidStack {
            val stack: FluidStack = getStack()
            if (isEmpty() || amount < 1 || !canFluidExtract(getStack(), access)) {
                return FluidStack.EMPTY
            }
            val current: Int = min(stack.amount, getCapacityAsInt(getStack()))
            val fixedAmount: Int = min(amount, current)
            val result: FluidStack = stack.copyWithAmount(fixedAmount)
            if (!simulate) {
                shrinkStack(fixedAmount, false)
                onContentsChanged()
            }
            return result
        }

        /**
         * 指定された[stack]をこのスロットに搬入できるか判定します。
         * @param stack 搬入される[FluidStack]
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        fun isFluidValidForInsert(stack: FluidStack, access: HTStorageAccess): Boolean = isValid(stack)

        /**
         * 指定された[stack]をこのスロットに搬出できるか判定します。
         * @param stack 搬出される[FluidStack]
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        fun canFluidExtract(stack: FluidStack, access: HTStorageAccess): Boolean = true

        /**
         * 指定された[amount]から，現在の個数を置換します。
         * @param amount 置換する個数の最大値
         * @param simulate `true`の場合のみ実際に置換を行います。
         * @return 実際に置換された個数
         */
        fun setStackSize(amount: Int, simulate: Boolean): Int {
            if (isEmpty()) return 0
            if (amount <= 0) {
                if (!simulate) setEmpty()
                return 0
            }
            val stack: FluidStack = getStack()
            val maxStackSize: Int = getCapacityAsInt(stack)
            val fixedAmount: Int = min(amount, maxStackSize)
            if (stack.amount == fixedAmount || simulate) {
                return fixedAmount
            }
            setStack(stack.copyWithAmount(fixedAmount))
            onContentsChanged()
            return fixedAmount
        }

        /**
         * 指定された[amount]から，現在の個数に追加します。
         * @param amount 追加する個数の最大値
         * @param simulate `true`の場合のみ実際に追加を行います。
         * @return 実際に追加された個数
         */
        fun growStack(amount: Int, simulate: Boolean): Int {
            val current: Int = getAmountAsInt()
            if (current == 0) return 0
            val fixedAmount: Int = if (amount > 0) {
                min(amount, getCapacityAsInt(getStack()))
            } else {
                amount
            }
            val newSize: Int = setStackSize(current + fixedAmount, simulate)
            return newSize - current
        }

        /**
         * 指定された[amount]から，現在の個数を削除します。
         * @param amount 削除する個数の最大値
         * @param simulate `true`の場合のみ実際に削除を行います。
         * @return 実際に削除された個数
         */
        fun shrinkStack(amount: Int, simulate: Boolean): Int = -growStack(-amount, simulate)
    }
}
