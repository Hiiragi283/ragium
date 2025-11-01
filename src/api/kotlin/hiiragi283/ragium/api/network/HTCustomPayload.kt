package hiiragi283.ragium.api.network

import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

sealed interface HTCustomPayload : CustomPacketPayload {
    /**
     * @see appeng.core.network.ClientboundPacket.handleOnClient
     */
    interface S2C : HTCustomPayload {
        fun handle(player: AbstractClientPlayer, minecraft: Minecraft)
    }

    /**
     * @see appeng.core.network.ServerboundPacket.handleOnServer
     */
    interface C2S : HTCustomPayload {
        fun handle(player: ServerPlayer, server: MinecraftServer)
    }
}
