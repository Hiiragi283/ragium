package hiiragi283.ragium.api.upgrade

import com.google.common.collect.Maps
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs

data class HTUpgrade(val base: Float, val attributeMap: Map<String, Float> = mapOf()) {
    companion object {
        @JvmStatic
        private val RAW_CODEC: Codec<Map<String, Float>> = Codec.unboundedMap(Codec.STRING, ExtraCodecs.POSITIVE_FLOAT)

        @JvmField
        val CODEC: Codec<HTUpgrade> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("base").forGetter(HTUpgrade::base),
                    RAW_CODEC.optionalFieldOf("attributes", mapOf()).forGetter(HTUpgrade::attributeMap),
                ).apply(instance, ::HTUpgrade)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTUpgrade> = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            HTUpgrade::base,
            ByteBufCodecs.map(
                Maps::newHashMapWithExpectedSize,
                ByteBufCodecs.STRING_UTF8,
                ByteBufCodecs.FLOAT,
            ),
            HTUpgrade::attributeMap,
            ::HTUpgrade,
        )
    }

    fun getValue(key: String): Float = attributeMap[key] ?: base
}
