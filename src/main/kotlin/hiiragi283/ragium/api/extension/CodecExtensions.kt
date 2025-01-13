package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import java.util.Optional
import java.util.OptionalDouble
import java.util.OptionalInt
import java.util.OptionalLong
import java.util.function.Function
import kotlin.jvm.optionals.getOrNull

//    Codec    //

/**
 * 指定した[entries]から[Codec]を返します。
 * @param T [StringRepresentable]を継承したクラス
 * @return [Codec.STRING]をベースに変換された[Codec]
 */
fun <T : StringRepresentable> stringCodec(entries: Iterable<T>): Codec<T> = Codec.STRING.comapFlatMap(
    { name: String ->
        entries.firstOrNull { it.serializedName == name }.toDataResult { "Unknown entry: $name!" }
    },
    StringRepresentable::getSerializedName,
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
 * 指定した[builder]から[Container]の[Codec]を返します。
 * @param T [Container]を継承したクラス
 * @return [ItemStack.OPTIONAL_CODEC]をベースに変換された[Codec]
 */
fun <T : Container> createInventoryCodec(builder: (Int) -> T): Codec<T> = ItemStack.OPTIONAL_CODEC.listOf().xmap(
    { list: List<ItemStack> ->
        builder(list.size).apply {
            list.forEachIndexed(this::setItem)
        }
    },
    TODO(),
)

fun <A : Any> Codec<List<A>>.filterNotEmpty(filter: (A) -> Boolean): Codec<List<A>> = xmap(
    { list: List<A> -> list.filterNot(filter) },
    Function.identity(),
)

fun <A : Any> simpleOrComplex(simple: Codec<A>, complex: Codec<A>, simplePredicate: (A) -> Boolean): Codec<A> = object : Codec<A> {
    override fun <T : Any> encode(input: A, ops: DynamicOps<T>, prefix: T): DataResult<T> = when {
        simplePredicate(input) -> simple.encode(input, ops, prefix)
        else -> complex.encode(input, ops, prefix)
    }

    override fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<MPair<A, T>> {
        val result: DataResult<MPair<A, T>> = simple.decode(ops, input)
        return if (result.isSuccess) result else complex.decode(ops, input)
    }
}

//    MapCodec    //

fun <O : Any, A : Any> MapCodec<Optional<A>>.forOptionalGetter(getter: Function<O, A?>): RecordCodecBuilder<O, Optional<A>> =
    forGetter(getter.andThen(Optional<A>::ofNullable))

//    StreamCodec    //

/**
 * [List]の[StreamCodec]に変換します。
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.toList(): StreamCodec<B, List<V>> = apply(ByteBufCodecs.list())

/**
 * [Set]の[StreamCodec]に変換します。
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.toSet(): StreamCodec<B, Set<V>> =
    apply { codec: StreamCodec<B, V> -> ByteBufCodecs.collection(::HashSet, codec) }

/**
 * 指定した[entries]から[StreamCodec]を返します。
 * @param T [StringRepresentable]を継承したクラス
 * @return [stringCodec]をベースに変換された[StreamCodec]
 */
fun <T : StringRepresentable> stringStreamCodec(entries: Iterable<T>): StreamCodec<ByteBuf, T> =
    ByteBufCodecs.fromCodec(stringCodec(entries))

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
