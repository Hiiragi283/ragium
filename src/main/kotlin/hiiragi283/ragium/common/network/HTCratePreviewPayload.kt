package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.storage.HTItemVariantStack
import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos

data class HTCratePreviewPayload(val pos: BlockPos, val stack: HTItemVariantStack) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTCratePreviewPayload> = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            HTCratePreviewPayload::pos,
            HTItemVariantStack.PACKET_CODEC,
            HTCratePreviewPayload::stack,
            ::HTCratePreviewPayload,
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.CRATE_PREVIEW
}
