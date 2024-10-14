package hiiragi283.ragium.common.network

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.packetCodecOf
import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.StringIdentifiable

enum class HTOpenBackpackPayload :
    CustomPayload,
    StringIdentifiable {
    NORMAL,
    ENDER,
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTOpenBackpackPayload> = codecOf(entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTOpenBackpackPayload> = packetCodecOf(entries)

        @JvmStatic
        fun fromString(name: String): HTOpenBackpackPayload = entries.first { it.asString() == name }
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.OPEN_BACKPACK

    override fun asString(): String = name.lowercase()
}
