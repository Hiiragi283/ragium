package hiiragi283.ragium.api.machine

import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.math.BlockPos

data class HTMachinePacket(val machineType: HTMachineType, val tier: HTMachineTier, val pos: BlockPos) {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachinePacket> = PacketCodec.tuple(
            HTMachineTypeRegistry.PACKET_CODEC,
            HTMachinePacket::machineType,
            HTMachineTier.PACKET_CODEC,
            HTMachinePacket::tier,
            BlockPos.PACKET_CODEC,
            HTMachinePacket::pos,
            ::HTMachinePacket,
        )
    }
}
