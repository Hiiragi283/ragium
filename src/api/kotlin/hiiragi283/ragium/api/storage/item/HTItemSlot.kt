package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.INBTSerializable
import java.util.Optional
import kotlin.math.min

/**
 * 単一の[ItemStack]を保持するインターフェース
 * @see [mekanism.api.inventory.IInventorySlot]
 */
interface HTItemSlot :
    INBTSerializable<CompoundTag>,
    HTContentListener {
    /**
     * 保持している[ItemStack]を返します。
     */
    fun getStack(): ItemStack

    /**
     * 指定された[stack]を保持します。
     */
    fun setStack(stack: ItemStack)

    /**
     * 指定された引数から[ItemStack]を搬入します。
     * @param stack 搬入される[ItemStack]
     * @param simulate `true`の場合のみ実際に搬入を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬入されなかった[ItemStack]
     */
    fun insertItem(stack: ItemStack, simulate: Boolean, access: HTStorageAccess): ItemStack {
        if (stack.isEmpty) return ItemStack.EMPTY

        val needed: Int = getLimit(stack) - count
        if (needed <= 0 || !isItemValidForInsert(stack, access)) return stack

        val sameType: Boolean = ItemStack.isSameItemSameComponents(getStack(), stack)
        if (isEmpty || sameType) {
            val toAdd: Int = min(stack.count, needed)
            if (!simulate) {
                if (sameType) {
                    growStack(toAdd, false)
                    onContentsChanged()
                } else {
                    setStack(stack.copyWithCount(toAdd))
                }
            }
            return stack.copyWithCount(stack.count - toAdd)
        }
        return stack
    }

    /**
     * 指定された引数から[ItemStack]を搬出します。
     * @param amount 搬出する個数の最大値
     * @param simulate `true`の場合のみ実際に搬出を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬出された[ItemStack]
     */
    fun extractItem(amount: Int, simulate: Boolean, access: HTStorageAccess): ItemStack {
        val stack: ItemStack = getStack()
        if (isEmpty || amount < 1 || !canItemExtract(getStack(), access)) {
            return ItemStack.EMPTY
        }
        val current: Int = min(stack.count, stack.maxStackSize)
        val fixedAmount: Int = min(amount, current)
        val result: ItemStack = stack.copyWithCount(fixedAmount)
        if (!simulate) {
            shrinkStack(fixedAmount, false)
            onContentsChanged()
        }
        return result
    }

    /**
     * 指定された[stack]から，このスロットの容量を返します。
     */
    fun getLimit(stack: ItemStack): Int

    /**
     * 指定された[stack]をこのスロットが保持できるか判定します。
     * @return 保持できる場合は`true`
     */
    fun isItemValid(stack: ItemStack): Boolean

    /**
     * 指定された[stack]をこのスロットに搬入できるか判定します。
     * @param stack 搬入される[ItemStack]
     * @param access このスロットへのアクセスの種類
     * @return 搬入できる場合は`true`
     */
    fun isItemValidForInsert(stack: ItemStack, access: HTStorageAccess): Boolean = isItemValid(stack)

    /**
     * 指定された[stack]をこのスロットに搬出できるか判定します。
     * @param stack 搬出される[ItemStack]
     * @param access このスロットへのアクセスの種類
     * @return 搬出できる場合は`true`
     */
    fun canItemExtract(stack: ItemStack, access: HTStorageAccess): Boolean = true

    /**
     * GUIにおける[Slot]を返します。
     */
    fun createContainerSlot(): Slot?

    /**
     * 指定された[amount]から，現在の個数を置換します。
     * @param amount 置換する個数の最大値
     * @param simulate `true`の場合のみ実際に置換を行います。
     * @return 実際に置換された個数
     */
    fun setStackSize(amount: Int, simulate: Boolean): Int {
        if (isEmpty) return 0
        if (amount <= 0) {
            if (!simulate) setEmpty()
            return 0
        }
        val stack: ItemStack = getStack()
        val maxStackSize: Int = getLimit(stack)
        val fixedAmount: Int = min(amount, maxStackSize)
        if (stack.count == fixedAmount || simulate) {
            return fixedAmount
        }
        setStack(stack.copyWithCount(fixedAmount))
        return fixedAmount
    }

    /**
     * 指定された[amount]から，現在の個数に追加します。
     * @param amount 追加する個数の最大値
     * @param simulate `true`の場合のみ実際に追加を行います。
     * @return 実際に追加された個数
     */
    fun growStack(amount: Int, simulate: Boolean): Int {
        val current: Int = this.count
        if (current == 0) return 0
        val fixedAmount: Int = if (amount > 0) {
            min(amount, getLimit(getStack()))
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

    /**
     * 指定された[ingredient]から，現在の個数を削除します。
     * @param ingredient 削除する個数を提供する材料
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return 実際に削除された個数
     */
    fun shrinkStack(ingredient: HTItemIngredient, simulate: Boolean): Int = shrinkStack(ingredient.getRequiredAmount(getStack()), simulate)

    /**
     * 指定された[ingredient]から，現在の個数を削除します。
     * @param ingredient 削除する個数を提供する材料
     * @param simulate `true`の場合のみ実際に削除を行います。
     * @return [Optional.isEmpty]の場合は`0`，それ以外は実際に削除された個数
     */
    fun shrinkStack(ingredient: Optional<HTItemIngredient>, simulate: Boolean): Int = ingredient
        .map { ingredient1: HTItemIngredient -> shrinkStack(ingredient1, simulate) }
        .orElse(0)

    /**
     * このスロットが空かどうか判定します。
     */
    val isEmpty: Boolean get() = getStack().isEmpty

    /**
     * このスロットを空にします。
     */
    fun setEmpty() {
        setStack(ItemStack.EMPTY)
    }

    /**
     * このスロットの個数を返します。
     */
    val count: Int get() = getStack().count

    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag = buildNbt {
        put(RagiumConst.ITEM, getStack().saveOptional(provider))
    }
}
