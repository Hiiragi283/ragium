package hiiragi283.ragium.api.storage

import com.mojang.serialization.DataResult
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant
import net.minecraft.component.ComponentChanges
import net.minecraft.component.ComponentHolder
import net.minecraft.component.ComponentMap

/**
 * [net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount]を発展させたクラス
 * @param O オブジェクトの値
 * @param T [TransferVariant]を継承したクラス
 * @see HTItemVariantStack
 * @see HTFluidVariantStack
 */
abstract class HTVariantStack<O : Any, T : TransferVariant<O>>(val variant: T, val amount: Long) : ComponentHolder {
    val components: ComponentChanges
        get() = variant.components

    /**
     * [TransferVariant.isBlank]または[amount]が0以下の場合はtrue，それ以外の場合はfalse
     */
    val isEmpty: Boolean
        get() = variant.isBlank || amount <= 0

    val isNotEmpty: Boolean
        get() = !isEmpty

    /**
     * 指定した[other]と[variant]が一致するか判定します。
     */
    fun isOf(other: T): Boolean = variant == other

    /**
     * 指定した[obj]と[variant]のオブジェクトが一致するか判定します。
     */
    fun isOf(obj: O): Boolean = variant.isOf(obj)

    operator fun component1(): T = variant

    operator fun component2(): Long = amount

    companion object {
        @JvmStatic
        fun <T : HTVariantStack<*, *>> validate(stack: T): DataResult<T> {
            if (stack.isEmpty) {
                return DataResult.error { "Cannot encode/decode empty variant stack!" }
            }
            return DataResult.success(stack)
        }
    }

    //    ComponentHolder    //

    final override fun getComponents(): ComponentMap = variant.componentMap
}
