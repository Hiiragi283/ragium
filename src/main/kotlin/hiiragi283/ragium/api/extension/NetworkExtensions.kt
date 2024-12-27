package hiiragi283.ragium.api.extension

import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.s2c.play.TitleS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.world.World

//    Sending    //

/**
 * サーバー側からクライアント側に向けた[payload]を送信します。
 */
fun BlockEntity.sendPacket(payload: CustomPayload) {
    world?.ifServer { PlayerLookup.tracking(this@sendPacket).firstOrNull()?.sendPacket(payload) }
}

/**
 * サーバー側からクライアント側に向けた[payload]を送信します。
 */
fun World.sendPacket(payload: CustomPayload) {
    ifServer { PlayerLookup.world(this).firstOrNull()?.sendPacket(payload) }
}

/**
 * サーバー側からクライアント側に向けた[payload]を送信します。
 */
fun PlayerEntity.sendPacket(payload: CustomPayload) {
    asServerPlayer()?.sendPacket(payload)
}

/**
 * サーバー側からクライアント側に向けた[payload]を送信します。
 */
fun ServerPlayerEntity.sendPacket(payload: CustomPayload) {
    asServerPlayer()?.let { ServerPlayNetworking.send(it, payload) }
}

/**
 * サーバー側からクライアント側に向けたタイトルを送信します。
 */
fun PlayerEntity.sendTitle(title: Text) {
    asServerPlayer()?.networkHandler?.sendPacket(TitleS2CPacket(title))
}
