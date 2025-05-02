package hiiragi283.ragium.api.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.HTCodecs
import hiiragi283.ragium.api.data.HTStreamCodecs
import hiiragi283.ragium.api.extension.listOf
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.UseAnim

data class HTConsumableData(
    val consumeSeconds: Int = 32,
    val animation: UseAnim = UseAnim.EAT,
    val sound: SoundEvent = SoundEvents.GENERIC_EAT,
    val consumeEffects: List<HTConsumeEffect> = listOf(),
) {
    companion object {
        @JvmField
        val CODEC: Codec<HTConsumableData> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("consume_seconds", 320).forGetter(HTConsumableData::consumeSeconds),
                    HTCodecs.USE_ANIM.optionalFieldOf("animation", UseAnim.EAT).forGetter(HTConsumableData::animation),
                    BuiltInRegistries.SOUND_EVENT
                        .byNameCodec()
                        .optionalFieldOf(
                            "sound",
                            SoundEvents.GENERIC_EAT,
                        ).forGetter(HTConsumableData::sound),
                    HTConsumeEffect.CODEC
                        .listOf()
                        .optionalFieldOf("on_consume_effects", listOf())
                        .forGetter(HTConsumableData::consumeEffects),
                ).apply(instance, ::HTConsumableData)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTConsumableData> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            HTConsumableData::consumeSeconds,
            HTStreamCodecs.USE_ANIM,
            HTConsumableData::animation,
            ByteBufCodecs.registry(Registries.SOUND_EVENT),
            HTConsumableData::sound,
            HTConsumeEffect.STREAM_CODEC.listOf(),
            HTConsumableData::consumeEffects,
            ::HTConsumableData,
        )
    }
}
