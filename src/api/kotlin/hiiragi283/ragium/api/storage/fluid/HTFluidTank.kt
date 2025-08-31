package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * 単一の[FluidStack]を保持するインターフェース
 * @see [mekanism.api.fluid.IExtendedFluidTank]
 */
interface HTFluidTank :
    IFluidTank,
    INBTSerializable<CompoundTag>,
    HTContentListener {
    /**
     * 保持している[FluidStack]を返します。
     */
    fun getStack(): FluidStack

    /**
     * 指定された[stack]を保持します。
     */
    fun setStack(stack: FluidStack)

    /**
     * 指定された引数から[FluidStack]を搬入します。
     * @param stack 搬入される[FluidStack]
     * @param simulate `true`の場合のみ実際に搬入を行います。
     * @param access このタンクへのアクセスの種類
     * @return 搬入されなかった[FluidStack]
     */
    fun insert(stack: FluidStack, simulate: Boolean, access: HTStorageAccess): FluidStack {
        if (stack.isEmpty || !isFluidValidForInsert(stack, access)) return stack

        val needed: Int = getNeeded()
        if (needed <= 0) return stack

        val sameType: Boolean = FluidStack.isSameFluidSameComponents(getStack(), stack)
        if (isEmpty || sameType) {
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

    /**
     * 指定された引数から[FluidStack]を搬出します。
     * @param amount 搬出する数量の最大値
     * @param simulate `true`の場合のみ実際に搬出を行います。
     * @param access このタンクへのアクセスの種類
     * @return 搬出された[FluidStack]
     */
    fun extract(amount: Int, simulate: Boolean, access: HTStorageAccess): FluidStack {
        val stack: FluidStack = getStack()
        if (isEmpty || amount < 1 || !canFluidExtract(getStack(), access)) {
            return FluidStack.EMPTY
        }
        val result: FluidStack = stack.copyWithAmount(min(fluidAmount, amount))
        if (!simulate) {
            shrinkStack(result.amount, false)
            onContentsChanged()
        }
        return result
    }

    /**
     * 指定された[stack]をこのタンクに搬入できるか判定します。
     * @param stack 搬入される[FluidStack]
     * @param access このタンクへのアクセスの種類
     * @return 搬入できる場合は`true`
     */
    fun isFluidValidForInsert(stack: FluidStack, access: HTStorageAccess): Boolean = isFluidValid(stack)

    /**
     * 指定された[stack]をこのタンクに搬出できるか判定します。
     * @param stack 搬出される[FluidStack]
     * @param access このタンクへのアクセスの種類
     * @return 搬出できる場合は`true`
     */
    fun canFluidExtract(stack: FluidStack, access: HTStorageAccess): Boolean = true

    fun toSingleHandler(): IFluidHandler = HTFluidTankWrapper(this)

    /**
     * 指定された[amount]から，現在の数量を置換します。
     * @param amount 置換する数量の最大値
     * @param simulate `true`の場合のみ実際に置換を行います。
     * @return 実際に置換された数量
     */
    fun setStackSize(amount: Int, simulate: Boolean): Int {
        if (isEmpty) return 0
        if (amount <= 0) {
            if (!simulate) setEmpty()
            return 0
        }
        val stack: FluidStack = getStack()
        val fixedAmount: Int = min(amount, capacity)
        if (stack.amount == fixedAmount || simulate) {
            return fixedAmount
        }
        setStack(stack.copyWithAmount(fixedAmount))
        onContentsChanged()
        return fixedAmount
    }

    /**
     * 指定された[amount]から，現在の数量に追加します。
     * @param amount 追加する数量の最大値
     * @param simulate `true`の場合のみ実際に追加を行います。
     * @return 実際に追加された数量
     */
    fun growStack(amount: Int, simulate: Boolean): Int {
        val current: Int = this.fluidAmount
        if (current == 0) return 0
        val fixedAmount: Int = if (amount > 0) {
            min(amount, getNeeded())
        } else {
            amount
        }
        val newSize: Int = setStackSize(current + fixedAmount, simulate)
        return newSize - current
    }

    /**
     * 指定された[amount]から，現在の数量を削除します。
     * @param amount 削除する数量の最大値
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return 実際に削除された数量
     */
    fun shrinkStack(amount: Int, simulate: Boolean): Int = -growStack(-amount, simulate)

    /**
     * 指定された[ingredient]から，現在の数量を削除します。
     * @param ingredient 削除する数量を提供する材料
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return 実際に削除された数量
     */
    fun shrinkStack(ingredient: HTFluidIngredient, simulate: Boolean): Int = shrinkStack(ingredient.getRequiredAmount(getStack()), simulate)

    /**
     * 指定された[ingredient]から，現在の数量を削除します。
     * @param ingredient 削除する数量を提供する材料
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return [Optional.isEmpty]の場合は`0`，それ以外は実際に削除された数量
     */
    fun shrinkStack(ingredient: Optional<HTFluidIngredient>, simulate: Boolean): Int = ingredient
        .map { ingredient1: HTFluidIngredient -> shrinkStack(ingredient1, simulate) }
        .orElse(0)

    /**
     * このタンクが空かどうか判定します。
     */
    val isEmpty: Boolean get() = getStack().isEmpty

    /**
     * このタンクを空にします。
     */
    fun setEmpty() {
        setStack(FluidStack.EMPTY)
    }

    fun getNeeded(): Int = max(0, capacity - fluidAmount)

    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag = buildNbt {
        put(RagiumConst.FLUID, getStack().saveOptional(provider))
    }

    //    IFluidTank    //

    @Deprecated("Use getStack() instead of this")
    override fun getFluid(): FluidStack = getStack()

    override fun getFluidAmount(): Int = getStack().amount

    @Deprecated("Use insert(FluidStack, Boolean, HTStorageAccess) instead of this")
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int =
        resource.amount - insert(resource, action.simulate(), HTStorageAccess.EXTERNAL).amount

    @Deprecated("Use extract(FluidStack, Boolean, HTStorageAccess) instead of this")
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack =
        extract(maxDrain, action.simulate(), HTStorageAccess.EXTERNAL)

    @Deprecated("Use extract(FluidStack, Boolean, HTStorageAccess) instead of this")
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (!FluidStack.isSameFluidSameComponents(resource, getStack())) return FluidStack.EMPTY
        return extract(resource.amount, action.simulate(), HTStorageAccess.EXTERNAL)
    }
}
