package hiiragi283.lib.network

import hiiragi283.lib.text.HTCommonTranslation
import net.minecraft.client.Minecraft
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

/**
 * [HTCustomPayload]の処理をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTPayloadHandlers {
    fun <T : HTCustomPayload.S2C> handleS2C(payload: T, context: IPayloadContext) {
        context
            .enqueueWork { payload.handle(context.player(), Minecraft.getInstance()) }
            .exceptionally { throwable: Throwable ->
                context.disconnect(HTCommonTranslation.INVALID_PACKET_S2C.translate(throwable.localizedMessage))
                null
            }
    }

    fun <T : HTCustomPayload.C2S> handleC2S(payload: T, context: IPayloadContext) {
        context
            .enqueueWork { payload.handle(context.player() as ServerPlayer) }
            .exceptionally { throwable: Throwable ->
                context.disconnect(HTCommonTranslation.INVALID_PACKET_C2S.translate(throwable.localizedMessage))
                null
            }
    }
}
