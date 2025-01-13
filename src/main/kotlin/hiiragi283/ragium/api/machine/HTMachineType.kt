package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.stringCodec
import hiiragi283.ragium.api.extension.stringStreamCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable

/**
 * 機械のタイプを管理するクラス
 */
enum class HTMachineType : StringRepresentable {
    GENERATOR,
    PROCESSOR,
    CONSUMER,
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineType> = stringCodec(HTMachineType.entries)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMachineType> = stringStreamCodec(HTMachineType.entries)
    }

    override fun getSerializedName(): String = name.lowercase()
}
