package hiiragi283.lib.serialization.network

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

//    Collection    //

/**
 * この[StreamCodec][this]を[List]の[StreamCodec]に変換します。
 * @param B パケットのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.listOf(): StreamCodec<B, List<V>> = this.apply(ByteBufCodecs.list())
