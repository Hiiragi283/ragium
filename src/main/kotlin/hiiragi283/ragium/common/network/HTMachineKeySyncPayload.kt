package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos

data class HTMachineKeySyncPayload(val pos: BlockPos, val key: HTMachineKey) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineKeySyncPayload> = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            HTMachineKeySyncPayload::pos,
            HTMachineKey.PACKET_CODEC,
            HTMachineKeySyncPayload::key,
            ::HTMachineKeySyncPayload,
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.MACHINE_SYNC
}
