package hiiragi283.ragium.common.network

import hiiragi283.ragium.common.init.RagiumNetworks
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos

data class HTFluidStoragePayload(
    val pos: BlockPos,
    val index: Int,
    val variant: FluidVariant,
    val amount: Long,
) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFluidStoragePayload> = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            HTFluidStoragePayload::pos,
            PacketCodecs.VAR_INT,
            HTFluidStoragePayload::index,
            FluidVariant.PACKET_CODEC,
            HTFluidStoragePayload::variant,
            PacketCodecs.VAR_LONG,
            HTFluidStoragePayload::amount,
            ::HTFluidStoragePayload,
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.FLUID_STORAGE
}
