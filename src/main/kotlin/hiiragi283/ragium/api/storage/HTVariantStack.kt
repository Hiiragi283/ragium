package hiiragi283.ragium.api.storage

import com.mojang.serialization.DataResult
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant

/**
 * Advanced [net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount] which checks [TransferVariant.isBlank] and negative amount
 */
interface HTVariantStack<O : Any, T : TransferVariant<O>> {
    val variant: T
    val amount: Long

    val isEmpty: Boolean
        get() = variant.isBlank || amount <= 0

    val isNotEmpty: Boolean
        get() = !isEmpty

    fun isOf(other: T): Boolean = variant == other

    fun isOf(obj: O): Boolean = variant.isOf(obj)

    operator fun component1(): T = variant

    operator fun component2(): Long = amount

    companion object {
        /**
         * Use for [com.mojang.serialization.Codec.validate]
         */
        @JvmStatic
        fun <T : HTVariantStack<*, *>> validate(stack: T): DataResult<T> {
            if (stack.isEmpty) {
                return DataResult.error { "Cannot encode/decode empty variant stack!" }
            }
            return DataResult.success(stack)
        }
    }
}
