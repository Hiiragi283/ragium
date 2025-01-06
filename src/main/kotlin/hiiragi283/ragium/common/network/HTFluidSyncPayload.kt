package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

data class HTFluidSyncPayload(val index: Int, val stack: HTFluidVariantStack) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFluidSyncPayload> = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            HTFluidSyncPayload::index,
            HTFluidVariantStack.PACKET_CODEC,
            HTFluidSyncPayload::stack,
            ::HTFluidSyncPayload,
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.FLUID_SYNC
}
