package hiiragi283.ragium.common.network

import hiiragi283.ragium.common.init.RagiumNetworks
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.impl.transfer.VariantCodecs
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos

@Suppress("UnstableApiUsage")
data class HTCratePreviewPayload(val pos: BlockPos, val variant: ItemVariant, val amount: Long) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTCratePreviewPayload> = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            HTCratePreviewPayload::pos,
            VariantCodecs.ITEM_PACKET_CODEC,
            HTCratePreviewPayload::variant,
            PacketCodecs.VAR_LONG,
            HTCratePreviewPayload::amount,
            ::HTCratePreviewPayload,
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.CRATE_PREVIEW
}
