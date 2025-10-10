package hiiragi283.ragium.api.storage

import com.google.common.base.Predicates
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch
import java.util.function.Predicate

interface HTStorageStack<T : Any> : DataComponentHolder {
    companion object {
        @JvmStatic
        fun <STACK : HTStorageStack<*>> alwaysTrue(): Predicate<STACK> = Predicates.alwaysTrue<STACK>()
    }

    fun isEmpty(): Boolean

    fun value(): T

    fun holder(): Holder<T>

    fun amountAsLong(): Long = amountAsInt().toLong()

    fun amountAsInt(): Int

    fun copy(): HTStorageStack<T>

    fun copyWithAmount(amount: Int): HTStorageStack<T>

    fun componentsPatch(): DataComponentPatch
}
