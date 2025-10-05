package hiiragi283.ragium.client.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import io.netty.buffer.ByteBuf
import io.wispforest.accessories.api.AccessoriesCapability
import io.wispforest.accessories.api.slot.SlotEntryReference
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

data object HTOpenPotionBundlePacket : HTCustomPayload.C2S {
    @JvmField
    val TYPE: CustomPacketPayload.Type<HTOpenPotionBundlePacket> = CustomPacketPayload.Type(RagiumAPI.id("open_potion_bundle"))

    @JvmField
    val STREAM_CODEC: StreamCodec<ByteBuf, HTOpenPotionBundlePacket> = StreamCodec.unit(HTOpenPotionBundlePacket)

    override fun type(): CustomPacketPayload.Type<HTOpenPotionBundlePacket> = TYPE

    override fun handle(player: ServerPlayer, server: MinecraftServer) {
        val capability: AccessoriesCapability = RagiumPlatform.INSTANCE.getAccessoryCap(player) ?: return
        val slot: SlotEntryReference = capability.getFirstEquipped(RagiumItems.POTION_BUNDLE.get()) ?: return
        RagiumMenuTypes.POTION_BUNDLE.openMenu(player, null, slot.stack)
    }
}
