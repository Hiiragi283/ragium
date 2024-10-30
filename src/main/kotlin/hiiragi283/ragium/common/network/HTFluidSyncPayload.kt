package hiiragi283.ragium.common.network

import hiiragi283.ragium.common.init.RagiumNetworks
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

data class HTFluidSyncPayload(val index: Int, val variant: FluidVariant, val amount: Long) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFluidSyncPayload> = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            HTFluidSyncPayload::index,
            FluidVariant.PACKET_CODEC,
            HTFluidSyncPayload::variant,
            PacketCodecs.VAR_LONG,
            HTFluidSyncPayload::amount,
            ::HTFluidSyncPayload,
        )
    }

    val resourceAmount: ResourceAmount<FluidVariant> = ResourceAmount(variant, amount)

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.FLUID_SYNC
}
