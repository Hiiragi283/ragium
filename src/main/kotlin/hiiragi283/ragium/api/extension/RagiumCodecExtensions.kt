package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.minecraft.block.entity.BlockEntity
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.s2c.play.TitleS2CPacket
import net.minecraft.registry.RegistryKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable
import net.minecraft.world.PersistentState
import net.minecraft.world.World

//    Network    //

fun BlockEntity.sendPacket(action: (ServerPlayerEntity) -> Unit) {
    val world: World = world ?: return
    if (!world.isClient) {
        PlayerLookup.tracking(this)?.firstOrNull()?.let(action)
    }
}

fun World.sendPacketForPlayers(action: (ServerPlayerEntity) -> Unit) {
    (this as? ServerWorld)?.let(PlayerLookup::world)?.forEach(action)
}

fun ServerPlayerEntity.sendTitle(title: Text) {
    networkHandler.sendPacket(TitleS2CPacket(title))
}

//    Codec    //

/*inline fun <reified T : StringIdentifiable> List<T>.createCodec(): Codec<T> =
    StringIdentifiable.BasicCodec(this.toTypedArray(), this::matchName, this::indexOf)

fun <T : StringIdentifiable> Iterable<T>.matchName(name: String): T = first { it.asString() == name }

fun <T : StringIdentifiable> Iterable<T>.matchNameOrNull(name: String): T? = firstOrNull { it.asString() == name }*/

// fun <T : StringIdentifiable> codecOf(from: (String) -> T): Codec<T> = Codec.STRING.xmap(from, StringIdentifiable::asString)

fun <T : StringIdentifiable> codecOf(entries: Iterable<T>): Codec<T> = Codec.STRING.xmap(
    { name: String -> entries.firstOrNull { it.asString() == name } },
    StringIdentifiable::asString,
)

//    PacketCodec    //

fun <B : ByteBuf, V : Any> PacketCodec<B, V>.toList(): PacketCodec<B, List<V>> = collect(PacketCodecs.toList())

// fun <T : StringIdentifiable> packetCodecOf(from: (String) -> T): PacketCodec<RegistryByteBuf, T> =
//     PacketCodec.tuple(PacketCodecs.STRING, StringIdentifiable::asString, from)

fun <T : StringIdentifiable> packetCodecOf(entries: Iterable<T>): PacketCodec<RegistryByteBuf, T> = PacketCodec.tuple(
    PacketCodecs.STRING,
    StringIdentifiable::asString,
) { name: String -> entries.firstOrNull { it.asString() == name } }

//    PersistentState    //

fun <T : PersistentState> getState(world: ServerWorld, type: PersistentState.Type<T>, id: Identifier): T = world.persistentStateManager
    .getOrCreate(type, id.splitWith('_'))
    .apply { markDirty() }

fun <T : PersistentState> getState(world: World, type: PersistentState.Type<T>, id: Identifier): T? {
    val key: RegistryKey<World> = world.registryKey
    val server: MinecraftServer = world.server ?: return null
    return getState(server, key, type, id)
}

fun <T : PersistentState> getState(
    server: MinecraftServer,
    key: RegistryKey<World>,
    type: PersistentState.Type<T>,
    id: Identifier,
): T? = server.getWorld(key)?.let { getState(it, type, id) }
