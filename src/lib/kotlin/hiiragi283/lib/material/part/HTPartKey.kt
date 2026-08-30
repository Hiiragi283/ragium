package hiiragi283.lib.material.part

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * 部品の種類を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
@JvmRecord
data class HTPartKey(val name: String) : Comparable<HTPartKey> {
    companion object {
        @JvmField
        val CODEC: Codec<HTPartKey> = Codec.STRING.xmap(::HTPartKey, HTPartKey::name)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTPartKey> = ByteBufCodecs.STRING_UTF8.map(::HTPartKey, HTPartKey::name)
    }

    override fun compareTo(other: HTPartKey): Int = this.name.compareTo(other.name)
}
