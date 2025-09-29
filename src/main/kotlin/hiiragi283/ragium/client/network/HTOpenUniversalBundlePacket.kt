package hiiragi283.ragium.client.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.common.item.HTUniversalBundleItem
import hiiragi283.ragium.setup.RagiumItems
import io.netty.buffer.ByteBuf
import io.wispforest.accessories.api.AccessoriesCapability
import io.wispforest.accessories.api.slot.SlotEntryReference
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

data object HTOpenUniversalBundlePacket : HTCustomPayload.C2S {
    @JvmField
    val TYPE: CustomPacketPayload.Type<HTOpenUniversalBundlePacket> = CustomPacketPayload.Type(RagiumAPI.id("open_universal_bundle"))

    @JvmField
    val STREAM_CODEC: StreamCodec<ByteBuf, HTOpenUniversalBundlePacket> = StreamCodec.unit(HTOpenUniversalBundlePacket)

    override fun type(): CustomPacketPayload.Type<HTOpenUniversalBundlePacket> = TYPE

    override fun handle(player: ServerPlayer, server: MinecraftServer) {
        val capability: AccessoriesCapability = RagiumAPI.INSTANCE.getAccessoryCap(player) ?: return
        val slot: SlotEntryReference = capability.getFirstEquipped(RagiumItems.UNIVERSAL_BUNDLE.get()) ?: return
        HTUniversalBundleItem.openBundle(player.level(), player, slot.stack)
    }
}
