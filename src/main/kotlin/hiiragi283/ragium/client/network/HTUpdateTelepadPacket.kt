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

class HTUpdateTelepadPacket(val pos: BlockPos, val teleportPos: HTTeleportPos) : HTCustomPayload.C2S {
    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<HTUpdateTelepadPacket>(RagiumAPI.id("update_telepad"))

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTUpdateTelepadPacket> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTUpdateTelepadPacket::pos,
            HTTeleportPos.CODEC.streamCodec,
            HTUpdateTelepadPacket::teleportPos,
            ::HTUpdateTelepadPacket,
        )
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateTelepadPacket> = TYPE

    override fun handle(player: ServerPlayer, server: MinecraftServer) {
        val telepad: HTTelepadBlockentity = player.level().getBlockEntity(pos) as? HTTelepadBlockentity ?: return
        telepad.updateDestination(teleportPos)
    }
}
