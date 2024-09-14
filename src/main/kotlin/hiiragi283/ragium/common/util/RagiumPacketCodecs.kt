package hiiragi283.ragium.common.util

import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec

object RagiumPacketCodecs {

    @JvmField
    val UNIT_KT: PacketCodec<in RegistryByteBuf, Unit> = PacketCodec.unit(Unit)

}