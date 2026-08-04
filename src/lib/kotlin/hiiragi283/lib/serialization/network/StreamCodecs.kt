package hiiragi283.lib.serialization.network

import hiiragi283.lib.util.Option
import io.netty.buffer.ByteBuf
import java.util.Optional
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

//    Collection    //

/**
 * この[StreamCodec][this]を[Collection]の[StreamCodec]に変換します。
 * @param B パケットのクラス
 * @param V 値のクラス
 * @param factory 要素数からコレクションを作成するブロック
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <B : ByteBuf, V : Any, C : Collection<V>> StreamCodec<B, V>.toCollection(factory: (Int) -> C): StreamCodec<B, C> = ByteBufCodecs.collection(factory, this)

/**
 * この[StreamCodec][this]を[List]の[StreamCodec]に変換します。
 * @param B パケットのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.listOf(): StreamCodec<B, List<V>> = this.toCollection(::ArrayList)

/**
 * この[StreamCodec][this]を[Set]の[StreamCodec]に変換します。
 * @param B パケットのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.setOf(): StreamCodec<B, Set<V>> = this.toCollection(::LinkedHashSet)

/**
 * [Optional]の[StreamCodec]を[Option]の[StreamCodec]に変換します。
 * @param B パケットのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.asOption(): StreamCodec<B, Option<V>> = OptionStreamCodec(this)

/**
 * @suppress
 */
@JvmInline
private value class OptionStreamCodec<B : ByteBuf, V : Any>(private val codec: StreamCodec<B, V>) : StreamCodec<B, Option<V>> {
    override fun encode(output: B, value: Option<V>) {
        value.fold(
            { output.writeBoolean(false) },
            {
                output.writeBoolean(true)
                codec.encode(output, it)
            },
        )
    }

    override fun decode(input: B): Option<V> = when (input.readBoolean()) {
        true -> Option.some(codec.decode(input))
        false -> Option.none()
    }
}
