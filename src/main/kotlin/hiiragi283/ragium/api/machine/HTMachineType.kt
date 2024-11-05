package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.component.ComponentType
import net.minecraft.network.codec.PacketCodec

class HTMachineType(override val key: HTMachineKey) : HTMachine {
    companion object {
        @JvmField
        val CODEC: Codec<HTMachineType> =
            HTMachineKey.CODEC.xmap(
                ::HTMachineType,
                HTMachineType::key,
            )

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType> =
            HTMachineKey.PACKET_CODEC.xmap(
                ::HTMachineType,
                HTMachineType::key,
            )

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTMachineType> = ComponentType
            .builder<HTMachineType>()
            .codec(CODEC)
            .packetCodec(PACKET_CODEC)
            .build()
    }

    override fun toString(): String = "MachineType[${key.id}]"
}
