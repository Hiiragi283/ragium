package hiiragi283.ragium.api.storage

import com.google.common.base.Predicates
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.text.HTHasText
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import java.util.function.Predicate

/**
 * [HTStackSlot]で使われるスタックのラッパー
 * @param T スタックが保持する種類のクラス
 * @param STACK [HTStackSlot]を実装したクラス
 */
interface HTStorageStack<T : Any, STACK : HTStorageStack<T, STACK>> :
    DataComponentHolder,
    HTHasText,
    HTHolderLike {
    companion object {
        @JvmStatic
        fun <STACK : HTStorageStack<*, *>> alwaysTrue(): Predicate<STACK> = Predicates.alwaysTrue<STACK>()
    }

    /**
     * このスタックが空からどうか判定します。
     */
    fun isEmpty(): Boolean

    /**
     * このスタックの種類を返します。
     */
    fun value(): T

    /**
     * このスタックの種類の[Holder]を返します。
     */
    fun holder(): Holder<T>

    /**
     * このスタックの量を[Long]値で返します。
     */
    fun amountAsLong(): Long = amountAsInt().toLong()

    /**
     * このスタックの量を[Int]値で返します。
     */
    fun amountAsInt(): Int

    /**
     * このスタックのコピーを返します。。
     */
    fun copy(): STACK

    /**
     * このスタックのコピーを指定した個数で返します。
     * @param amount コピー後の個数
     */
    fun copyWithAmount(amount: Int): STACK

    /**
     * このスタックの[DataComponentPatch]を返します。
     */
    fun componentsPatch(): DataComponentPatch

    override fun getId(): ResourceLocation = holder().idOrThrow
}
