package hiiragi283.ragium.api.network

import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext
import net.neoforged.neoforge.network.registration.PayloadRegistrar

class HTPayloadRegister(private val registrar: PayloadRegistrar) {
    fun <T : HTCustomPayload.S2C> registerS2C(type: CustomPacketPayload.Type<T>, streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>) {
        registrar.playToClient(type, streamCodec) { payload: T, context: IPayloadContext ->
            context
                .enqueueWork {
                    val clientPlayer: AbstractClientPlayer = context.player() as? AbstractClientPlayer ?: return@enqueueWork
                    payload.handle(clientPlayer, Minecraft.getInstance())
                }.exceptionally { throwable: Throwable ->
                    context.disconnect(Component.literal("Failed to handle S2C packet: ${throwable.localizedMessage}"))
                    null
                }
        }
    }

    fun <T : HTCustomPayload.C2S> registerC2S(type: CustomPacketPayload.Type<T>, streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>) {
        registrar.playToServer(type, streamCodec) { payload: T, context: IPayloadContext ->
            context
                .enqueueWork {
                    val serverPlayer: ServerPlayer = context.player() as? ServerPlayer ?: return@enqueueWork
                    payload.handle(serverPlayer, serverPlayer.server)
                }.exceptionally { throwable: Throwable ->
                    context.disconnect(Component.literal("Failed to handle C2S packet: ${throwable.localizedMessage}"))
                    null
                }
        }
    }
}
