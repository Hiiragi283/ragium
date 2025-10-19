package hiiragi283.ragium.client.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfigSetter
import hiiragi283.ragium.api.util.access.HTAccessConfiguration
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

@JvmRecord
data class HTUpdateAccessConfigPayload(val pos: BlockPos, val direction: Direction, val transferIO: HTAccessConfiguration) :
    HTCustomPayload.C2S {
    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<HTUpdateAccessConfigPayload>(RagiumAPI.id("update_access_config"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTUpdateAccessConfigPayload> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTUpdateAccessConfigPayload::pos,
            Direction.STREAM_CODEC,
            HTUpdateAccessConfigPayload::direction,
            HTAccessConfiguration.CODEC.streamCodec,
            HTUpdateAccessConfigPayload::transferIO,
            ::HTUpdateAccessConfigPayload,
        )
    }

    override fun handle(player: ServerPlayer, server: MinecraftServer) {
        val receiver: HTAccessConfigSetter = player.serverLevel().getBlockEntity(pos) as? HTAccessConfigSetter ?: return
        receiver.setAccessConfig(direction, transferIO)
        if (receiver is HTContentListener) {
            receiver.onContentsChanged()
        }
    }

    override fun type(): CustomPacketPayload.Type<HTUpdateAccessConfigPayload> = TYPE
}
