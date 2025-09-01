package hiiragi283.ragium.client.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTTransferIO
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

data class HTUpdateTransferIOPayload(val pos: BlockPos, val direction: Direction, val transferIO: HTTransferIO) : HTCustomPayload.C2S {
    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<HTUpdateTransferIOPayload>(RagiumAPI.id("update_transfer_io"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTUpdateTransferIOPayload> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTUpdateTransferIOPayload::pos,
            Direction.STREAM_CODEC,
            HTUpdateTransferIOPayload::direction,
            HTTransferIO.CODEC.streamCodec,
            HTUpdateTransferIOPayload::transferIO,
            ::HTUpdateTransferIOPayload,
        )
    }

    override fun handle(player: ServerPlayer, server: MinecraftServer) {
        val receiver: HTTransferIO.Receiver = player.serverLevel().getBlockEntity(pos) as? HTTransferIO.Receiver ?: return
        receiver[direction] = transferIO
        if (receiver is HTContentListener) {
            receiver.onContentsChanged()
        }
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateTransferIOPayload> = TYPE
}
