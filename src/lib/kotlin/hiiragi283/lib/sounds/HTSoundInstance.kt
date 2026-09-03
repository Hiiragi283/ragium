package hiiragi283.lib.sounds

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent

/**
 * SEに関するデータをまとめたクラスです。
 * @param sound SEの種類
 * @param volume SEの音量
 * @param pitch SEのピッチ
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTSoundInstance @JvmOverloads constructor(
    val sound: Holder<SoundEvent>,
    val volume: Float = 1f,
    val pitch: Float = 1f
) {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTSoundInstance> = HTCodecs.recordMap { instance ->
            instance.group(
                SoundEvent.CODEC.fieldOf("sound").forGetter(HTSoundInstance::sound),
                Codec.floatRange(0f, 1f).optionalFieldOf("volume", 1f).forGetter(HTSoundInstance::volume),
                Codec.floatRange(0f, 1f).optionalFieldOf("pitch", 1f).forGetter(HTSoundInstance::pitch)
            ).apply(instance, ::HTSoundInstance)
        }

        @JvmField
        val CODEC: Codec<HTSoundInstance> = MAP_CODEC.codec()

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSoundInstance> = StreamCodec.composite(
            SoundEvent.STREAM_CODEC,
            HTSoundInstance::sound,
            ByteBufCodecs.FLOAT,
            HTSoundInstance::volume,
            ByteBufCodecs.FLOAT,
            HTSoundInstance::pitch,
            ::HTSoundInstance
        )
    }

    @JvmOverloads
    constructor(
        sound: SoundEvent,
        volume: Float = 1f,
        pitch: Float = 1f
    ) : this(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), volume, pitch)
}
