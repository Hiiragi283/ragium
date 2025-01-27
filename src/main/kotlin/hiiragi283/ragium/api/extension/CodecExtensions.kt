package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.StringRepresentable
import java.util.*
import java.util.function.Function

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

//    Pair    //

fun <F : Any, S : Any> Pair<F, S>.toMojang(): MPair<F, S> = MPair(first, second)

fun <F : Any, S : Any> MPair<F, S>.toKotlin(): Pair<F, S> = first to second
