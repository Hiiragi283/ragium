package hiiragi283.ragium.api.machine

import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.math.BlockPos

data class HTMachinePacket(val key: HTMachineKey, val tier: HTMachineTier, val pos: BlockPos) {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachinePacket> = PacketCodec.tuple(
            HTMachineKey.PACKET_CODEC,
            HTMachinePacket::key,
            HTMachineTier.PACKET_CODEC,
            HTMachinePacket::tier,
            BlockPos.PACKET_CODEC,
            HTMachinePacket::pos,
            ::HTMachinePacket,
        )
    }
}
