package hiiragi283.ragium.api.stack

import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.text.HTHasText
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.IWithData

/**
 * 不変なスタックのラッパー
 *
 * EMPTYなんか大っ嫌い！
 * @param TYPE スタックが保持する種類のクラス
 * @param STACK [ImmutableStack]を実装したクラス
 */
interface ImmutableStack<TYPE : Any, STACK : ImmutableStack<TYPE, STACK>> :
    HTHasText,
    HTHolderLike,
    IWithData<TYPE> {
    /**
     * このスタックの種類を返します。
     */
    fun value(): TYPE

    /**
     * このスタックの種類の[Holder]を返します。
     */
    fun holder(): Holder<TYPE>

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

    override fun getId(): ResourceLocation = holder().idOrThrow

    override fun <T : Any> getData(type: DataMapType<TYPE, T>): T? = holder().getData(type)
}
