package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.extension.wrapAction
import hiiragi283.ragium.api.storage.HTStackSlot
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.value.HTValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import kotlin.math.min

interface HTFluidTank :
    HTStackSlot<HTFluidStorageStack>,
    IFluidTank {
    override fun serialize(output: HTValueOutput) {
        output.store(RagiumConst.FLUID, HTFluidStorageStack.CODEC, getStack())
    }

    fun toSingleHandler(): IFluidHandler = HTFluidTankWrapper(this)

    //    IFluidTank    //

    @Deprecated("Use `getStack()` instead", ReplaceWith("this.getStack()"), DeprecationLevel.ERROR)
    override fun getFluid(): FluidStack = getFluidStack()

    @Deprecated("Use `getAmountAsInt()` instead", ReplaceWith("this.getAmountAsInt()"), DeprecationLevel.ERROR)
    override fun getFluidAmount(): Int = getAmountAsInt()

    @Deprecated("Use `getCapacityAsInt(FluidStack)` instead", level = DeprecationLevel.ERROR)
    override fun getCapacity(): Int = getCapacityAsInt(getStack())

    @Deprecated("Use `isValid(FluidStack)` instead", ReplaceWith("this.isValid(stack)"), DeprecationLevel.ERROR)
    override fun isFluidValid(stack: FluidStack): Boolean = isValid(stack)

    @Deprecated("Use `insert(FluidStack, Boolean, HTStorageAccess) `instead")
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int =
        resource.amount - insertFluid(resource, action.wrapAction(), HTStorageAccess.EXTERNAL).amount

    @Deprecated("Use `extract(FluidStack, Boolean, HTStorageAccess)` instead")
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack =
        extractFluid(maxDrain, action.wrapAction(), HTStorageAccess.EXTERNAL)

    @Deprecated("Use `extract(FluidStack, Boolean, HTStorageAccess)` instead")
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (!FluidStack.isSameFluidSameComponents(resource, getFluidStack())) return FluidStack.EMPTY
        return extractFluid(resource.amount, action.wrapAction(), HTStorageAccess.EXTERNAL)
    }

    //    Mutable    //

    interface Mutable :
        HTFluidTank,
        HTStackSlot.Mutable<HTFluidStorageStack> {
        override fun insert(stack: HTFluidStorageStack, action: HTStorageAction, access: HTStorageAccess): HTFluidStorageStack {
            if (stack.isEmpty()) return HTFluidStorageStack.EMPTY

            val needed: Int = getNeededAsInt(stack)
            if (needed <= 0 || !isFluidValidForInsert(stack, access)) return stack

            val sameType: Boolean = HTFluidStorageStack.isSameFluidSameComponents(getStack(), stack)
            if (isEmpty() || sameType) {
                val toAdd: Int = min(stack.amountAsInt(), needed)
                if (action.execute) {
                    if (sameType) {
                        growStack(toAdd, action)
                        onContentsChanged()
                    } else {
                        setStack(stack.copyWithAmount(toAdd))
                    }
                }
                return stack.copyWithAmount(stack.amountAsInt() - toAdd)
            }
            return stack
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): HTFluidStorageStack {
            val stack: HTFluidStorageStack = getStack()
            if (isEmpty() || amount < 1 || !canFluidExtract(getStack(), access)) {
                return HTFluidStorageStack.EMPTY
            }
            val current: Int = min(stack.amountAsInt(), getCapacityAsInt(getStack()))
            val fixedAmount: Int = min(amount, current)
            val result: HTFluidStorageStack = stack.copyWithAmount(fixedAmount)
            if (action.execute) {
                shrinkStack(fixedAmount, action)
                onContentsChanged()
            }
            return result
        }

        /**
         * 指定された[stack]をこのスロットに搬入できるか判定します。
         * @param stack 搬入される[HTFluidStorageStack]
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        fun isFluidValidForInsert(stack: HTFluidStorageStack, access: HTStorageAccess): Boolean = isValid(stack)

        /**
         * 指定された[stack]をこのスロットに搬出できるか判定します。
         * @param stack 搬出される[HTFluidStorageStack]
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        fun canFluidExtract(stack: HTFluidStorageStack, access: HTStorageAccess): Boolean = true

        /**
         * 指定された[amount]から，現在の個数を置換します。
         * @param amount 置換する個数の最大値
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に置換を行います。
         * @return 実際に置換された個数
         */
        override fun setStackSize(amount: Int, action: HTStorageAction): Int {
            if (isEmpty()) return 0
            if (amount <= 0) {
                if (action.execute) setStack(HTFluidStorageStack.EMPTY)
                return 0
            }
            val stack: HTFluidStorageStack = getStack()
            val maxStackSize: Int = getCapacityAsInt(stack)
            val fixedAmount: Int = min(amount, maxStackSize)
            if (stack.amountAsInt() == fixedAmount || !action.execute) {
                return fixedAmount
            }
            setStack(stack.copyWithAmount(fixedAmount))
            onContentsChanged()
            return fixedAmount
        }
    }
}
