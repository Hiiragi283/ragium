package hiiragi283.ragium.client.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.common.block.entity.device.HTTelepadBlockentity
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

class HTTelepadUpdatePacket(val pos: BlockPos, val teleportPos: HTTeleportPos) : HTCustomPayload.C2S {
    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<HTTelepadUpdatePacket>(RagiumAPI.id("telepad_update"))

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTTelepadUpdatePacket> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTTelepadUpdatePacket::pos,
            HTTeleportPos.CODEC.streamCodec,
            HTTelepadUpdatePacket::teleportPos,
            ::HTTelepadUpdatePacket,
        )
    }

    override fun type(): CustomPacketPayload.Type<HTTelepadUpdatePacket> = TYPE

    override fun handle(player: ServerPlayer, server: MinecraftServer) {
        val telepad: HTTelepadBlockentity = player.level().getBlockEntity(pos) as? HTTelepadBlockentity ?: return
        telepad.updateDestination(teleportPos)
    }
}
