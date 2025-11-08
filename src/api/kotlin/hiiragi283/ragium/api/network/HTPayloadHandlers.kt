package hiiragi283.ragium.api.network

import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

object HTPayloadHandlers {
    fun <T : HTCustomPayload.S2C> handleS2C(payload: T, context: IPayloadContext) {
        context
            .enqueueWork {
                val clientPlayer: AbstractClientPlayer = context.player() as? AbstractClientPlayer ?: return@enqueueWork
                payload.handle(clientPlayer, Minecraft.getInstance())
            }.exceptionally { throwable: Throwable ->
                context.disconnect(Component.literal("Failed to handle S2C packet: ${throwable.localizedMessage}"))
                null
            }
    }

    fun <T : HTCustomPayload.C2S> handleC2S(payload: T, context: IPayloadContext) {
        context
            .enqueueWork {
                val serverPlayer: ServerPlayer = context.player() as? ServerPlayer ?: return@enqueueWork
                payload.handle(serverPlayer, serverPlayer.server)
            }.exceptionally { throwable: Throwable ->
                context.disconnect(Component.literal("Failed to handle C2S packet: ${throwable.localizedMessage}"))
                null
            }
    }

    fun <T> handleBoth(payload: T, context: IPayloadContext) where T : HTCustomPayload.S2C, T : HTCustomPayload.C2S {
        if (context.player().level().isClientSide) {
            handleS2C(payload, context)
        } else {
            handleC2S(payload, context)
        }
    }
}
