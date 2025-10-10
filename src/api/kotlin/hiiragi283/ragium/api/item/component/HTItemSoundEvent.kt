package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.sounds.SoundEvent

@JvmInline
value class HTItemSoundEvent private constructor(val holder: Holder<SoundEvent>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemSoundEvent> =
            VanillaBiCodecs.holder(Registries.SOUND_EVENT).xmap(::HTItemSoundEvent, HTItemSoundEvent::holder)

        @JvmStatic
        fun create(sound: SoundEvent): HTItemSoundEvent = create(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound))

        @JvmStatic
        fun create(holder: Holder<SoundEvent>): HTItemSoundEvent = HTItemSoundEvent(holder.delegate)
    }

    val sound: SoundEvent get() = holder.value()
}
