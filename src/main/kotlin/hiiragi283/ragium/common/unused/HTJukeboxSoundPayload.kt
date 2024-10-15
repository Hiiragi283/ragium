package hiiragi283.ragium.common.unused

import net.minecraft.block.jukebox.JukeboxSong
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

sealed interface HTJukeboxSoundPayload : CustomPayload {
    companion object {
        @JvmStatic
        fun create(sound: RegistryKey<JukeboxSong>?): HTJukeboxSoundPayload = when (sound != null) {
            true -> Play(sound)
            false -> Stop
        }
    }

    val sound: RegistryKey<JukeboxSong>?

    data class Play(override val sound: RegistryKey<JukeboxSong>) : HTJukeboxSoundPayload {
        constructor(id: Identifier) : this(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, id))

        val id: Identifier = sound.value

        companion object {
            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Play> = PacketCodec.tuple(
                Identifier.PACKET_CODEC,
                Play::id,
                HTJukeboxSoundPayload::Play,
            )
        }

        override fun getId(): CustomPayload.Id<out CustomPayload> = TODO()
    }

    data object Stop : HTJukeboxSoundPayload {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, Stop> = PacketCodec.unit(Stop)

        override val sound: RegistryKey<JukeboxSong>? = null

        override fun getId(): CustomPayload.Id<out CustomPayload> = TODO()
    }
}
