package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.util.HTUnitResult
import io.netty.buffer.ByteBuf
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.StringIdentifiable
import java.util.*
import java.util.function.Function
import kotlin.jvm.optionals.getOrNull

//    Codec    //

/**
 * 指定した[entries]から[Codec]を返します。
 * @param T [StringIdentifiable]を継承したクラス
 * @return [Codec.STRING]をベースに変換された[Codec]
 */
fun <T : StringIdentifiable> identifiedCodec(entries: Iterable<T>): Codec<T> = Codec.STRING.comapFlatMap(
    { name: String ->
        entries.firstOrNull { it.asString() == name }.toDataResult { "Unknown entry: $name!" }
    },
    StringIdentifiable::asString,
)

/**
 * 指定した[first]と [second]から[Map]の[Codec]を返します。
 */
fun <A : Any, B : Any> mappedCodecOf(first: MapCodec<A>, second: MapCodec<B>): Codec<Map<A, B>> =
    Codec.pair(first.codec(), second.codec()).listOf().xmap(
        { pairs: List<MPair<A, B>> -> pairs.associate(MPair<A, B>::toKotlin) },
        { map: Map<A, B> -> map.toList().map(Pair<A, B>::toMojang) },
    )

/**
 * 指定した[min]と[max]を含む範囲の[Codec]を返します。
 */
fun longRangeCodec(min: Long, max: Long): Codec<Long> {
    val func: Function<Long, DataResult<Long>> = Codec.checkRange(min, max)
    return Codec.LONG.flatXmap(func, func)
}

/**
 * 0から[Long.MAX_VALUE]までの範囲を含む[Codec]です。
 */
val NON_NEGATIVE_LONG_CODEC: Codec<Long> = longRangeCodec(0, Long.MAX_VALUE)

/**
 * 1から[Long.MAX_VALUE]までの範囲を含む[Codec]です。
 */
val POSITIVE_LONG_CODEC: Codec<Long> = longRangeCodec(1, Long.MAX_VALUE)

/**
 * 指定した[builder]から[Inventory]の[Codec]を返します。
 * @param T [Inventory]を継承したクラス
 * @return [ItemStack.OPTIONAL_CODEC]をベースに変換された[Codec]
 */
fun <T : Inventory> createInventoryCodec(builder: (Int) -> T): Codec<T> = ItemStack.OPTIONAL_CODEC.listOf().xmap(
    { list: List<ItemStack> ->
        builder(list.size).apply {
            list.forEachIndexed(this::setStack)
        }
    },
    Inventory::iterateStacks,
)

//    MapCodec    //

fun <O : Any, A : Any> MapCodec<Optional<A>>.forOptionalGetter(getter: Function<O, A?>): RecordCodecBuilder<O, Optional<A>> =
    forGetter(getter.andThen(Optional<A>::ofNullable))

//    PacketCodec    //

/**
 * [List]の[PacketCodec]に変換します。
 */
fun <B : ByteBuf, V : Any> PacketCodec<B, V>.toList(): PacketCodec<B, List<V>> = collect(PacketCodecs.toList())

/**
 * [Set]の[PacketCodec]に変換します。
 */
fun <B : ByteBuf, V : Any> PacketCodec<B, V>.toSet(): PacketCodec<B, Set<V>> =
    collect { codec: PacketCodec<B, V> -> PacketCodecs.collection(::HashSet, codec) }

/**
 * 指定した[entries]から[PacketCodec]を返します。
 * @param T [StringIdentifiable]を継承したクラス
 * @return [identifiedCodec]をベースに変換された[PacketCodec]
 */
fun <T : StringIdentifiable> identifiedPacketCodec(entries: Iterable<T>): PacketCodec<RegistryByteBuf, T> =
    PacketCodecs.registryCodec(identifiedCodec(entries))

/**
 * [RegistryEntry]の[PacketCodec]を返します。
 */
val <T : Any> Registry<T>.entryPacketCodec: PacketCodec<RegistryByteBuf, RegistryEntry<T>>
    get() = PacketCodecs.registryEntry(this.key)

//    DataResult    //

/**
 * [checker]で検証された[DataResult]を返します。
 * @param errorMessage [DataResult.error]を返す場合のメッセージ
 * @return [checker]がtrueの場合は[DataResult.success]，それ以外の場合は[DataResult.error]
 */
fun <R : Any> DataResult<R>.validate(checker: (R) -> Boolean, errorMessage: () -> String): DataResult<R> = flatMap { result: R ->
    when (checker(result)) {
        true -> DataResult.success(result)
        false -> DataResult.error(errorMessage)
    }
}

/**
 * 指定した[transform]で[HTUnitResult]に変換します。
 */
fun <R : Any> DataResult<R>.unitMap(transform: (R) -> HTUnitResult): HTUnitResult =
    map(transform).mapOrElse(Function.identity()) { HTUnitResult.errorString { it.message() } }

/**
 * [Optional]を[DataResult]に変換します。
 * @param errorMessage [DataResult.error]を返す場合のメッセージ
 * @return [Optional.isPresent]がtrueの場合は[DataResult.success]，それ以外の場合は[DataResult.error]
 */
fun <T : Any> Optional<T>.toDataResult(errorMessage: () -> String): DataResult<T> =
    map(DataResult<T>::success).orElse(DataResult.error(errorMessage))

fun OptionalDouble.toDataResult(errorMessage: () -> String): DataResult<Double> = when (this.isPresent) {
    true -> DataResult.success(this.asDouble)
    false -> DataResult.error(errorMessage)
}

fun OptionalInt.toDataResult(errorMessage: () -> String): DataResult<Int> = when (this.isPresent) {
    true -> DataResult.success(this.asInt)
    false -> DataResult.error(errorMessage)
}

fun OptionalLong.toDataResult(errorMessage: () -> String): DataResult<Long> = when (this.isPresent) {
    true -> DataResult.success(this.asLong)
    false -> DataResult.error(errorMessage)
}

/**
 * [T]を[DataResult]に変換します。
 * @param errorMessage [DataResult.error]を返す場合のメッセージ
 * @return [T]がnullの場合は[DataResult.error]，それ以外の場合は[DataResult.success]
 */
fun <T : Any> T?.toDataResult(errorMessage: () -> String): DataResult<T> =
    this?.let(DataResult<T>::success) ?: DataResult.error(errorMessage)

/**
 * [DataResult]の結果を返します。
 * @return 結果がない場合はnull
 */
fun <T : Any> DataResult<T>.getOrNull(): T? = result().getOrNull()

/**
 * [DataResult]の結果を返します。
 * @return 結果がない場合は[other]
 */
fun <T : Any> DataResult<T>.orElse(other: T): T = result().orElse(other)

//    Pair    //

fun <F : Any, S : Any> Pair<F, S>.toMojang(): MPair<F, S> = MPair(first, second)

fun <F : Any, S : Any> MPair<F, S>.toKotlin(): Pair<F, S> = first to second
