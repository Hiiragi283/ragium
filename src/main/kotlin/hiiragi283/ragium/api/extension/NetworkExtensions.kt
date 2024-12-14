package hiiragi283.ragium.api.extension

import hiiragi283.ragium.common.network.HTItemSyncPayload
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.s2c.play.TitleS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

//    Packet    //

/**
 * Run [action] for each [HTItemSyncPayload] from [this] inventory
 */
fun Inventory.sendS2CPacket(pos: BlockPos, action: (HTItemSyncPayload) -> Unit) {
    asMap().map { (slot: Int, stack: ItemStack) -> HTItemSyncPayload(pos, slot, stack) }.forEach(action)
}

//    Sending    //

/**
 * Run [action] for the first [ServerPlayerEntity] in [BlockEntity.world] on server side
 */
fun BlockEntity.sendPacket(action: (ServerPlayerEntity) -> Unit) {
    world?.ifServer {
        PlayerLookup.tracking(this@sendPacket)?.firstOrNull()?.let(action)
    }
}

fun BlockEntity.sendPacket(payload: CustomPayload) {
    sendPacket { player: ServerPlayerEntity -> ServerPlayNetworking.send(player, payload) }
}

/**
 * Run [action] for the first [ServerPlayerEntity] in world on server side
 */
fun World.sendPacket(action: (ServerPlayerEntity) -> Unit) {
    ifServer {
        PlayerLookup.world(this).firstOrNull()?.let(action)
    }
}

fun World.sendPacket(payload: CustomPayload) {
    sendPacket { player: ServerPlayerEntity -> ServerPlayNetworking.send(player, payload) }
}

/**
 * Run [action] for each [ServerPlayerEntity] in world on server side
 */
fun World.sendPacketForPlayers(action: (ServerPlayerEntity) -> Unit) {
    ifServer {
        PlayerLookup.world(this).forEach(action)
    }
}

/**
 * Send [payload] if [this] player is [ServerPlayerEntity]
 */
fun PlayerEntity.sendPacket(payload: CustomPayload) {
    asServerPlayer()?.let { ServerPlayNetworking.send(it, payload) }
}

fun PlayerEntity.sendTitle(title: Text) {
    asServerPlayer()?.networkHandler?.sendPacket(TitleS2CPacket(title))
}
