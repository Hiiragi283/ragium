package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.packetCodecOf
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.StringIdentifiable

enum class HTMachineType : StringIdentifiable {
    GENERATOR,
    PROCESSOR,
    CONSUMER,
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineType> = codecOf(entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineType> = packetCodecOf(entries)
    }

    override fun asString(): String = name.lowercase()
}
