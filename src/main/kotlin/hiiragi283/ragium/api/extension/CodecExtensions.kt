package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.s2c.play.TitleS2CPacket
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.StringIdentifiable
import net.minecraft.world.World
import java.util.*
import java.util.function.Function
import com.mojang.datafixers.util.Pair as MPair

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

fun PlayerEntity.sendPacket(payload: CustomPayload) {
    (this as? ServerPlayerEntity)?.let { ServerPlayNetworking.send(it, payload) }
}

fun ServerPlayerEntity.sendTitle(title: Text) {
    networkHandler.sendPacket(TitleS2CPacket(title))
}

//    Codec    //

fun <T : StringIdentifiable> codecOf(entries: Iterable<T>): Codec<T> = Codec.STRING.comapFlatMap(
    { name: String ->
        entries.firstOrNull { it.asString() == name }.toDataResult { "Unknown entry: $name!" }
    },
    StringIdentifiable::asString,
)

fun <A : Any, B : Any> pairCodecOf(first: MapCodec<A>, second: MapCodec<B>): Codec<MPair<A, B>> = Codec.pair(first.codec(), second.codec())

fun <A : Any, B : Any> mappedCodecOf(first: MapCodec<A>, second: MapCodec<B>): Codec<Map<A, B>> = pairCodecOf(first, second)
    .toMap()

fun <A : Any, B : Any> Codec<MPair<A, B>>.toMap(): Codec<Map<A, B>> = this.listOf().xmap(
    { pairs: List<MPair<A, B>> -> pairs.associate(MPair<A, B>::toKotlin) },
    { map: Map<A, B> -> map.toList().map(Pair<A, B>::toMojang) },
)

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

fun <T : StringIdentifiable> packetCodecOf(entries: Iterable<T>): PacketCodec<RegistryByteBuf, T> = PacketCodec.tuple(
    PacketCodecs.STRING,
    StringIdentifiable::asString,
) { name: String -> entries.firstOrNull { it.asString() == name } }

val <T : Any> Registry<T>.entryPacketCodec: PacketCodec<ByteBuf, RegistryEntry<T>>
    get() = RegistryKey.createPacketCodec(key).xmap(
        this::getEntryOrThrow,
        { entry: RegistryEntry<T> -> entry.key.orElseThrow() },
    )

//    DataResult    //

fun <R : Any> DataResult<R>.validate(checker: (R) -> Boolean, errorMessage: () -> String): DataResult<R> = flatMap { result: R ->
    when (checker(result)) {
        true -> DataResult.success(result)
        false -> DataResult.error(errorMessage)
    }
}

fun <T : Any> Optional<T>.toDataResult(errorMessage: () -> String): DataResult<T> =
    map(DataResult<T>::success).orElse(DataResult.error(errorMessage))

fun <T : Any> T?.toDataResult(errorMessage: () -> String): DataResult<T> =
    this?.let(DataResult<T>::success) ?: DataResult.error(errorMessage)

//    Pair    //

fun <F : Any, S : Any> Pair<F, S>.toMojang(): MPair<F, S> = MPair(first, second)

fun <F : Any, S : Any> MPair<F, S>.toKotlin(): Pair<F, S> = first to second

operator fun <F : Any, S : Any> MPair<F, S>.component1(): F = first

operator fun <F : Any, S : Any> MPair<F, S>.component2(): S = second
