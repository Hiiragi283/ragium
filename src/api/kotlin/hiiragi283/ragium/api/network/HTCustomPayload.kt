package hiiragi283.ragium.api.network

import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

sealed interface HTCustomPayload : CustomPacketPayload {
    /**
     * @see [appeng.core.network.ClientboundPacket.handleOnClient]
     */
    interface S2C : HTCustomPayload {
        fun handle(context: IPayloadContext) {
            context.enqueueWork {
                val clientPlayer: AbstractClientPlayer = context.player() as? AbstractClientPlayer ?: return@enqueueWork
                handle(clientPlayer, Minecraft.getInstance())
            }
        }

        fun handle(player: AbstractClientPlayer, minecraft: Minecraft)
    }

    /**
     * @see [appeng.core.network.ServerboundPacket.handleOnServer]
     */
    interface C2S : HTCustomPayload {
        fun handle(context: IPayloadContext) {
            context.enqueueWork {
                val serverPlayer: ServerPlayer = context.player() as? ServerPlayer ?: return@enqueueWork
                handle(serverPlayer, serverPlayer.server)
            }
        }

        fun handle(player: ServerPlayer, server: MinecraftServer)
    }
}
