package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.network.HTCustomPayload
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.neoforged.neoforge.network.PacketDistributor

object HTPacketHelper {
    //    S2C    //

    @JvmStatic
    fun sendToClient(level: ServerLevel, pos: BlockPos, packet: HTCustomPayload.S2C?) {
        if (packet == null) return
        PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos(pos), packet)
    }

    //    C2S    //

    @JvmStatic
    fun sendToServer(packet: HTCustomPayload.C2S?) {
        if (packet == null) return
        PacketDistributor.sendToServer(packet)
    }
}
