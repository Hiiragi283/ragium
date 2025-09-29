package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.sounds.SoundEvent

@ConsistentCopyVisibility
data class HTItemSoundEvent private constructor(val holder: Holder<SoundEvent>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemSoundEvent> =
            BiCodecs.holder(Registries.SOUND_EVENT).xmap(::HTItemSoundEvent, HTItemSoundEvent::holder)

        @JvmStatic
        fun create(sound: SoundEvent): HTItemSoundEvent = create(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound))

        @JvmStatic
        fun create(holder: Holder<SoundEvent>): HTItemSoundEvent = HTItemSoundEvent(holder.delegate)
    }

    val sound: SoundEvent get() = holder.value()
}
