package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.util.HTUnitResult
import io.netty.buffer.ByteBuf
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.StringIdentifiable
import java.util.*
import java.util.function.Function

//    Codec    //

/**
 * Create a new [Codec] instance for an enum implementing [StringIdentifiable]
 */
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

/**
 * Create a new [Codec] instance of [Long] range from [min] to [max]
 */
fun longRangeCodec(min: Long, max: Long): Codec<Long> {
    val func: Function<Long, DataResult<Long>> = Codec.checkRange(min, max)
    return Codec.LONG.flatXmap(func, func)
}

/**
 * An [Long] ranged [Codec] instance from 0 to [Long.MAX_VALUE]
 */
val NON_NEGATIVE_LONG_CODEC: Codec<Long> = longRangeCodec(0, Long.MAX_VALUE)

/**
 * An [Long] ranged [Codec] instance from 1 to [Long.MAX_VALUE]
 */
val POSITIVE_LONG_CODEC: Codec<Long> = longRangeCodec(1, Long.MAX_VALUE)

fun <T : Inventory> createInventoryCodec(builder: (Int) -> T): Codec<T> = ItemStack.OPTIONAL_CODEC.listOf().xmap(
    { list: List<ItemStack> ->
        builder(list.size).apply {
            list.forEachIndexed(this::setStack)
        }
    },
) { it.iterateStacks() }

//    PacketCodec    //

/**
 * Create a new list [PacketCodec] instance
 */
fun <B : ByteBuf, V : Any> PacketCodec<B, V>.toList(): PacketCodec<B, List<V>> = collect(PacketCodecs.toList())

fun <B : ByteBuf, V : Any> PacketCodec<B, V>.validate(checker: (V) -> DataResult<V>): PacketCodec<B, V> = xmap(
    { checker(it).orThrow },
    { checker(it).orThrow },
)

/**
 * Create a new [PacketCodecs] instance for an enum implementing [StringIdentifiable]
 */
fun <T : StringIdentifiable> packetCodecOf(entries: Iterable<T>): PacketCodec<RegistryByteBuf, T> = PacketCodec.tuple(
    PacketCodecs.STRING,
    StringIdentifiable::asString,
) { name: String -> entries.firstOrNull { it.asString() == name } }

/**
 * Create a new [PacketCodec] instance for [RegistryEntry], based on [RegistryKey.createPacketCodec]
 */
val <T : Any> Registry<T>.entryPacketCodec: PacketCodec<ByteBuf, RegistryEntry<T>>
    get() = RegistryKey.createPacketCodec(key).xmap(
        this::getEntryOrThrow,
        { entry: RegistryEntry<T> -> entry.key.orElseThrow() },
    )

//    DataResult    //

/**
 * validate [this] data result
 * @param checker check [this] data result is valid
 * @param errorMessage used when [checker] returns [DataResult.Error]
 */
fun <R : Any> DataResult<R>.validate(checker: (R) -> Boolean, errorMessage: () -> String): DataResult<R> = flatMap { result: R ->
    when (checker(result)) {
        true -> DataResult.success(result)
        false -> DataResult.error(errorMessage)
    }
}

fun <R : Any, T : Any> DataResult<R>.mapNotNull(transform: (R) -> T?): DataResult<T> =
    flatMap { result: R -> transform(result).toDataResult { "Transformed value was null!" } }

/**
 * Transform [this] data result into [HTUnitResult] by [transform]
 */
fun <R : Any> DataResult<R>.unitMap(transform: (R) -> HTUnitResult): HTUnitResult =
    map(transform).mapOrElse(Function.identity()) { HTUnitResult.errorString { it.message() } }

/**
 * Transform [this] optional value into [DataResult]
 * @param errorMessage used when [this] optional value is empty
 * @return [DataResult.Success] if [Optional.isPresent], or [DataResult.Error] if [Optional.isEmpty]
 */
fun <T : Any> Optional<T>.toDataResult(errorMessage: () -> String): DataResult<T> =
    map(DataResult<T>::success).orElse(DataResult.error(errorMessage))

/**
 * Transform [this] nullable value into [DataResult]
 * @param errorMessage used when [this] optional value is empty
 * @return [DataResult.Success] if [this] is not null, or [DataResult.Error] if [this] is null
 */
fun <T : Any> T?.toDataResult(errorMessage: () -> String): DataResult<T> =
    this?.let(DataResult<T>::success) ?: DataResult.error(errorMessage)

//    Pair    //

fun <F : Any, S : Any> Pair<F, S>.toMojang(): MPair<F, S> = MPair(first, second)

fun <F : Any, S : Any> MPair<F, S>.toKotlin(): Pair<F, S> = first to second
