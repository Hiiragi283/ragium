package hiiragi283.ragium.api.data.interaction

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.context.UseOnContext

class HTPlaySoundBlockAction(sound: Holder<SoundEvent>, private val volume: Float, private val pitch: Float) : HTBlockAction {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTPlaySoundBlockAction> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    RegistryFixedCodec
                        .create(Registries.SOUND_EVENT)
                        .fieldOf("sound")
                        .forGetter(HTPlaySoundBlockAction::sound),
                    ExtraCodecs.POSITIVE_FLOAT
                        .optionalFieldOf("volume", 1f)
                        .forGetter(HTPlaySoundBlockAction::volume),
                    ExtraCodecs.POSITIVE_FLOAT
                        .optionalFieldOf("pitch", 1f)
                        .forGetter(HTPlaySoundBlockAction::pitch),
                ).apply(instance, ::HTPlaySoundBlockAction)
        }
    }

    constructor(
        sound: SoundEvent,
        volume: Float = 1f,
        pitch: Float = 1f,
    ) : this(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), volume, pitch)

    private val sound: Holder<SoundEvent> = sound.delegate

    override val codec: MapCodec<out HTBlockAction> = CODEC

    override fun applyAction(context: UseOnContext) {
        context.level.playSound(null, context.clickedPos, sound.value(), SoundSource.BLOCKS, volume, pitch)
    }
}
