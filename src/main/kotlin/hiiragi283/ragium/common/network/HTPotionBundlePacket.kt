package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data object HTPotionBundlePacket : CustomPacketPayload {
    @JvmField
    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTPotionBundlePacket> =
        StreamCodec.unit(HTPotionBundlePacket)

    @JvmField
    val TYPE = CustomPacketPayload.Type<HTPotionBundlePacket>(RagiumAPI.id("potion_bundle"))

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
