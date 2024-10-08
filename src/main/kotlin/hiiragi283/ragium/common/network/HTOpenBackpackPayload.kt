package hiiragi283.ragium.common.network

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.init.RagiumNetworks
import hiiragi283.ragium.common.util.createCodec
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
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
        val CODEC: Codec<HTOpenBackpackPayload> = HTOpenBackpackPayload.entries.createCodec()

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTOpenBackpackPayload> = PacketCodecs.registryCodec(CODEC)
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.OPEN_BACKPACK

    override fun asString(): String = name.lowercase()
}
