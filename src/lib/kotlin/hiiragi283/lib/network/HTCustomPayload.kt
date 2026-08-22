package hiiragi283.lib.network

import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

/**
 * Hiiragi Seriesで使用される[CustomPacketPayload]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed interface HTCustomPayload : CustomPacketPayload {
    /**
     * サーバー側からクライアント側に送る[HTCustomPayload]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    interface S2C : HTCustomPayload {
        /**
         * 指定された引数からパケットを処理します。
         * @param player クライアント側のプレイヤー
         * @param minecraft クライアントのインスタンス
         */
        fun handle(player: Player, minecraft: Minecraft)
    }

    /**
     * クライアント側からサーバー側に送る[HTCustomPayload]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    interface C2S : HTCustomPayload {
        /**
         * 指定された引数からパケットを処理します。
         * @param player サーバー側のプレイヤー
         * @param server サーバーのインスタンス
         */
        fun handle(player: ServerPlayer)
    }
}
