package hiiragi283.ragium.api.machine

import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos

data class HTMachinePacket(val key: HTMachineKey, val tier: HTMachineTier, val pos: BlockPos) : CustomPayload {
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

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.MACHINE_SYNC
}
