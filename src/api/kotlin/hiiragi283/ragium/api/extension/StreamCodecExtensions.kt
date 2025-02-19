package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

//    StreamCodec    //

/**
 * [List]の[StreamCodec]に変換します。
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.toList(): StreamCodec<B, List<V>> = apply(ByteBufCodecs.list())

/**
 * この[Codec]を[StreamCodec]に変換します。
 */
fun <T : Any> Codec<T>.toRegistryStream(): StreamCodec<RegistryFriendlyByteBuf, T> = ByteBufCodecs.fromCodecWithRegistries(this)

/**
 * この[MapCodec]を[StreamCodec]に変換します。
 */
fun <T : Any> MapCodec<T>.toRegistryStream(): StreamCodec<RegistryFriendlyByteBuf, T> = codec().toRegistryStream()
