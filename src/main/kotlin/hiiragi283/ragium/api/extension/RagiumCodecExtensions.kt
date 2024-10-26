package hiiragi283.ragium.api.extension

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.block.entity.BlockEntity
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.s2c.play.TitleS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.StringIdentifiable
import net.minecraft.world.World
import java.util.*
import java.util.function.Function

//    Network    //

fun BlockEntity.sendPacket(action: (ServerPlayerEntity) -> Unit) {
    val world: World = world ?: return
    if (!world.isClient) {
        PlayerLookup.tracking(this)?.firstOrNull()?.let(action)
    }
}

fun World.sendPacket(action: (ServerPlayerEntity) -> Unit) {
    (this as? ServerWorld)?.let(PlayerLookup::world)?.firstOrNull()?.let(action)
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

fun <A : Any, B : Any> pairCodecOf(first: MapCodec<A>, second: MapCodec<B>): Codec<Pair<A, B>> = Codec.pair(first.codec(), second.codec())

fun <A : Any, B : Any> mappedCodecOf(first: MapCodec<A>, second: MapCodec<B>): Codec<Map<A, B>> = pairCodecOf(first, second)
    .toMap()

fun <A : Any, B : Any> Codec<Pair<A, B>>.toMap(): Codec<Map<A, B>> = this.listOf().xmap(
    { pairs: List<Pair<A, B>> -> pairs.associate { it.first to it.second } },
    { map: Map<A, B> -> map.toList().map { Pair(it.first, it.second) } },
)

fun <T : Any> pairCodecOf(codec: Codec<T>, defaultValue: T, name: String): MapCodec<Pair<T, T>> = pairCodecOf(
    codec.optionalFieldOf("first", defaultValue),
    codec.optionalFieldOf("second", defaultValue),
).optionalFieldOf(name, Pair.of(defaultValue, defaultValue))

fun longRangeCodec(min: Long, max: Long): Codec<Long> {
    val func: Function<Long, DataResult<Long>> = Codec.checkRange(min, max)
    return Codec.LONG.flatXmap(func, func)
}

fun <T : Any> resourcePacketCodec(resourceCodec: PacketCodec<RegistryByteBuf, T>): PacketCodec<RegistryByteBuf, ResourceAmount<T>> =
    PacketCodec.tuple(
        resourceCodec,
        ResourceAmount<T>::resource,
        PacketCodecs.VAR_LONG,
        ResourceAmount<T>::amount,
        ::ResourceAmount,
    )

//    PacketCodec    //

fun <B : ByteBuf, V : Any> PacketCodec<B, V>.toList(): PacketCodec<B, List<V>> = collect(PacketCodecs.toList())

fun <B : ByteBuf, V : Any> PacketCodec<B, V>.validate(checker: (V) -> DataResult<V>): PacketCodec<B, V> = xmap(
    { checker(it).orThrow },
    { checker(it).orThrow },
)

// fun <T : StringIdentifiable> packetCodecOf(from: (String) -> T): PacketCodec<RegistryByteBuf, T> =
//     PacketCodec.tuple(PacketCodecs.STRING, StringIdentifiable::asString, from)

fun <T : StringIdentifiable> packetCodecOf(entries: Iterable<T>): PacketCodec<RegistryByteBuf, T> = PacketCodec.tuple(
    PacketCodecs.STRING,
    StringIdentifiable::asString,
) { name: String -> entries.firstOrNull { it.asString() == name } }

fun <A : Any, B : Any> pairPacketCodecOf(
    first: PacketCodec<RegistryByteBuf, A>,
    second: PacketCodec<RegistryByteBuf, B>,
): PacketCodec<RegistryByteBuf, Pair<A, B>> = PacketCodec.tuple(first, Pair<A, B>::getFirst, second, Pair<A, B>::getSecond, ::Pair)
