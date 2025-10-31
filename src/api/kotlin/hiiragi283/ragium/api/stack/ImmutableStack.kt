package hiiragi283.ragium.api.stack

import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.text.HTHasText
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation

/**
 * 不変なスタックのラッパー
 *
 * EMPTYなんか大っ嫌い！
 * @param T スタックが保持する種類のクラス
 * @param STACK [ImmutableStack]を実装したクラス
 */
interface ImmutableStack<T : Any, STACK : ImmutableStack<T, STACK>> :
    DataComponentHolder,
    HTHasText,
    HTHolderLike {
    /**
     * このスタックの種類を返します。
     */
    fun value(): T

    /**
     * このスタックの種類の[Holder]を返します。
     */
    fun holder(): Holder<T>

    /**
     * このスタックの量を[Int]値で返します。
     */
    fun amount(): Int

    /**
     * このスタックのコピーを指定した個数で返します。
     * @param amount コピー後の個数
     * @return 新しいスタックが無効の場合は`null`
     */
    fun copyWithAmount(amount: Int): STACK?

    /**
     * このスタックの[DataComponentPatch]を返します。
     */
    fun componentsPatch(): DataComponentPatch

    override fun getId(): ResourceLocation = holder().idOrThrow
}
