package hiiragi283.ragium.client.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.api.storage.HTTransferIO
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

data class HTTransferIOUpdatePayload(val pos: BlockPos, val direction: Direction, val transferIO: HTTransferIO) : HTCustomPayload.C2S {
    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<HTTransferIOUpdatePayload>(RagiumAPI.id("transfer_io_update"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTTransferIOUpdatePayload> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTTransferIOUpdatePayload::pos,
            Direction.STREAM_CODEC,
            HTTransferIOUpdatePayload::direction,
            HTTransferIO.STREAM_CODEC,
            HTTransferIOUpdatePayload::transferIO,
            ::HTTransferIOUpdatePayload,
        )
    }

    override fun handle(player: ServerPlayer, server: MinecraftServer) {
        val machine: HTMachineBlockEntity = player.serverLevel().getBlockEntity(pos) as? HTMachineBlockEntity ?: return
        machine.transferIOCache[direction] = transferIO
        machine.setChanged()
    }

    override fun type(): CustomPacketPayload.Type<HTTransferIOUpdatePayload> = TYPE
}
